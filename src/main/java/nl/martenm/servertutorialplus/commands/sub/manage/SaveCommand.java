package nl.martenm.servertutorialplus.commands.sub.manage;

import net.md_5.bungee.api.ChatColor;
import nl.martenm.servertutorialplus.ServerTutorialPlus;
import nl.martenm.servertutorialplus.language.Lang;
import nl.martenm.simplecommands.SimpleCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class SaveCommand extends SimpleCommand {

    public SaveCommand() {
        super("save", Lang.HELP_SAVE.toString(), "+save", false);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] arrgs) {
        ServerTutorialPlus plugin = ServerTutorialPlus.getInstance();

        plugin.saveSigns();
        plugin.saveTutorials();

        sender.sendMessage(ChatColor.GREEN + "Successfully saved all the tutorials and blocks.");
        return true;
    }
}
