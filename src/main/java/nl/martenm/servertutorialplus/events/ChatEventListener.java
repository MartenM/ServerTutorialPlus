package nl.martenm.servertutorialplus.events;

import nl.martenm.servertutorialplus.ServerTutorialPlus;
import nl.martenm.servertutorialplus.objects.TutorialController;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * @author MartenM
 * @since 24-10-2018.
 */
public class ChatEventListener implements Listener {

    private ServerTutorialPlus plugin;

    public ChatEventListener(ServerTutorialPlus plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChatEvent(AsyncPlayerChatEvent event) {
        event.getRecipients().removeIf(player -> {
            TutorialController controller = plugin.inTutorial.get(player.getUniqueId());
            if (controller != null) {
                return controller.getTutorial().isChatBlocked();
            }
            return false;
        });
    }

}
