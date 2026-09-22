# 1.2.0-beta.1 — first public experimental release

A small opt-in patch that preserves loaded JJ2096 surface defaults while
TerraBlender continues to apply namespace-specific biome-mod surface rules.

**Exact target:** Minecraft 1.21.1, Java 21, NeoForge 21.1.251 and TerraBlender
4.1.0.8. Requires an enabled activation marker and Overworld noise settings
`min_y=-64,height=2096`; optional Nether handling requires `min_y=0,height=2032`.

## Downloads

- `jj2096-surface-compat-1.2.0-beta.1.jar`: install in `mods`.
- `jj2096-surface-compat-activation-1.2.0-beta.1.zip`: enable as a data pack unless
  your integration pack already supplies `jj2096_compat:active.txt`.
- `jj2096-surface-compat-1.2.0-beta.1-sources.jar`: source archive, not a mod to install.
- `SHA256SUMS.txt`: checksums of these three assets.

The activation pack only contains metadata and a marker. JJThunder, Hell Is
Fire, BOP, TerraBlender, Terralith and the private full-ecology integration pack
are not bundled. This JAR does not extend height or inject biomes by itself.

## Validation

- Standard clean build and independent offline clean rebuild passed; four
  lifecycle/guard tests passed and all three local artifact hashes matched.
- The beta JAR completed isolated dedicated-server startup, both matching
  dimension activations, save and clean exit.
- This builds on the private prototype's surface-dispatch and terrain checks;
  those private integration assets are not part of this release.

## Limits

Experimental release. No support claim for concurrent servers in one JVM,
custom dimensions sharing TerraBlender categories, other mods overwriting the
same defaults later, other dependency versions, or worldgen hot reload.
Repeated integrated-client world switching has unit-level lifecycle coverage
but has not been manually exercised end to end. No aircraft, LOD, shader or
exhaustive biome/structure validation is implied.

Original code and activation content: MIT, copyright 2026 Misaka2002. This is
an independent community patch, not an official upstream release.

---

首个公开实验版。安装主 JAR，并按说明启用仅含标记的 activation 数据包；
需要另行准备对应高度的数据包及精确版本的依赖。本补丁只修复地表规则分派，
不会自动接入完整生态、增加世界高度或修复航空/光影。

已通过4项自动测试、两次产物一致的本地构建和隔离服务端启动/保存退出测试。
真实客户端连续切换多个世界尚未手测；请先备份并用一次性世界验证。
