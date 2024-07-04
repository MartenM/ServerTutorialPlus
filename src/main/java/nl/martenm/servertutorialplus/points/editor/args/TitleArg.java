package nl.martenm.servertutorialplus.points.editor.args;

import net.md_5.bungee.api.ChatColor;
import nl.martenm.servertutorialplus.helpers.dataholders.PlayerTitle;
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
public class TitleArg extends PointArg {

    public TitleArg() {
        super("title");
    }

    @Override
    public boolean run(ServerTutorial serverTutorial, ServerTutorialPoint point, CommandSender sender, String[] args) {

        if (args.length < 1) {
            sender.sendMessage(Lang.WRONG_COMMAND_FORMAT + "/st editpoint <t> <p> title <title / subtitle /reset / fadein / fadeout / stay>");
            return false;
        }

        switch (args[0]) {
            case "reset":
                point.setTitleInfo(null);
                break;

            case "title":
                if (args.length < 2) {
                    point.getTitleInfo().title = "";
                    return true;
                }

                if (point.getTitleInfo() == null) {
                    point.setTitleInfo(new PlayerTitle());
                }

                String message = StringUtils.join(args, ' ', 1, args.length);
                point.getTitleInfo().title = message;
                break;

            case "subtitle":
                if (args.length < 2) {
                    point.getTitleInfo().subtitle = "";
                    return true;
                }

                if (point.getTitleInfo() == null) {
                    point.setTitleInfo(new PlayerTitle());
                }

                message = StringUtils.join(args, ' ', 1, args.length);
                point.getTitleInfo().subtitle = message;
                break;

            case "fadein":
                int time;

                if (point.getTitleInfo() == null) {
                    sender.sendMessage(Lang.TITLE_NOTITLE_SET.toString());
                    return false;
                }

                if (args.length < 2) {
                    sender.sendMessage(Lang.TITLE_CURRENT.toString().replace("%type%", args[0]).replace("%ticks%", point.getTitleInfo().fadeIn + ""));
                    return false;
                }

                try {
                    time = Integer.parseInt(args[1]);
                } catch (NumberFormatException ex) {
                    sender.sendMessage(Lang.ERROR_INVALID_TIME.toString());
                    return false;
                }

                if (point.getTitleInfo() == null) {
                    point.setTitleInfo(new PlayerTitle());
                }

                point.getTitleInfo().fadeIn = time;
                break;

            case "fadeout":
                if (point.getTitleInfo() == null) {
                    sender.sendMessage(Lang.TITLE_NOTITLE_SET.toString());
                    return false;
                }

                if (args.length < 2) {
                    sender.sendMessage(Lang.TITLE_CURRENT.toString().replace("%type%", args[0]).replace("%ticks%", point.getTitleInfo().fadeOut + ""));
                    return false;
                }

                try {
                    time = Integer.parseInt(args[1]);
                } catch (NumberFormatException ex) {
                    sender.sendMessage(ChatColor.RED + "Invalid time.");
                    return false;
                }

                if (point.getTitleInfo() == null) {
                    point.setTitleInfo(new PlayerTitle());
                }

                point.getTitleInfo().fadeOut = time;
                break;

            case "stay":

                if (point.getTitleInfo() == null) {
                    sender.sendMessage(Lang.TITLE_NOTITLE_SET.toString());
                    return false;
                }

                if (args.length < 2) {
                    sender.sendMessage(Lang.TITLE_CURRENT.toString().replace("%type%", args[0]).replace("%ticks%", point.getTitleInfo().time + ""));
                    return false;
                }


                try {
                    time = Integer.parseInt(args[1]);
                } catch (NumberFormatException ex) {
                    sender.sendMessage(Lang.ERROR_INVALID_TIME.toString());
                    return false;
                }

                if (point.getTitleInfo() == null) {
                    point.setTitleInfo(new PlayerTitle());
                }

                point.getTitleInfo().time = time;
                break;

            default:
                sender.sendMessage(Lang.WRONG_COMMAND_FORMAT + "/st editpoint <t> <p> title <title / subtitle /reset / fadein / fadeout / stay>");
                return false;
        }
        return true;
    }
}
