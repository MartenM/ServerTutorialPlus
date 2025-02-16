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
        if(args.length < 1){
            sender.sendMessage(Lang.WRONG_COMMAND_FORMAT + "/st editpoint <t> <p> actionbar <clear/message/show-after/hide-after> <args>");
            return false;
        }

        switch (args[0]) {
            case "clear":
                point.setMessageActionbar(null);
                break;
            case "message":
                if (args.length < 2) {
                    sender.sendMessage(Lang.ERROR_ATLEAST_ONE_WORD.toString());
                    return false;
                }

                String message = StringUtils.join(args, ' ', 1, args.length);
                point.setMessageActionbar(message);
                break;
            case "show-after":
                double showAfter;
                try {
                    showAfter = Double.parseDouble(args[1]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(Lang.ERROR_INVALID_NUMBNER.toString());
                    return false;
                }
                point.setActionbarShowAfter(showAfter);
                break;
            case "hide-after":
                double hideAfter;
                try {
                    hideAfter = Double.parseDouble(args[1]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(Lang.ERROR_INVALID_NUMBNER.toString());
                    return false;
                }
                point.setActionbarHideAfter(hideAfter);
                break;
            default:
                sender.sendMessage(Lang.WRONG_COMMAND_FORMAT + "/st editpoint <t> <p> actionbar <clear/set/show-after/hide-after>");
                return false;
        }
        return true;
    }
}
