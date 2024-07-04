package nl.martenm.servertutorialplus.helpers;

import net.md_5.bungee.api.ChatColor;
import nl.martenm.servertutorialplus.ServerTutorialPlus;

/**
 * Created by Marten on 5-3-2017.
 */
public class Messages {

    public static String noPermissionTutorial(ServerTutorialPlus plugin) {
        return ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("no permission tutorial"));
    }
}
