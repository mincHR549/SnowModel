package snowymc.top.snowchat;

import snowymc.top.snowchat.bootstrap.CommandRegistry;
import snowymc.top.snowchat.bootstrap.ListenerRegistry;
import snowymc.top.snowchat.config.ConfigManager;
import snowymc.top.snowchat.config.ProjectMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class SnowChat extends JavaPlugin {

    private static SnowChat instance;
    private ConfigManager configManager;
    private ProjectMeta meta;

    @Override
    public void onEnable() {
        instance = this;

        // 打包的默认资源
        saveResource("project.yml", false);
        saveResource("config.yml", false);
        saveResource("database.yml", false);
        saveResource("features.yml", false);
        saveResource("lang/en_US.yml", false);
        saveResource("lang/zh_CN.yml", false);

        // 元信息
        meta = new ProjectMeta();
        meta.load(getDataFolder(), getClassLoader());
        getLogger().info(meta.getName() + " v" + meta.getVersion() + " by " + meta.getAuthor());

        // 配置与语言管理（含热重载）
        configManager = new ConfigManager(this);
        configManager.init();

        // 指令与监听器
        CommandRegistry.register(this);
        ListenerRegistry.register(this);
    }

    @Override
    public void onDisable() {
        if (configManager != null) configManager.shutdown();
        getLogger().info(meta.getName() + " disabled.");
    }

    public static SnowChat get() { return instance; }
    public ConfigManager getConfigManager() { return configManager; }
    public ProjectMeta getMeta() { return meta; }
}

