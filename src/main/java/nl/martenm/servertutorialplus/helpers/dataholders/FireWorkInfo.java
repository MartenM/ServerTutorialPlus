package nl.martenm.servertutorialplus.helpers.dataholders;

import org.bukkit.Location;
import org.bukkit.inventory.meta.FireworkMeta;

/**
 * Created by Marten on 19-3-2017.
 */
public class FireWorkInfo {

    private Location location;
    private FireworkMeta fireworkMeta;

    public FireWorkInfo(Location location, FireworkMeta fireworkMeta) {
        this.fireworkMeta = fireworkMeta;
        this.location = location;
    }

    public Location getLoc() {
        return location;
    }

    public FireworkMeta getFireworkMeta() {
        return fireworkMeta;
    }
}
