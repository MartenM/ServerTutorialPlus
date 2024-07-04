package nl.martenm.servertutorialplus.commands.sub.tutorials;

import nl.martenm.servertutorialplus.ServerTutorialPlus;
import nl.martenm.servertutorialplus.helpers.PluginUtils;
import nl.martenm.servertutorialplus.language.Lang;
import nl.martenm.servertutorialplus.objects.ServerTutorial;
import nl.martenm.servertutorialplus.objects.TutorialController;
import nl.martenm.simplecommands.SimpleCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayTutorialCommand extends SimpleCommand {

    public PlayTutorialCommand() {
        super("play", Lang.HELP_PLAY.toString(), "+play", false);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        ServerTutorialPlus plugin = ServerTutorialPlus.getInstance();

        if (args.length < 1) {
            sender.sendMessage(Lang.WRONG_COMMAND_FORMAT + "/st play <id>");
            return true;
        }

        ServerTutorial serverTutorial = PluginUtils.getTutorial(plugin, args[0]);
        if (serverTutorial == null) {
            sender.sendMessage(Lang.TUTORIAL_ID_NOT_FOUND.toString());
            return true;
        }

        Player target = null;
        if (args.length != 1) {
            if (!sender.hasPermission(getFullPermission() + ".others")) {
                sender.sendMessage(Lang.NO_PERMS.toString());
                return true;
            }

            target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage(Lang.ERROR_PLAYER_OFFLINE.toString());
                return true;
            }

            if (plugin.blockPlayers.contains((target).getUniqueId())) {
                sender.sendMessage(Lang.ERROR_WAIT_TO_END.toString());
                return true;
            }

            TutorialController controller = plugin.inTutorial.get(target.getUniqueId());
            if (controller != null) {
                controller.cancel(true, false);
                sender.sendMessage(Lang.WARNING_TUTORIAL_OTHER_CANCELLED.toString()
                        .replace("%username%", target.getName())
                        .replace("%tutorial%", serverTutorial.getId()));
            }

        } else {
            if (!(sender instanceof Player)) {
                sender.sendMessage(Lang.PLAYER_ONLY_COMMAND.toString());
                return true;
            }

            Player player = (Player) sender;

            target = player;

            if (plugin.inTutorial.containsKey(player.getUniqueId())) {
                player.sendMessage(Lang.ERROR_WAIT_TO_END_TUTORIAL.toString());
                return true;
            }
        }

        if (!plugin.enabled) {
            return true;
        }

        TutorialController tutorialController = new TutorialController(plugin, target, serverTutorial);
        tutorialController.start();
        return true;
    }
}
