package nl.martenm.servertutorialplus.helpers;

import nl.martenm.servertutorialplus.MainClass;
import net.md_5.bungee.api.ChatColor;

/**
 * Created by Marten on 5-3-2017.
 */
public class Messages {

    public static String noPermissionTutorial(MainClass plugin){
        return ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("no permission tutorial"));
    }
}
