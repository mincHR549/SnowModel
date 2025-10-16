package snowymc.top.snowmodel.config;

import snowymc.top.snowmodel.SnowModel;
import org.bukkit.configuration.file.FileConfiguration;

import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConfigManager {

    private final SnowModel plugin;
    private final Map<String, ManagedConfig> configs = new ConcurrentHashMap<>();
    private LanguageManager languageManager;
    private FileWatcher watcher;

    public ConfigManager(SnowModel plugin) {
        this.plugin = plugin;
    }

    public void init() {
        register("config.yml");
        register("database.yml");
        register("features.yml");

        languageManager = new LanguageManager(plugin, "lang", "en_US");
        languageManager.init();

        watcher = new FileWatcher(plugin.getDataFolder().toPath(), this::onFileChanged);
        watcher.start();
    }

    public void shutdown() {
        if (watcher != null) watcher.stop();
        configs.clear();
        if (languageManager != null) languageManager.shutdown();
    }

    public ManagedConfig register(String fileName) {
        ManagedConfig cfg = new ManagedConfig(plugin, fileName);
        cfg.ensureExists();
        cfg.reload();
        configs.put(fileName, cfg);
        return cfg;
    }

    public ManagedConfig get(String fileName) {
        return configs.get(fileName);
    }

    public FileConfiguration getConfig(String fileName) {
        ManagedConfig c = configs.get(fileName);
        return c == null ? null : c.getConfig();
    }

    public LanguageManager lang() {
        return languageManager;
    }

    private void onFileChanged(java.nio.file.Path changed) {
        Path base = plugin.getDataFolder().toPath();
        if (!changed.startsWith(base) || !changed.toString().endsWith(".yml")) return;
        String fileName = base.relativize(changed).toString().replace("\\", "/");

        if (fileName.startsWith(languageManager.getLangDir() + "/")) {
            plugin.getLogger().info("[Snow-Model] Language file changed: " + fileName + ", reloading...");
            languageManager.reload();
            return;
        }

        ManagedConfig cfg = configs.get(fileName);
        if (cfg != null) {
            plugin.getLogger().info("[Snow-Model] Config changed: " + fileName + ", reloading...");
            cfg.reload();
        }
    }
}
