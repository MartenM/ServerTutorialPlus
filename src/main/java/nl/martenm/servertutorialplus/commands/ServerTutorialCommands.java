package nl.martenm.servertutorialplus.commands;

import nl.martenm.servertutorialplus.MainClass;
import nl.martenm.servertutorialplus.gui.GuiInventories;
import nl.martenm.servertutorialplus.helpers.*;
import nl.martenm.servertutorialplus.helpers.dataholders.OldValuesPlayer;
import nl.martenm.servertutorialplus.language.Lang;
import nl.martenm.servertutorialplus.objects.*;
import nl.martenm.servertutorialplus.points.IPlayPoint;
import nl.martenm.servertutorialplus.points.IPointCallBack;
import nl.martenm.servertutorialplus.points.PointType;
import nl.martenm.servertutorialplus.points.ServerTutorialPoint;
import nl.martenm.servertutorialplus.points.custom.CheckPoint;
import nl.martenm.servertutorialplus.points.custom.ClickBlockPoint;
import nl.martenm.servertutorialplus.points.custom.TimedPoint;
import nl.martenm.servertutorialplus.points.editor.PointEditor;
import net.md_5.bungee.api.ChatColor;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.HashSet;
import java.util.UUID;

/**
 * General class for the /st command.
 * Created by Marten on 5-3-2017.
 */
public class ServerTutorialCommands implements CommandExecutor{

    private MainClass plugin;
    public ServerTutorialCommands(MainClass plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if(args.length > 0){
            if(args[0].equalsIgnoreCase("create")){
                //region Create
                if(!sender.hasPermission("servertutorialplus.command.create")){
                    sender.sendMessage(Lang.NO_PERMS.toString());
                    return true;
                }

                if(args.length == 1){
                    sender.sendMessage(ChatColor.RED + "Wrong usage. Use the command like this: /st create <id>");
                    return true;
                }

                String id = args[1];
                for(ServerTutorial st : plugin.serverTutorials){
                    if(st.getId().equalsIgnoreCase(id)){
                        sender.sendMessage(ChatColor.RED + "There already exists a server tutorial with that ID!");
                        return true;
                    }
                }
                ServerTutorial st = new ServerTutorial(id);
                plugin.serverTutorials.add(st);
                sender.sendMessage(Lang.TUTORIAL_CREATED.toString().replace("%id%", id));
                return true;
                //endregion
            }

            else if(args[0].equalsIgnoreCase("remove")){
                //region Remove
                if(!sender.hasPermission("servertutorialplus.command.remove")){
                    sender.sendMessage(Lang.NO_PERMS.toString());
                    return true;
                }

                if(args.length == 1){
                    sender.sendMessage(Lang.WRONG_COMMAND_FORMAT + "/st remove <id>");
                    return true;
                }

                for(ServerTutorial st : plugin.serverTutorials){
                    if(st.getId().equalsIgnoreCase(args[1])){
                        plugin.serverTutorials.remove(st);
                        sender.sendMessage(Lang.TUTORIAL_REMOVED.toString().replace("%id%", args[1]));
                        plugin.tutorialSaves.set("tutorials." + st.getId(), null);
                        plugin.tutorialSaves.save();
                        return true;
                    }
                }

                sender.sendMessage(Lang.SAVE_SUCCES.toString());
                return true;
                //endregion
            }

            else if(args[0].equalsIgnoreCase("addpoint")){
                //region addpoint
                if(!sender.hasPermission("servertutorialplus.command.addpoint")){
                    sender.sendMessage(Lang.NO_PERMS.toString());
                    return true;
                }

                if(!(sender instanceof Player)){
                    sender.sendMessage(Lang.PLAYER_ONLY_COMMAND.toString());
                    return true;
                }

                if(args.length < 3){
                    sender.sendMessage(Lang.WRONG_COMMAND_FORMAT + "/st addpoint <id> <timed/checkpoint/clickblock>");
                    return true;
                }

                ServerTutorial serverTutorial = null;
                for(ServerTutorial st : plugin.serverTutorials){
                    if(st.getId().equalsIgnoreCase(args[1])){
                        serverTutorial = st;
                        break;
                    }
                }

                if(serverTutorial == null){
                    sender.sendMessage(Lang.TUTORIAL_ID_NOT_FOUND.toString());
                    return true;
                }

                Player player = (Player) sender;//
                ServerTutorialPoint point;

                switch (args[2]) {
                    case "TIME":
                    case "time":
                    case "timed":
                        point = new TimedPoint(plugin, player.getLocation());
                        break;
                    case "checkpoint":
                    case "CHECKPOINT":
                        point = new CheckPoint(plugin, player.getLocation());
                        break;
                    case "clickblock":
                    case "CLICKBLOCK":
                    case "click_block":
                        point = new ClickBlockPoint(plugin, player.getLocation(), true);
                        break;
                    default:
                        sender.sendMessage(Lang.POINT_INVALID_TYPE.toString());

                        TextComponent message = new TextComponent(Lang.POINT_EXAMPLE_MESSAGE.toString());

                        for (int i = 0; i < PointType.values().length; i++){
                            PointType type = PointType.values()[i];
                            TextComponent component = new TextComponent(ChatColor.GREEN + type.toString().toLowerCase());
                            component.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/st addpoint " + args[1] + " " + type.toString().toLowerCase()));
                            component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Lang.POINT_EXAMPLE_COMMAND_CLICK.toString().replace("%type%", type.toString())).create()));
                            message.addExtra(component);
                            if(i < PointType.values().length - 1) message.addExtra(ChatColor.GRAY + " / ");
                        }

                        player.spigot().sendMessage(message);
                        return true;
                }

                serverTutorial.points.add(point);
                player.sendMessage(Lang.POINT_ADDED.toString());

                return true;
                //endregion
            }

            else if(args[0].equalsIgnoreCase("removepoint")){
                //region removepoint
                if(!sender.hasPermission("servertutorialplus.command.removepoint")){
                    sender.sendMessage(Lang.NO_PERMS.toString());
                    return true;
                }

                if(args.length < 3){
                    sender.sendMessage(Lang.WRONG_COMMAND_FORMAT + "/st removepoint <server tutorial ID> <point index>");
                    return true;
                }

                ServerTutorial serverTutorial = null;
                for(ServerTutorial st : plugin.serverTutorials){
                    if(st.getId().equalsIgnoreCase(args[1])){
                        serverTutorial = st;
                        break;
                    }
                }

                if(serverTutorial == null){
                    sender.sendMessage(Lang.TUTORIAL_ID_NOT_FOUND.toString());
                    return true;
                }

                int index;
                try{
                    index = Integer.valueOf(args[2]);
                }
                catch (Exception e){
                    sender.sendMessage(Lang.COMMAND_HASTOBE_NUMBER.toString());
                    return true;
                }

                if(index - 1 < 0 || index > serverTutorial.points.size() ){
                    sender.sendMessage(Lang.COMMAND_INVALID_INDEX.toString());
                    return true;
                }

                serverTutorial.points.remove(index - 1);
                sender.sendMessage(Lang.POINT_REMOVED.toString().replace("%id%", args[1]));
                return true;
                //endregion
            }

            else if(args[0].equalsIgnoreCase("info")){
                //region info
                if(!sender.hasPermission("servertutorialplus.command.info")){
                    sender.sendMessage(Lang.NO_PERMS.toString());
                }

                if(args.length == 1){
                    sender.sendMessage(ChatColor.DARK_GRAY + "+──────────┤ " + ChatColor.GREEN + ChatColor.BOLD + "Info" + ChatColor.DARK_GRAY + "├──────────+");
                    sender.sendMessage(" ");
                    if(plugin.serverTutorials.size() == 0){
                        sender.sendMessage("  " + Lang.INFO_NONE_EXISTING);
                    } else{
                        for(ServerTutorial st : plugin.serverTutorials){
                            sender.sendMessage(ChatColor.GREEN + "  " + st.getId());
                        }
                    }
                    sender.sendMessage(" ");
                    sender.sendMessage("  " + Lang.INFO_MORE_INFO);
                    sender.sendMessage(ChatColor.DARK_GRAY + "+─────────────────────────+");
                    return true;
                }

                if(plugin.serverTutorials.size() == 0){
                    sender.sendMessage(Lang.INFO_NONE_EXISTING.toString());
                    return true;
                }

                ServerTutorial serverTutorial = null;
                for(ServerTutorial st : plugin.serverTutorials){
                    if(st.getId().equalsIgnoreCase(args[1])){
                        serverTutorial = st;
                        break;
                    }
                }

                if(serverTutorial == null){
                    sender.sendMessage(Lang.TUTORIAL_ID_NOT_FOUND.toString());
                    return true;
                }

                sender.sendMessage(ChatColor.DARK_GRAY + "+──────────┤ " + ChatColor.GREEN + ChatColor.BOLD + "Info" + ChatColor.DARK_GRAY + "├──────────+");
                sender.sendMessage(" ");
                sender.sendMessage(formatInfo(Lang.ID.toString(), serverTutorial.getId()));
                sender.sendMessage(formatInfo(Lang.INVISIBLE.toString(), (serverTutorial.invisiblePlayer ? ChatColor.DARK_GREEN.toString() : ChatColor.RED.toString()) + serverTutorial.invisiblePlayer + ""));
                sender.sendMessage(formatInfo(Lang.PERMISSION.toString(), (serverTutorial.getNeedsPermission() ? ChatColor.DARK_GREEN.toString() : ChatColor.RED.toString()) + serverTutorial.getNeedsPermission() + ""));
                sender.sendMessage(formatInfo(Lang.TIMES_PLAYED.toString(), String.valueOf(serverTutorial.plays)));
                sender.sendMessage(formatInfo(Lang.AMOUNT_OF_POINTS.toString() , (serverTutorial.points.size()) + ""));
                sender.sendMessage(" ");
                for(int i = 0; i < serverTutorial.points.size(); i++){
                    ServerTutorialPoint point = serverTutorial.points.get(i);
                    Location loc = point.getLoc().clone();
                    //Location
                    sender.sendMessage("   " + ChatColor.GREEN + ChatColor.BOLD.toString() + (i + 1) + ChatColor.GRAY + " - " + ChatColor.YELLOW + loc.getWorld().getName() + " " + loc.getBlockX()  + " " + loc.getBlockY() + " " + loc.getBlockZ());
                    //Time
                    sender.sendMessage("     " + ChatColor.GREEN + Lang.TIME + ": " + ChatColor.YELLOW + point.getTime() + " " + Lang.SECONDS);
                    //Actionbar
                    if(point.getMessage_actionBar() != null && !point.getMessage_actionBar().equalsIgnoreCase("")) sender.sendMessage("     " + ChatColor.GREEN + "Actionbar: " + ChatColor.YELLOW + point.getMessage_actionBar());
                    //Title
                    if(point.getTitleInfo() != null){
                        if(!point.getTitleInfo().title.equalsIgnoreCase("")) sender.sendMessage("     " + ChatColor.GREEN + "Title: " + ChatColor.YELLOW + ChatColor.translateAlternateColorCodes('&', point.getTitleInfo().title));
                        if(!point.getTitleInfo().subtitle.equalsIgnoreCase("")) sender.sendMessage("     " + ChatColor.GREEN + "Sub Title: " + ChatColor.YELLOW + ChatColor.translateAlternateColorCodes('&', point.getTitleInfo().subtitle));
                    }

                    if(point.getMessage_chat() != null){
                        if(point.getMessage_chat().size() > 0){
                            sender.sendMessage("     " + ChatColor.GREEN + Lang.CHAT_MESSAGES + ": ");
                            for(int index = 0; index < point.getMessage_chat().size(); index++){
                                sender.sendMessage("       " + ChatColor.BLUE + (index + 1) + ": " + ChatColor.YELLOW + ChatColor.translateAlternateColorCodes('&', point.getMessage_chat().get(index)));
                            }
                        }
                    }

                    if(point.getCommands() != null){
                        if(point.getCommands().size() > 0){
                            sender.sendMessage("     " + ChatColor.GREEN + "Commands: ");
                            for(int index = 0; index < point.getCommands().size(); index++){
                                sender.sendMessage("       " + ChatColor.BLUE + (index + 1) + ": " + ChatColor.YELLOW + ChatColor.translateAlternateColorCodes('&', point.getCommands().get(index)));
                            }
                        }
                    }

                    sender.sendMessage(" ");
                }
                sender.sendMessage(ChatColor.DARK_GRAY + "+─────────────────────────+");
                return true;
                //endregion
            }

            else if(args[0].equalsIgnoreCase("play")){
                //region play
                if(!sender.hasPermission("servertutorialplus.command.play")){
                    sender.sendMessage(Lang.NO_PERMS.toString());
                    return true;
                }

                if(args.length < 2){
                    sender.sendMessage(Lang.WRONG_COMMAND_FORMAT + "/st play <id>");
                    return true;
                }

                if(!(sender instanceof Player)){
                   sender.sendMessage(Lang.PLAYER_ONLY_COMMAND.toString());
                   return true;
                }

                if(plugin.blockPlayers.contains(((Player) sender).getUniqueId())){
                    sender.sendMessage(Lang.ERROR_WAIT_TO_END.toString());
                    return true;
                }

                ServerTutorial serverTutorial = PluginUtils.getTutorial(plugin, args[1]);
                if(serverTutorial == null){
                    sender.sendMessage(Lang.TUTORIAL_ID_NOT_FOUND.toString());
                    return true;
                }

                Player player = (Player) sender;
                if(args.length != 2){
                    Player target = Bukkit.getPlayer(args[2]);
                    if(target == null){
                        player.sendMessage(Lang.ERROR_PLAYER_OFFLINE.toString());
                        return true;
                    }

                    if(plugin.inTutorial.containsKey(player.getUniqueId())){
                        player.sendMessage(Lang.ERROR_PERSON_IN_TUTORIAL.toString());
                        return true;
                    }

                    if(!plugin.enabled){
                        return true;
                    }

                    TutorialController tutorialController = new TutorialController(plugin, target, serverTutorial);
                    tutorialController.start();
                    return true;
                }

                if(plugin.inTutorial.containsKey(player.getUniqueId())){
                    player.sendMessage(Lang.ERROR_WAIT_TO_END_TUTORIAL.toString());
                    return true;
                }

                if(!plugin.enabled){
                    return true;
                }

                TutorialController tutorialController = new TutorialController(plugin, player, serverTutorial);
                tutorialController.start();
                return true;
                //endregion
            }

            else if(args[0].equalsIgnoreCase("quit")){
                //region quit
                if(!sender.hasPermission("servertutorialplus.command.quit")){
                    sender.sendMessage(Lang.NO_PERMS.toString());
                    return true;
                }

                if(!(sender instanceof Player)){
                    sender.sendMessage(Lang.PLAYER_ONLY_COMMAND.toString());
                    return true;
                }

                Player player = (Player) sender;

                if(plugin.inTutorial.containsKey(player.getUniqueId())){
                    TutorialController tc = plugin.inTutorial.get(player.getUniqueId());
                    tc.cancel(true);
                    player.sendMessage(Lang.COMMAND_SUCCESFULLY_LEFT.toString());
                    return true;
                }

                player.sendMessage(Lang.COMMAND_QUIT_NOTIN.toString());
                return true;
                //endregion
            }

            else if(args[0].equalsIgnoreCase("setblock")){
                //region setblock
                if(!sender.hasPermission("servertutorialplus.command.setblock")){
                    sender.sendMessage(Lang.NO_PERMS.toString());
                    return true;
                }

                if(!(sender instanceof Player)){
                    sender.sendMessage(Lang.PLAYER_ONLY_COMMAND.toString());
                    return true;
                }

                if(args.length < 2){
                    sender.sendMessage(Lang.WRONG_COMMAND_FORMAT + "/st setblock <id>");
                    return true;
                }

                Player player = (Player) sender;
                Block block = player.getTargetBlock(new HashSet<Material>(Arrays.asList(Material.AIR)), 20);
                if(block.getType().equals(Material.AIR)){
                    sender.sendMessage(Lang.COMMAND_SETBLOCK_FAIL.toString());
                    return true;
                }

                plugin.tutorialSigns.add(new TutorialSign(block, args[1]));
                player.sendMessage(Lang.COMMAND_SETBLOCK_SUCCES.toString().replace("%id%", args[1]).replace("%type%", block.getType().toString()));
                return true;
                //endregion
            }

            else if(args[0].equalsIgnoreCase("edit")){
                //region edit tutorial
                if(!sender.hasPermission("servertutorialplus.command.edit")){
                    sender.sendMessage(Lang.NO_PERMS.toString());
                    return true;
                }

                if(args.length < 4){
                    sender.sendMessage(Lang.WRONG_COMMAND_FORMAT.toString() + "/st edit <server tutorial ID> <invisible/rewards/permission>");
                    return true;
                }

                ServerTutorial serverTutorial = PluginUtils.getTutorial(plugin, args[1]);

                if(serverTutorial == null){
                    sender.sendMessage(Lang.TUTORIAL_ID_NOT_FOUND.toString());
                    return true;
                }

                switch (args[2]) {
                    case "invisible":
                        try {
                            serverTutorial.invisiblePlayer = Boolean.parseBoolean(args[3]);
                            sender.sendMessage(Lang.COMMAND_SETTING_SET.toString().replace("%setting%", args[2]).replace("%id%", serverTutorial.getId()));
                        } catch (Exception ex) {
                            sender.sendMessage(Lang.WRONG_COMMAND_FORMAT + "/st edit <server tutorial ID> <invisible/rewards> TRUE/FALSE");
                            return true;
                        }
                        break;
                    case "permission":
                        try {
                            serverTutorial.setNeedsPermission(Boolean.parseBoolean(args[3]));
                            sender.sendMessage(Lang.COMMAND_SETTING_SET.toString().replace("%setting%", args[2]).replace("%id%", serverTutorial.getId()));
                        } catch (Exception ex) {
                            sender.sendMessage(Lang.WRONG_COMMAND_FORMAT + "/st edit <server tutorial ID> <invisible/rewards/permission> TRUE/FALSE");
                            return true;
                        }
                        break;

                    case "rewards":
                        if(args.length < 3){
                            sender.sendMessage(Lang.WRONG_COMMAND_FORMAT + "/st edit <server tutorial ID> rewards add/remove/list/clear");
                            return false;
                        }

                        switch (args[3]) {
                            case "clear":
                                serverTutorial.getRewards().clear();
                                sender.sendMessage(Lang.COMMAND_SETTING_REWARDS_CLEARED.toString());
                                return true;

                            case "add":
                                if (args.length < 5) {
                                    sender.sendMessage(Lang.ERROR_ATLEAST_ONE_WORD.toString());
                                    return false;
                                }

                                String message = StringUtils.join(args, ' ', 4, args.length);
                                serverTutorial.getRewards().add(message);

                                sender.sendMessage(Lang.COMMAND_SETTING_REWARDS_ADDED.toString());
                                return true;

                            case "remove":
                                if (args.length < 5) {
                                    sender.sendMessage(Lang.ERROR_NO_INDEX.toString());
                                    return false;
                                }

                                try {
                                    serverTutorial.getRewards().remove(Integer.parseInt(args[4]) - 1);
                                } catch (NumberFormatException ex) {
                                    sender.sendMessage(Lang.ERROR_NO_INDEX.toString());
                                    return false;
                                } catch (IndexOutOfBoundsException ex) {
                                    sender.sendMessage(Lang.ERROR_NOTEXISTING_INDEX.toString());
                                    return false;
                                }
                                sender.sendMessage(Lang.COMMAND_SETTING_REWARDS_REMOVED.toString());
                                return true;

                            case "list":
                                sender.sendMessage(ChatColor.GRAY + "[ " + ChatColor.GOLD + Lang.REWARD_COMMANDS);
                                for (int i = 0; i < serverTutorial.getRewards().size(); i++) {
                                    sender.sendMessage(ChatColor.GRAY + "[ " + ChatColor.GREEN + (i + 1) + ChatColor.YELLOW + " " + ChatColor.translateAlternateColorCodes('&', serverTutorial.getRewards().get(i))
                                    );
                                }
                                return false;

                            default:
                                sender.sendMessage(Lang.WRONG_COMMAND_FORMAT.toString() + "/st edit <server tutorial ID> rewards add/remove/list/clear");
                                return false;
                        }



                    default:
                        sender.sendMessage(Lang.UNKOWN_ARGUMENT + "<invisible/rewards/permission>");
                        sender.sendMessage(Lang.TIP_EDITPOINT.toString());
                        break;
                }
                return true;
                //endregion
            }

            else if(args[0].equalsIgnoreCase("editpoint")){
                //region edit point
                if(!sender.hasPermission("servertutorialplus.command.edit")){
                    sender.sendMessage(Lang.NO_PERMS.toString());
                    return true;
                }

                if(args.length < 3){
                    sender.sendMessage(Lang.WRONG_COMMAND_FORMAT + "/st editpoint <server tutorial ID> <point index> <args>");
                    return true;
                }

                int index;
                try{
                    index = Integer.parseInt(args[2]);
                } catch (Exception e){
                    sender.sendMessage(Lang.WRONG_COMMAND_FORMAT + "/st editpoint <server tutorial ID> <point index> <args>");
                    return true;
                }

                ServerTutorial serverTutorial = PluginUtils.getTutorial(plugin, args[1]);

                if(serverTutorial == null){
                    sender.sendMessage(Lang.TUTORIAL_ID_NOT_FOUND.toString());
                    return true;
                }

                if(index - 1 >= serverTutorial.points.size()){
                    sender.sendMessage(Lang.ERROR_INVALID_POINT.toString());
                    return true;
                }

                if(index < 1){
                    sender.sendMessage(Lang.ERROR_INVALID_INDEX.toString());
                    return true;
                }

                ServerTutorialPoint tutorialPoint = serverTutorial.points.get(index - 1);

                if(args.length == 3){
                    sender.sendMessage(Lang.UNKOWN_ARGUMENT.toString() + ChatColor.GRAY + tutorialPoint.getArgsString());
                    return true;
                }

                if(args[3].equalsIgnoreCase("switch")){
                    if(args.length < 5){
                        sender.sendMessage(Lang.WRONG_COMMAND_FORMAT + "/steditpoint <id> <p> switch <to>");
                        return true;
                    }

                    int to;
                    try{
                        to = Integer.parseInt(args[4]);
                    } catch (Exception ex){
                        sender.sendMessage(Lang.WRONG_COMMAND_FORMAT + "/st editpoint <id> <p> switch <to>");
                        return true;
                    }

                    ServerTutorialPoint point = serverTutorial.points.get(index - 1);
                    serverTutorial.points.set(index - 1, serverTutorial.points.get(to - 1));
                    serverTutorial.points.set(to - 1, point);
                    sender.sendMessage(Lang.COMMAND_SWITCH_SUCCESSFUL.toString().replace("%1%", (index - 1) + "").replace("%2&", to + ""));
                    return true;
                }

                if(args[3].equalsIgnoreCase("infront")){
                    if(args.length < 5){
                        sender.sendMessage(Lang.WRONG_COMMAND_FORMAT + "/st editpoint <id> <p> infront <p>");
                        return true;
                    }

                    int to;
                    try{
                        to = Integer.parseInt(args[4]);
                    } catch (Exception ex){
                        sender.sendMessage(Lang.WRONG_COMMAND_FORMAT + "/st editpoint <id> <p> infront <p>");
                        return true;
                    }

                    ServerTutorialPoint point = serverTutorial.points.get(index - 1);
                    serverTutorial.points.remove(index - 1);
                    serverTutorial.points.add(to - 1, point);
                    sender.sendMessage(Lang.COMMAND_MOVE_INFRONT_SUCCESFULL.toString().replace("%1%", (index - 1) + "").replace("%2&", to + ""));
                    return true;
                }


                PointEditor pointEditor = PointEditor.getPointeditor(tutorialPoint);
                pointEditor.execute(serverTutorial, tutorialPoint, sender, args);
                return true;
                //endregion
            }

            else if(args[0].equalsIgnoreCase("editall")){
                //region editall
                if(!sender.hasPermission("servertutorialplus.command.edit")){
                    sender.sendMessage(Lang.NO_PERMS.toString());
                    return true;
                }

                if(args.length < 2){
                    sender.sendMessage(Lang.WRONG_COMMAND_FORMAT + "/st editall <server tutorial ID> <args>");
                    return true;
                }

                ServerTutorial serverTutorial = PluginUtils.getTutorial(plugin, args[1]);

                if(serverTutorial == null){
                    sender.sendMessage(Lang.TUTORIAL_ID_NOT_FOUND.toString());
                    return true;
                }

                if(args.length == 2){
                    sender.sendMessage(Lang.COMMAND_EDITALL_CANBE.toString().replace("%args%", ServerTutorialPoint.getArgsString(new TimedPoint(null, null).getArgs())));
                    return true;
                }

                String[] arguments = new String[args.length + 1];
                for(int i = 0; i < args.length; i++){
                    arguments[1 + i] = args[i];
                }

                for(ServerTutorialPoint point : serverTutorial.points){
                    PointEditor pointEditor = PointEditor.getPointeditor(point);
                    if(!pointEditor.execute(serverTutorial, point, sender, arguments)){
                        sender.sendMessage(Lang.ERROR_EDITALL_FAIL.toString());
                        return true;
                    }
                }
                sender.sendMessage(Lang.COMMAND_EDITALL_SUCCES.toString());
                return true;
                //endregion
            }

            else if(args[0].equalsIgnoreCase("reload")){
                //region reload plugin
                if(!sender.hasPermission("servertutorialplus.command.reload")){
                    sender.sendMessage(Lang.NO_PERMS.toString());
                    return true;
                }

                plugin.enabled = false;

                for(TutorialController tc : plugin.inTutorial.values()){
                    tc.cancel(true);
                    tc.getPlayer().sendMessage(Lang.RELOAD_STOPTUTORIAL.toString());
                }
                plugin.inTutorial.clear();
                plugin.tutorialSigns.clear();
                plugin.serverTutorials.clear();
                plugin.lockedPlayers.clear();

                plugin.reloadConfig();
                plugin.tutorialSaves = new Config(plugin, "tutorialsaves");
                plugin.signSaves = new Config(plugin, "blockSaves");

                plugin.loadTutorials();
                plugin.loadSigns();

                sender.sendMessage(Lang.RELOAD_SUCCES.toString());
                plugin.enabled = true;
                return true;
                //endregion
            }

            else if(args[0].equalsIgnoreCase("help")){
                //region Help
                if(!sender.hasPermission("servertutorialplus.command.help")){
                    sender.sendMessage(Lang.NO_PERMS.toString());
                    return true;
                }
                sendHelp(sender);
                return true;
                //endregion
            }

            else if(args[0].equalsIgnoreCase("save")){
                //region save
                if(!sender.hasPermission("servertutorialplus.command.save")){
                    sender.sendMessage(Lang.NO_PERMS.toString());
                    return true;
                }
                plugin.saveSigns();
                plugin.saveTutorials();

                sender.sendMessage(ChatColor.GREEN + "Successfully saved all the tutorials and blocks.");
                return true;
                //endregion
            }

            else if(args[0].equalsIgnoreCase("playpoint")){
                //region Playpoint
                if(!sender.hasPermission("servertutorialplus.command.playpoint")){
                    sender.sendMessage(Lang.NO_PERMS.toString());
                    return true;
                }

                if(args.length < 3){
                    sender.sendMessage(Lang.WRONG_COMMAND_FORMAT + "/st playpoint <id> <point>");
                    return true;
                }

                if(!(sender instanceof Player)){
                    sender.sendMessage(Lang.PLAYER_ONLY_COMMAND.toString());
                    return true;
                }

                if(plugin.blockPlayers.contains(((Player) sender).getUniqueId())){
                    sender.sendMessage(Lang.ERROR_WAIT_TO_END.toString());
                    return true;
                }

                ServerTutorial serverTutorial = PluginUtils.getTutorial(plugin, args[1]);
                if(serverTutorial == null){
                    sender.sendMessage(Lang.TUTORIAL_ID_NOT_FOUND.toString());
                    return true;
                }

                int index;
                try{
                    index = Integer.parseInt(args[2]);
                } catch (Exception e){
                    sender.sendMessage(Lang.ERROR_INVALID_INDEX.toString());
                    return true;
                }

                if(index > serverTutorial.points.size() || index <= 0){
                    sender.sendMessage(Lang.ERROR_INVALID_POINT.toString());
                    return true;
                }

                Player player = (Player) sender;

                if(plugin.inTutorial.containsKey(player.getUniqueId())){
                    player.sendMessage(Lang.ERROR_WAIT_TO_END_TUTORIAL.toString());
                    return true;
                }

                plugin.blockPlayers.add(player.getUniqueId());
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        plugin.blockPlayers.remove(player.getUniqueId());
                    }
                }.runTaskLater(plugin, (long) serverTutorial.points.get(index - 1).getTime() * 20 + 6);

                OldValuesPlayer oldValuesPlayer = new OldValuesPlayer(player);

                IPointCallBack callBack = () -> {
                    if(plugin.lockedPlayers.contains(player.getUniqueId())){
                        plugin.lockedPlayers.remove(player.getUniqueId());
                    }
                    if(plugin.lockedViews.contains(player.getUniqueId())){
                        plugin.lockedViews.remove(player.getUniqueId());
                    }
                    player.setFlySpeed(oldValuesPlayer.getOriginal_flySpeed());
                    player.setWalkSpeed(oldValuesPlayer.getOriginal_walkSpeed());
                    player.setFlying(oldValuesPlayer.getFlying());
                };
                IPlayPoint handler = serverTutorial.points.get(index - 1).createPlay(player, oldValuesPlayer, callBack);
                handler.start();
                return true;
                //endregion
            }

            else if(args[0].equalsIgnoreCase("npc")){
                //region NPC
                //region NPC checks
                if(!sender.hasPermission("servertutorialplus.command.npc")){
                    sender.sendMessage(Lang.NO_PERMS.toString());
                    return true;
                }

                if(args.length <= 1){
                    sender.sendMessage(Lang.COMMAND_ARGUMENTS_AVAILABLE.toString().replace("%args%", "add, remove, list, bind, text, height"));
                    return true;
                }
                //endregion

                if(args[1].equalsIgnoreCase("add")){
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

                    if(args.length < 5){
                        sender.sendMessage(Lang.WRONG_COMMAND_FORMAT + "/st npc add <NPC id> <ServerTutorial> <livingEntity>");
                        return true;
                    }

                    Player player = (Player) sender;

                    if(PluginUtils.getNPC(plugin, args[2]) != null){
                         sender.sendMessage(Lang.NPC_ID_EXIST.toString());
                         return true;
                    }

                    EntityType et;
                    try{
                        et = EntityType.valueOf(args[4]);
                    } catch (Exception e){
                        sender.sendMessage(Lang.NPC_TESTED_MOBS + PluginUtils.allMobs());
                        sender.sendMessage(Lang.NPC_WRONG_TYPE.toString().replace("%type%", args[4]));
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

                                NPCInfo info = new NPCInfo(plugin, args[2], npc.getUniqueId(), new UUID[] {armorStand_1.getUniqueId(), armorStand_2.getUniqueId()}, args[3]);
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

                                NPCInfo info = new NPCInfo(plugin, args[2], npc.getUniqueId(), new UUID[] {armorStand_1.getUniqueId(), armorStand_2.getUniqueId()}, args[3]);
                                plugin.clickableNPCs.put(npc.getUniqueId(), info);

                                npc.teleport(npc.getLocation());
                                sender.sendMessage(Lang.NPC_CREATION_SUCCESS.toString().replace("%id%", info.getId()).replace("%tutorial%", info.getServerTutorialID()));
                            }
                        }.runTaskLater(plugin, 1);

                        return true;
                    }
                    //endregion
                }

                else if(args[1].equalsIgnoreCase("bind"))
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

                    if(args.length < 4){
                        sender.sendMessage(Lang.WRONG_COMMAND_FORMAT + "/st npc bind <NPC id> <ServerTutorial>");
                        return true;
                    }

                    ServerTutorial tutorial;
                    tutorial = PluginUtils.getTutorial(plugin, args[3]);
                    if(tutorial == null){
                        sender.sendMessage(Lang.TUTORIAL_ID_NOT_FOUND.toString());
                        return true;
                    }

                    TutorialEntitySelector selector = new TutorialEntitySelector(player, tutorial, args[2]);
                    plugin.selectingNpc.put(player.getUniqueId(), selector);
                    player.sendMessage(Lang.NPC_SELECTION_MESSAGE.toString());
                    return true;
                    //endregion
                }

                else if(args[1].equalsIgnoreCase("remove")){
                    //region remove
                    if(args.length <= 2){
                        sender.sendMessage(Lang.WRONG_COMMAND_FORMAT + "Wrong usage. Use the command like this: /st npc remove <NPC id>");
                        return true;
                    }

                    NPCInfo info = PluginUtils.getNPC(plugin, args[2]);
                    if(info == null){
                        sender.sendMessage(Lang.NPC_ID_NOT_EXISTING.toString().replace("%id%", args[2]));
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

                else if(args[1].equalsIgnoreCase("info")){
                    //region List
                    if(args.length <= 2) {
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
                        sender.sendMessage("  " + Lang.NPC_INFO_MORE_INFO);
                        sender.sendMessage(ChatColor.DARK_GRAY + "+─────────────────────────+");
                        return true;
                    }

                    NPCInfo info = PluginUtils.getNPC(plugin, args[2]);
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

                else if(args[1].equalsIgnoreCase("text")){
                    //region Text
                    if(args.length < 5){
                        sender.sendMessage(Lang.WRONG_COMMAND_FORMAT + "/st npc text <NPC id> <top/bot> <text>");
                        return true;
                    }

                    NPCInfo info = PluginUtils.getNPC(plugin, args[2]);
                    if(info == null){
                        sender.sendMessage(Lang.NPC_ID_NOT_EXISTING.toString());
                        return true;
                    }

                    Entity stand;
                    if(args[3].equalsIgnoreCase("bot")){
                        stand = SpigotUtils.getEntity(info.getArmorstandIDs()[0]);
                    } else if(args[3].equalsIgnoreCase("top")){
                        stand = SpigotUtils.getEntity(info.getArmorstandIDs()[1]);
                    } else {
                        sender.sendMessage(Lang.WRONG_COMMAND_FORMAT + "/st npc text <NPC id> <top/bot> <text>");
                        return true;
                    }

                    String message = "";
                    for(int i = 4; i < args.length; i++){
                        message = message + args[i] + (args.length - 1 != i ? " " : "");
                    }
                    message = ChatColor.translateAlternateColorCodes('&', message);
                    stand.setCustomName(message);
                    sender.sendMessage(Lang.NPC_TEXT_CHANGE.toString());
                    return true;
                    //endregion
                }

                else if(args[1].equalsIgnoreCase("height")){
                    //region Height
                    if(args.length < 4){
                        sender.sendMessage(Lang.WRONG_COMMAND_FORMAT + "/st npc height <NPC id> <value like 0.25>");
                        return true;
                    }

                    NPCInfo info = PluginUtils.getNPC(plugin, args[2]);
                    if(info == null){
                        sender.sendMessage(Lang.NPC_ID_NOT_EXISTING.toString());
                        return true;
                    }

                    double height = 0;
                    try{
                        height = Double.parseDouble(args[3]);
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

            else if(args[0].equalsIgnoreCase("gui")){
                //region gui
                if(!sender.hasPermission("servertutorialplus.command.gui")){
                    sender.sendMessage(Lang.NO_PERMS.toString());
                    return true;
                }

                if(!(sender instanceof Player)){
                    sender.sendMessage(Lang.PLAYER_ONLY_COMMAND.toString());
                    return true;
                }

                Player player = (Player) sender;

                if(args.length <= 1){
                    GuiInventories.openMainMenu(plugin, player);
                    return true;
                }
                //endregiong gui;
            }

            else if(args[0].equalsIgnoreCase("player")){
                if(!sender.hasPermission("servertutorialplus.command.player")){
                    sender.sendMessage(Lang.NO_PERMS.toString());
                    return true;
                }

                if(args.length <= 1){
                    sender.sendMessage(ChatColor.YELLOW + "Please define a player to check. /st player <name>");
                    return true;
                }

                OfflinePlayer target = null;

                target = plugin.getServer().getPlayer(args[1]);
                if(target == null){
                    target = plugin.getServer().getOfflinePlayer(args[1]);
                }

                if(target == null){
                    sender.sendMessage(Lang.COMMAND_LOOKUP_UUID_ERROR.toString());
                    return true;
                }

                if(!target.hasPlayedBefore()){
                    sender.sendMessage(Lang.COMMAND_LOOKUP_NEVER_PLAYED.toString());
                    return true;
                }

                if(args.length > 3){
                    new BukkitRunnable(){

                        private OfflinePlayer target;

                        public BukkitRunnable setTarget(OfflinePlayer target){
                            this.target = target;
                            return this;
                        }

                        @Override
                        public void run() {
                            String id = args[2];
                            PlayerLookUp.sendLookupAsync(plugin, sender, target);
                            if(args[3].equalsIgnoreCase("set")){
                                if(!plugin.getDataSource().hasPlayedTutorial(target.getUniqueId(), id)){
                                    plugin.getDataSource().addPlayedTutorial(target.getUniqueId(), id);
                                    sender.sendMessage(Lang.COMMAND_LOOKUP_SET.toString());
                                } else {
                                    sender.sendMessage(Lang.COMMAND_LOOKUP_SET_ERROR.toString());
                                }
                            } else if(args[3].equalsIgnoreCase("unset")){
                                if(plugin.getDataSource().hasPlayedTutorial(target.getUniqueId(), id)){
                                    plugin.getDataSource().removePlayedTutorial(target.getUniqueId(), id);
                                    sender.sendMessage(Lang.COMMAND_LOOKUP_UNSET.toString());
                                } else {
                                    sender.sendMessage(Lang.COMMAND_LOOKUP_UNSET_ERROR.toString());
                                }
                            } else{
                                sender.sendMessage(Lang.UNKOWN_ARGUMENT.toString());
                            }

                        }
                    }.setTarget(target).runTaskAsynchronously(plugin);
                    return true;
                }

                PlayerLookUp.sendLookupAsync(plugin, sender, target);
                return true;
            }

            else if(args[0].equalsIgnoreCase("cancel")){
                if(!(sender instanceof Player)) return true;
                Player player = (Player) sender;

                if(!plugin.getClickManager().hasClickaction(player.getUniqueId())){
                    player.sendMessage(Lang.NOTHING_TO_CANCEL.toString());
                } else{
                    plugin.getClickManager().removeClickaction(player.getUniqueId());
                    player.sendMessage(Lang.ACTION_CANCELLED.toString());
                }
                return true;
            }

            //region debug
            /*
            else if(args[0].equalsIgnoreCase("debug")){
                //region debug
                if(!sender.hasPermission("servertutorialplus.command.debug")){
                    sender.sendMessage(Lang.NO_PERMS.toString());
                    return true;
                }

                sender.sendMessage(ChatColor.DARK_RED + "----------------------------------------");
                sender.sendMessage(ChatColor.GRAY + "    Debug information");
                sender.sendMessage(ChatColor.DARK_RED + "----------------------------------------");
                sender.sendMessage(ChatColor.RED + "inTutorial: ");
                for(TutorialController tc : plugin.inTutorial.values()){
                    sender.sendMessage(" --> Unknown");
                }
                return true;
                //endregion
            }*/
            //endregion

            sender.sendMessage(Lang.INVALID_ARGS.toString());
            return true;
        }
        if(sender.hasPermission("servertutorialplus.command.help")){
            sendHelp(sender);
        } else{
            sender.sendMessage(Lang.NO_PERMS.toString());
        }
        return true;
    }


    private String formatCommand(String command, String description){
        return ChatColor.GREEN + "  " + command + ChatColor.DARK_GRAY + " - " + ChatColor.YELLOW + description;
    }

    private String formatInfo(String command, String description){
        return ChatColor.GREEN + "  " + command + ChatColor.DARK_GRAY + " : " + ChatColor.YELLOW + description;
    }

    private void sendHelp(CommandSender sender){

        sender.sendMessage(ChatColor.DARK_GRAY + "+──────┤ " + ChatColor.GREEN + ChatColor.BOLD + "Server Tutorial" + ChatColor.GOLD + " + " + ChatColor.DARK_GRAY + "├──────+");
        sender.sendMessage(" ");
        if(sender.hasPermission("servertutorialplus.command.help")) sender.sendMessage(formatCommand("/st help", Lang.HELP_HELP.toString()));
        if(sender.hasPermission("servertutorialplus.command.create")) sender.sendMessage(formatCommand("/st create",Lang.HELP_CREATE.toString()));
        if(sender.hasPermission("servertutorialplus.command.remove")) sender.sendMessage(formatCommand("/st remove", Lang.HELP_REMOVE.toString()));
        if(sender.hasPermission("servertutorialplus.command.addpoint")) sender.sendMessage(formatCommand("/st addpoint", Lang.HELP_ADDPOINT.toString()));
        if(sender.hasPermission("servertutorialplus.command.removepoint")) sender.sendMessage(formatCommand("/st removepoint", Lang.HELP_REMOVEPOINT.toString()));
        if(sender.hasPermission("servertutorialplus.command.edit")) sender.sendMessage(formatCommand("/st edit", Lang.HELP_EDIT.toString()));
        if(sender.hasPermission("servertutorialplus.command.edit")) sender.sendMessage(formatCommand("/st editpoint", Lang.HELP_EDITPOINT.toString()));
        if(sender.hasPermission("servertutorialplus.command.edit")) sender.sendMessage(formatCommand("/st editall", Lang.HELP_EDITALL.toString()));
        if(sender.hasPermission("servertutorialplus.command.info")) sender.sendMessage(formatCommand("/st info", Lang.HELP_INFO.toString()));
        if(sender.hasPermission("servertutorialplus.command.player")) sender.sendMessage(formatCommand("/st player", Lang.HELP_PLAYER.toString()));
        if(sender.hasPermission("servertutorialplus.command.gui")) sender.sendMessage(formatCommand("/st gui", Lang.HELP_GUI.toString()));
        if(sender.hasPermission("servertutorialplus.command.play")) sender.sendMessage(formatCommand("/st play", Lang.HELP_PLAY.toString()));
        if(sender.hasPermission("servertutorialplus.command.quit")) sender.sendMessage(formatCommand("/st quit", Lang.HELP_QUIT.toString()));
        if(sender.hasPermission("servertutorialplus.command.playpoint")) sender.sendMessage(formatCommand("/st playpoint", Lang.HELP_PLAYPOINT.toString()));
        if(sender.hasPermission("servertutorialplus.command.setblock")) sender.sendMessage(formatCommand("/st setblock", Lang.HELP_SETBLOCK.toString()));
        if(sender.hasPermission("servertutorialplus.command.npc")) sender.sendMessage(formatCommand("/st npc", Lang.HELP_NPC.toString()));
        if(sender.hasPermission("servertutorialplus.command.reload")) sender.sendMessage(formatCommand("/st reload", Lang.HELP_RELOAD.toString()));
        if(sender.hasPermission("servertutorialplus.command.save")) sender.sendMessage(formatCommand("/st save", Lang.HELP_SAVE.toString()));
        sender.sendMessage(" ");
        sender.sendMessage(ChatColor.DARK_GRAY + "+──────────────────────────+");
    }


}
