# ❄️ Snow-Model 插件模板

基于 **Gradle + Paper/Spigot API** 的 Minecraft 插件开发模板，提供快速启动与可维护扩展的基础能力。

---

## ✨ 功能特性

* 多配置文件管理（支持热重载）
* 多语言文件管理（支持占位符与热重载）
* 插件元信息集中管理（`project.yml`）
* 动态命令前缀与权限前缀，避免家族插件冲突
* 内置基础指令：`help`、`reload`、`info`
* 模块化结构：命令、监听器、管理器分层清晰

---

## 🚀 新项目启动流程

### 1. 修改必要文件

**settings.gradle**

```groovy
rootProject.name = "Snow-Chat"
```

**gradle.properties**

```properties
javaVersion=21
pluginGroup=snowymc.top.snowchat
pluginVersion=0.1.0
author=SnowyMC
paperApi=1.21-R0.1-SNAPSHOT
commandPrefix=snowchat
permissionPrefix=snow.chat
mainClass=snowymc.top.snowchat.SnowChat
```

**src/main/resources/project.yml**

```yaml
name: Snow-Chat
version: 0.1.0
author: SnowyMC
description: 聊天扩展插件
command-prefix: snowchat
permission-prefix: snow.chat
```

**包名与主类**

* 将 `snowymc.top.snowmodel` 替换为 `snowymc.top.snowchat`
* 将主类 `SnowModel` 改为 `SnowChat`
* 同步更新 `gradle.properties` 的 `mainClass`

---

## 🧩 模板核心能力

### 插件元信息

```java
plugin.getMeta().getName();
plugin.getMeta().getVersion();
plugin.getMeta().getAuthor();
plugin.getMeta().getDescription();
plugin.getMeta().getCommandPrefix();
plugin.getMeta().getPermissionPrefix();
```

---

### 配置管理

**读取**

```java
plugin.getConfigManager().getConfig("config.yml").getBoolean("debug");
plugin.getConfigManager().getConfig("config.yml").getInt("feature.cooldown", 5);
```

**修改并保存**

```java
ManagedConfig cfg = plugin.getConfigManager().get("config.yml");
cfg.getConfig().set("debug", true);
cfg.save();
```

**新增配置文件**

在 `resources` 添加 `mymodule.yml`，启动时自动复制。

如需托管缓存：

```java
plugin.getConfigManager().register("mymodule.yml");
```

---

### 语言管理

**翻译**

```java
plugin.getConfigManager().lang().tr("join.welcome", Map.of("player", p.getName()));
```

**帮助列表**

```java
plugin.getConfigManager().lang().getList("help.list");
```

**切换语言**

```java
plugin.getConfigManager().lang().setLocale("zh_CN");
```

---

### 指令扩展

**创建子命令类**

实现 `SubCommand` 接口：

```java
public class FooCommand implements SubCommand {
    private final SnowChat plugin;
    public FooCommand(SnowChat plugin) { this.plugin = plugin; }

    @Override public String name() { return "foo"; }
    @Override public String permission() { return plugin.getMeta().getPermissionPrefix() + ".use"; }
    @Override public String description() { return "示例命令"; }
    @Override public String usage() { return "/" + plugin.getMeta().getCommandPrefix() + " foo <arg>"; }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        sender.sendMessage("执行 foo 子命令！");
        return true;
    }
}
```

**注册指令**

```java
base.register(new FooCommand(plugin));
```

---

### 监听器扩展

```java
public class BlockBreakListener implements Listener {
    private final SnowChat plugin;
    public BlockBreakListener(SnowChat plugin) { this.plugin = plugin; }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        e.getPlayer().sendMessage("你破坏了一个方块！");
    }
}
```

注册：

```java
plugin.getServer().getPluginManager().registerEvents(new BlockBreakListener(plugin), plugin);
```

---

## ⚙️ 构建与运行

### 构建

```bash
./gradlew build
```

### 部署

将 `build/libs/<ProjectName>-<version>.jar` 放入服务器 `plugins/` 目录。

### 验证

1. 控制台显示插件名/版本/作者（来自 `project.yml`）
2. 执行 `/<command-prefix> help` 查看帮助
3. 修改配置或语言文件后无需重启即可生效（热重载）

---

## 🧠 常见问题与解决方案

| 问题                           | 解决方案                                                       |
|------------------------------|------------------------------------------------------------|
| Java 不兼容                     | 升级到 Java 21                                                |
| 命令/权限冲突                      | 修改 `project.yml` 中的 `command-prefix` 与 `permission-prefix` |
| plugin.yml 与 project.yml 不一致 | 构建期由 Gradle 注入，运行期由 `project.yml` 控制                       |
| 文案零散                         | 你的问题                                                       |

---

## 🧱 扩展示例

### 新增子命令

```java
public class StatsCommand implements SubCommand {
    private final SnowChat plugin;
    public StatsCommand(SnowChat plugin) { this.plugin = plugin; }

    @Override public String name() { return "stats"; }
    @Override public String permission() { return plugin.getMeta().getPermissionPrefix() + ".use"; }
    @Override public String description() { return "查看玩家统计"; }
    @Override public String usage() { return "/" + plugin.getMeta().getCommandPrefix() + " stats <player>"; }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length < 1) return false;
        sender.sendMessage("统计信息: " + args[0]);
        return true;
    }
}
```

### 新增监听器

```java
public class BlockBreakListener implements Listener {
    private final SnowChat plugin;
    public BlockBreakListener(SnowChat plugin) { this.plugin = plugin; }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        e.getPlayer().sendMessage("你破坏了一个方块！太牛逼啦！");
    }
}
```

---

## ✅ 新项目标准流程清单

1. 修改 `settings.gradle`、`gradle.properties`、`project.yml`
2. 替换包名与主类（与 `mainClass` 保持一致）
3. 构建并部署（`shadowJar → plugins/`）
4. 验证 `/<command-prefix> help`
5. 扩展子命令、监听器、配置文件

---

## ⚠️ 注意事项

* **Paper 1.21+** 必须使用 **Java 21**；**Paper 1.20.4** 可使用 **Java 17**
* 不要在代码中硬编码命令或权限，请使用
  `plugin.getMeta().getCommandPrefix()` 与 `plugin.getMeta().getPermissionPrefix()`
* `plugin.yml` 的元信息由 **Gradle 注入**；运行期展示由 `project.yml` 控制
* 配置与语言支持热重载，但修改 `plugin.yml` 需重新构建

---

## 🔮 后续扩展方向

* 数据库支持（HikariCP，SQLite/MySQL 自动切换）
* Typed 配置绑定（配置项映射为对象）
* 自动生成命令帮助（根据 SubCommand 描述）
* 模块化功能开关（`features.yml`）

---
> **🧊 Powered by Snow-Model Template** — 优雅、高效、可扩展的插件开发基石。

> **🪶 Documentation crafted with care by ChatGPT**

