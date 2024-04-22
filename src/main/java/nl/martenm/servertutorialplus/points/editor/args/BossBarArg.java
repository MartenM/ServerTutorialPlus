package nl.martenm.servertutorialplus.points.editor.args;

import nl.martenm.servertutorialplus.language.Lang;
import nl.martenm.servertutorialplus.objects.ServerTutorial;
import nl.martenm.servertutorialplus.points.ServerTutorialPoint;
import nl.martenm.servertutorialplus.points.editor.PointArg;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.command.CommandSender;

public class BossBarArg extends PointArg {

    public BossBarArg() {
        super("bossbar");
    }

    @Override
    public boolean run(ServerTutorial serverTutorial, ServerTutorialPoint point, CommandSender sender, String[] args) {
        if(args.length < 1){
            sender.sendMessage(Lang.WRONG_COMMAND_FORMAT + "/st editpoint <t> <p> bossbar <title/progress/color/style/show-after/hide-after> <args>");
            return false;
        }

        if (args.length < 2) {
            sender.sendMessage(Lang.ERROR_ATLEAST_ONE_WORD.toString());
            return false;
        }

        switch (args[0]) {
            case "title":
                String title = StringUtils.join(args, ' ', 1, args.length);
                point.setBossBarTitle(title);
                break;
            case "progress":
                double progress;
                try {
                    progress = Double.parseDouble(args[1]);
                    if (progress > 1.0 || progress < 0.0) {
                        sender.sendMessage(Lang.ERROR_INVALID_NUMBNER.toString());
                        return false;
                    }
                } catch (NumberFormatException e) {
                    sender.sendMessage(Lang.ERROR_INVALID_NUMBNER.toString());
                    return false;
                }
                point.setBossBarProgress(progress);
                break;
            case "color":
                BarColor color;
                try {
                    color = BarColor.valueOf(args[1]);
                } catch (IllegalArgumentException e) {
                    sender.sendMessage(Lang.INVALID_ARGS.toString());
                    return false;
                }
                point.setBossBarColor(color);
                break;
            case "style":
                BarStyle style;
                try {
                    style = BarStyle.valueOf(args[1]);
                } catch (IllegalArgumentException e) {
                    sender.sendMessage(Lang.INVALID_ARGS.toString());
                    return false;
                }
                point.setBossBarStyle(style);
                break;
            case "show-after":
                double showAfter;
                try {
                    showAfter = Double.parseDouble(args[1]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(Lang.ERROR_INVALID_NUMBNER.toString());
                    return false;
                }
                point.setBossBarShowAfter(showAfter);
                break;
            case "hide-after":
                double hideAfter;
                try {
                    hideAfter = Double.parseDouble(args[1]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(Lang.ERROR_INVALID_NUMBNER.toString());
                    return false;
                }
                point.setBossBarHideAfter(hideAfter);
                break;
            default:
                sender.sendMessage(Lang.WRONG_COMMAND_FORMAT + "/st editpoint <t> <p> bossbar <title/progress/color/style/show-after/hide-after> <args>");
                return false;
        }
        return true;
    }
}
