# JJThunder–TerraBlender Surface Compatibility

[简体中文](README.zh-CN.md)

A small, opt-in **Minecraft 1.21.1 / NeoForge** compatibility mod that preserves a loaded JJThunder-height world's surface rules while TerraBlender continues to dispatch biome-mod-specific rules.

**Experimental, narrowly scoped compatibility patch.** This is not a height extender, a biome injector, a terrain generator, or a complete JJThunder/BOP/Terralith integration pack.

## What it fixes

In the tested TerraBlender 4.1.0.8 setup, the `minecraft` namespace surface rules were replaced by TerraBlender's defaults. Retaining a data pack's JSON on disk was therefore insufficient to preserve its effective surface rules.

This mod captures the loaded noise settings' underlying surface rules before TerraBlender's server-start initialization and assigns them through `SurfaceRuleManager`. TerraBlender can still inject namespace-specific rules, including BOP's. Previous defaults are restored at the end of the server lifecycle.

It does not copy, replace, or modify TerraBlender's classes. It does depend on the implementation type `NamespacedSurfaceRuleSource` and the tested event ordering; this is why support is deliberately restricted.

## Supported setup

| Component | Release target |
| --- | --- |
| Minecraft | 1.21.1 |
| Java | 21 |
| Loader | NeoForge 21.1.251 |
| Required library | TerraBlender 4.1.0.8 |
| Overworld noise settings | `min_y=-64`, `height=2096` |
| Optional Nether noise settings | `min_y=0`, `height=2032` |

The experimental compatibility target is JJThunder To The Max 0.6.0 for 1.21–1.21.1, with optional Hell Is Fire 0.1.0. Those data packs are **not included**. BOP is useful for the intended setup but is not a hard dependency of this small patch.

Other Minecraft/loaders, TerraBlender versions, terrain packs, or arbitrary custom dimensions are not claimed to work. The affected TerraBlender defaults are category-wide, so other dimensions using the same category need their own compatibility review.

## Install

1. Back up your instance and use a disposable new world first. Install the separately obtained, matching NeoForge and TerraBlender versions, your height/world-generation data packs, and any intended biome mods.
2. Put the main mod JAR from [Releases](https://github.com/Misaka2002/jjthunder-terrablender-surface-compat/releases) in `mods`. Do not install the sources JAR. Replace any earlier private `jj2096_surface_compat` build rather than installing both.
3. Enable the release's **activation data pack** when creating the world. It contains only a marker and pack metadata: it does not increase height, modify terrain, or provide biomes. A separately installed global data-pack loader is optional, not required.
4. Restart the game/server after changing this setup. A successful `/reload` is not a substitute for rebuilding world-generation registries or starting a fresh test world.

Activation requires both `data/jj2096_compat/active.txt` and the exact Overworld noise range above. Without them, the mod makes no changes. Nether handling is additionally conditional on the Nether noise range above. Matching only the dimension's build limit, while leaving the noise generator at 384 blocks, does not activate the patch.

If an integration pack already provides the marker, the separate activation pack is unnecessary. A marker is an explicit opt-in, not proof that a pack was made by JJThunder.

## What this release does not supply

- JJThunder, Hell Is Fire, TerraBlender, BOP, Terralith, or any other third-party JAR/data-pack contents.
- The private full-ecology pack used during development, its biome table, or its BOP configuration.
- A guarantee that every structure, cave feature, or plant expands to the world's full height.
- Tectonic/JJThunder terrain merging, aircraft physics, LOD, or shader fixes.

In particular, installing this JAR alone does **not** add all Terralith/BOP biomes. Biome selection and surface-rule compatibility are separate tasks.

## Build

Install JDK 21, then run:

```sh
./gradlew clean build
```

On Windows:

```powershell
.\gradlew.bat clean build
```

The first build downloads Gradle and declared dependencies. No local Minecraft installation, game-instance classpath, or absolute machine path should be needed. See [testing notes](docs/TESTING.md) for what the automated and integration checks establish.

## Reporting issues

Include the exact Minecraft, NeoForge, TerraBlender, patch, and data-pack versions; enabled data-pack order; whether the marker is enabled; a minimal reproduction; and the relevant log excerpt. Remove access tokens, account details, and personal filesystem paths before posting. Do not upload a whole modpack or private save without reviewing its contents.

## License and credits

Original project code and activation-pack contents are MIT licensed; see [LICENSE](LICENSE). Third-party projects remain under their own licenses; see [NOTICE.md](NOTICE.md). This is an independent community patch, not an official JJThunder, Glitchfiend, NeoForge, Mojang, or Microsoft release.
