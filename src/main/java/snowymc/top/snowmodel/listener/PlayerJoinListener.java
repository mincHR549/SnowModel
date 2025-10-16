package snowymc.top.snowmodel.listener;

import snowymc.top.snowmodel.SnowModel;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.HashMap;
import java.util.Map;

public class PlayerJoinListener implements Listener {
    private final SnowModel plugin;
    public PlayerJoinListener(SnowModel plugin) { this.plugin = plugin; }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Map<String, String> p = new HashMap<>();
        p.put("player", e.getPlayer().getName());
        String msg = plugin.getConfigManager().lang().tr("join.welcome", p);
        e.getPlayer().sendMessage(msg);
    }
}
