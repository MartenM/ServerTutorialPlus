package nl.martenm.servertutorialplus.hooks;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.OfflinePlayer;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import nl.martenm.servertutorialplus.ServerTutorialPlus;
import nl.martenm.servertutorialplus.objects.TutorialController;

/**
 * @author alan67160
 * @since 30-09-2023.
 */
public class PlaceholderAPIExpansion extends PlaceholderExpansion {
    
    private final ServerTutorialPlus plugin;
    
    public PlaceholderAPIExpansion(ServerTutorialPlus plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }
    
    @Override
    public String getIdentifier() {
        return plugin.getDescription().getName();
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }
    
    @Override
    public boolean persist() {
        return true; // This is required or else PlaceholderAPI will unregister the Expansion on reload
    }
    
    @Override
    public String onRequest(OfflinePlayer player, String params) {
        // if(params.equalsIgnoreCase("get")){

        // }
        // the following part is for online player only
        if (!player.isOnline()) return null;
        HashMap<UUID, TutorialController> tutorialMap = plugin.inTutorial;
        if (tutorialMap.containsKey(player.getUniqueId())) {
            TutorialController tc = tutorialMap.get(player.getUniqueId());
            if(params.equalsIgnoreCase("getTutorial")) {
                // get the tutorial name of the current tutorial
                return tc.getTutorial().getId();
            }
            if(params.equalsIgnoreCase("getPoints")) {
                // get the point index of the current tutorial
                return String.valueOf(tc.getCurrentPoint());
            }
            if(params.equalsIgnoreCase("getMaxPoints")) {
                // get the max point index of the current tutorial
                return String.valueOf(tc.getTutorial().points.size() -1);
            }
        }
        // unknown placeholder
        return null;
    }

}
