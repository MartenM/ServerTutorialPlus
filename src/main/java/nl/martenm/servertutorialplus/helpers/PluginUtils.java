package nl.martenm.servertutorialplus.helpers;

import nl.martenm.servertutorialplus.ServerTutorialPlus;
import nl.martenm.servertutorialplus.objects.NPCInfo;
import nl.martenm.servertutorialplus.objects.ServerTutorial;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Marten on 7-3-2017.
 */
public abstract class PluginUtils {

    public static ServerTutorial getTutorial(ServerTutorialPlus plugin, String ID) {
        if (ID == null) return null;
        for (ServerTutorial serverTutorial : plugin.serverTutorials) {
            if (serverTutorial.getId().equalsIgnoreCase(ID)) {
                return serverTutorial;
            }
        }

        //Nothing found!
        return null;
    }

    public static List<Location> getHollowCube(Location corner1, Location corner2, double particleDistance) {
        List<Location> result = new ArrayList<>();
        World world = corner1.getWorld();
        double minX = Math.min(corner1.getX(), corner2.getX());
        double minY = Math.min(corner1.getY(), corner2.getY());
        double minZ = Math.min(corner1.getZ(), corner2.getZ());
        double maxX = Math.max(corner1.getX(), corner2.getX());
        double maxY = Math.max(corner1.getY(), corner2.getY());
        double maxZ = Math.max(corner1.getZ(), corner2.getZ());

        for (double x = minX; x <= maxX; x += particleDistance) {
            for (double y = minY; y <= maxY; y += particleDistance) {
                for (double z = minZ; z <= maxZ; z += particleDistance) {
                    int components = 0;
                    if (x == minX || x == maxX) components++;
                    if (y == minY || y == maxY) components++;
                    if (z == minZ || z == maxZ) components++;
                    if (components >= 2) {
                        result.add(new Location(world, x, y, z));
                    }
                }
            }
        }

        return result;
    }

    public static Location fromString(ServerTutorialPlus plugin, String message) {
        String[] data = message.split(" ");
        String world = data[0];
        double x = Double.parseDouble(data[1]);
        double y = Double.parseDouble(data[2]);
        double z = Double.parseDouble(data[3]);
        float yaw = Float.parseFloat(data[4]);
        float pitch = Float.parseFloat(data[5]);
        return new Location(plugin.getServer().getWorld(world), x, y, z, yaw, pitch);

    }

    public static String fromLocation(Location loc) {
        return loc.getWorld().getName() + " " +
                loc.getX() + " " +
                loc.getY() + " " +
                loc.getZ() + " " +
                loc.getYaw() + " " +
                loc.getPitch();
    }

    public static String replaceVariables(boolean PlaceHolders, Player player, String message) {
        if (PlaceHolders) {
            return PlaceholderAPI.setPlaceholders(player, replaceIntern(player, message));
        } else {
            return ChatColor.translateAlternateColorCodes('&', replaceIntern(player, message));
        }
    }

    private static String replaceIntern(Player player, String message) {
        return message
                .replace("{player_name}", player.getName())
                .replaceAll("%user_?name%", player.getName());
    }

    public static String allMobs() {
        return "VILLAGER, ZOMBIE, HUSK, WITCH, SPIDER, SLIME, SKELETON, CREEPER, PIG_ZOMBIE, BLAZE, CAVE_SPIDER, ENDERMAN, BAT, MAGMA_CUBE, WITHER, RABBIT, PIG, COW, SHEEP, CHICKEN, WOLF, ENDERMITE, BLAZE, GUARDIAN, HORSE, POLAR_BEAR";
    }

}
