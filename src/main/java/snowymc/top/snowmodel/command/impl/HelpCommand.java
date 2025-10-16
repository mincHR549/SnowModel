package snowymc.top.snowmodel.command.impl;

import snowymc.top.snowmodel.SnowModel;
import snowymc.top.snowmodel.command.SubCommand;
import org.bukkit.command.CommandSender;
import java.util.Collections;
import java.util.List;

public class HelpCommand implements SubCommand {
    private final SnowModel plugin;
    public HelpCommand(SnowModel plugin) { this.plugin = plugin; }

    @Override public String name() { return "help"; }
    @Override public String permission() {
        return plugin.getMeta().getPermissionPrefix() + ".use";
    }
    @Override public String description() { return "显示帮助"; }
    @Override
    public String usage() {
        return "/" + plugin.getMeta().getCommandPrefix() + " help";
    }


    @Override
    public boolean execute(CommandSender sender, String[] args) {
        sender.sendMessage(plugin.getConfigManager().lang().tr("help.header",
                java.util.Map.of(
                        "plugin", plugin.getMeta().getName(),
                        "version", plugin.getMeta().getVersion()
                )));
        for (String line : plugin.getConfigManager().lang().getList("help.list")) {
            sender.sendMessage(line.replace('&', '§'));
        }
        return true;
    }

    @Override
    public List<String> tab(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }
}
