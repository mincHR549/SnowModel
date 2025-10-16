package snowymc.top.snowmodel.command;

import snowymc.top.snowmodel.SnowModel;
import snowymc.top.snowmodel.util.Text;
import org.bukkit.command.*;
import java.util.*;

public class BaseCommand implements CommandExecutor, TabCompleter {

    private final SnowModel plugin;
    private final Map<String, SubCommand> subs = new LinkedHashMap<>();

    public BaseCommand(SnowModel plugin) {
        this.plugin = plugin;
    }

    public void register(SubCommand cmd) {
        subs.put(cmd.name().toLowerCase(Locale.ROOT), cmd);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(Text.color("&b" + plugin.getMeta().getName() + " &7- &fUse /" + label + " help"));
            return true;
        }
        SubCommand sub = subs.get(args[0].toLowerCase(Locale.ROOT));
        if (sub == null) {
            sender.sendMessage(plugin.getConfigManager().lang().tr("command.unknown"));
            return true;
        }
        String perm = sub.permission();
        if (perm != null && !sender.hasPermission(perm)) {
            sender.sendMessage(plugin.getConfigManager().lang().tr("command.no-permission"));
            return true;
        }
        boolean ok = sub.execute(sender, Arrays.copyOfRange(args, 1, args.length));
        if (!ok) sender.sendMessage(Text.color("&eUsage: " + sub.usage()));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> names = new ArrayList<>(subs.keySet());
            names.removeIf(n -> {
                SubCommand c = subs.get(n);
                String perm = c.permission();
                return perm != null && !sender.hasPermission(perm);
            });
            return filter(names, args[0]);
        } else {
            SubCommand sub = subs.get(args[0].toLowerCase(Locale.ROOT));
            if (sub != null) {
                return sub.tab(sender, Arrays.copyOfRange(args, 1, args.length));
            }
        }
        return Collections.emptyList();
    }

    private List<String> filter(List<String> list, String prefix) {
        if (prefix == null || prefix.isEmpty()) return list;
        String p = prefix.toLowerCase(Locale.ROOT);
        List<String> out = new ArrayList<>();
        for (String s : list) if (s.toLowerCase(Locale.ROOT).startsWith(p)) out.add(s);
        return out;
    }
}
