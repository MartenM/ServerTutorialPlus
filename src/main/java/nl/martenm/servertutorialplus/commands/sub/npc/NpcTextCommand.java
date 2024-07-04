package nl.martenm.servertutorialplus.commands.sub.npc;

import net.md_5.bungee.api.ChatColor;
import nl.martenm.servertutorialplus.ServerTutorialPlus;
import nl.martenm.servertutorialplus.helpers.SpigotUtils;
import nl.martenm.servertutorialplus.language.Lang;
import nl.martenm.servertutorialplus.managers.NPCManager;
import nl.martenm.servertutorialplus.objects.NPCInfo;
import nl.martenm.simplecommands.SimpleCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;

public class NpcTextCommand extends SimpleCommand {

    public NpcTextCommand() {
        super("text", "Set the text height", null, true);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        NPCManager npcManager = ServerTutorialPlus.getInstance().getNpcManager();

        if (args.length < 3) {
            sender.sendMessage(Lang.WRONG_COMMAND_FORMAT + "/st npc text <NPC id> <top/bot> <text>");
            return true;
        }

        NPCInfo info = npcManager.getNPC(args[0]);
        if (info == null) {
            sender.sendMessage(Lang.NPC_ID_NOT_EXISTING.toString());
            return true;
        }

        Entity stand;
        if (args[1].equalsIgnoreCase("bot")) {
            stand = SpigotUtils.getEntity(info.getArmorstandIDs()[0]);
        } else if (args[1].equalsIgnoreCase("top")) {
            stand = SpigotUtils.getEntity(info.getArmorstandIDs()[1]);
        } else {
            sender.sendMessage(Lang.WRONG_COMMAND_FORMAT + "/st npc text <NPC id> <top/bot> <text>");
            return true;
        }

        String message = "";
        for (int i = 2; i < args.length; i++) {
            message = message + args[i] + (args.length - 1 != i ? " " : "");
        }
        message = ChatColor.translateAlternateColorCodes('&', message);
        stand.setCustomName(message);
        sender.sendMessage(Lang.NPC_TEXT_CHANGE.toString());
        return true;
    }
}
