package nl.martenm.servertutorialplus.points.editor.args;

import nl.martenm.servertutorialplus.language.Lang;
import nl.martenm.servertutorialplus.objects.ServerTutorial;
import nl.martenm.servertutorialplus.points.ServerTutorialPoint;
import nl.martenm.servertutorialplus.points.editor.PointArg;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.CommandSender;

/**
 * @author Multitallented
 * @since 25-11-2018.
 */
public class TriggerCommandArg extends PointArg {

    public TriggerCommandArg() {
        super("triggerCommand");
    }

    @Override
    public boolean run(ServerTutorial serverTutorial, ServerTutorialPoint point, CommandSender sender, String[] args) {

        if(args.length < 1){
            sender.sendMessage(Lang.WRONG_COMMAND_FORMAT.toString() + "/st editpoint <t> <p> triggerCommands <command args here>");
            return false;
        }

        String message = StringUtils.join(args, ' ', 0, args.length);
        point.setTriggerCommand(message);
        return true;
    }
}
