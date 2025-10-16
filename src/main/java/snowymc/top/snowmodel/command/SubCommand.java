package snowymc.top.snowmodel.command;

import org.bukkit.command.CommandSender;

import java.util.List;

public interface SubCommand {
    String name();
    String permission(); // 可返回 null 表示无需权限
    String description();
    String usage();
    boolean execute(CommandSender sender, String[] args);
    List<String> tab(CommandSender sender, String[] args);
}
