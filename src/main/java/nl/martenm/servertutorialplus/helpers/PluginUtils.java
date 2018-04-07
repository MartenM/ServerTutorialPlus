package nl.martenm.servertutorialplus.helpers;

import nl.martenm.servertutorialplus.MainClass;
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

    public static ServerTutorial getTutorial(MainClass plugin, String ID){
        if (ID == null) return null;
        for(ServerTutorial serverTutorial : plugin.serverTutorials){
            if(serverTutorial.getId().equalsIgnoreCase(ID)){
                return serverTutorial;
            }
        }

        //Nothing found!
        return null;
    }

    public static NPCInfo getNPC(MainClass plugin, String ID){
        if(ID == null) return null;
        for(NPCInfo info : plugin.clickableNPCs.values()){
            if(info.getId().equalsIgnoreCase(ID)){
                return info;
            }
        }
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

        for (double x = minX; x <= maxX; x+=particleDistance) {
            for (double y = minY; y <= maxY; y+=particleDistance) {
                for (double z = minZ; z <= maxZ; z+=particleDistance) {
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

    public static Location fromString(MainClass plugin, String message){
            String[] data = message.split(" ");
            String world = data[0];
            double x = Double.parseDouble(data[1]);
            double y = Double.parseDouble(data[2]);
            double z = Double.parseDouble(data[3]);
            float yaw = Float.parseFloat(data[4]);
            float pitch = Float.parseFloat(data[5]);
            return new Location(plugin.getServer().getWorld(world), x, y, z, yaw, pitch);

    }

    public static String fromLocation(Location loc){
        return loc.getWorld().getName() + " " +
                loc.getX() + " " +
                loc.getY() + " " +
                loc.getZ() + " " +
                loc.getYaw() + " " +
                loc.getPitch();
    }

    public static String replaceVariables(boolean PlaceHolders, Player player, String message){
        if(PlaceHolders){
            return PlaceholderAPI.setPlaceholders(player, message.replace("{player_name}", player.getName()));
        } else{
            return ChatColor.translateAlternateColorCodes('&', message.replace("{player_name}", player.getName()));
        }
    }

    public static String allMobs(){
        return "VILLAGER, ZOMBIE, HUSK, WITCH, SPIDER, SLIME, SKELETON, CREEPER, PIG_ZOMBIE, BLAZE, CAVE_SPIDER, ENDERMAN, BAT, MAGMA_CUBE, WITHER, RABBIT, PIG, COW, SHEEP, CHICKEN, WOLF, ENDERMITE, BLAZE, GUARDIAN, HORSE, POLAR_BEAR";
    }

    public static boolean createNpc(MainClass plugin, LivingEntity entity, String id, Player player, ServerTutorial tutorial) {

        if (!(entity instanceof LivingEntity)) {
            player.sendMessage(ChatColor.RED + "The entity has to be of a living type!");
            entity.remove();
            return true;
        }

        LivingEntity npc = (LivingEntity) entity;

        ArmorStand armorStand_1 = (ArmorStand) player.getWorld().spawnEntity(player.getLocation(), EntityType.ARMOR_STAND);
        armorStand_1.setGravity(false);

        if (Bukkit.getVersion().contains("1.8") || Bukkit.getVersion().contains("1.9") || Bukkit.getVersion().contains("1.10")) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "  "));
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7(&c!&7) &eSpigot version < 1.11. Using alternative text method."));
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aTIP: &7Use /st npc set height <npc ID> to set the text heigh relative to the NPC."));
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "  "));
            armorStand_1.teleport(npc.getLocation().add(0, 0.45, 0));

            new BukkitRunnable() {
                @Override
                public void run() {
                    Location loc = armorStand_1.getLocation();

                    ArmorStand armorStand_2 = (ArmorStand) loc.getWorld().spawnEntity(loc.add(0, 0.45, 0), EntityType.ARMOR_STAND);
                    armorStand_2.setGravity(false);

                    armorStand_1.setMarker(true);
                    armorStand_2.setMarker(true);

                    armorStand_1.setVisible(false);
                    armorStand_2.setVisible(false);

                    armorStand_1.setCustomNameVisible(true);
                    armorStand_2.setCustomNameVisible(true);

                    armorStand_1.setCustomName(ChatColor.YELLOW + ChatColor.BOLD.toString() + "Right click!");
                    armorStand_2.setCustomName(ChatColor.GREEN + "Tutorial");

                    armorStand_1.teleport(loc.add(0, -0.25, 0));

                    npc.setInvulnerable(true);
                    armorStand_1.setInvulnerable(true);
                    armorStand_2.setInvulnerable(true);

                    NPCInfo info = new NPCInfo(plugin, id, npc.getUniqueId(), new UUID[]{armorStand_1.getUniqueId(), armorStand_2.getUniqueId()}, tutorial.getId());
                    plugin.clickableNPCs.put(npc.getUniqueId(), info);

                    npc.teleport(npc.getLocation());
                    player.sendMessage(ChatColor.GREEN + "Successfully created a NPC with NPC ID: " + ChatColor.YELLOW + info.getId() + ChatColor.GREEN + " that plays server tutorial: " + ChatColor.YELLOW + info.getServerTutorialID());
                }
            }.runTaskLater(plugin, 1);

            if (Bukkit.getVersion().contains("1.8") || Bukkit.getVersion().contains("1.9") || Bukkit.getVersion().contains("1.10")) {
                new BukkitRunnable() {
                    Location loc = npc.getLocation();

                    @Override
                    public void run() {
                        try {
                            npc.teleport(loc);
                            npc.setVelocity(new Vector(0, 0, 0));
                        } catch (Exception ex) {
                            this.cancel();
                        }
                    }
                }.runTaskTimer(plugin, 0, 5);
            }

        } else {
            npc.addPassenger(armorStand_1);

            new BukkitRunnable() {
                @Override
                public void run() {
                    //Do stuff
                    Location loc = armorStand_1.getLocation();
                    npc.removePassenger(armorStand_1);

                    ArmorStand armorStand_2 = (ArmorStand) loc.getWorld().spawnEntity(loc.add(0, 0.45, 0), EntityType.ARMOR_STAND);
                    armorStand_2.setGravity(false);

                    armorStand_1.setMarker(true);
                    armorStand_2.setMarker(true);

                    armorStand_1.setVisible(false);
                    armorStand_2.setVisible(false);

                    armorStand_1.setCustomNameVisible(true);
                    armorStand_2.setCustomNameVisible(true);

                    armorStand_1.setCustomName(ChatColor.YELLOW + ChatColor.BOLD.toString() + "Right click!");
                    armorStand_2.setCustomName(ChatColor.GREEN + "Tutorial");

                    armorStand_1.teleport(loc.add(0, -0.25, 0));

                    npc.setInvulnerable(true);
                    armorStand_1.setInvulnerable(true);
                    armorStand_2.setInvulnerable(true);

                    NPCInfo info = new NPCInfo(plugin, id, npc.getUniqueId(), new UUID[]{armorStand_1.getUniqueId(), armorStand_2.getUniqueId()}, tutorial.getId());
                    plugin.clickableNPCs.put(npc.getUniqueId(), info);

                    npc.teleport(npc.getLocation());
                    player.sendMessage(ChatColor.GREEN + "Successfully created a NPC with NPC ID: " + ChatColor.YELLOW + info.getId() + ChatColor.GREEN + " that plays server tutorial: " + ChatColor.YELLOW + info.getServerTutorialID());
                    //Do stuff
                }
            }.runTaskLater(plugin, 1);
        }
        return true;
    }
}
