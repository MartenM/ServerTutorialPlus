package nl.martenm.servertutorialplus.points.editor.args;

import nl.martenm.servertutorialplus.helpers.dataholders.FireWorkInfo;
import nl.martenm.servertutorialplus.language.Lang;
import nl.martenm.servertutorialplus.objects.ServerTutorial;
import nl.martenm.servertutorialplus.points.ServerTutorialPoint;
import nl.martenm.servertutorialplus.points.editor.PointArg;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

/**
 * @author MartenM
 * @since 29-11-2017.
 */
public class FireworkArg extends PointArg {

    public FireworkArg() {
        super("firework");
    }

    @Override
    public boolean run(ServerTutorial serverTutorial, ServerTutorialPoint point, CommandSender sender, String[] args) {

        if (args.length < 1) {
            sender.sendMessage(Lang.WRONG_COMMAND_FORMAT + "/st editpoint <t> <p> firework <add/remove/clear>");
            return false;
        }

        switch (args[0]) {
            case "clear":
                point.setFireworks(null);
                break;

            case "remove":
                if (!(sender instanceof Player)) {
                    sender.sendMessage(Lang.PLAYER_ONLY_COMMAND.toString());
                    return false;
                }

                if (args.length > 1) {
                    sender.sendMessage(Lang.FIREWORK_REMOVE_INFO.toString());
                    return false;
                }

                Player player = (Player) sender;

                FireWorkInfo fw = null;
                double distance = 1000;

                for (FireWorkInfo info : point.getFireworks()) {
                    if (info.getLoc().distance(player.getLocation()) < distance) {
                        fw = info;
                        distance = info.getLoc().distance(player.getLocation());
                    }
                }

                if (fw == null) {
                    sender.sendMessage(Lang.FIREWORK_REMOVE_FAILED.toString());
                    return false;
                }


                point.getFireworks().remove(fw);
                break;

            case "add":
                if (!(sender instanceof Player)) {
                    sender.sendMessage(Lang.PLAYER_ONLY_COMMAND.toString());
                    return false;
                }

                player = (Player) sender;
                if (player.getInventory().getItemInMainHand().getType() == Material.FIREWORK_ROCKET) {
                    ItemStack firework = player.getInventory().getItemInMainHand();

                    if (!(firework.getItemMeta() instanceof FireworkMeta)) {
                        sender.sendMessage(Lang.FIREWORK_ADD_WRONGUSAGE.toString());
                        return false;
                    }

                    FireworkMeta fireworkMeta = (FireworkMeta) firework.getItemMeta();
                    point.getFireworks().add(new FireWorkInfo(player.getLocation(), fireworkMeta));
                } else {
                    sender.sendMessage(Lang.FIREWORK_ADD_WRONGUSAGE.toString());
                    return false;
                }
                break;

            default:
                sender.sendMessage(Lang.WRONG_COMMAND_FORMAT + "/st editpoint <t> <p> firework <add/remove/clear>");
                return false;
        }
        return true;
    }
}
