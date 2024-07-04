package nl.martenm.servertutorialplus.api.hooks;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import nl.martenm.servertutorialplus.ServerTutorialPlus;
import nl.martenm.servertutorialplus.objects.TutorialController;
import org.bukkit.OfflinePlayer;

import java.util.HashMap;
import java.util.UUID;

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
        // the following part is for online player only
        if (!player.isOnline()) return null;
        HashMap<UUID, TutorialController> tutorialMap = plugin.inTutorial;

        TutorialController tc = tutorialMap.get(player.getUniqueId());

        String replacement = inTutorialPlaceholders(tc, params);
        if (replacement != null) return replacement;

        // Space for future placeholders

        // unknown placeholder
        return null;
    }

    /**
     * Placeholders that are available when a TC is found.
     *
     * @param tc     The tutorial controller of the player. Can be NULL.
     * @param params The parameter.
     * @return A string if the placeholder is found.
     */
    private String inTutorialPlaceholders(TutorialController tc, String params) {
        if (params.equalsIgnoreCase("inTutorialBoolean")) {
            return tc == null ? "true" : "false";
        }

        if (params.equalsIgnoreCase("getTutorial")) {
            // get the tutorial name of the current tutorial
            if (tc == null) return "Unavailable";
            return tc.getTutorial().getId();
        }

        if (params.equalsIgnoreCase("getPoint")) {
            // get the point index of the current tutorial
            if (tc == null) return "Unavailable";

            // Increase by 1 for none programmers!
            return String.valueOf(tc.getCurrentPoint() + 1);
        }

        if (params.equalsIgnoreCase("getMaxPoints")) {
            // get the max point index of the current tutorial
            if (tc == null) return "Unavailable";
            return String.valueOf(tc.getTutorial().points.size() - 1);
        }

        return null;
    }

}
