package nl.martenm.servertutorialplus.events;

import nl.martenm.servertutorialplus.ServerTutorialPlus;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleFlightEvent;

/**
 * Player toggle flight listener.
 * Cancel toggle when locked flight.
 *
 * @author MartenM
 */
public class OnPlayerToggleFlight implements Listener {

    private ServerTutorialPlus plugin;

    public OnPlayerToggleFlight(ServerTutorialPlus plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void OnPlayerToggleFlightEvent(PlayerToggleFlightEvent event) {
        if (plugin.lockedPlayers.contains(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
        }
    }

}
