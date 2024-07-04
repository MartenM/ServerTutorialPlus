package nl.martenm.servertutorialplus.points.editor.args;

import nl.martenm.servertutorialplus.language.Lang;
import nl.martenm.servertutorialplus.objects.ServerTutorial;
import nl.martenm.servertutorialplus.points.ServerTutorialPoint;
import nl.martenm.servertutorialplus.points.editor.PointArg;
import org.bukkit.command.CommandSender;

/**
 * @author MartenM
 * @since 30-11-2017.
 */
public class TimeArg extends PointArg {

    public TimeArg() {
        super("time");
    }

    @Override
    public boolean run(ServerTutorial serverTutorial, ServerTutorialPoint point, CommandSender sender, String[] args) {

        if (args.length < 1) {
            sender.sendMessage(Lang.TIME_CURRENT.toString().replace("%time%", point.getTime() + ""));
            return true;
        }

        try {
            point.setTime(Integer.parseInt(args[0]));
        } catch (NumberFormatException ex) {
            sender.sendMessage(Lang.ERROR_INVALID_NUMBNER.toString());
            return false;
        }

        return true;
    }
}
