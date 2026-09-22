# JJThunder–TerraBlender 地表规则兼容补丁

[English](README.md)

这是面向 **Minecraft 1.21.1 + NeoForge** 的小型实验性模组：在特定 JJThunder 高度世界中保留数据包提供的地表规则，同时允许 TerraBlender 继续注入 BOP 等模组的专属规则。

它不负责增加世界高度，不负责注入群系，也不是完整的 JJThunder/BOP/Terralith 整合包。

## 解决的问题

在已测试的 TerraBlender 4.1.0.8 组合中，`minecraft` 命名空间的地表规则会被库的默认规则替换。因此，即使数据包文件保持原样，实际运行时也未必使用它的地表规则。

本模组在服务器启动时读取加载好的 noise settings，通过 `SurfaceRuleManager` 保留其底层规则。BOP 等命名空间仍可以使用自己的规则；退出服务器后恢复原来的默认值。这里没有改写 JJThunder 的山体密度公式。

代码仍依赖 TerraBlender 的 `NamespacedSurfaceRuleSource` 实现类及事件顺序，所以首版只面向明确测试的版本。

## 支持范围

- Minecraft 1.21.1、Java 21。
- NeoForge 21.1.251、TerraBlender 4.1.0.8。
- 主世界 **noise settings**：`min_y=-64`、`height=2096`。
- 可选下界 **noise settings**：`min_y=0`、`height=2032`。
- 数据包目标：JJThunder To The Max 0.6.0（1.21–1.21.1），可选 Hell Is Fire 0.1.0。项目不附带这些数据包。

尚未承诺其他版本、其他加载器或任意自定义维度兼容。TerraBlender 默认规则按类别共享；使用相同类别的其他维度需要单独检查。

## 安装

1. 备份实例，先用一次性新世界测试。自行安装对应版本的 NeoForge、TerraBlender、目标高度数据包，以及需要的生物群系模组。
2. 从 [Releases](https://github.com/Misaka2002/jjthunder-terrablender-surface-compat/releases) 下载主模组 JAR 放入 `mods`，不要安装 `sources` JAR。若已有私用版 `jj2096_surface_compat`，先替换旧版，不能同时安装两份。
3. 创建世界时启用 Release 提供的 **activation 启用数据包**。它只有标记文件和包元数据，不会生成山体、增加高度或添加群系。也可使用你自己的全局数据包加载器，但这不是硬性依赖。
4. 改变组合后重新启动游戏/服务器。不要把 `/reload` 成功理解为世界生成注册表可以随意热切换。

模组需要同时检测到 `data/jj2096_compat/active.txt` 和上述主世界噪声高度才会生效。仅提高建筑上限、噪声生成器仍为384格时，不会激活。下界还要额外满足对应的2032格噪声高度。

若现有整合数据包已经包含该标记，无需重复启用 activation 包。标记代表明确选择启用，不代表模组已经认证了任意第三方数据包。

## 不包含的内容

- JJThunder、Hell Is Fire、BOP、Terralith、TerraBlender 或其他第三方模组/数据包。
- 开发时私用的完整生态整合包、群系表或 BOP 配置。
- 把所有建筑、洞穴和植物都自动扩展到全高度的功能。
- Tectonic 与 JJThunder 山体融合、航空器、远景或光影修复。

**只安装这个 JAR 不会自动接入所有 BOP/Terralith 群系。**群系选择与地表规则是两个不同层面的工作。

## 编译和验证

安装 JDK 21，Windows 执行 `.\gradlew.bat clean build`，其他系统执行 `./gradlew clean build`。首次构建会下载 Gradle 和声明的依赖，不应依赖作者电脑路径或已有 Minecraft 实例。

验证范围和限制见 [docs/TESTING.md](docs/TESTING.md)。反馈问题时请提供准确版本、数据包顺序、最小复现步骤及相关日志，删除令牌、个人路径等无关信息。

原创代码和启用包使用 [MIT 许可证](LICENSE)。依赖和工具的许可证见 [NOTICE.md](NOTICE.md)。本项目是独立社区补丁，不是相关项目的官方发行版。
