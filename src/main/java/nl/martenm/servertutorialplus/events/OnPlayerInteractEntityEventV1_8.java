package nl.martenm.servertutorialplus.events;

import nl.martenm.servertutorialplus.MainClass;
import nl.martenm.servertutorialplus.helpers.PluginUtils;
import nl.martenm.servertutorialplus.language.Lang;
import nl.martenm.servertutorialplus.objects.NPCInfo;
import nl.martenm.servertutorialplus.objects.ServerTutorial;
import nl.martenm.servertutorialplus.objects.TutorialController;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

/**
 * Player interact entity event for mc versions = 1.8
 * Used to check if player clicks a NPC.
 * @author MartenM
 */
public class OnPlayerInteractEntityEventV1_8 implements Listener {

    private MainClass plugin;

    public OnPlayerInteractEntityEventV1_8(MainClass plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event) {

        plugin.getClickManager().handleClickAction(event);

        if (plugin.clickableNPCs.containsKey(event.getRightClicked().getUniqueId())) {
            event.setCancelled(true);

            NPCInfo info = plugin.clickableNPCs.get(event.getRightClicked().getUniqueId());
            ServerTutorial serverTutorial = PluginUtils.getTutorial(plugin, info.getServerTutorialID());
            if (serverTutorial == null) {
                event.getPlayer().sendMessage(Lang.ERROR_FAILED_FINDING_TUTORIAL_ADMIN.toString().replace("&id&", info.getServerTutorialID()));
                return;
            }
            if(plugin.inTutorial.containsKey(event.getPlayer().getUniqueId())){
                event.getPlayer().sendMessage(Lang.ERROR_WAIT_TO_END_TUTORIAL.toString());
                return;
            }

            TutorialController tutorialController = new TutorialController(plugin, event.getPlayer(), serverTutorial);
            tutorialController.start();
            event.setCancelled(true);
        }
    }
}