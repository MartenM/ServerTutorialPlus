package nl.martenm.servertutorialplus.managers.clickactions;

import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Interface that can be used for the ClickManager.
 * Allows for both playerInteract events with and without entities.
 *
 * @author MartenM
 * @since 17-1-2018.
 */
public interface IClickAction {
    void run(PlayerInteractEvent event);

    void run(PlayerInteractEntityEvent event);
}
