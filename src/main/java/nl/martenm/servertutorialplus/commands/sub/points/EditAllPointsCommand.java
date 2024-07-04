package nl.martenm.servertutorialplus.commands.sub.points;

import nl.martenm.servertutorialplus.ServerTutorialPlus;
import nl.martenm.servertutorialplus.helpers.PluginUtils;
import nl.martenm.servertutorialplus.language.Lang;
import nl.martenm.servertutorialplus.objects.ServerTutorial;
import nl.martenm.servertutorialplus.points.ServerTutorialPoint;
import nl.martenm.servertutorialplus.points.custom.TimedPoint;
import nl.martenm.servertutorialplus.points.editor.PointEditor;
import nl.martenm.simplecommands.SimpleCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class EditAllPointsCommand extends SimpleCommand {

    public EditAllPointsCommand() {
        super("editall", Lang.HELP_EDITALL.toString(), "+edit", false);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        ServerTutorialPlus plugin = ServerTutorialPlus.getInstance();

        if (args.length < 1) {
            sender.sendMessage(Lang.WRONG_COMMAND_FORMAT + "/st editall <server tutorial ID> <args>");
            return true;
        }

        ServerTutorial serverTutorial = PluginUtils.getTutorial(plugin, args[0]);

        if (serverTutorial == null) {
            sender.sendMessage(Lang.TUTORIAL_ID_NOT_FOUND.toString());
            return true;
        }

        if (args.length == 1) {
            sender.sendMessage(Lang.COMMAND_EDITALL_CANBE.toString().replace("%args%", ServerTutorialPoint.getArgsString(new TimedPoint(null, null).getArgs())));
            return true;
        }

        String[] arguments = new String[args.length + 1];
        System.arraycopy(args, 0, arguments, 1, args.length);

        for (ServerTutorialPoint point : serverTutorial.points) {
            PointEditor pointEditor = PointEditor.getPointeditor(point);
            if (!pointEditor.execute(serverTutorial, point, sender, arguments)) {
                sender.sendMessage(Lang.ERROR_EDITALL_FAIL.toString());
                return true;
            }
        }
        sender.sendMessage(Lang.COMMAND_EDITALL_SUCCES.toString());
        return true;
    }
}
