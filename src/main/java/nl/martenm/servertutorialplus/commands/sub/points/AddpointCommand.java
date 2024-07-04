package nl.martenm.servertutorialplus.commands.sub.points;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import nl.martenm.servertutorialplus.ServerTutorialPlus;
import nl.martenm.servertutorialplus.language.Lang;
import nl.martenm.servertutorialplus.objects.ServerTutorial;
import nl.martenm.servertutorialplus.points.PointType;
import nl.martenm.servertutorialplus.points.ServerTutorialPoint;
import nl.martenm.servertutorialplus.points.custom.CheckPoint;
import nl.martenm.servertutorialplus.points.custom.ClickBlockPoint;
import nl.martenm.servertutorialplus.points.custom.CommandPoint;
import nl.martenm.servertutorialplus.points.custom.TimedPoint;
import nl.martenm.simplecommands.SimpleCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AddpointCommand extends SimpleCommand {

    public AddpointCommand() {
        super("addpoint", Lang.HELP_ADDPOINT.toString(), "+addpoint", true);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        ServerTutorialPlus plugin = ServerTutorialPlus.getInstance();

        if (args.length < 2) {
            sender.sendMessage(Lang.WRONG_COMMAND_FORMAT + "/st addpoint <id> <timed/checkpoint/clickblock/command>");
            return true;
        }

        ServerTutorial serverTutorial = null;
        for (ServerTutorial st : plugin.serverTutorials) {
            if (st.getId().equalsIgnoreCase(args[0])) {
                serverTutorial = st;
                break;
            }
        }

        if (serverTutorial == null) {
            sender.sendMessage(Lang.TUTORIAL_ID_NOT_FOUND.toString());
            return true;
        }

        Player player = (Player) sender;//
        ServerTutorialPoint point;

        switch (args[1]) {
            case "TIME":
            case "time":
            case "timed":
                point = new TimedPoint(plugin, player.getLocation());
                break;
            case "checkpoint":
            case "CHECKPOINT":
                point = new CheckPoint(plugin, player.getLocation());
                break;
            case "clickblock":
            case "CLICKBLOCK":
            case "click_block":
                point = new ClickBlockPoint(plugin, player.getLocation(), true);
                break;
            case "command":
            case "COMMAND":
                point = new CommandPoint(plugin, player.getLocation());
                player.sendMessage(Lang.COMMAND_ADDPOINT_COMMAND_HINT.toString());
                break;
            default:
                sender.sendMessage(Lang.POINT_INVALID_TYPE.toString());

                TextComponent message = new TextComponent(Lang.POINT_EXAMPLE_MESSAGE.toString());

                for (int i = 0; i < PointType.values().length; i++) {
                    PointType type = PointType.values()[i];
                    TextComponent component = new TextComponent(ChatColor.GREEN + type.toString().toLowerCase());
                    component.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/st addpoint " + args[0] + " " + type.toString().toLowerCase()));
                    component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Lang.POINT_EXAMPLE_COMMAND_CLICK.toString().replace("%type%", type.toString())).create()));
                    message.addExtra(component);
                    if (i < PointType.values().length - 1) message.addExtra(ChatColor.GRAY + " / ");
                }

                player.spigot().sendMessage(message);
                return true;
        }

        serverTutorial.points.add(point);
        player.sendMessage(Lang.POINT_ADDED.toString().replace("%index%", String.valueOf(serverTutorial.points.size())));

        return true;
    }
}
