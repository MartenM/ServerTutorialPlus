package nl.martenm.servertutorialplus.commands.sub;

import net.md_5.bungee.api.ChatColor;
import nl.martenm.servertutorialplus.ServerTutorialPlus;
import nl.martenm.servertutorialplus.helpers.PluginUtils;
import nl.martenm.servertutorialplus.helpers.SpigotUtils;
import nl.martenm.servertutorialplus.language.Lang;
import nl.martenm.servertutorialplus.managers.NPCManager;
import nl.martenm.servertutorialplus.objects.NPCInfo;
import nl.martenm.servertutorialplus.objects.ServerTutorial;
import nl.martenm.servertutorialplus.objects.TutorialEntitySelector;
import nl.martenm.simplecommands.SimpleCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class NpcCommand extends SimpleCommand {

    public NpcCommand() {
        super("npc", Lang.HELP_NPC.toString(), "+npc", false);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        ServerTutorialPlus plugin = ServerTutorialPlus.getInstance();
        NPCManager npcManager = plugin.getNpcManager();

        if(args.length <= 0){
            sender.sendMessage(Lang.COMMAND_ARGUMENTS_AVAILABLE.toString().replace("%args%", "add, remove, list, bind, text, height"));
            return true;
        }
        //endregion

        if(args[0].equalsIgnoreCase("add")){
            //region add
            if(!(sender instanceof Player)){
                sender.sendMessage(Lang.PLAYER_ONLY_COMMAND.toString());
                return true;
            }

            if(Bukkit.getVersion().contains("1.8") || Bukkit.getVersion().contains("1.9")){
                for(int i = 0; i < 3; i++) sender.sendMessage(" ");
                String warningPrefix = ChatColor.translateAlternateColorCodes('&', "&8[&4&l!&r&8]&r ");

                sender.sendMessage(warningPrefix + ChatColor.GREEN + "Hey there!" + ChatColor.RESET + " We noticed that you are trying to use ST NPCs on a version lower then 1.10 :(");
                sender.sendMessage(warningPrefix + ChatColor.translateAlternateColorCodes('&', "Minecraft has progressed a &6&lLOT&r since this the update this server is running on. New methods have been implemented to create cool features!"));
                sender.sendMessage(warningPrefix + ChatColor.translateAlternateColorCodes('&', "&cSadly&r, this also means that some things might not work on lower versions. ST NPCs do not work on versions lower then 1.10..."));
                sender.sendMessage(warningPrefix + ChatColor.translateAlternateColorCodes('&', "It would require &ca lot of time&r to keep updating for backwards compatibility and it would remove the ability to create &anew&r features that use new methods."));
                sender.sendMessage(" ");
                sender.sendMessage(warningPrefix + ChatColor.translateAlternateColorCodes('&',ChatColor.YELLOW + "To create an NPC you could use another plugin to spawn the NPC and use the command &r/st npc bind <npc id> <server tutorial>&e to bind it to a tutorial."));
                return true;
            }

            if(args.length < 4){
                sender.sendMessage(Lang.WRONG_COMMAND_FORMAT + "/st npc add <NPC id> <ServerTutorial> <livingEntity>");
                return true;
            }

            Player player = (Player) sender;

            if(npcManager.getNPC(plugin, args[1]) != null){
                sender.sendMessage(Lang.NPC_ID_EXIST.toString());
                return true;
            }

            EntityType et;
            try{
                et = EntityType.valueOf(args[3]);
            } catch (Exception e){
                sender.sendMessage(Lang.NPC_TESTED_MOBS + PluginUtils.allMobs());
                sender.sendMessage(Lang.NPC_WRONG_TYPE.toString().replace("%type%", args[3]));
                return true;
            }

            NPCInfo info = npcManager.createNPC(et, player.getLocation(), args[1], args[2]);
            sender.sendMessage(Lang.NPC_CREATION_SUCCESS.toString().replace("%id%", info.getId()).replace("%tutorial%", info.getServerTutorialID()));
            //endregion
        }

        else if(args[0].equalsIgnoreCase("bind"))
        {
            //region bind
            if(!(sender instanceof Player)){
                sender.sendMessage(Lang.PLAYER_ONLY_COMMAND.toString());
                return true;
            }
            Player player = (Player) sender;
            if(plugin.selectingNpc.containsKey(player.getUniqueId())){
                plugin.selectingNpc.remove(player.getUniqueId());
                sender.sendMessage(Lang.NPC_SELECTION_CANCELLED.toString());
                return true;
            }

            if(args.length < 3){
                sender.sendMessage(Lang.WRONG_COMMAND_FORMAT + "/st npc bind <NPC id> <ServerTutorial>");
                return true;
            }

            ServerTutorial tutorial;
            tutorial = PluginUtils.getTutorial(plugin, args[2]);
            if(tutorial == null){
                sender.sendMessage(Lang.TUTORIAL_ID_NOT_FOUND.toString());
                return true;
            }

            TutorialEntitySelector selector = new TutorialEntitySelector(player, tutorial, args[1]);
            plugin.selectingNpc.put(player.getUniqueId(), selector);
            player.sendMessage(Lang.NPC_SELECTION_MESSAGE.toString());
            return true;
            //endregion
        }

        else if(args[0].equalsIgnoreCase("remove")){
            //region remove
            if(args.length <= 1){
                sender.sendMessage(Lang.WRONG_COMMAND_FORMAT + "Wrong usage. Use the command like this: /st npc remove <NPC id>");
                return true;
            }

            NPCInfo info = npcManager.getNPC(plugin, args[1]);
            if(info == null){
                sender.sendMessage(Lang.NPC_ID_NOT_EXISTING.toString().replace("%id%", args[1]));
                return true;
            }

            npcManager.deleteNPC(info);
            sender.sendMessage(Lang.NPC_REMOVED_SUCCESS.toString());
            return true;
            //endregion
        }

        else if(args[0].equalsIgnoreCase("info")){
            //region List
            if(args.length <= 1) {
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

            NPCInfo info = npcManager.getNPC(plugin, args[1]);
            if(info == null){
                sender.sendMessage(Lang.NPC_ID_NOT_EXISTING.toString());
                return true;
            }

            sender.sendMessage(ChatColor.DARK_GRAY + "+──────────┤ " + ChatColor.GREEN + ChatColor.BOLD + "NPC" + ChatColor.DARK_GRAY + "├──────────+");
            sender.sendMessage(" ");
            sender.sendMessage(formatInfo(Lang.ID.toString(), info.getId()));
            sender.sendMessage(formatInfo(Lang.NPC_TYPE.toString(), SpigotUtils.getEntity(info.getNpcId()).getType().toString()));
            sender.sendMessage(formatInfo(Lang.SERVERTUTORIAL_ID.toString() , info.getServerTutorialID()));
            sender.sendMessage(" ");
            sender.sendMessage(ChatColor.DARK_GRAY + "+─────────────────────────+");
            return true;
            //endregion
        }

        else if(args[0].equalsIgnoreCase("text")){
            //region Text
            if(args.length < 4){
                sender.sendMessage(Lang.WRONG_COMMAND_FORMAT + "/st npc text <NPC id> <top/bot> <text>");
                return true;
            }

            NPCInfo info = npcManager.getNPC(plugin, args[1]);
            if(info == null){
                sender.sendMessage(Lang.NPC_ID_NOT_EXISTING.toString());
                return true;
            }

            Entity stand;
            if(args[2].equalsIgnoreCase("bot")){
                stand = SpigotUtils.getEntity(info.getArmorstandIDs()[0]);
            } else if(args[2].equalsIgnoreCase("top")){
                stand = SpigotUtils.getEntity(info.getArmorstandIDs()[1]);
            } else {
                sender.sendMessage(Lang.WRONG_COMMAND_FORMAT + "/st npc text <NPC id> <top/bot> <text>");
                return true;
            }

            String message = "";
            for(int i = 3; i < args.length; i++){
                message = message + args[i] + (args.length - 1 != i ? " " : "");
            }
            message = ChatColor.translateAlternateColorCodes('&', message);
            stand.setCustomName(message);
            sender.sendMessage(Lang.NPC_TEXT_CHANGE.toString());
            return true;
            //endregion
        }

        else if(args[0].equalsIgnoreCase("height")){
            //region Height
            if(args.length < 3){
                sender.sendMessage(Lang.WRONG_COMMAND_FORMAT + "/st npc height <NPC id> <value like 0.25>");
                return true;
            }

            NPCInfo info = npcManager.getNPC(plugin, args[1]);
            if(info == null){
                sender.sendMessage(Lang.NPC_ID_NOT_EXISTING.toString());
                return true;
            }

            double height = 0;
            try{
                height = Double.parseDouble(args[2]);
            } catch (Exception ex){
                sender.sendMessage(Lang.ERROR_INVALID_NUMBNER.toString());
            }

            for(int i = 0; i < info.getArmorstandIDs().length; i++){
                ArmorStand stand = (ArmorStand) SpigotUtils.getEntity(info.getArmorstandIDs()[i]);
                stand.teleport(SpigotUtils.getEntity(info.getNpcId()).getLocation().add(0, height + (-0.25 * i), 0));
            }

            sender.sendMessage(Lang.NPC_HEIGHT_CHANGE.toString());
            return true;
            //endregion
        }

        else if(args[0].equalsIgnoreCase("test")) {
            plugin.getNpcManager().loadNPCs();
            sender.sendMessage(ChatColor.YELLOW + "LOading");
            return true;
        }

        sender.sendMessage(Lang.COMMAND_ARGUMENTS_AVAILABLE.toString().replace("%args%", "add, remove, list, bind, info, text, height"));
        return true;
        //endregion
    }

    private String formatInfo(String command, String description){
        return ChatColor.GREEN + "  " + command + ChatColor.DARK_GRAY + " : " + ChatColor.YELLOW + description;
    }
}
