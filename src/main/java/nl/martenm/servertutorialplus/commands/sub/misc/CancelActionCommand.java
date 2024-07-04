package nl.martenm.servertutorialplus.commands.sub.misc;

import nl.martenm.servertutorialplus.ServerTutorialPlus;
import nl.martenm.servertutorialplus.language.Lang;
import nl.martenm.simplecommands.SimpleCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CancelActionCommand extends SimpleCommand {

    public CancelActionCommand() {
        super("cancel", true);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        ServerTutorialPlus plugin = ServerTutorialPlus.getInstance();

        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;

        if (!plugin.getClickManager().hasClickaction(player.getUniqueId())) {
            player.sendMessage(Lang.NOTHING_TO_CANCEL.toString());
        } else {
            plugin.getClickManager().removeClickaction(player.getUniqueId());
            player.sendMessage(Lang.ACTION_CANCELLED.toString());
        }
        return true;
    }

    @Override
    public boolean isAllowed(CommandSender sender) {
        return true;
    }

    @Override
    public boolean checkPermission(CommandSender sender) {
        return true;
    }
}
