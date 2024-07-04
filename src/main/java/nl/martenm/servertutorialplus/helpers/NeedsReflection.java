package nl.martenm.servertutorialplus.helpers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;

/**
 * Class for reflection for certain methods.
 * Credits to: bramhaag.
 */
public class NeedsReflection {

    /**
     * Send a title to player
     *
     * @param player      Player to send the title to
     * @param text        The text displayed in the title
     * @param fadeInTime  The time the title takes to fade in
     * @param showTime    The time the title is displayed
     * @param fadeOutTime The time the title takes to fade out
     * @param color       The color of the title
     */
    public static void sendTitle(Player player, String text, int fadeInTime, int showTime, int fadeOutTime, ChatColor color) {
        try {
            String title_text = net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', text);

            Constructor timeConstructor = getNMSClass("PacketPlayOutTitle").getConstructor(int.class, int.class, int.class);

            Object enumTitle = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TITLE").get(null);
            Object title = getNMSClass("IChatBaseComponent$ChatSerializer").getMethod("a", String.class).invoke(null, "{\"text\": \"" + title_text + "\"}");
            Constructor titleConstuctor = getNMSClass("PacketPlayOutTitle").getConstructor(getNMSClass("PacketPlayOutTitle$EnumTitleAction"), getNMSClass("IChatBaseComponent"), int.class, int.class, int.class);
            Object packet = titleConstuctor.newInstance(enumTitle, title, fadeInTime, showTime, fadeOutTime);

            Object packetTimes = timeConstructor.newInstance(fadeInTime, showTime, fadeOutTime);

            sendPacket(player, packetTimes);
            sendPacket(player, packet);
        } catch (Exception ex) {
            ex.printStackTrace();            //Do something
        }
    }

    public static void sendSubTitle(Player player, String text, int fadeInTime, int showTime, int fadeOutTime, ChatColor color) {
        try {
            String title_text = net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', text);

            Object enumTitle = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("SUBTITLE").get(null);
            Object title = getNMSClass("IChatBaseComponent$ChatSerializer").getMethod("a", String.class).invoke(null, "{\"text\": \"" + title_text + "\"}");

            Constructor titleConstuctor = getNMSClass("PacketPlayOutTitle").getConstructor(getNMSClass("PacketPlayOutTitle$EnumTitleAction"), getNMSClass("IChatBaseComponent"), int.class, int.class, int.class);
            Object packet = titleConstuctor.newInstance(enumTitle, title, fadeInTime, showTime, fadeOutTime);
            sendPacket(player, packet);
        } catch (Exception ex) {
            ex.printStackTrace();            //Do something
        }
    }

    public static void sendActionBar(Player player, String text) {
        try {
            String title_text = net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', text);

            Object enumTitle = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("ACTIONBAR").get(null);
            Object title = getNMSClass("IChatBaseComponent$ChatSerializer").getMethod("a", String.class).invoke(null, "{\"text\": \"" + title_text + "\"}");

            Constructor titleConstuctor = getNMSClass("PacketPlayOutTitle").getConstructor(getNMSClass("PacketPlayOutTitle$EnumTitleAction"), getNMSClass("IChatBaseComponent"));
            Object packet = titleConstuctor.newInstance(enumTitle, title);
            sendPacket(player, packet);
        } catch (Exception ex) {
            ex.printStackTrace();            //Do something
        }
    }

    private static void sendPacket(Player player, Object packet) {
        try {
            Object handle = player.getClass().getMethod("getHandle").invoke(player);
            Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
            playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(playerConnection, packet);
        } catch (Exception ex) {
            //Do something
        }
    }

    /**
     * Get NMS class using reflection
     *
     * @param name Name of the class
     * @return Class
     */
    private static Class<?> getNMSClass(String name) {
        try {
            return Class.forName("net.minecraft.server." + Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3] + "." + name);
        } catch (ClassNotFoundException ex) {
            //Do something
            System.out.println(ex.toString());
            return null;
        }
    }

}
