package snowymc.top.snowmodel.command.impl;

import snowymc.top.snowmodel.SnowModel;
import snowymc.top.snowmodel.command.SubCommand;
import org.bukkit.command.CommandSender;
import java.util.Collections;
import java.util.List;

public class ReloadCommand implements SubCommand {

    private final SnowModel plugin;

    public ReloadCommand(SnowModel plugin) {
        this.plugin = plugin;
    }

    @Override public String name() { return "reload"; }
    @Override public String permission() {
        return plugin.getMeta().getPermissionPrefix() + ".admin";
    }
    @Override public String description() { return "重载所有配置与语言文件"; }
    @Override
    public String usage() {
        return "/" + plugin.getMeta().getCommandPrefix() + " reload";
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        plugin.getConfigManager().get("config.yml").reload();
        plugin.getConfigManager().get("database.yml").reload();
        plugin.getConfigManager().get("features.yml").reload();
        plugin.getConfigManager().lang().reload();
        sender.sendMessage(plugin.getConfigManager().lang().tr("command.reloaded"));
        return true;
    }

    @Override
    public List<String> tab(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }
}
