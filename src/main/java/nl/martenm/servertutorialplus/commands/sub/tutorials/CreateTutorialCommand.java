package nl.martenm.servertutorialplus.commands.sub.tutorials;

import net.md_5.bungee.api.ChatColor;
import nl.martenm.servertutorialplus.ServerTutorialPlus;
import nl.martenm.servertutorialplus.language.Lang;
import nl.martenm.servertutorialplus.objects.ServerTutorial;
import nl.martenm.simplecommands.SimpleCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class CreateTutorialCommand extends SimpleCommand {


    public CreateTutorialCommand() {
        super("create", Lang.HELP_CREATE.toString(), "+create", false);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        ServerTutorialPlus plugin = ServerTutorialPlus.getInstance();

        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Wrong usage. Use the command like this: /st create <id>");
            return true;
        }

        String id = args[0];
        for (ServerTutorial st : plugin.serverTutorials) {
            if (st.getId().equalsIgnoreCase(id)) {
                sender.sendMessage(ChatColor.RED + "There already exists a server tutorial with that ID!");
                return true;
            }
        }
        ServerTutorial st = new ServerTutorial(id);
        plugin.serverTutorials.add(st);
        sender.sendMessage(Lang.TUTORIAL_CREATED.toString().replace("%id%", id));
        return true;
    }
}
