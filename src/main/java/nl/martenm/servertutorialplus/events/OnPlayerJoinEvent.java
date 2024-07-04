package nl.martenm.servertutorialplus.events;

import nl.martenm.servertutorialplus.ServerTutorialPlus;
import nl.martenm.servertutorialplus.helpers.PluginUtils;
import nl.martenm.servertutorialplus.managers.FlatFileManager;
import nl.martenm.servertutorialplus.objects.ServerTutorial;
import nl.martenm.servertutorialplus.objects.TutorialController;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONObject;

/**
 * Player join event listener.
 * Will start the first-join-tutorial if set and the user is new on the server.
 * This class will check if a player should have his old properties back (saved in player quit event).
 *
 * @author MartenM
 */
// TODO: Make it so that it checks the datasource (if mysql) instead of the .hasPlayedBefore();
public class OnPlayerJoinEvent implements Listener {

    private ServerTutorialPlus plugin;

    public OnPlayerJoinEvent(ServerTutorialPlus plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {

        plugin.inTutorial.keySet().stream().forEach(uuid -> {
            Player toHide = plugin.getServer().getPlayer(uuid);
            if (toHide == null) return;

            event.getPlayer().hidePlayer(plugin.getServer().getPlayer(uuid));
        });

        if (!event.getPlayer().hasPlayedBefore()) {
            if (plugin.getConfig().getBoolean("enable first join tutorial")) {
                ServerTutorial st = PluginUtils.getTutorial(plugin, plugin.getConfig().getString("first join tutorial id"));
                if (st == null) {
                    return;
                }

                // Delay this such that other plugins can handle the spawn of the player first.
                plugin.getLogger().info("Starting tutorial for player " + event.getPlayer().getName() + " [First Join]");
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        TutorialController tutorialController = new TutorialController(plugin, event.getPlayer(), st);
                        tutorialController.start();
                    }
                }.runTaskLater(plugin, 20);

                return;
            }
            return;
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                JSONObject object = FlatFileManager.getPlayerData(plugin, event.getPlayer().getUniqueId());
                if (object == null) {
                    this.cancel();
                    return;
                }
                FlatFileManager.setPlayerData(plugin, event.getPlayer(), object);
                FlatFileManager.deleteFile(plugin, event.getPlayer().getUniqueId());
            }
        }.runTaskAsynchronously(plugin);
    }


}
