# DH 3.3.2 / SSRD 1.8.7 compatibility audit (Minecraft 1.21.1)

This audit concerns the optional rendering mods Distant Horizons (DH) and
Separate Sable Render Distance (SSRD). It does not change the tested runtime
dependencies of the surface compatibility mod: Minecraft 1.21.1, NeoForge
21.1.251, and TerraBlender 4.1.0.8.

## Source paths and decision

- `SurfaceCompat` handles `ServerAboutToStartEvent` and `ServerStoppedEvent`.
  It reads the loaded `NoiseGeneratorSettings` and writes only TerraBlender's
  `SurfaceRuleManager` defaults. The project has no DH or SSRD dependency,
  import, mixin, configuration access, or render callback.
- TerraBlender's `SurfaceRuleManager` constructs the namespace dispatch rule
  from its `minecraft` default and separately registered namespace rules.
  That is the behavior this mod preserves for marked JJ2096 worlds.
- DH's `StepSurface.generateGroup` calls Minecraft's chunk generator
  `buildSurface(...)`; it does not write TerraBlender defaults. DH 3.3.2's
  documented API 7.2.0 and reverse-Z changes concern rendering. DH marks
  SSRD 1.8.6 and earlier incompatible with this change.
- SSRD 1.8.7 has its own DH rendering integration. Its `DHDepth` class in the
  1.21.1 JAR resolves DH's `getDepthDirection()` and `getDepthRange()` at
  runtime. That adaptation belongs to SSRD, not to this surface-rule mod.

**Result:** No Java, Gradle dependency, or `neoforge.mods.toml` change is
justified for the DH 3.3.2 / SSRD 1.8.7 update. In particular, adding DH or
SSRD as a required dependency would needlessly restrict installations that do
not use distant rendering.

## Evidence and limits

The inspected local 1.21.1 instance contains DH 3.3.2, SSRD 1.8.7,
TerraBlender 4.1.0.8, and the earlier private surface patch 1.1.0. Its retained
`latest.log` was produced before those rendering-mod updates: it lists DH
3.3.1 and SSRD 1.8.6. Therefore that log cannot establish successful runtime
behavior for the updated pair. On 2026-09-25, the maintainer reported that
the updated mod combination works in this local instance. No new log or
screenshots were reviewed for this audit, and that report is not a test of
the public beta JAR or exhaustive LOD, shader, and distant sub-level behavior.

For a reproducible client regression record, use a copied instance and a new
test world with the exact mod versions. Confirm that the JJ2096 marker activates
the surface patch, compare generated surface blocks and BOP-specific surfaces,
then inspect high-altitude DH LODs, depth/occlusion, shaders, and distant Sable
sub-levels. Record the new run's log and screenshots with the exact versions.
See [TESTING.md](TESTING.md) for the existing world-generation checks.

## Upstream references

- [DH 3.3.2 release notes](https://modrinth.com/mod/distanthorizons/version/JfNo1Cub)
  (the linked asset is for another Minecraft version; verify the separate
  1.21.1 asset before installing)
- [DH `StepSurface` source](https://gitlab.com/distant-horizons-team/distant-horizons/-/raw/main/common/src/main/java/com/seibel/distanthorizons/common/wrappers/worldGeneration/step/StepSurface.java)
- [SSRD source and version](https://github.com/RanoldStranold/SSRD/blob/main/gradle.properties)
- [SSRD DH mixins](https://github.com/RanoldStranold/SSRD/tree/main/src/main/java/net/ranold/ssrd/mixin)

Upstream `main` links can change after this audit. The local 1.21.1 JAR
inspection is the evidence for SSRD 1.8.7's `DHDepth` behavior.
