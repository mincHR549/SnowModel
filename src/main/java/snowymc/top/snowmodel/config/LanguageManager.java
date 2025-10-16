package snowymc.top.snowmodel.config;

import snowymc.top.snowmodel.SnowModel;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class LanguageManager {

    private final SnowModel plugin;
    private final String langDir;
    private final String defaultLocale;
    private String currentLocale;
    private final Map<String, FileConfiguration> locales = new ConcurrentHashMap<>();

    public LanguageManager(SnowModel plugin, String langDir, String defaultLocale) {
        this.plugin = plugin;
        this.langDir = langDir;
        this.defaultLocale = defaultLocale;
        this.currentLocale = defaultLocale;
    }

    public void init() {
        File base = new File(plugin.getDataFolder(), langDir);
        base.mkdirs();

        // 复制默认语言文件
        copyDefault("en_US.yml");
        copyDefault("zh_CN.yml");

        // 加载所有语言文件
        reload();

        // 从配置读取语言设置
        String configured = plugin.getConfigManager().getConfig("config.yml").getString("language", "auto");

        if ("auto".equalsIgnoreCase(configured)) {
            String sys = detectSystemLocale();
            if (locales.containsKey(sys)) {
                currentLocale = sys;
                plugin.getLogger().info("[Language] Auto detected system locale: " + sys);
            } else {
                currentLocale = defaultLocale;
                plugin.getLogger().warning("[Language] System locale " + sys + " not found, fallback to " + defaultLocale);
            }
        } else {
            currentLocale = configured;
            if (!locales.containsKey(currentLocale)) {
                plugin.getLogger().warning("[Language] Configured locale " + currentLocale + " not found, fallback to " + defaultLocale);
                currentLocale = defaultLocale;
            }
        }
    }

    private void copyDefault(String name) {
        try (InputStream in = plugin.getResource(langDir + "/" + name)) {
            if (in != null) {
                File target = new File(plugin.getDataFolder(), langDir + "/" + name);
                if (!target.exists()) {
                    Files.copy(in, target.toPath());
                }
            }
        } catch (Exception e) {
            plugin.getLogger().warning("Failed to copy default lang " + name + ": " + e.getMessage());
        }
    }

    public void reload() {
        locales.clear();
        File base = new File(plugin.getDataFolder(), langDir);
        File[] files = base.listFiles((dir, name) -> name.endsWith(".yml"));
        if (files != null) {
            for (File f : files) {
                locales.put(f.getName().replace(".yml", ""), YamlConfiguration.loadConfiguration(f));
            }
        }
    }

    private String detectSystemLocale() {
        Locale locale = Locale.getDefault();
        // 返回 zh_CN / en_US 等
        return locale.toString();
    }

    public void setLocale(String locale) {
        if (locales.containsKey(locale)) {
            this.currentLocale = locale;
        } else {
            plugin.getLogger().warning("[Language] Locale " + locale + " not found, fallback to " + defaultLocale);
            this.currentLocale = defaultLocale;
        }
    }

    public String getLocale() {
        return currentLocale;
    }

    public String getLangDir() {
        return langDir;
    }

    public String tr(String key, Map<String, String> placeholders) {
        String raw = getString(key);
        if (raw == null) raw = key;
        if (placeholders != null) {
            for (Map.Entry<String, String> e : placeholders.entrySet()) {
                raw = raw.replace("{" + e.getKey() + "}", String.valueOf(e.getValue()));
            }
        }
        return raw.replace('&', '§');
    }

    public String tr(String key) {
        return tr(key, null);
    }

    public List<String> getList(String key) {
        FileConfiguration cur = locales.get(currentLocale);
        if (cur != null && cur.isList(key)) return colorize(cur.getStringList(key));
        FileConfiguration def = locales.get(defaultLocale);
        if (def != null && def.isList(key)) return colorize(def.getStringList(key));
        return Collections.emptyList();
    }

    private List<String> colorize(List<String> in) {
        List<String> out = new ArrayList<>(in.size());
        for (String s : in) out.add(s.replace('&', '§'));
        return out;
    }

    private String getString(String key) {
        FileConfiguration cur = locales.get(currentLocale);
        if (cur != null && cur.isString(key)) return cur.getString(key);
        FileConfiguration def = locales.get(defaultLocale);
        if (def != null && def.isString(key)) return def.getString(key);
        return null;
    }

    public void shutdown() {
        locales.clear();
    }
}
