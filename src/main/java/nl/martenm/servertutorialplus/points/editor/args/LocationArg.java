package nl.martenm.servertutorialplus.points.editor.args;

import nl.martenm.servertutorialplus.objects.ServerTutorial;
import nl.martenm.servertutorialplus.points.ServerTutorialPoint;
import nl.martenm.servertutorialplus.points.editor.PointArg;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Argument to change the point location.
 *
 * @author MartenM
 * @since 29-11-2017.
 */
public class LocationArg extends PointArg {

    public LocationArg() {
        super("location");
    }

    @Override
    public boolean run(ServerTutorial serverTutorial, ServerTutorialPoint point, CommandSender sender, String[] args) {

        if (!(sender instanceof Player)) {
            return false;
        }

        Player player = (Player) sender;

        point.setLoc(player.getLocation());
        return true;
    }
}
