package nl.martenm.servertutorialplus.objects;

import nl.martenm.servertutorialplus.points.ServerTutorialPoint;

import java.util.ArrayList;
import java.util.List;

/**
 * The ServerTutorial class represents a single sever tutorial. It contains all the data about the tutorial.
 * The object contains a List which contains TutorialPoints. These points store the unique data for each point.
 *
 * @author MartenM
 * @since 5-3-2017.
 */
public class ServerTutorial {

    /**
     * Public variable for tracking plays.
     */
    public int plays;
    public boolean invisiblePlayer;
    /**
     * The list of TutorialPoints for this tutorial.
     */
    public List<ServerTutorialPoint> points;
    private String id;
    private boolean blocksCommands;
    private List<String> commandWhiteList;
    /**
     * List of rewards that are given when the tutorial is completed for the first time.
     */
    private List<String> rewards;
    /**
     * If the user should have the permission 'servertutorialplus.tutorial.ID to play this tutorial.
     */
    private boolean needsPermission;
    /**
     * If set to true the incoming chat messages to the player will be blocked.
     */
    private boolean blockChat;

    public ServerTutorial(String id) {
        this.id = id;
        this.points = new ArrayList<>();
        this.rewards = new ArrayList<>();
        this.commandWhiteList = new ArrayList<>();
    }

    public ServerTutorial(String id, List<ServerTutorialPoint> points) {
        this.id = id;
        this.points = points;
        this.rewards = new ArrayList<>();
        this.commandWhiteList = new ArrayList<>();
    }

    public ServerTutorial(String id, List<ServerTutorialPoint> points, List<String> rewards) {
        this.id = id;
        this.points = points;
        this.rewards = rewards;
        this.commandWhiteList = new ArrayList<>();
    }

    /**
     * The unique id of the server tutorial.
     *
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the unique id of a server tutorial.
     * USE WITH CAUTION!
     *
     * @param id The new ID.
     */
    public void setId(String id) {
        this.id = id;
    }

    public List<String> getRewards() {
        return rewards;
    }

    public void setRewards(List<String> rewards) {
        if (rewards == null) this.rewards = new ArrayList<>();

        this.rewards = rewards;
    }

    /**
     * Gets if you need permission to play this tutorial.
     *
     * @return If permission is needed.
     */
    public boolean getNeedsPermission() {
        return needsPermission;
    }

    /**
     * Sets if you need permission to play this tutorial.
     *
     * @param needsPermission true means permission is needed.
     */
    public void setNeedsPermission(boolean needsPermission) {
        this.needsPermission = needsPermission;
    }

    /**
     * Get whether this tutorial blocks commands by other plugins and Bukkit/Spigot it self.
     *
     * @return true if blocks commands
     */
    public boolean isBlockingCommands() {
        return blocksCommands;
    }

    /**
     * Set whether this servertutorial blocks commands from the player.
     */
    public void setBlocksCommands(boolean blocksCommands) {
        this.blocksCommands = blocksCommands;
    }

    public List<String> getCommandWhiteList() {
        return commandWhiteList;
    }

    public void setCommandWhiteList(List<String> commandWhiteList) {
        if (commandWhiteList == null) this.commandWhiteList = new ArrayList<>();
        this.commandWhiteList = commandWhiteList;
    }

    /**
     * Wheter the player is receiving chat messages or not while in the tutorial.
     */
    public boolean isChatBlocked() {
        return blockChat;
    }

    /**
     * Set if the player should not receive chat messages from other players.
     */
    public void setChatBlocked(boolean blockChat) {
        this.blockChat = blockChat;
    }
}
