package nl.martenm.servertutorialplus.points.editor.args;

import net.md_5.bungee.api.ChatColor;
import nl.martenm.servertutorialplus.language.Lang;
import nl.martenm.servertutorialplus.objects.ServerTutorial;
import nl.martenm.servertutorialplus.points.ServerTutorialPoint;
import nl.martenm.servertutorialplus.points.editor.PointArg;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.CommandSender;

/**
 * @author MartenM
 * @since 29-11-2017.
 */
public class CommandsArg extends PointArg {

    public CommandsArg() {
        super("commands");
    }

    @Override
    public boolean run(ServerTutorial serverTutorial, ServerTutorialPoint point, CommandSender sender, String[] args) {

        if (args.length < 1) {
            sender.sendMessage(Lang.WRONG_COMMAND_FORMAT.toString() + "/st editpoint <t> <p> commands <add/remove/clear/list>");
            return false;
        }

        switch (args[0]) {
            case "clear":
                point.getCommands().clear();
                break;

            case "add":
                if (args.length < 2) {
                    sender.sendMessage(Lang.ERROR_ATLEAST_ONE_WORD.toString());
                    return false;
                }

                String message = StringUtils.join(args, ' ', 1, args.length);
                point.getCommands().add(message);
                break;

            case "remove":
                if (args.length < 2) {
                    sender.sendMessage(Lang.ERROR_INVALID_INDEX.toString());
                    return false;
                }

                try {
                    point.getCommands().remove(Integer.parseInt(args[1]) - 1);
                } catch (NumberFormatException ex) {
                    sender.sendMessage(Lang.ERROR_INVALID_NUMBNER.toString());
                    return false;
                } catch (IndexOutOfBoundsException ex) {
                    sender.sendMessage(Lang.ERROR_NOTEXISTING_INDEX.toString());
                    return false;
                }
                break;

            case "list":
                sender.sendMessage(ChatColor.GRAY + "[ " + ChatColor.YELLOW + "Commands");
                for (int i = 0; i < point.getCommands().size(); i++) {
                    sender.sendMessage(ChatColor.GRAY + "[ " + ChatColor.GREEN + (i + 1) + ChatColor.YELLOW + " " + point.getCommands().get(i));
                }
                return false;

            default:
                sender.sendMessage(Lang.WRONG_COMMAND_FORMAT + "/st editpoint <t> <p> commands <add/remove/clear/list>");
                return false;
        }
        return true;
    }
}
