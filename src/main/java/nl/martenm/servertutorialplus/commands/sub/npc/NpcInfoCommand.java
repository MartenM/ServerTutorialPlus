package nl.martenm.servertutorialplus.commands.sub.npc;

import net.md_5.bungee.api.ChatColor;
import nl.martenm.servertutorialplus.ServerTutorialPlus;
import nl.martenm.servertutorialplus.language.Lang;
import nl.martenm.servertutorialplus.managers.NPCManager;
import nl.martenm.servertutorialplus.objects.NPCInfo;
import nl.martenm.simplecommands.SimpleCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class NpcInfoCommand extends SimpleCommand {

    public NpcInfoCommand() {
        super("info", "Display information", null, false);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        NPCManager npcManager = ServerTutorialPlus.getInstance().getNpcManager();

        if (args.length == 0) {
            sender.sendMessage(ChatColor.DARK_GRAY + "+──────────┤ " + ChatColor.GREEN + ChatColor.BOLD + "NPCs" + ChatColor.DARK_GRAY + "├──────────+");
            sender.sendMessage(" ");
            if (npcManager.getNPCs().size() == 0) {
                sender.sendMessage("  " + Lang.NPC_INFO_NONE);
            } else {
                for (NPCInfo info : npcManager.getNPCs()) {
                    sender.sendMessage(ChatColor.GREEN + "  " + info.getId());
                }

            }
            sender.sendMessage(" ");
            sender.sendMessage("  " + Lang.NPC_INFO_MORE_INFO.toString());
            sender.sendMessage(ChatColor.DARK_GRAY + "+─────────────────────────+");
            return true;
        }

        NPCInfo info = npcManager.getNPC(args[0]);
        if (info == null) {
            sender.sendMessage(Lang.NPC_ID_NOT_EXISTING.toString());
            return true;
        }

        sender.sendMessage(ChatColor.DARK_GRAY + "+──────────┤ " + ChatColor.GREEN + ChatColor.BOLD + "NPC" + ChatColor.DARK_GRAY + "├──────────+");
        sender.sendMessage(" ");
        sender.sendMessage(formatInfo(Lang.ID.toString(), info.getId()));
        sender.sendMessage(formatInfo(Lang.SERVERTUTORIAL_ID.toString(), info.getServerTutorialID()));
        sender.sendMessage(ChatColor.GRAY + String.format("  %s %s %s %s", info.getLocation().getWorld().getName(), info.getLocation().getBlockX(), info.getLocation().getBlockY(), info.getLocation().getBlockZ()));
        sender.sendMessage(" ");
        sender.sendMessage(ChatColor.DARK_GRAY + "+─────────────────────────+");
        return true;
    }

    private String formatInfo(String command, String description) {
        return ChatColor.GREEN + "  " + command + ChatColor.DARK_GRAY + " : " + ChatColor.YELLOW + description;
    }
}
