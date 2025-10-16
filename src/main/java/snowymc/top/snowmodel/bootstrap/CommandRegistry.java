package snowymc.top.snowmodel.bootstrap;

import snowymc.top.snowmodel.SnowModel;
import snowymc.top.snowmodel.command.BaseCommand;
import snowymc.top.snowmodel.command.impl.HelpCommand;
import snowymc.top.snowmodel.command.impl.InfoCommand;
import snowymc.top.snowmodel.command.impl.ReloadCommand;
import org.bukkit.command.PluginCommand;

public class CommandRegistry {

    public static void register(SnowModel plugin) {
        BaseCommand base = new BaseCommand(plugin);

        base.register(new HelpCommand(plugin));
        base.register(new ReloadCommand(plugin));
        base.register(new InfoCommand(plugin));

        PluginCommand pc = plugin.getCommand(plugin.getMeta().getCommandPrefix());
        if (pc != null) {
            pc.setExecutor(base);
            pc.setTabCompleter(base);
        } else {
            plugin.getLogger().severe("[SnowyMC] Command not found in plugin.yml");
        }
    }
}
