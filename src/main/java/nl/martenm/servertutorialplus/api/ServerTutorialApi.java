package nl.martenm.servertutorialplus.api;

import nl.martenm.servertutorialplus.ServerTutorialPlus;
import nl.martenm.servertutorialplus.helpers.PluginUtils;
import nl.martenm.servertutorialplus.objects.ServerTutorial;
import nl.martenm.servertutorialplus.objects.TutorialController;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Contains all API methods that can be safely used.
 *
 * @author MartenM
 * @since 17-11-2017
 */
public class ServerTutorialApi {

    private ServerTutorialPlus plugin;

    public ServerTutorialApi(ServerTutorialPlus plugin) {
        this.plugin = plugin;
    }

    /**
     * This method is used to fetch the API with ease.
     *
     * @return An Api Object
     */
    public static ServerTutorialApi getApi() {
        ServerTutorialPlus plugin = (ServerTutorialPlus) Bukkit.getServer().getPluginManager().getPlugin("ServerTutorialPlus");
        return plugin.getApi();
    }

    /**
     * Starts a server tutorial for the defined player.
     * Returns true if the tutorial has been started. False in all other cases
     *
     * @param id     Id of the server tutorial.
     * @param player The player.
     * @return A boolean that represents if the tutorial has been started.
     */
    public boolean startTutorial(String id, Player player) {

        if (plugin.blockPlayers.contains(player.getUniqueId())) {
            return false;
        }

        ServerTutorial serverTutorial = PluginUtils.getTutorial(plugin, id);
        if (serverTutorial == null) {
            return false;
        }

        if (plugin.inTutorial.containsKey(player.getUniqueId())) {
            return false;
        }

        if (!plugin.enabled) {
            return false;
        }

        TutorialController tutorialController = new TutorialController(plugin, player, serverTutorial);
        tutorialController.start();
        return true;
    }

    /**
     * Returns if the player is currently in a server tutorial.
     *
     * @param uuid The players UUID
     * @return A boolean
     */
    public boolean isInTutorial(UUID uuid) {
        return plugin.inTutorial.containsKey(uuid);
    }

    /**
     * Get the current servertutorial of a player.
     *
     * @param uuid The UUID of the player
     * @return The servertutorial or null if not in one.
     */
    public ServerTutorial getCurrentTutorial(UUID uuid) {
        if (plugin.inTutorial.containsKey(uuid)) {
            return plugin.inTutorial.get(uuid).getTutorial();
        }
        return null;
    }

    /**
     * Gets the controller object that manges the players tutorial.
     *
     * @param uuid Uuid of the targeted player.
     * @return The controller object. Null if not in a tutorial.
     */
    public TutorialController getController(UUID uuid) {
        if (plugin.inTutorial.containsKey(uuid)) {
            return plugin.inTutorial.get(uuid);
        }
        return null;
    }
}
