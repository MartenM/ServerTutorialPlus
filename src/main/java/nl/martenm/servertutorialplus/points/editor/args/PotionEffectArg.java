package nl.martenm.servertutorialplus.points.editor.args;

import net.md_5.bungee.api.ChatColor;
import nl.martenm.servertutorialplus.language.Lang;
import nl.martenm.servertutorialplus.objects.ServerTutorial;
import nl.martenm.servertutorialplus.points.ServerTutorialPoint;
import nl.martenm.servertutorialplus.points.editor.PointArg;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * @author MartenM
 * @since 30-11-2017.
 */
public class PotionEffectArg extends PointArg {

    public PotionEffectArg() {
        super("potion");
    }

    @Override
    public boolean run(ServerTutorial serverTutorial, ServerTutorialPoint point, CommandSender sender, String[] args) {

        if (args.length < 1) {
            sender.sendMessage(Lang.WRONG_COMMAND_FORMAT + "/st editpoint <t> <p> potion <add / remove / list / clear>");
            return false;
        }

        switch (args[0]) {
            case "clear":
                point.getPointionEffects().clear();
                break;

            case "list":
                sender.sendMessage(ChatColor.GRAY + "[ " + ChatColor.YELLOW + "Potion effects");
                for (int i = 0; i < point.getPointionEffects().size(); i++) {
                    PotionEffect effect = point.getPointionEffects().get(i);
                    sender.sendMessage(ChatColor.GRAY + "[ " + ChatColor.GREEN + (i + 1) + ChatColor.YELLOW + " " + effect.getType().getName() + " Time:" + effect.getDuration());
                }
                break;

            case "remove":
                if (args.length < 2) {
                    sender.sendMessage(Lang.ERROR_NO_INDEX.toString());
                    return false;
                }

                try {
                    point.getPointionEffects().remove(Integer.parseInt(args[1]) - 1);
                } catch (NumberFormatException ex) {
                    sender.sendMessage(Lang.ERROR_INVALID_NUMBNER.toString());
                    return false;
                } catch (IndexOutOfBoundsException ex) {
                    sender.sendMessage(Lang.ERROR_NOTEXISTING_INDEX.toString());
                    return false;
                }
                break;

            case "add":
                if (!(sender instanceof Player)) {
                    sender.sendMessage(Lang.PLAYER_ONLY_COMMAND.toString());
                    return false;
                }
                Player player = (Player) sender;
                ItemStack inhand = player.getInventory().getItemInMainHand();

                int time;
                int amplifier = 1;
                boolean isAmbient = true;
                boolean hasParticles = false;
                PotionEffectType type = null;

                if (args.length == 4) {
                    try {
                        type = PotionEffectType.getByName(args[1]);
                        if (type == null) {
                            throw new Exception(Lang.ERROR_INVALID_EFFECT.toString());
                        }
                    } catch (Exception ex) {
                        player.sendMessage(Lang.ERROR_INVALID_EFFECT.toString());
                        return false;
                    }
                }

                if (args.length >= 2 + (type == null ? 0 : 1)) {
                    try {
                        time = Integer.parseInt(args[1 + (type == null ? 0 : 1)]) * 20;
                        player.sendMessage(Lang.POTIONEFFECT_TIME.toString()
                                .replace("%ticks%", time + "")
                                .replace("%seconds%", args[1 + (type == null ? 0 : 1)]));
                    } catch (Exception ex) {
                        player.sendMessage(Lang.ERROR_INVALID_TIME.toString());
                        return false;
                    }
                } else {
                    time = (int) point.getTime() * 20;
                    player.sendMessage(Lang.POTIONEFFECT_NOTIME.toString());
                }

                if (args.length >= 3 + (type == null ? 0 : 1)) {
                    try {
                        amplifier = Integer.parseInt(args[2 + (type == null ? 0 : 1)]);
                        player.sendMessage(Lang.POTIONEFFECT_AMPLIFIER.toString().replace("%amp%", amplifier + ""));
                    } catch (Exception ex) {
                        player.sendMessage(Lang.ERROR_INVALID_AMPLIFIER.toString());
                        return false;
                    }
                }


                if (type == null) {
                    if (inhand == null) {
                        player.sendMessage(Lang.POTIONEFFECT_WRONGUSAGE_1.toString());
                        player.sendMessage(Lang.POTIONEFFECT_WRONGUSAGE_2.toString());
                        return false;
                    }

                    if (!(inhand.getItemMeta() instanceof PotionMeta)) {
                        player.sendMessage(Lang.POTIONEFFECT_WRONGUSAGE_1.toString());
                        player.sendMessage(Lang.POTIONEFFECT_WRONGUSAGE_2.toString());
                        return false;
                    }

                    PotionMeta meta = (PotionMeta) inhand.getItemMeta();
                    type = meta.getBasePotionData().getType().getEffectType();
                }

                point.getPointionEffects().add(new PotionEffect(type, time, amplifier, isAmbient, hasParticles));
                break;
        }

        return true;
    }
}
