package nl.martenm.servertutorialplus.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import nl.martenm.servertutorialplus.ServerTutorialPlus;
import nl.martenm.servertutorialplus.language.Lang;
import nl.martenm.servertutorialplus.objects.ServerTutorial;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

/**
 * @author MartenM
 * @since 27-12-2017.
 */
public class PlayerLookUp {

    public static void sendLookupAsync(ServerTutorialPlus plugin, CommandSender sender, OfflinePlayer target) {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (int i = 0; i < 4; i++) {
                    sender.sendMessage(" ");
                }

                List<String> played = plugin.getDataSource().getPlayedTutorials(target.getUniqueId());

                sender.sendMessage(ChatColor.DARK_GRAY + "+──────┤ " + Lang.LOOKUP + " : " + ChatColor.GOLD + target.getName() + ChatColor.DARK_GRAY + "├──────+");
                for (ServerTutorial tutorial : plugin.serverTutorials) {
                    boolean playedBefore = played.contains(tutorial.getId());


                    TextComponent message = new TextComponent("  " + (playedBefore ? ChatColor.GREEN : ChatColor.YELLOW) + tutorial.getId());

                    TextComponent extra = null;
                    if (playedBefore) {
                        extra = new TextComponent(createSpacing(tutorial.getId(), 20) + ChatColor.GRAY + "  :  " + Lang.LOOKUP_SET_UNFINISHED);
                        extra.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/st player " + target.getName() + " " + tutorial.getId() + " unset"));
                        extra.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Lang.LOOKUP_SET_UNFINIHSHED_MESSAGE.toString()).create()));
                    } else {
                        extra = new TextComponent(createSpacing(tutorial.getId(), 20) + ChatColor.GRAY + "  :  " + Lang.LOOKUP_SET_FINISHED);
                        extra.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/st player " + target.getName() + " " + tutorial.getId() + " set"));
                        extra.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Lang.LOOKUP_SET_FINIHSHED_MESSAGE.toString()).create()));
                    }

                    if (sender instanceof Player) {
                        message.addExtra(extra);
                        Player player = (Player) sender;
                        player.spigot().sendMessage(message);
                    } else {
                        sender.sendMessage(message.getText());
                    }
                }


                sender.sendMessage("  " + ChatColor.YELLOW + "O" + ChatColor.GRAY + " = " + Lang.UNCOMPLETED + ChatColor.DARK_GRAY + "  |  " + ChatColor.GREEN + "O" + ChatColor.GRAY + " = " + Lang.COMPLETED);
                sender.sendMessage(ChatColor.DARK_GRAY + "+──────────────────────────+");
            }
        }.runTaskAsynchronously(plugin);
    }


    public static String createSpacing(String input, int amount) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < (amount - input.length()); i++) {
            if (i % 2 == 0) {
                sb.append("   ");
            } else
                sb.append(" ");
        }
        return sb.toString();
    }

}
