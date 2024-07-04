package nl.martenm.servertutorialplus.points.editor.args;

import nl.martenm.servertutorialplus.language.Lang;
import nl.martenm.servertutorialplus.objects.ServerTutorial;
import nl.martenm.servertutorialplus.points.ServerTutorialPoint;
import nl.martenm.servertutorialplus.points.editor.PointArg;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.CommandSender;

/**
 * @author MartenM
 * @since 29-11-2017.
 */
public class ActionbarArg extends PointArg {

    public ActionbarArg() {
        super("actionbar");
    }

    @Override
    public boolean run(ServerTutorial serverTutorial, ServerTutorialPoint point, CommandSender sender, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(Lang.WRONG_COMMAND_FORMAT + "/st editpoint <t> <p> actionbar <clear/set>");
            return false;
        }

        switch (args[0]) {
            case "clear":
                point.setMessage_actionBar(null);
                break;
            case "set":
                if (args.length < 2) {
                    sender.sendMessage(Lang.ERROR_ATLEAST_ONE_WORD.toString());
                    return false;
                }

                String message = StringUtils.join(args, ' ', 1, args.length);
                point.setMessage_actionBar(message);
                break;

            default:
                sender.sendMessage(Lang.WRONG_COMMAND_FORMAT + "/st editpoint <t> <p> actionbar <clear/set>");
                return false;
        }
        return true;
    }
}
