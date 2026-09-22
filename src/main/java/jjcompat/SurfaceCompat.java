package jjcompat;

import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import org.slf4j.Logger;
import terrablender.api.SurfaceRuleManager;
import terrablender.api.SurfaceRuleManager.RuleCategory;
import terrablender.worldgen.surface.NamespacedSurfaceRuleSource;

/** Preserves pack defaults for the explicitly marked, tested JJ2096 preset. */
@Mod("jj2096_surface_compat")
public final class SurfaceCompat {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final ResourceLocation MARKER = ResourceLocation.parse("jj2096_compat:active.txt");
    private final SurfaceDefaultsSession<RuleCategory, SurfaceRules.RuleSource> defaults =
            new SurfaceDefaultsSession<>(SurfaceRuleManager::getDefaultSurfaceRules,
                    SurfaceRuleManager::setDefaultSurfaceRules);

    public SurfaceCompat() {
        // TerraBlender 4.1.0.8 wraps the registry at LOWEST; capture pack rules first.
        NeoForge.EVENT_BUS.addListener(EventPriority.HIGHEST, this::starting);
        NeoForge.EVENT_BUS.addListener(this::stopped);
    }

    private void stopped(ServerStoppedEvent event) {
        defaults.restore();
    }

    private static SurfaceRules.RuleSource unwrapped(SurfaceRules.RuleSource rule) {
        while (rule instanceof NamespacedSurfaceRuleSource namespaced) rule = namespaced.base();
        return rule;
    }

    private void starting(ServerAboutToStartEvent event) {
        // Also recover a missed stop before entering another world in the same JVM.
        // TerraBlender defaults are process-global: concurrent servers are unsupported.
        defaults.restore();
        var server = event.getServer();
        boolean marker = server.getResourceManager().getResource(MARKER).isPresent();
        if (!marker) return;
        var registry = server.registryAccess().registryOrThrow(Registries.NOISE_SETTINGS);
        var overworld = registry.get(NoiseGeneratorSettings.OVERWORLD);
        if (overworld == null || !SurfaceDefaultsSession.matchesOverworld(marker,
                overworld.noiseSettings().minY(), overworld.noiseSettings().height())) return;

        // Deliberately inspect only the vanilla registry keys, never arbitrary dimensions.
        defaults.replace(RuleCategory.OVERWORLD, unwrapped(overworld.surfaceRule()));
        var nether = registry.get(NoiseGeneratorSettings.NETHER);
        boolean replaceNether = nether != null && SurfaceDefaultsSession.matchesNether(
                nether.noiseSettings().minY(), nether.noiseSettings().height());
        if (replaceNether) defaults.replace(RuleCategory.NETHER, unwrapped(nether.surfaceRule()));
        LOGGER.info("JJ2096 surface compatibility active: overworld=true, nether={}; "
                + "pack defaults preserved, TerraBlender namespace rules retained", replaceNether);
    }
}
