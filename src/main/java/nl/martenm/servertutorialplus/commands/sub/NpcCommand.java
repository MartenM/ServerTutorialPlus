package nl.martenm.servertutorialplus.commands.sub;

import net.md_5.bungee.api.ChatColor;
import nl.martenm.servertutorialplus.ServerTutorialPlus;
import nl.martenm.servertutorialplus.helpers.PluginUtils;
import nl.martenm.servertutorialplus.helpers.SpigotUtils;
import nl.martenm.servertutorialplus.language.Lang;
import nl.martenm.servertutorialplus.objects.NPCInfo;
import nl.martenm.servertutorialplus.objects.ServerTutorial;
import nl.martenm.servertutorialplus.objects.TutorialEntitySelector;
import nl.martenm.simplecommands.SimpleCommand;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.UUID;

public class NpcCommand extends SimpleCommand {

    public NpcCommand() {
        super("npc", Lang.HELP_NPC.toString(), "+npc", false);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        ServerTutorialPlus plugin = ServerTutorialPlus.getInstance();


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

            if(PluginUtils.getNPC(plugin, args[1]) != null){
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

            Entity entity_npc = player.getWorld().spawnEntity(player.getLocation(), et);
            if(!(entity_npc instanceof LivingEntity)){
                sender.sendMessage(Lang.NPC_LIVING_TYPE.toString());
                entity_npc.remove();
                return true;
            }

            LivingEntity npc = (LivingEntity) entity_npc;
            npc.setAI(false);
            npc.setCanPickupItems(false);
            npc.setGravity(false);
            npc.setCollidable(false);

            ArmorStand armorStand_1 = (ArmorStand) player.getWorld().spawnEntity(player.getLocation(), EntityType.ARMOR_STAND);
            armorStand_1.setGravity(false);

            if(Bukkit.getVersion().contains("1.8") || Bukkit.getVersion().contains("1.9") || Bukkit.getVersion().contains("1.10")){
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "  "));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7(&c!&7) &eSpigot version < 1.11. Using alternative text method."));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aTIP: &7Use /st npc set height <npc ID> to set the text heigh relative to the NPC."));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "  "));
                armorStand_1.teleport(npc.getLocation().add(0, 0.45, 0));

                new BukkitRunnable(){
                    @Override
                    public void run() {
                        Location loc = armorStand_1.getLocation();

                        ArmorStand armorStand_2 = (ArmorStand) loc.getWorld().spawnEntity(loc.add(0, 0.45, 0), EntityType.ARMOR_STAND);
                        armorStand_2.setGravity(false);

                        armorStand_1.setMarker(true);
                        armorStand_2.setMarker(true);

                        armorStand_1.setVisible(false);
                        armorStand_2.setVisible(false);

                        armorStand_1.setCustomNameVisible(true);
                        armorStand_2.setCustomNameVisible(true);

                        armorStand_1.setCustomName(ChatColor.YELLOW + ChatColor.BOLD.toString() +"Right click!");
                        armorStand_2.setCustomName(ChatColor.GREEN +  "Tutorial");

                        armorStand_1.teleport(loc.add(0, -0.25, 0));

                        npc.setInvulnerable(true);
                        armorStand_1.setInvulnerable(true);
                        armorStand_2.setInvulnerable(true);

                        NPCInfo info = new NPCInfo(plugin, args[1], npc.getUniqueId(), new UUID[] {armorStand_1.getUniqueId(), armorStand_2.getUniqueId()}, args[2]);
                        plugin.clickableNPCs.put(npc.getUniqueId(), info);

                        npc.teleport(npc.getLocation());
                        sender.sendMessage(Lang.NPC_CREATION_SUCCESS.toString().replace("%id%", info.getId()).replace("%tutorial%", info.getServerTutorialID()));
                    }
                }.runTaskLater(plugin, 1);

                if(Bukkit.getVersion().contains("1.8") || Bukkit.getVersion().contains("1.9") || Bukkit.getVersion().contains("1.10")) {
                    new BukkitRunnable(){
                        Location loc = npc.getLocation();
                        @Override
                        public void run() {
                            try {
                                npc.teleport(loc);
                                npc.setVelocity(new Vector(0, 0, 0));
                            }
                            catch (Exception ex){
                                this.cancel();
                            }
                        }
                    }.runTaskTimer(plugin, 0, 5);
                }

            }
            else{
                npc.addPassenger(armorStand_1);

                new BukkitRunnable(){
                    @Override
                    public void run() {
                        //Do stuff
                        Location loc = armorStand_1.getLocation();
                        npc.removePassenger(armorStand_1);

                        ArmorStand armorStand_2 = (ArmorStand) loc.getWorld().spawnEntity(loc.add(0, 0.45, 0), EntityType.ARMOR_STAND);
                        armorStand_2.setGravity(false);

                        armorStand_1.setMarker(true);
                        armorStand_2.setMarker(true);

                        armorStand_1.setVisible(false);
                        armorStand_2.setVisible(false);

                        armorStand_1.setCustomNameVisible(true);
                        armorStand_2.setCustomNameVisible(true);

                        armorStand_1.setCustomName(ChatColor.YELLOW + ChatColor.BOLD.toString() +"Right click!");
                        armorStand_2.setCustomName(ChatColor.GREEN +  "Tutorial");

                        armorStand_1.teleport(loc.add(0, -0.25, 0));

                        npc.setInvulnerable(true);
                        armorStand_1.setInvulnerable(true);
                        armorStand_2.setInvulnerable(true);

                        NPCInfo info = new NPCInfo(plugin, args[1], npc.getUniqueId(), new UUID[] {armorStand_1.getUniqueId(), armorStand_2.getUniqueId()}, args[2]);
                        plugin.clickableNPCs.put(npc.getUniqueId(), info);

                        npc.teleport(npc.getLocation());
                        sender.sendMessage(Lang.NPC_CREATION_SUCCESS.toString().replace("%id%", info.getId()).replace("%tutorial%", info.getServerTutorialID()));
                    }
                }.runTaskLater(plugin, 1);

                return true;
            }
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

            NPCInfo info = PluginUtils.getNPC(plugin, args[1]);
            if(info == null){
                sender.sendMessage(Lang.NPC_ID_NOT_EXISTING.toString().replace("%id%", args[1]));
                return true;
            }

            if(SpigotUtils.getEntity(info.getNpcId()) != null)
                SpigotUtils.getEntity(info.getNpcId()).remove();
            SpigotUtils.getEntity(info.getArmorstandIDs()[0]).remove();
            SpigotUtils.getEntity(info.getArmorstandIDs()[1]).remove();
            plugin.clickableNPCs.remove(info.getNpcId());
            sender.sendMessage(Lang.NPC_REMOVED_SUCCESS.toString());
            return true;
            //endregion
        }

        else if(args[0].equalsIgnoreCase("info")){
            //region List
            if(args.length <= 1) {
                sender.sendMessage(ChatColor.DARK_GRAY + "+──────────┤ " + ChatColor.GREEN + ChatColor.BOLD + "NPCs" + ChatColor.DARK_GRAY + "├──────────+");
                sender.sendMessage(" ");
                if (plugin.clickableNPCs.size() == 0) {
                    sender.sendMessage("  " + Lang.NPC_INFO_NONE);
                } else {
                    for (NPCInfo info : plugin.clickableNPCs.values()) {
                        sender.sendMessage(ChatColor.GREEN + "  " + info.getId());
                    }

                }
                sender.sendMessage(" ");
                sender.sendMessage("  " + Lang.NPC_INFO_MORE_INFO.toString());
                sender.sendMessage(ChatColor.DARK_GRAY + "+─────────────────────────+");
                return true;
            }

            NPCInfo info = PluginUtils.getNPC(plugin, args[1]);
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

            NPCInfo info = PluginUtils.getNPC(plugin, args[1]);
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

            NPCInfo info = PluginUtils.getNPC(plugin, args[1]);
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

        sender.sendMessage(Lang.COMMAND_ARGUMENTS_AVAILABLE.toString().replace("%args%", "add, remove, list, bind, info, text, height"));
        return true;
        //endregion
    }

    private String formatInfo(String command, String description){
        return ChatColor.GREEN + "  " + command + ChatColor.DARK_GRAY + " : " + ChatColor.YELLOW + description;
    }
}
