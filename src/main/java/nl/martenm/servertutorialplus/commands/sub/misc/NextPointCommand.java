package nl.martenm.servertutorialplus.commands.sub.misc;

import nl.martenm.servertutorialplus.language.Lang;
import nl.martenm.servertutorialplus.points.custom.CommandPoint;
import nl.martenm.simplecommands.SimpleCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NextPointCommand extends SimpleCommand {

    // TODO: Hide these from the HELP

    public NextPointCommand() {
        super("next", true);
        setHidden(true);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Lang.PLAYER_ONLY_COMMAND.toString());
            return true;
        }
        Player player = (Player) sender;
        CommandPoint.handle(player.getUniqueId());
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
