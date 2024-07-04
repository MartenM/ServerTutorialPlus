package nl.martenm.servertutorialplus.events;

import nl.martenm.servertutorialplus.ServerTutorialPlus;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * Listener for the player move event.
 * Used to block movement if a player has been locked.
 *
 * @author MartenM
 */
public class onPlayerMoveEvent implements Listener {

    private ServerTutorialPlus plugin;

    public onPlayerMoveEvent(ServerTutorialPlus plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void playerMoveEvent(PlayerMoveEvent event) {
        if (plugin.lockedPlayers.contains(event.getPlayer().getUniqueId())) {
            if (event.getFrom().getX() != event.getTo().getX() || event.getFrom().getY() != event.getTo().getY() || event.getFrom().getZ() != event.getTo().getZ()) {
                //Player moved!
                event.setCancelled(true);
            }
        }

        if (plugin.lockedViews.contains(event.getPlayer().getUniqueId())) {
            if (event.getFrom().getYaw() != event.getTo().getYaw() || event.getFrom().getPitch() != event.getTo().getPitch()) {
                //Player moved!
                event.setCancelled(true);
            }
        }
    }
}
