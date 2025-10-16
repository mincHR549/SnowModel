package snowymc.top.snowmodel.command.impl;

import snowymc.top.snowmodel.SnowModel;
import snowymc.top.snowmodel.command.SubCommand;
import org.bukkit.command.CommandSender;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class InfoCommand implements SubCommand {
    private final SnowModel plugin;
    public InfoCommand(SnowModel plugin) { this.plugin = plugin; }

    @Override public String name() { return "info"; }
    @Override public String permission() {
        return plugin.getMeta().getPermissionPrefix() + ".use";
    }
    @Override public String description() { return "显示插件信息"; }
    @Override
    public String usage() {
        return "/" + plugin.getMeta().getCommandPrefix() + " info";
    }


    @Override
    public boolean execute(CommandSender sender, String[] args) {
        sender.sendMessage(plugin.getConfigManager().lang().tr("command.info-header",
                Map.of("plugin", plugin.getMeta().getName(), "version", plugin.getMeta().getVersion())));
        sender.sendMessage(plugin.getConfigManager().lang().tr("command.info-author",
                Map.of("author", plugin.getMeta().getAuthor())));
        sender.sendMessage(plugin.getConfigManager().lang().tr("command.info-desc",
                Map.of("desc", plugin.getMeta().getDescription())));
        return true;
    }

    @Override
    public List<String> tab(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }
}
