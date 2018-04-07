package nl.martenm.servertutorialplus.points.editor;

import nl.martenm.servertutorialplus.objects.ServerTutorial;
import nl.martenm.servertutorialplus.points.ServerTutorialPoint;
import org.bukkit.command.CommandSender;

/**
 * @author MartenM
 * @since 29-11-2017.
 */
public interface IPointArg {

    boolean run(ServerTutorial serverTutorial, ServerTutorialPoint point, CommandSender sender, String[] args);

}
