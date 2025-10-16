package snowymc.top.snowmodel.bootstrap;

import snowymc.top.snowmodel.SnowModel;
import snowymc.top.snowmodel.listener.PlayerJoinListener;

public class ListenerRegistry {
    public static void register(SnowModel plugin) {
        plugin.getServer().getPluginManager().registerEvents(new PlayerJoinListener(plugin), plugin);
    }
}
