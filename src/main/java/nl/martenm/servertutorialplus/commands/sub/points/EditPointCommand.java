package nl.martenm.servertutorialplus.commands.sub.points;

import net.md_5.bungee.api.ChatColor;
import nl.martenm.servertutorialplus.ServerTutorialPlus;
import nl.martenm.servertutorialplus.helpers.PluginUtils;
import nl.martenm.servertutorialplus.language.Lang;
import nl.martenm.servertutorialplus.objects.ServerTutorial;
import nl.martenm.servertutorialplus.points.ServerTutorialPoint;
import nl.martenm.servertutorialplus.points.editor.PointEditor;
import nl.martenm.simplecommands.SimpleCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class EditPointCommand extends SimpleCommand {

    public EditPointCommand() {
        super("editpoint", Lang.HELP_EDITPOINT.toString(), "+edit", false);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        ServerTutorialPlus plugin = ServerTutorialPlus.getInstance();

        if (args.length < 2) {
            sender.sendMessage(Lang.WRONG_COMMAND_FORMAT + "/st editpoint <server tutorial ID> <point index> <args>");
            return true;
        }

        int index;
        try {
            index = Integer.parseInt(args[1]);
        } catch (Exception e) {
            sender.sendMessage(Lang.WRONG_COMMAND_FORMAT + "/st editpoint <server tutorial ID> <point index> <args>");
            return true;
        }

        ServerTutorial serverTutorial = PluginUtils.getTutorial(plugin, args[0]);

        if (serverTutorial == null) {
            sender.sendMessage(Lang.TUTORIAL_ID_NOT_FOUND.toString());
            return true;
        }

        if (index - 1 >= serverTutorial.points.size()) {
            sender.sendMessage(Lang.ERROR_INVALID_POINT.toString());
            return true;
        }

        if (index < 1) {
            sender.sendMessage(Lang.ERROR_INVALID_INDEX.toString());
            return true;
        }

        ServerTutorialPoint tutorialPoint = serverTutorial.points.get(index - 1);

        if (args.length == 2) {
            sender.sendMessage(Lang.UNKOWN_ARGUMENT.toString() + ChatColor.GRAY + tutorialPoint.getArgsString());
            return true;
        }

        if (args[2].equalsIgnoreCase("switch")) {
            if (args.length < 4) {
                sender.sendMessage(Lang.WRONG_COMMAND_FORMAT + "/steditpoint <id> <p> switch <to>");
                return true;
            }

            int to;
            try {
                to = Integer.parseInt(args[3]);
            } catch (Exception ex) {
                sender.sendMessage(Lang.WRONG_COMMAND_FORMAT + "/st editpoint <id> <p> switch <to>");
                return true;
            }

            ServerTutorialPoint point = serverTutorial.points.get(index - 1);
            serverTutorial.points.set(index - 1, serverTutorial.points.get(to - 1));
            serverTutorial.points.set(to - 1, point);
            sender.sendMessage(Lang.COMMAND_SWITCH_SUCCESSFUL.toString().replace("%1%", (index - 1) + "").replace("%2&", to + ""));
            return true;
        }

        if (args[2].equalsIgnoreCase("infront")) {
            if (args.length < 4) {
                sender.sendMessage(Lang.WRONG_COMMAND_FORMAT + "/st editpoint <id> <p> infront <p>");
                return true;
            }

            int to;
            try {
                to = Integer.parseInt(args[3]);
            } catch (Exception ex) {
                sender.sendMessage(Lang.WRONG_COMMAND_FORMAT + "/st editpoint <id> <p> infront <p>");
                return true;
            }

            ServerTutorialPoint point = serverTutorial.points.get(index - 1);
            serverTutorial.points.remove(index - 1);
            serverTutorial.points.add(to - 1, point);
            sender.sendMessage(Lang.COMMAND_MOVE_INFRONT_SUCCESFULL.toString().replace("%1%", (index - 1) + "").replace("%2&", to + ""));
            return true;
        }


        PointEditor pointEditor = PointEditor.getPointeditor(tutorialPoint);
        pointEditor.execute(serverTutorial, tutorialPoint, sender, args);
        return true;
    }
}
