package nl.martenm.servertutorialplus.events;

import nl.martenm.servertutorialplus.ServerTutorialPlus;
import nl.martenm.servertutorialplus.language.Lang;
import nl.martenm.servertutorialplus.objects.TutorialController;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

/**
 * @author MartenM
 * @since 30-5-2018.
 */
public class OnCommandPreprocessEvent implements Listener {

    public ServerTutorialPlus plugin;

    public OnCommandPreprocessEvent(ServerTutorialPlus plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onCommandProcessEvent(PlayerCommandPreprocessEvent event) {

        Player player = event.getPlayer();
        if (!plugin.inTutorial.containsKey(player.getUniqueId())) {
            return;
        }

        TutorialController controller = plugin.inTutorial.get(player.getUniqueId());
        if (!controller.getTutorial().isBlockingCommands()) {
            return;
        }

        if (player.hasPermission("servertutorial.tutorial.bypass")) {
            return;
        }

        // Remove the / from the command using substring.
        String commandString = event.getMessage().substring(1);
        String[] args = commandString.split(" ");

        if (args.length < 1) {
            // We are not supposed to hit this, but it doesn't hurt to check either.
            return;
        }

        if (commandString.startsWith("st") || commandString.startsWith("servertutorial")) {
            return;
        }

        for (String arg : controller.getTutorial().getCommandWhiteList()) {
            if (arg.equalsIgnoreCase(args[0])) {
                // This matches the whitelist.
                // Nothing is supposed to happen so return.
                return;
            }
        }

        for (String arg : plugin.getConfig().getStringList("command-whitelist")) {
            if (arg.equalsIgnoreCase(args[0])) {
                // This matches the whitelist.
                // Nothing is supposed to happen so return.
                return;
            }
        }

        // All checks passed. Block the command and send the player a message.
        event.setCancelled(true);
        player.sendMessage(Lang.ERROR_COMMAND_BLOCKED.toString());
    }
}
