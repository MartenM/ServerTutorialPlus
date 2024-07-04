package nl.martenm.servertutorialplus.api.events;

import nl.martenm.servertutorialplus.objects.ServerTutorial;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * An event that is fired when an event ServerTutorial ends (triggered by the ServerTutorialController).
 *
 * @author MartenM
 * @since 17-11-2017
 */
public class TutorialEndEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private ServerTutorial tutorial;
    private Player player;
    private boolean cancelled;

    public TutorialEndEvent(ServerTutorial tutorial, Player player, boolean cancelled) {
        this.tutorial = tutorial;
        this.player = player;
        this.cancelled = cancelled;
    }

    public static HandlerList getHandlerList() {
        return handlers;
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

    /**
     * True if the tutorial has been cancelled.
     * False if the tutorial has ended normally.
     *
     * @return cancelled
     */
    public boolean isCancelled() {
        return cancelled;
    }
}
