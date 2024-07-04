package nl.martenm.servertutorialplus.commands.sub.tutorials;

import net.md_5.bungee.api.ChatColor;
import nl.martenm.servertutorialplus.ServerTutorialPlus;
import nl.martenm.servertutorialplus.language.Lang;
import nl.martenm.servertutorialplus.objects.ServerTutorial;
import nl.martenm.servertutorialplus.points.ServerTutorialPoint;
import nl.martenm.simplecommands.SimpleCommand;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class InfoCommand extends SimpleCommand {

    public InfoCommand() {
        super("info", Lang.HELP_INFO.toString(), "+info", false);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        ServerTutorialPlus plugin = ServerTutorialPlus.getInstance();

        if (args.length == 0) {
            sender.sendMessage(ChatColor.DARK_GRAY + "+──────────┤ " + ChatColor.GREEN + ChatColor.BOLD + "Info" + ChatColor.DARK_GRAY + "├──────────+");
            sender.sendMessage(" ");
            if (plugin.serverTutorials.size() == 0) {
                sender.sendMessage("  " + Lang.INFO_NONE_EXISTING);
            } else {
                for (ServerTutorial st : plugin.serverTutorials) {
                    sender.sendMessage(ChatColor.GREEN + "  " + st.getId());
                }
            }
            sender.sendMessage(" ");
            sender.sendMessage("  " + Lang.INFO_MORE_INFO);
            sender.sendMessage(ChatColor.DARK_GRAY + "+─────────────────────────+");
            return true;
        }

        if (plugin.serverTutorials.size() == 0) {
            sender.sendMessage(Lang.INFO_NONE_EXISTING.toString());
            return true;
        }

        ServerTutorial serverTutorial = null;
        for (ServerTutorial st : plugin.serverTutorials) {
            if (st.getId().equalsIgnoreCase(args[0])) {
                serverTutorial = st;
                break;
            }
        }

        if (serverTutorial == null) {
            sender.sendMessage(Lang.TUTORIAL_ID_NOT_FOUND.toString());
            return true;
        }

        sender.sendMessage(ChatColor.DARK_GRAY + "+──────────┤ " + ChatColor.GREEN + ChatColor.BOLD + "Info" + ChatColor.DARK_GRAY + "├──────────+");
        sender.sendMessage(" ");
        sender.sendMessage(formatInfo(Lang.ID.toString(), serverTutorial.getId()));
        sender.sendMessage(formatInfo(Lang.INVISIBLE.toString(), (serverTutorial.invisiblePlayer ? ChatColor.DARK_GREEN.toString() : ChatColor.RED.toString()) + serverTutorial.invisiblePlayer + ""));
        sender.sendMessage(formatInfo(Lang.PERMISSION.toString(), (serverTutorial.getNeedsPermission() ? ChatColor.DARK_GREEN.toString() : ChatColor.RED.toString()) + serverTutorial.getNeedsPermission() + ""));
        sender.sendMessage(formatInfo(Lang.CHAT_BLOCKED.toString(), (serverTutorial.isChatBlocked() ? ChatColor.DARK_GREEN.toString() : ChatColor.RED.toString()) + serverTutorial.isChatBlocked() + ""));
        sender.sendMessage(formatInfo(Lang.TIMES_PLAYED.toString(), String.valueOf(serverTutorial.plays)));
        sender.sendMessage(formatInfo(Lang.AMOUNT_OF_POINTS.toString(), (serverTutorial.points.size()) + ""));
        sender.sendMessage(" ");
        sender.sendMessage(formatInfo(Lang.BLOCKS_COMMANDS.toString(), (serverTutorial.isBlockingCommands() ? ChatColor.DARK_GREEN.toString() : ChatColor.RED.toString()) + serverTutorial.isBlockingCommands() + ""));
        sender.sendMessage(formatCommand(Lang.WHITELISTED_COMMANDS.toString(), ""));
        if (serverTutorial.getCommandWhiteList().size() == 0)
            sender.sendMessage("   " + ChatColor.YELLOW + Lang.NONE.toString());
        else {
            for (String white : serverTutorial.getCommandWhiteList()) {
                sender.sendMessage("  " + ChatColor.GRAY + "-" + ChatColor.YELLOW + white);
            }
        }
        sender.sendMessage(" ");
        for (int i = 0; i < serverTutorial.points.size(); i++) {
            ServerTutorialPoint point = serverTutorial.points.get(i);
            Location loc = point.getLoc().clone();
            //Location
            sender.sendMessage("   " + ChatColor.GREEN + ChatColor.BOLD.toString() + (i + 1) + ChatColor.GRAY + " - " + ChatColor.YELLOW + loc.getWorld().getName() + " " + loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ());
            //Time
            sender.sendMessage("     " + ChatColor.GREEN + Lang.TIME + ": " + ChatColor.YELLOW + point.getTime() + " " + Lang.SECONDS);
            //Actionbar
            if (point.getMessage_actionBar() != null && !point.getMessage_actionBar().equalsIgnoreCase(""))
                sender.sendMessage("     " + ChatColor.GREEN + "Actionbar: " + ChatColor.YELLOW + point.getMessage_actionBar());
            //Title
            if (point.getTitleInfo() != null) {
                if (!point.getTitleInfo().title.equalsIgnoreCase(""))
                    sender.sendMessage("     " + ChatColor.GREEN + "Title: " + ChatColor.YELLOW + ChatColor.translateAlternateColorCodes('&', point.getTitleInfo().title));
                if (!point.getTitleInfo().subtitle.equalsIgnoreCase(""))
                    sender.sendMessage("     " + ChatColor.GREEN + "Sub Title: " + ChatColor.YELLOW + ChatColor.translateAlternateColorCodes('&', point.getTitleInfo().subtitle));
            }

            if (point.getMessage_chat() != null) {
                if (point.getMessage_chat().size() > 0) {
                    sender.sendMessage("     " + ChatColor.GREEN + Lang.CHAT_MESSAGES + ": ");
                    for (int index = 0; index < point.getMessage_chat().size(); index++) {
                        sender.sendMessage("       " + ChatColor.BLUE + (index + 1) + ": " + ChatColor.YELLOW + ChatColor.translateAlternateColorCodes('&', point.getMessage_chat().get(index)));
                    }
                }
            }

            if (point.getCommands() != null) {
                if (point.getCommands().size() > 0) {
                    sender.sendMessage("     " + ChatColor.GREEN + "Commands: ");
                    for (int index = 0; index < point.getCommands().size(); index++) {
                        sender.sendMessage("       " + ChatColor.BLUE + (index + 1) + ": " + ChatColor.YELLOW + ChatColor.translateAlternateColorCodes('&', point.getCommands().get(index)));
                    }
                }
            }

            sender.sendMessage(" ");
        }
        sender.sendMessage(ChatColor.DARK_GRAY + "+─────────────────────────+");
        return true;
    }

    private String formatCommand(String command, String description) {
        return ChatColor.GREEN + "  " + command + ChatColor.DARK_GRAY + " - " + ChatColor.YELLOW + description;
    }

    private String formatInfo(String command, String description) {
        return ChatColor.GREEN + "  " + command + ChatColor.DARK_GRAY + " : " + ChatColor.YELLOW + description;
    }
}
