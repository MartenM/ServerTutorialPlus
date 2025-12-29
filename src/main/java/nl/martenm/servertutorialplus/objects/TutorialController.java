package nl.martenm.servertutorialplus.objects;

import nl.martenm.servertutorialplus.ServerTutorialPlus;
import net.md_5.bungee.api.ChatColor;
import nl.martenm.servertutorialplus.api.events.TutorialEndEvent;
import nl.martenm.servertutorialplus.api.events.TutorialStartEvent;
import nl.martenm.servertutorialplus.helpers.Messages;
import nl.martenm.servertutorialplus.helpers.PluginUtils;
import nl.martenm.servertutorialplus.helpers.dataholders.OldValuesPlayer;
import nl.martenm.servertutorialplus.points.IPlayPoint;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * The tutorial controller has all the logic to play a ServerTutorial.
 * It contains the main loop for the tutorial and it has methods like start() and cancel().
 * @author MartenM
 * @since 7-3-2017
 */
public class TutorialController {

    private ServerTutorialPlus plugin;
    private Player player;
    private ServerTutorial serverTutorial;
    private boolean running = true;

    private int current = 0;
    private IPlayPoint playedPoint;

    private OldValuesPlayer oldValuesPlayer;

    public TutorialController(ServerTutorialPlus plugin, Player player, ServerTutorial serverTutorial){
        this.plugin = plugin;
        this.player = player;
        this.serverTutorial = serverTutorial;
        this.oldValuesPlayer = new OldValuesPlayer(player);
    }

    /**
    * Starts a tutorial.
     */
    public void start(){
        if(serverTutorial.getNeedsPermission()){
            if(!player.hasPermission("servertutorialplus.tutorials." + serverTutorial.getId())){
                stopController(true);

                player.sendMessage(Messages.noPermissionTutorial(plugin));
                return;
            }
        }

        //FIRE event!
        TutorialStartEvent event = new TutorialStartEvent(serverTutorial, player);
        plugin.getServer().getPluginManager().callEvent(event);
        if (!(plugin.getServer().getPluginManager().getPlugin("TAB") == null)) {
            player.performCommand("tab scoreboard off " + player.getName() + " -s");
        }

        if(event.isCancelled()){
            plugin.inTutorial.remove(player.getUniqueId());
            plugin.lockedPlayers.remove(player.getUniqueId());
            plugin.lockedViews.remove(player.getUniqueId());
            if (!(plugin.getServer().getPluginManager().getPlugin("TAB") == null)) {
                player.performCommand("tab scoreboard on " + player.getName() + " -s");
            }
            return;
        }

        if(serverTutorial.points.size() == 0){
            player.sendMessage(ChatColor.RED + "Tutorial cancelled. No points to be played.");
            cancel(true);
            return;
        }

        //Hide player from other players.
        if(serverTutorial.invisiblePlayer){
            plugin.getServer().getOnlinePlayers().stream().forEach(p -> p.hidePlayer(player));
        }

        plugin.inTutorial.put(player.getUniqueId(), this);
        serverTutorial.plays += 1;

        playedPoint = serverTutorial.points.get(current).createPlay(player, oldValuesPlayer, this::finishPoint);
        playedPoint.start();
    }

    /**
    * Cancels / stops the current the tutorial.
    * @param cancelled   -  Identifies if a tutorial was cancelled, or just stopped.
     */
    public void cancel(boolean cancelled) {
        cancel(cancelled, cancelled);
    }

    public void cancel(boolean cancelled, boolean originalLocation) {
        // Fire event!
        plugin.getServer().getPluginManager().callEvent(new TutorialEndEvent(serverTutorial, player, cancelled));

        stopController(cancelled);
        restorePlayer(originalLocation);
    }

    private void restorePlayer(boolean originalLocation){
        if(plugin.enabled) {
            // plugin.getServer().getScheduler().runTask(plugin, () -> {
                player.setFlySpeed(oldValuesPlayer.getOriginal_flySpeed());
                player.setWalkSpeed(oldValuesPlayer.getOriginal_walkSpeed());
                player.setAllowFlight(oldValuesPlayer.isAllowFlight());
                player.setFlying(oldValuesPlayer.getFlying());
                player.setGameMode(oldValuesPlayer.getGamemode());
                if (originalLocation) {
                    player.teleport(oldValuesPlayer.getLoc());
                }
            // });
        }
    }

    private void stopController(boolean cancelled){
        if(cancelled){
            if(playedPoint != null) {
                playedPoint.stop();
            }
        }

        //Show player again
        if (serverTutorial.invisiblePlayer && plugin.enabled) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    plugin.getServer().getOnlinePlayers().stream().forEach(p -> p.showPlayer(player));
                }
            }.runTask(plugin);
        }

        plugin.inTutorial.remove(player.getUniqueId());
        plugin.lockedPlayers.remove(player.getUniqueId());
        plugin.lockedViews.remove(player.getUniqueId());
        running = false;
    }

    private void finishPoint(){
        if(current == serverTutorial.points.size() - 1){
            //Tutorial has been finished!
            finish();
            if (!(plugin.getServer().getPluginManager().getPlugin("TAB") == null)) {
                player.performCommand("tab scoreboard on " + player.getName() + " -s");
            }
        } else{
            current++;
            playedPoint = serverTutorial.points.get(current).createPlay(player, oldValuesPlayer, this::finishPoint);
            playedPoint.start();
        }
    }

    /**
     * Used to execute code that should only be executed if the tutorial has been fairly completed.
     */
    public void finish(){
        //Cancel tutorial to make everything stop and set back old values.
        cancel(false);

        //Run additional commands for (first) completion
        new BukkitRunnable(){
            @Override
            public void run() {
                boolean playedBefore = plugin.getDataSource().hasPlayedTutorial(player.getUniqueId(), serverTutorial.getId());

                if(!playedBefore) {
                    //HAS NOT PLAYED THIS TUTORIAL BEFORE!
                    // If query is return true (succes) make played before false to give rewards.
                    playedBefore = !plugin.getDataSource().addPlayedTutorial(player.getUniqueId(), serverTutorial.getId());
                }

                final boolean result = playedBefore;
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        giveRewards(result);
                    }
                }.runTask(plugin);
            }
        }.runTaskAsynchronously(plugin);
    }

    /**
     * Executes the commands that are used when a tutorial has been finished.
     * Executes as fast as the last point has been played.
     * @param playedBefore True if the player does the tutorial for the first time.
     */
    public void giveRewards(boolean playedBefore){
        if(!playedBefore) {
            for (String command : serverTutorial.getRewards()) {
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), PluginUtils.replaceVariables(plugin.placeholderAPI, player, command));
            }
        }
    }

    /**
    * Gets the player associated with this TutorialController.
    * @return Player
     */
    public Player getPlayer(){
        return this.player;
    }

    /**
    * Gets the old values for a player, used to restore the players state before starting the tutorial.
    * @return Old values of a player.
     */
    public OldValuesPlayer getOldValuesPlayer(){
        return oldValuesPlayer;
    }

    /**
    * Gets the tutorial this controller is playing.
    * @return Server Tutorial.
     */
    public ServerTutorial getTutorial(){
        return serverTutorial;
    }

    /**
     * Gets whether the tutorial is being played.
     * @return Boolean that identifies if the tutorial controller is currently running a tutorial.
     */
    public boolean isRunning(){
        return this.running;
    }

    /**
     * Get which point are beeing playing
     * @return Integer that represent point index of the current point
     */
    public Integer getCurrentPoint() {
        return current;
    }
}
