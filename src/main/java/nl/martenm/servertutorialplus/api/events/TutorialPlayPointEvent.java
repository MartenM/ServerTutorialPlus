package nl.martenm.servertutorialplus.api.events;

import nl.martenm.servertutorialplus.objects.ServerTutorial;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * An event that is fired when a TutorialPoint is played.
 *
 * @author MartenM
 * @since 17-11-2017
 */
public class TutorialPlayPointEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private ServerTutorial tutorial;
    private Player player;
    private Integer index;

    public TutorialPlayPointEvent(ServerTutorial tutorial, int index, Player player) {
        this.tutorial = tutorial;
        this.player = player;
        this.index = index;
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
     * Returns the index of the point that is being played.
     *
     * @return index
     */
    public Integer getIndex() {
        return index;
    }
}
