package nl.martenm.servertutorialplus.commands.sub.npc;

import net.md_5.bungee.api.ChatColor;
import nl.martenm.servertutorialplus.ServerTutorialPlus;
import nl.martenm.servertutorialplus.helpers.PluginUtils;
import nl.martenm.servertutorialplus.language.Lang;
import nl.martenm.servertutorialplus.managers.NPCManager;
import nl.martenm.servertutorialplus.objects.NPCInfo;
import nl.martenm.simplecommands.SimpleCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class NpcAddCommand extends SimpleCommand {

    public NpcAddCommand() {
        super("add", "Used to create a NPC", null, true);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        NPCManager npcManager = ServerTutorialPlus.getInstance().getNpcManager();

        if (Bukkit.getVersion().contains("1.8") || Bukkit.getVersion().contains("1.9")) {
            for (int i = 0; i < 3; i++) sender.sendMessage(" ");
            String warningPrefix = ChatColor.translateAlternateColorCodes('&', "&8[&4&l!&r&8]&r ");

            sender.sendMessage(warningPrefix + ChatColor.GREEN + "Hey there!" + ChatColor.RESET + " We noticed that you are trying to use ST NPCs on a version lower then 1.10 :(");
            sender.sendMessage(warningPrefix + ChatColor.translateAlternateColorCodes('&', "Minecraft has progressed a &6&lLOT&r since this the update this server is running on. New methods have been implemented to create cool features!"));
            sender.sendMessage(warningPrefix + ChatColor.translateAlternateColorCodes('&', "&cSadly&r, this also means that some things might not work on lower versions. ST NPCs do not work on versions lower then 1.10..."));
            sender.sendMessage(warningPrefix + ChatColor.translateAlternateColorCodes('&', "It would require &ca lot of time&r to keep updating for backwards compatibility and it would remove the ability to create &anew&r features that use new methods."));
            sender.sendMessage(" ");
            sender.sendMessage(warningPrefix + ChatColor.translateAlternateColorCodes('&', ChatColor.YELLOW + "To create an NPC you could use another plugin to spawn the NPC and use the command &r/st npc bind <npc id> <server tutorial>&e to bind it to a tutorial."));
            return true;
        }

        if (args.length < 3) {
            sender.sendMessage(Lang.WRONG_COMMAND_FORMAT + "/st npc add <NPC id> <ServerTutorial> <livingEntity>");
            return true;
        }

        Player player = (Player) sender;

        if (npcManager.getNPC(args[0]) != null) {
            sender.sendMessage(Lang.NPC_ID_EXIST.toString());
            return true;
        }

        EntityType et;
        try {
            et = EntityType.valueOf(args[2]);
        } catch (Exception e) {
            sender.sendMessage(Lang.NPC_TESTED_MOBS + PluginUtils.allMobs());
            sender.sendMessage(Lang.NPC_WRONG_TYPE.toString().replace("%type%", args[2]));
            return true;
        }

        NPCInfo info = npcManager.createNPC(et, player.getLocation(), args[0], args[1]);
        sender.sendMessage(Lang.NPC_CREATION_SUCCESS.toString().replace("%id%", info.getId()).replace("%tutorial%", info.getServerTutorialID()));
        return true;
    }
}
