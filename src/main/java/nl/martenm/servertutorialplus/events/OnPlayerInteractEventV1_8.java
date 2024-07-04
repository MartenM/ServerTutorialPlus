package nl.martenm.servertutorialplus.events;

import nl.martenm.servertutorialplus.ServerTutorialPlus;
import nl.martenm.servertutorialplus.helpers.PluginUtils;
import nl.martenm.servertutorialplus.language.Lang;
import nl.martenm.servertutorialplus.objects.ServerTutorial;
import nl.martenm.servertutorialplus.objects.TutorialController;
import nl.martenm.servertutorialplus.objects.TutorialSign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Player interact event for versions = 1.8
 * Used for clickable blocks and signs.
 *
 * @author MartenM
 */
public class OnPlayerInteractEventV1_8 implements Listener {

    private ServerTutorialPlus plugin;

    public OnPlayerInteractEventV1_8(ServerTutorialPlus plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {

        plugin.getClickManager().handleClickAction(event);

        if (event.getClickedBlock() == null || event.getAction() == Action.LEFT_CLICK_BLOCK) {
            return;
        }

        if (!plugin.enabled) {
            return;
        }

        for (TutorialSign ts : plugin.tutorialSigns) {
            if (!ts.block.equals(event.getClickedBlock())) {
                continue;
            }
            ServerTutorial serverTutorial = PluginUtils.getTutorial(plugin, ts.ServerTutorialId);
            if (serverTutorial == null) {
                event.getPlayer().sendMessage(Lang.ERROR_FAILED_FINDING_TUTORIAL_ADMIN.toString().replace("%id%", serverTutorial.getId()));
                return;
            }
            if (plugin.inTutorial.containsKey(event.getPlayer().getUniqueId())) {
                event.getPlayer().sendMessage(Lang.ERROR_WAIT_TO_END_TUTORIAL.toString());
                return;
            }

            TutorialController tutorialController = new TutorialController(plugin, event.getPlayer(), serverTutorial);
            tutorialController.start();
            event.setCancelled(true);
            break;
        }
    }


}

