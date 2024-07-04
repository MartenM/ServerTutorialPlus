package nl.martenm.servertutorialplus.api.events;

import nl.martenm.servertutorialplus.objects.ServerTutorial;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * An event that is fired when a ServerTutorial is started.
 *
 * @author MartenM
 * @since 17-11-2017
 */
public class TutorialStartEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;

    private ServerTutorial tutorial;
    private Player player;

    public TutorialStartEvent(ServerTutorial tutorial, Player player) {
        this.tutorial = tutorial;
        this.player = player;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancelled = b;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    /**
     * Returns the serverTutorial.
     *
     * @return ServerTutorial
     */
    public ServerTutorial getTutorial() {
        return tutorial;
    }

    /**
     * Returns the player playing the tutorial.
     *
     * @return Player
     */
    public Player getPlayer() {
        return player;
    }
}
