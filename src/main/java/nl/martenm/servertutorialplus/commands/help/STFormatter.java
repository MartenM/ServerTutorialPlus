package nl.martenm.servertutorialplus.commands.help;

import net.md_5.bungee.api.ChatColor;
import nl.martenm.simplecommands.SimpleCommand;
import nl.martenm.simplecommands.SimpleHelpFormatter;
import org.bukkit.command.CommandSender;

import java.util.List;

public class STFormatter extends SimpleHelpFormatter {


    @Override
    public void sendHelp(CommandSender sender, List<SimpleCommand> subCommands) {
        sender.sendMessage(ChatColor.DARK_GRAY + "+──────┤ " + ChatColor.GREEN + ChatColor.BOLD + "Server Tutorial" + ChatColor.GOLD + " + " + ChatColor.DARK_GRAY + "├──────+");
        sender.sendMessage(" ");

        for (SimpleCommand command : subCommands) {
            if (!command.hasDescription()) continue;
            sender.sendMessage(formatCommand(command));
        }

        sender.sendMessage(" ");
        sender.sendMessage(ChatColor.DARK_GRAY + "+──────────────────────────+");
    }

    private String formatCommand(SimpleCommand command) {
        return ChatColor.GREEN + "  /" + command.getFullName() + ChatColor.DARK_GRAY + " - " + ChatColor.YELLOW + command.getDescription();
    }
}
