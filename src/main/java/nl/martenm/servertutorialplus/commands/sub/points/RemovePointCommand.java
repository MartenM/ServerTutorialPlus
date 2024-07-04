package nl.martenm.servertutorialplus.commands.sub.points;

import nl.martenm.servertutorialplus.ServerTutorialPlus;
import nl.martenm.servertutorialplus.language.Lang;
import nl.martenm.servertutorialplus.objects.ServerTutorial;
import nl.martenm.simplecommands.SimpleCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class RemovePointCommand extends SimpleCommand {

    public RemovePointCommand() {
        super("removepoint", Lang.HELP_REMOVEPOINT.toString(), "+removepoint", false);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        ServerTutorialPlus plugin = ServerTutorialPlus.getInstance();

        if (args.length < 2) {
            sender.sendMessage(Lang.WRONG_COMMAND_FORMAT + "/st removepoint <server tutorial ID> <point index>");
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

        int index;
        try {
            index = Integer.valueOf(args[1]);
        } catch (Exception e) {
            sender.sendMessage(Lang.COMMAND_HASTOBE_NUMBER.toString());
            return true;
        }

        if (index - 1 < 0 || index > serverTutorial.points.size()) {
            sender.sendMessage(Lang.COMMAND_INVALID_INDEX.toString());
            return true;
        }

        serverTutorial.points.remove(index - 1);
        sender.sendMessage(Lang.POINT_REMOVED.toString().replace("%id%", args[0]));
        return true;
    }
}
