package nl.martenm.servertutorialplus.commands.sub;

import net.md_5.bungee.api.ChatColor;
import nl.martenm.servertutorialplus.ServerTutorialPlus;
import nl.martenm.servertutorialplus.commands.PlayerLookUp;
import nl.martenm.servertutorialplus.language.Lang;
import nl.martenm.simplecommands.SimpleCommand;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerCommand extends SimpleCommand {


    public PlayerCommand() {
        super("player", Lang.HELP_PLAYER.toString(), "+player", false);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        ServerTutorialPlus plugin = ServerTutorialPlus.getInstance();

        if (args.length == 0) {
            sender.sendMessage(ChatColor.YELLOW + "Please define a player to check. /st player <name>");
            return true;
        }

        OfflinePlayer target = null;

        target = plugin.getServer().getPlayer(args[0]);
        if (target == null) {
            target = plugin.getServer().getOfflinePlayer(args[0]);
        }

        if (target == null) {
            sender.sendMessage(Lang.COMMAND_LOOKUP_UUID_ERROR.toString());
            return true;
        }

        if (!target.hasPlayedBefore()) {
            sender.sendMessage(Lang.COMMAND_LOOKUP_NEVER_PLAYED.toString());
            return true;
        }

        if (args.length > 2) {
            new BukkitRunnable() {

                private OfflinePlayer target;

                public BukkitRunnable setTarget(OfflinePlayer target) {
                    this.target = target;
                    return this;
                }

                @Override
                public void run() {
                    String id = args[1];
                    PlayerLookUp.sendLookupAsync(plugin, sender, target);
                    if (args[2].equalsIgnoreCase("set")) {
                        if (!plugin.getDataSource().hasPlayedTutorial(target.getUniqueId(), id)) {
                            plugin.getDataSource().addPlayedTutorial(target.getUniqueId(), id);
                            sender.sendMessage(Lang.COMMAND_LOOKUP_SET.toString());
                        } else {
                            sender.sendMessage(Lang.COMMAND_LOOKUP_SET_ERROR.toString());
                        }
                    } else if (args[2].equalsIgnoreCase("unset")) {
                        if (plugin.getDataSource().hasPlayedTutorial(target.getUniqueId(), id)) {
                            plugin.getDataSource().removePlayedTutorial(target.getUniqueId(), id);
                            sender.sendMessage(Lang.COMMAND_LOOKUP_UNSET.toString());
                        } else {
                            sender.sendMessage(Lang.COMMAND_LOOKUP_UNSET_ERROR.toString());
                        }
                    } else {
                        sender.sendMessage(Lang.UNKOWN_ARGUMENT.toString());
                    }

                }
            }.setTarget(target).runTaskAsynchronously(plugin);
            return true;
        }

        PlayerLookUp.sendLookupAsync(plugin, sender, target);
        return true;
    }
}
