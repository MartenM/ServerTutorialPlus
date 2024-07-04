package nl.martenm.servertutorialplus.events;

import nl.martenm.servertutorialplus.ServerTutorialPlus;
import nl.martenm.servertutorialplus.helpers.dataholders.OldValuesPlayer;
import nl.martenm.servertutorialplus.objects.TutorialController;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Player quit event listener.
 * Save the old state of a player if he leaves while in a tutorial. This makes sure we actually restore the properties of the player on the next join.
 *
 * @author MartenM
 */
public class OnPlayerQuitEvent implements Listener {

    private ServerTutorialPlus plugin;

    public OnPlayerQuitEvent(ServerTutorialPlus plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (plugin.inTutorial.containsKey(event.getPlayer().getUniqueId())) {
            TutorialController tc = plugin.inTutorial.get(event.getPlayer().getUniqueId());
            tc.cancel(true);

            OldValuesPlayer oldValuesPlayer = tc.getOldValuesPlayer();
            oldValuesPlayer.restore(event.getPlayer());
        }
    }
}
