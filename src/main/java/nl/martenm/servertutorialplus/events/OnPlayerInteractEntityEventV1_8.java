package nl.martenm.servertutorialplus.events;

import nl.martenm.servertutorialplus.ServerTutorialPlus;
import nl.martenm.servertutorialplus.helpers.PluginUtils;
import nl.martenm.servertutorialplus.language.Lang;
import nl.martenm.servertutorialplus.objects.NPCInfo;
import nl.martenm.servertutorialplus.objects.ServerTutorial;
import nl.martenm.servertutorialplus.objects.TutorialController;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

/**
 * Player interact entity event for mc versions = 1.8
 * Used to check if player clicks a NPC.
 *
 * @author MartenM
 */
public class OnPlayerInteractEntityEventV1_8 implements Listener {

    private ServerTutorialPlus plugin;

    public OnPlayerInteractEntityEventV1_8(ServerTutorialPlus plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event) {

        plugin.getClickManager().handleClickAction(event);

        NPCInfo info = plugin.getNpcManager().getByUUID(event.getRightClicked().getUniqueId());
        if (info == null) return;

        event.setCancelled(true);

        ServerTutorial serverTutorial = PluginUtils.getTutorial(plugin, info.getServerTutorialID());
        if (serverTutorial == null) {
            event.getPlayer().sendMessage(Lang.ERROR_FAILED_FINDING_TUTORIAL_ADMIN.toString().replace("&id&", info.getServerTutorialID()));
            return;
        }
        if (plugin.inTutorial.containsKey(event.getPlayer().getUniqueId())) {
            event.getPlayer().sendMessage(Lang.ERROR_WAIT_TO_END_TUTORIAL.toString());
            return;
        }

        TutorialController tutorialController = new TutorialController(plugin, event.getPlayer(), serverTutorial);
        tutorialController.start();
        event.setCancelled(true);

    }
}