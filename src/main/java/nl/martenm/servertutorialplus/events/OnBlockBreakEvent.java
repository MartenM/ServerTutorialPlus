package nl.martenm.servertutorialplus.events;

import nl.martenm.servertutorialplus.ServerTutorialPlus;
import nl.martenm.servertutorialplus.language.Lang;
import nl.martenm.servertutorialplus.objects.TutorialSign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

/**
 * Handles block break events. Used to remove click-able blocks.
 *
 * @author MartenM
 */
public class OnBlockBreakEvent implements Listener {

    private ServerTutorialPlus plugin;

    public OnBlockBreakEvent(ServerTutorialPlus plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent event) {

        for (TutorialSign ts : plugin.tutorialSigns) {
            if (ts.block.getLocation().equals(event.getBlock().getLocation())) {
                if (!event.getPlayer().hasPermission("servertutorial.action.removeblock")) {
                    event.getPlayer().sendMessage(Lang.EVENT_BLOCK_REMOVE_PERMISSION.toString());
                    event.setCancelled(true);
                    return;
                }
                plugin.tutorialSigns.remove(ts);
                event.getPlayer().sendMessage(Lang.EVENT_BLOCK_REMOVED.toString());
                break;
            }
        }


    }


}
