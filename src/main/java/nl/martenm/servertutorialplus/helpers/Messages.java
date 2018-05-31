package nl.martenm.servertutorialplus.helpers;

import nl.martenm.servertutorialplus.ServerTutorialPlus;
import net.md_5.bungee.api.ChatColor;

/**
 * Created by Marten on 5-3-2017.
 */
public class Messages {

    public static String noPermissionTutorial(ServerTutorialPlus plugin){
        return ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("no permission tutorial"));
    }
}
