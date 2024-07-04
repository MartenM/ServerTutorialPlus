package nl.martenm.servertutorialplus.commands.sub.points;

import nl.martenm.servertutorialplus.ServerTutorialPlus;
import nl.martenm.servertutorialplus.helpers.PluginUtils;
import nl.martenm.servertutorialplus.helpers.dataholders.OldValuesPlayer;
import nl.martenm.servertutorialplus.language.Lang;
import nl.martenm.servertutorialplus.objects.ServerTutorial;
import nl.martenm.servertutorialplus.points.IPlayPoint;
import nl.martenm.servertutorialplus.points.IPointCallBack;
import nl.martenm.simplecommands.SimpleCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayPointCommand extends SimpleCommand {

    public PlayPointCommand() {
        super("playpoint", Lang.HELP_PLAYPOINT.toString(), "+playpoint", true);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        ServerTutorialPlus plugin = ServerTutorialPlus.getInstance();

        if (args.length < 2) {
            sender.sendMessage(Lang.WRONG_COMMAND_FORMAT + "/st playpoint <id> <point>");
            return true;
        }

        if (plugin.blockPlayers.contains(((Player) sender).getUniqueId())) {
            sender.sendMessage(Lang.ERROR_WAIT_TO_END.toString());
            return true;
        }

        ServerTutorial serverTutorial = PluginUtils.getTutorial(plugin, args[0]);
        if (serverTutorial == null) {
            sender.sendMessage(Lang.TUTORIAL_ID_NOT_FOUND.toString());
            return true;
        }

        int index;
        try {
            index = Integer.parseInt(args[1]);
        } catch (Exception e) {
            sender.sendMessage(Lang.ERROR_INVALID_INDEX.toString());
            return true;
        }

        if (index > serverTutorial.points.size() || index <= 0) {
            sender.sendMessage(Lang.ERROR_INVALID_POINT.toString());
            return true;
        }

        Player player = (Player) sender;

        if (plugin.inTutorial.containsKey(player.getUniqueId())) {
            player.sendMessage(Lang.ERROR_WAIT_TO_END_TUTORIAL.toString());
            return true;
        }

        plugin.blockPlayers.add(player.getUniqueId());
        new BukkitRunnable() {
            @Override
            public void run() {
                plugin.blockPlayers.remove(player.getUniqueId());
            }
        }.runTaskLater(plugin, (long) serverTutorial.points.get(index - 1).getTime() * 20 + 6);

        OldValuesPlayer oldValuesPlayer = new OldValuesPlayer(player);

        IPointCallBack callBack = () -> {
            plugin.lockedPlayers.remove(player.getUniqueId());
            plugin.lockedViews.remove(player.getUniqueId());
            player.setFlySpeed(oldValuesPlayer.getOriginal_flySpeed());
            player.setWalkSpeed(oldValuesPlayer.getOriginal_walkSpeed());
            player.setFlying(oldValuesPlayer.getFlying());
        };
        IPlayPoint handler = serverTutorial.points.get(index - 1).createPlay(player, oldValuesPlayer, callBack);
        handler.start();
        return true;
    }
}
