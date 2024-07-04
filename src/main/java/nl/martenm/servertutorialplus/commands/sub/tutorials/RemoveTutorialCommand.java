package nl.martenm.servertutorialplus.commands.sub.tutorials;

import nl.martenm.servertutorialplus.ServerTutorialPlus;
import nl.martenm.servertutorialplus.language.Lang;
import nl.martenm.servertutorialplus.objects.ServerTutorial;
import nl.martenm.simplecommands.SimpleCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class RemoveTutorialCommand extends SimpleCommand {

    public RemoveTutorialCommand() {
        super("remove", Lang.HELP_REMOVE.toString(), "+remove", false);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        ServerTutorialPlus plugin = ServerTutorialPlus.getInstance();

        if (args.length == 0) {
            sender.sendMessage(Lang.WRONG_COMMAND_FORMAT + "/st remove <id>");
            return true;
        }

        for (ServerTutorial st : plugin.serverTutorials) {
            if (st.getId().equalsIgnoreCase(args[0])) {
                plugin.serverTutorials.remove(st);
                sender.sendMessage(Lang.TUTORIAL_REMOVED.toString().replace("%id%", args[0]));
                plugin.tutorialSaves.set("tutorials." + st.getId(), null);
                plugin.tutorialSaves.save();
                return true;
            }
        }

        sender.sendMessage(Lang.SAVE_SUCCES.toString());
        return true;
    }
}
