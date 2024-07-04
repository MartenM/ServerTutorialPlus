package nl.martenm.servertutorialplus.commands.sub.npc;

import nl.martenm.servertutorialplus.ServerTutorialPlus;
import nl.martenm.servertutorialplus.language.Lang;
import nl.martenm.servertutorialplus.managers.NPCManager;
import nl.martenm.servertutorialplus.objects.NPCInfo;
import nl.martenm.simplecommands.SimpleCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class NpcRemoveCommand extends SimpleCommand {

    public NpcRemoveCommand() {
        super("remove", "Remove a NPC", null, true);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        NPCManager npcManager = ServerTutorialPlus.getInstance().getNpcManager();

        if (args.length == 0) {
            sender.sendMessage(Lang.WRONG_COMMAND_FORMAT + "Wrong usage. Use the command like this: /st npc remove <NPC id>");
            return true;
        }

        NPCInfo info = npcManager.getNPC(args[0]);
        if (info == null) {
            sender.sendMessage(Lang.NPC_ID_NOT_EXISTING.toString().replace("%id%", args[0]));
            return true;
        }

        if (!info.getLocation().getChunk().isLoaded()) {
            sender.sendMessage(Lang.NPC_DELETE_CHUNK_UNLOADED.toString());
            return true;
        }

        npcManager.deleteNPC(info);
        sender.sendMessage(Lang.NPC_REMOVED_SUCCESS.toString());
        return true;
    }
}
