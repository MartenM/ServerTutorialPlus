package nl.martenm.servertutorialplus.helpers;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;

import java.util.UUID;

/**
 * Created by Marten on 4-6-2017.
 */
public class SpigotUtils {

    private SpigotUtils() {
        //Empty constructor.
    }

    public static Entity getEntity(UUID uuid) {
        if (Bukkit.getVersion().contains("1.8") || Bukkit.getVersion().contains("1.9") || Bukkit.getVersion().contains("1.10")) {
            for (World world : Bukkit.getServer().getWorlds()) {
                for (Entity entity : world.getEntities()) {
                    if (entity.getUniqueId().equals(uuid)) {
                        return entity;
                    }
                }
            }
            return null;
        }
        return Bukkit.getEntity(uuid);
    }
}
