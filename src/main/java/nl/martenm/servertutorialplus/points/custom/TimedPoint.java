package nl.martenm.servertutorialplus.points.custom;

import nl.martenm.servertutorialplus.ServerTutorialPlus;
import nl.martenm.servertutorialplus.points.PointType;
import nl.martenm.servertutorialplus.points.ServerTutorialPoint;
import org.bukkit.Location;

/**
 * @author MartenM
 * @since 22-11-2017.
 */
public class TimedPoint extends ServerTutorialPoint {

    public TimedPoint(ServerTutorialPlus plugin, Location loc) {
        super(plugin, loc, PointType.TIMED, true);
    }

    //Timed point is basically just a default point without additions.
}
