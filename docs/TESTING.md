# Validation scope

There are two distinct evidence levels: the original private 1.1.0 integration
exercise, and checks of this public beta. A passing unit test is not a full
Minecraft world-generation or client-rendering test.

## Original integration baseline

The private prototype used Minecraft 1.21.1, NeoForge 21.1.251, TerraBlender
4.1.0.8 and Java 21, with separately obtained JJThunder To The Max 0.6.0 and
Hell Is Fire 0.1.0. That environment also contained a private biome-integration
data pack and other content mods; those are not release assets of this project.

Observed results included:

- Startup and reload with Overworld noise range `[-64, 2032)` and Nether range
  `[0, 2032)`.
- Effective `minecraft` surface dispatch matched the loaded pack's underlying
  surface rules, while the BOP namespace rules remained present. JSON comparison
  accounted only for Minecraft codec normalization of default block properties
  and implicit resource namespaces.
- The JJ noise router and its 96 recursively referenced noise/density resources
  were unchanged by the private ecology integration. At seed `20962096`, 25
  sampled base heights matched the original JJ baseline exactly.
- Real high-elevation vegetation and natural structure starts were observed.

These observations establish the original problem and integration approach.
They do not mean this small public mod injects biomes, preserves every possible
terrain combination, or includes that private ecology pack.

## Public beta checks

The reproducible entry point is `./gradlew clean build` (Windows:
`.\gradlew.bat clean build`). The build includes the lifecycle test suite and
minimal activation data-pack packaging. The CI workflow performs the same build
without a pre-existing Minecraft installation.

The lifecycle tests exercise the small defaults-session component with test
values. They are intended to cover opt-in/height checks, restoration, null
defaults, and consecutive lifecycle use; they do not emulate the entire
NeoForge event bus or Minecraft registry loading process.

For 1.2.0-beta.1, a standard clean build and an independent offline clean rebuild
passed all four tests. The mod JAR, source JAR and activation ZIP had identical
SHA-256 hashes across those two local builds. The new beta JAR also loaded in an
isolated dedicated server, activated for both matching dimensions, reached the
ready state and saved/stopped successfully. No pre-existing game instance was
modified by that test.

The null-default test checks snapshot fidelity only: it does not claim that
TerraBlender can run a world with null surface rules. Consecutive session logic
has unit coverage; the marked/unmarked/marked sequence in a real integrated
client has not yet been manually exercised for this beta.

Release notes identify the actual build and runtime checks completed for a
specific binary. Do not interpret documentation of a test procedure as proof
that every release has been tested in every listed environment.

## Useful manual regressions

Use disposable worlds and exact dependency versions. Start the game/server
again when changing world-generation packs.

1. With no marker: verify the patch does not activate.
2. With the marker and ordinary 384-block noise settings: verify it does not
   activate.
3. With the marker and the tested JJ2096 noise settings: verify activation and
   compare effective surface defaults before/after TerraBlender initialization.
4. Save and leave that world, then enter an unmarked world in the same client
   process: check that previous defaults were restored.
5. Restart the marked world and generate previously ungenerated chunks. Test
   the optional Nether separately if using its 2032-block noise preset.

## Unsupported or unproven cases

- Concurrent servers inside one JVM. TerraBlender defaults are process-global.
- Arbitrary custom dimensions sharing TerraBlender's Overworld/Nether categories.
- Another mod replacing the same defaults later in the lifecycle.
- Other library versions, Fabric/Forge variants, or arbitrary height presets.
- Hot-swapping world-generation registries through `/reload`.
- Exhaustive biome/structure coverage, aircraft physics, distant LOD, shaders,
  or long-duration client gameplay.

If reporting a regression, supply exact versions, enabled pack order, marker
presence, noise ranges, and a minimal reproduction. Logs should be sanitized;
do not submit account tokens or an entire private instance.
