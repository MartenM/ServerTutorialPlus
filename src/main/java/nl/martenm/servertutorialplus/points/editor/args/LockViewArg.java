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
public class LockViewArg extends PointArg {

    public LockViewArg() {
        super("lockview");
    }

    @Override
    public boolean run(ServerTutorial serverTutorial, ServerTutorialPoint point, CommandSender sender, String[] args) {

        if (args.length < 1) {
            sender.sendMessage(Lang.WRONG_COMMAND_FORMAT + "/st editpoint <t> <p> lockview <false/true>");
            return false;
        }

        try {
            point.setLockView(Boolean.parseBoolean(args[0]));
        } catch (Exception ex) {
            sender.sendMessage(Lang.WRONG_COMMAND_FORMAT + "/st editpoint <t> <p> lockview <false/true>");
            return false;
        }
        return true;
    }

}
