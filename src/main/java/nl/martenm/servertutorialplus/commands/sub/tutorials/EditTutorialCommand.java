package nl.martenm.servertutorialplus.commands.sub.tutorials;

import net.md_5.bungee.api.ChatColor;
import nl.martenm.servertutorialplus.ServerTutorialPlus;
import nl.martenm.servertutorialplus.helpers.PluginUtils;
import nl.martenm.servertutorialplus.language.Lang;
import nl.martenm.servertutorialplus.objects.ServerTutorial;
import nl.martenm.simplecommands.SimpleCommand;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class EditTutorialCommand extends SimpleCommand {

    public EditTutorialCommand() {
        super("edit", Lang.HELP_EDIT.toString(), "+edit", false);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        // TODO: Convert this into a parsed command
        ServerTutorialPlus plugin = ServerTutorialPlus.getInstance();

        if (args.length < 3) {
            sender.sendMessage(Lang.WRONG_COMMAND_FORMAT.toString() + "/st edit <server tutorial ID> <invisible/rewards/permission/blockcommands/commands/chatblock>");
            return true;
        }

        ServerTutorial serverTutorial = PluginUtils.getTutorial(plugin, args[0]);

        if (serverTutorial == null) {
            sender.sendMessage(Lang.TUTORIAL_ID_NOT_FOUND.toString());
            return true;
        }

        switch (args[1]) {
            case "invisible":
                try {
                    serverTutorial.invisiblePlayer = Boolean.parseBoolean(args[2]);
                    sender.sendMessage(Lang.COMMAND_SETTING_SET.toString().replace("%setting%", args[1]).replace("%id%", serverTutorial.getId()));
                } catch (Exception ex) {
                    sender.sendMessage(Lang.WRONG_COMMAND_FORMAT + "/st edit <server tutorial ID> invisible TRUE/FALSE");
                    return true;
                }
                break;
            case "permission":
                try {
                    serverTutorial.setNeedsPermission(Boolean.parseBoolean(args[2]));
                    sender.sendMessage(Lang.COMMAND_SETTING_SET.toString().replace("%setting%", args[1]).replace("%id%", serverTutorial.getId()));
                } catch (Exception ex) {
                    sender.sendMessage(Lang.WRONG_COMMAND_FORMAT + "/st edit <server tutorial ID> permission TRUE/FALSE");
                    return true;
                }
                break;

            case "chatblock":
                try {
                    serverTutorial.setChatBlocked(Boolean.parseBoolean(args[2]));
                    sender.sendMessage(Lang.COMMAND_SETTING_SET.toString().replace("%setting%", args[1]).replace("%id%", serverTutorial.getId()));
                } catch (Exception ex) {
                    sender.sendMessage(Lang.WRONG_COMMAND_FORMAT + "/st edit <server tutorial ID> chatblock TRUE/FALSE");
                    return true;
                }
                break;

            case "blockcommands":
                try {
                    serverTutorial.setBlocksCommands(Boolean.parseBoolean(args[2]));
                    sender.sendMessage(Lang.COMMAND_SETTING_SET.toString().replace("%setting%", args[1]).replace("%id%", serverTutorial.getId()));
                } catch (Exception ex) {
                    sender.sendMessage(Lang.WRONG_COMMAND_FORMAT + "/st edit <server tutorial ID> blockcommands TRUE/FALSE");
                    return true;
                }
                break;

            case "rewards":
                if (args.length < 2) {
                    sender.sendMessage(Lang.WRONG_COMMAND_FORMAT + "/st edit <server tutorial ID> rewards add/remove/list/clear");
                    return false;
                }

                switch (args[2]) {
                    case "clear":
                        serverTutorial.getRewards().clear();
                        sender.sendMessage(Lang.COMMAND_SETTING_REWARDS_CLEARED.toString());
                        return true;

                    case "add":
                        if (args.length < 4) {
                            sender.sendMessage(Lang.ERROR_ATLEAST_ONE_WORD.toString());
                            return false;
                        }

                        String message = StringUtils.join(args, ' ', 3, args.length);
                        serverTutorial.getRewards().add(message);

                        sender.sendMessage(Lang.COMMAND_SETTING_REWARDS_ADDED.toString());
                        return true;

                    case "remove":
                        if (args.length < 4) {
                            sender.sendMessage(Lang.ERROR_NO_INDEX.toString());
                            return false;
                        }

                        try {
                            serverTutorial.getRewards().remove(Integer.parseInt(args[3]) - 1);
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

            case "commands":
                if (args.length < 2) {
                    sender.sendMessage(Lang.WRONG_COMMAND_FORMAT + "/st edit <server tutorial ID> commands add/remove/list/clear");
                    return false;
                }

                switch (args[2]) {
                    case "clear":
                        serverTutorial.getCommandWhiteList().clear();
                        sender.sendMessage(Lang.COMMAND_SETTING_COMMANDS_CLEARED.toString());
                        return true;

                    case "add":
                        if (args.length < 4) {
                            sender.sendMessage(Lang.ERROR_ATLEAST_ONE_WORD.toString());
                            return false;
                        }

                        String message = StringUtils.join(args, ' ', 3, args.length);
                        serverTutorial.getCommandWhiteList().add(message);

                        sender.sendMessage(Lang.COMMAND_SETTING_COMMANDS_ADDED.toString());
                        return true;

                    case "remove":
                        if (args.length < 4) {
                            sender.sendMessage(Lang.ERROR_NO_INDEX.toString());
                            return false;
                        }

                        try {
                            serverTutorial.getCommandWhiteList().remove(Integer.parseInt(args[3]) - 1);
                        } catch (NumberFormatException ex) {
                            sender.sendMessage(Lang.ERROR_NO_INDEX.toString());
                            return false;
                        } catch (IndexOutOfBoundsException ex) {
                            sender.sendMessage(Lang.ERROR_NOTEXISTING_INDEX.toString());
                            return false;
                        }
                        sender.sendMessage(Lang.COMMAND_SETTING_COMMANDS_REMOVED.toString());
                        return true;

                    case "list":
                        sender.sendMessage(ChatColor.GRAY + "[ " + ChatColor.GOLD + Lang.WHITELISTED_COMMANDS);
                        for (int i = 0; i < serverTutorial.getRewards().size(); i++) {
                            sender.sendMessage(ChatColor.GRAY + "[ " + ChatColor.GREEN + (i + 1) + ChatColor.YELLOW + " " + ChatColor.translateAlternateColorCodes('&', serverTutorial.getRewards().get(i))
                            );
                        }
                        return false;

                    default:
                        sender.sendMessage(Lang.WRONG_COMMAND_FORMAT.toString() + "/st edit <server tutorial ID> commands add/remove/list/clear");
                        return false;
                }


            default:
                sender.sendMessage(Lang.UNKOWN_ARGUMENT + "<invisible/rewards/permission/commands/chatblock>");
                sender.sendMessage(Lang.TIP_EDITPOINT.toString());
                break;
        }
        return true;
    }
}
