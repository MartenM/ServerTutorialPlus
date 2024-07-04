package nl.martenm.servertutorialplus.commands.sub.npc;

import nl.martenm.servertutorialplus.ServerTutorialPlus;
import nl.martenm.servertutorialplus.helpers.SpigotUtils;
import nl.martenm.servertutorialplus.language.Lang;
import nl.martenm.servertutorialplus.managers.NPCManager;
import nl.martenm.servertutorialplus.objects.NPCInfo;
import nl.martenm.simplecommands.SimpleCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;

public class NpcTextHeightCommand extends SimpleCommand {
    public NpcTextHeightCommand() {
        super("height", "Set the text height", null, true);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        NPCManager npcManager = ServerTutorialPlus.getInstance().getNpcManager();

        if (args.length < 2) {
            sender.sendMessage(Lang.WRONG_COMMAND_FORMAT + "/st npc height <NPC id> <value like 0.25>");
            return true;
        }

        NPCInfo info = npcManager.getNPC(args[0]);
        if (info == null) {
            sender.sendMessage(Lang.NPC_ID_NOT_EXISTING.toString());
            return true;
        }

        double height = 0;
        try {
            height = Double.parseDouble(args[1]);
        } catch (Exception ex) {
            sender.sendMessage(Lang.ERROR_INVALID_NUMBNER.toString());
        }

        for (int i = 0; i < info.getArmorstandIDs().length; i++) {
            ArmorStand stand = (ArmorStand) SpigotUtils.getEntity(info.getArmorstandIDs()[i]);
            stand.teleport(SpigotUtils.getEntity(info.getNpcId()).getLocation().add(0, height + (-0.25 * i), 0));
        }

        sender.sendMessage(Lang.NPC_HEIGHT_CHANGE.toString());
        return true;
    }
}
