package snowymc.top.snowmodel.config;

import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;

public class ProjectMeta {
    private String name;
    private String version;
    private String author;
    private String description;
    private String commandPrefix;
    private String permissionPrefix;

    public void load(File dataFolder, ClassLoader cl) {
        try {
            File file = new File(dataFolder, "project.yml");
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                try (InputStream in = cl.getResourceAsStream("project.yml")) {
                    if (in != null) Files.copy(in, file.toPath());
                }
            }
            YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
            this.name = cfg.getString("name", "Unknown");
            this.version = cfg.getString("version", "0.0.0");
            this.author = cfg.getString("author", "Unknown");
            this.description = cfg.getString("description", "");
            this.commandPrefix = cfg.getString("command-prefix", "snow");
            this.permissionPrefix = cfg.getString("permission-prefix", "snow");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getName() { return name; }
    public String getVersion() { return version; }
    public String getAuthor() { return author; }
    public String getDescription() { return description; }
    public String getCommandPrefix() { return commandPrefix; }
    public String getPermissionPrefix() { return permissionPrefix; }
}
