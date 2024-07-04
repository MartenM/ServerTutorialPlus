package nl.martenm.servertutorialplus.language;

import nl.martenm.servertutorialplus.helpers.Config;
import org.bukkit.ChatColor;

/**
 * Language file manger by Gomeow
 * https://bukkit.org/threads/language-files.149837/
 */
public enum Lang {

    //Basic command messages
    INVALID_ARGS("invalid-args", "&cInvalid args! Use /st help for help."),
    NO_PERMS("no-permissions", "&cYou don't have permission for that!"),
    PLAYER_ONLY_COMMAND("player-only-command", "&cThis is player only command!"),

    //GENERIC
    YES("yes", "&a&lYES"),
    NO("no", "&c&lNO"),
    ADD("add", "&a&lAdd"),
    CLEAR("clear", "clear"),
    COMPLETED("completed", "completed"),
    UNCOMPLETED("uncompleted", "uncompleted"),

    TIMES_PLAYED("times-played", "Times played"),
    AMOUNT_OF_POINTS("amount-of-points", "Amount of points"),
    PERMISSION("permission", "Permission"),
    ID("id", "ID"),
    INVISIBLE("invisible", "Invisible"),
    TIME("time", "Time"),
    SECONDS("seconds", "seconds(s)"),
    CHAT_MESSAGES("chat-messages", "Chat messages"),
    REWARD_COMMANDS("reward-commands", "Reward Commands"),
    NPC_TYPE("npc-type", "NPC type"),
    SERVERTUTORIAL_ID("servertutorial-id", "Server tutorial ID"),
    BLOCKS_COMMANDS("blocks-commands", "Blocks commands"),
    WHITELISTED_COMMANDS("white-listed-commands", "Whitelisted commands"),
    CHAT_BLOCKED("chat-blocked", "Chat Blocked"),
    NONE("none", "None"),

    ACTION_CANCELLED("action-cancelled", "&eAction has been cancelled"),
    NOTHING_TO_CANCEL("nothhing-to-cancel", "&cThere is nothing to cancel!"),

    // Player LOOKUP
    LOOKUP("player-lookup", "&a&lPlayer Lookup"),
    LOOKUP_SET_UNFINISHED("lookup-set-unfinished", "&c[Set unfinished]"),
    LOOKUP_SET_FINISHED("lookup-set-finished", "&e[Set finished]"),
    LOOKUP_SET_UNFINIHSHED_MESSAGE("lookup-set-unfinished-message", "&cClick to set the tutorial to &cunfinished"),
    LOOKUP_SET_FINIHSHED_MESSAGE("lookup-set-finished-message", "&cClick to set the tutorial to &afinished"),

    // COMMANDS
    TUTORIAL_CREATED("tutorial-created", "&aSuccessfully created the server tutorial with ID:&e %id%"),
    TUTORIAL_REMOVED("tutorial-removed", "&aSuccessfully removed the server tutorial with ID:&e %id%"),
    TUTORIAL_ID_NOT_FOUND("tutorial-id-not-found", "&cCould not find an server tutorial with that ID."),

    COMMAND_ADDPOINT_COMMAND_HINT("command-apddpoint-commandhint", "&7[&eNotice&7] The user needs to execute the command &e/st next &7to proceed this point."),

    COMMAND_HASTOBE_NUMBER("command-hastobe-number", "&cThe index has to be an number"),
    COMMAND_INVALID_INDEX("command-invalid-index", "&cThat's not a valid index."),
    COMMAND_SUCCESFULLY_LEFT("command-succesfully-left", "&aSuccessfully left the tutorial."),

    COMMAND_QUIT_NOTIN("command-quit-notin", "&4You are not in a tutorial."),

    COMMAND_SETBLOCK_FAIL("command-setblock-fail", "&cFailed to create the block. Don't stand too far away from the block you want to set!"),
    COMMAND_SETBLOCK_SUCCES("command-setblock-succes", "&aSuccessfully created a new tutorial block. Block Type: &e%type%&a Server tutorial: &e%id%"),

    COMMAND_SETTING_SET("command-setting-set", "&aSuccessfully set the setting &e%setting%&a for tutorial:&e %id%"),
    COMMAND_SETTING_REWARDS_CLEARED("command-setting-rewards-clear", "&aSuccessfully cleared all reward commands."),
    COMMAND_SETTING_REWARDS_ADDED("command-setting-rewards-added", "&aSuccessfully added the command."),
    COMMAND_SETTING_REWARDS_REMOVED("command-setting-rewards-removed", "&aSuccessfully removed the reward command."),

    COMMAND_SETTING_COMMANDS_CLEARED("command-setting-commands-clear", "&aSuccessfully cleared all whitelisted commands."),
    COMMAND_SETTING_COMMANDS_ADDED("command-setting-commands-added", "&aSuccessfully added the command to the whitelist."),
    COMMAND_SETTING_COMMANDS_REMOVED("command-setting-commands-removed", "&aSuccessfully removed the whitelisted command."),

    COMMAND_SWITCH_SUCCESSFUL("command-switch-successful", "Successfully switched the points %1% and %2%"),
    COMMAND_MOVE_INFRONT_SUCCESFULL("command-move-successful", "Successfully moved the point %1% in front off %2%"),

    COMMAND_ARGUMENTS_AVAILABLE("command-argument-available", "&aAvailable arguments: &7%args%"),

    COMMAND_EDITALL_CANBE("command-editall-canbe", "&eArguments that can be mass edited: &7%args%"),
    COMMAND_EDITALL_SUCCES("command-editall-succes", "&aSuccessfully edited all points"),

    COMMAND_LOOKUP_DEFINE_PLAYER("command-lookup-define-player", "&ePlease define a player to check. &7/st player <name>"),
    COMMAND_LOOKUP_UUID_ERROR("command-lookup-uuid-error", "&cUnable to get the UUID of the targed player. Are you sure he/she has played on this server before?"),
    COMMAND_LOOKUP_NEVER_PLAYED("command-lookup-never-played", "&cThat player has never joined the server."),
    COMMAND_LOOKUP_SET("command-lookup-set", "&aSuccessfully set the tutorial to completed."),
    COMMAND_LOOKUP_UNSET("command-lookup-unset", "&cSuccessfully set the tutorial to incompleted."),
    COMMAND_LOOKUP_SET_ERROR("command-lookup-set-error", "&cPlayer has already completed this tutorial."),
    COMMAND_LOOKUP_UNSET_ERROR("command-lookup-unset-error", "&cPlayer has not yet completed this tutorial."),

    EVENT_BLOCK_REMOVE_PERMISSION("event-block-remove", "&cYou do not have permission to remove this tutorial block."),
    EVENT_BLOCK_REMOVED("event-block-removed", "&aSuccessfully removed the tutorial block!"),

    RELOAD_STOPTUTORIAL("reload-stoptutorial", "&cYour tutorial has been cancelled because the plugin has been reloaded."),
    RELOAD_SUCCES("reload-succes", "&aSuccessfully reloaded the plugin."),

    POTIONEFFECT_TIME("potion-effect-time", "&ePotion time: &7%ticks%&e ticks - &7%seconds%&e seconds (20 ticks = 1 second)"),
    POTIONEFFECT_NOTIME("potion-effect-notime", "&eNo time was giving. Using the point time instead."),
    POTIONEFFECT_AMPLIFIER("potion-effect-amplifier", "Potion amplifier: %amp%"),
    POTIONEFFECT_WRONGUSAGE_1("potion-effect-wrong-usage-1", "&cWrong usage. Hold a potion in your hand and use this command: &7/st edit <id> <point> add potion <time> <level>"),
    POTIONEFFECT_WRONGUSAGE_2("potion-effect-wrong-usage-2", "&cOr use this command: &7/st edit <id> <point> add potion <potioneffect> <time (s)> <level>"),

    FIREWORK_REMOVE_INFO("firework-remove-info", "&eThis commands removes the nearest firework. No need for more arguments."),
    FIREWORK_REMOVE_FAILED("firework-remove-failed", "&cCould not find a firework in a range of 1000 blocks."),
    FIREWORK_ADD_WRONGUSAGE("firework-add-wrongusage", "&cWrong usage. Hold a firework in your hand."),

    TIME_CURRENT("current-time", "Current time: %time%"),

    TITLE_NOTITLE_SET("title-notitle-set", "&cThis point does not have a title yet. Please set one before setting timings."),
    TITLE_CURRENT("title-current", "&eThe current &a%type%&e time is: &a%ticks%&e ticks."),

    SETTING_EDITED("setting-edited", "&aSuccessfully edited the setting: &e%setting%"),

    SAVE_SUCCES("save-succes", "&aSuccessfully saved all the tutorials and blocks."),

    POINT_REMOVED("point-removed", "&aSuccessfully removed the point&e %id%"),
    POINT_INVALID_TYPE("point-invalid-type", "&cInvalid point type!"),
    POINT_EXAMPLE_COMMAND_CLICK("point-example-command-click", "&eClick to show the &a%type%&e point command"),
    POINT_EXAMPLE_MESSAGE("point-example-message", "&eClick one of the following options to show the correct command: "),
    POINT_ADDED("point-added", "&aSuccessfully added the new point. (&e%index%&a)"),

    INFO_NONE_EXISTING("info-none-existing", "&eThere do not exist any server tutorials at the moment."),
    INFO_MORE_INFO("info-more-info", "&7&oUse /st info <id> to get more info."),

    NPC_ID_EXIST("npc-id-exists", "&cThere already exists an NPC with that ID!"),
    NPC_TESTED_MOBS("npc-tested-mobs", "&eTested mobs: &7"),
    NPC_WRONG_TYPE("npc-wrong-type", "&cInvalid entity type. '&e%type%&c' (Does not exist)"),
    NPC_LIVING_TYPE("npc-living-type", "&cEntity has to be a living type!"),
    NPC_CREATION_SUCCESS("npc-creation-success", "Successfully created a NPC with NPC ID: &e%id%&a that plays server tutorial: &e%tutorial%"),
    NPC_SELECTION_CANCELLED("npc-selection-cancelled", "&cSelecting an NPC has been cancelled."),
    NPC_SELECTION_MESSAGE("npc-selection-message", "&eRight click an NPC to select it. &7(or use the command again to exit selection mode"),
    NPC_ID_NOT_EXISTING("npc-id-not-existing", "&cThere does not exist a NPC with ID: &e%id%"),
    NPC_REMOVED_SUCCESS("npc-removed-succes", "&aSuccessfully removed the NCP!"),
    NPC_TEXT_CHANGE("npc-text-change", "&aSuccessfully changed the text!"),
    NPC_HEIGHT_CHANGE("npc-height-change", "&aSuccessfully changed the height!"),
    NPC_PLAYER_SELECTED("npc-player-selected", "&cTo prevent errors, you cannot select a player as NPC!"),
    NPC_INVALID_ENTITY("npc-invalid-entity", "&cThe entity you tried to bind is not supported."),
    NPC_DELETE_CHUNK_UNLOADED("npc-delete-chunk-unloaded", "&cCannot delete a NPC that's in an unloaded chunk!"),

    NPC_INFO_NONE("npc-info-none", "&eNo NPCs have been made yet!"),
    NPC_INFO_MORE_INFO("npc-info-more-info", "&7&iUse /st npc info <id> to get more info."),

    HELP_HELP("help-help", "Shows this helpful help."),
    HELP_CREATE("help-create", "Create a new server tutorial."),
    HELP_REMOVE("help-remove", "Remove a server tutorial."),
    HELP_ADDPOINT("help-addpoint", "Add a point to a server tutorial."),
    HELP_REMOVEPOINT("help-removepoint", "Remove a point from a server tutorial"),
    HELP_EDIT("help-edit", "Edit settings of a servertutorial."),
    HELP_EDITPOINT("help-editpoint", "Edit a servertutorial point."),
    HELP_EDITALL("help-editall", "Edit all servertutorial point of a tutorial."),
    HELP_INFO("help-info", "Show info about a server tutorial."),
    HELP_PLAYER("help-player", "Shows info about the player"),
    HELP_GUI("help-gui", "Opens a helpful GUI."),
    HELP_PLAY("help-play", "Play a server tutorial."),
    HELP_QUIT("help-quit", "Quit a server tutorial."),
    HELP_PLAYPOINT("help-playpoint", "Play only 1 point of a server tutorial."),
    HELP_SETBLOCK("help-setblock", "Set a block that can be right clicked to start."),
    HELP_NPC("help-npc", "Spawn an NPC that can be right clicked to start."),
    HELP_RELOAD("help-reload", "Reload from the configs without saving."),
    HELP_SAVE("help-reload", "Save current data to the configs."),

    ERROR_WAIT_TO_END("error-wait-to-end", "&cPlease wait for the current point to end!"),
    ERROR_PLAYER_OFFLINE("error-player-offline", "&cThat player is currently not online!"),
    ERROR_PERSON_IN_TUTORIAL("error-person-in-tutorial", "&cThat person is already in a tutorial!"),
    ERROR_WAIT_TO_END_TUTORIAL("error-wait-to-end-tutorial", "&cPlease wait for the current tutorial to end before starting a new one."),
    ERROR_ATLEAST_ONE_WORD("error-atleast-one-word", "&cYou need to enter a minimal of one word to add."),
    ERROR_NO_INDEX("error-no-index", "&cPlease define a valid index."),
    ERROR_NOTEXISTING_INDEX("error-notexisting-index", "&cThat index does not exist."),
    ERROR_INVALID_INDEX("error-invalid-point", "&cPlease define a valid index."),
    ERROR_INVALID_POINT("error-invalid-point", "&cPlease define a valid tutorial point."),
    ERROR_EDITALL_FAIL("error-editall", "&cFailed to set setting. Stopped editing the points."),
    ERROR_INVALID_NUMBNER("error-invalid-number", "&cInvalid number."),
    ERROR_FAILED_FINDING_TUTORIAL("error-failed-finding-tutorial", "Failed to find server tutorial..."),
    ERROR_FAILED_FINDING_TUTORIAL_ADMIN("error-failed-finding-tutorial-admin", "&cCould not find a server tutorial with the id &e%id%&c. Please contact the server administrator."),
    ERROR_INVALID_EFFECT("error-invalid-effect", "&cInvalid potion effect."),
    ERROR_INVALID_TIME("error-invalid-time", "&cThat is not a valid time. (Time is in seconds)"),
    ERROR_INVALID_AMPLIFIER("error-invalid-amplifier", "That is not a valid amplifier."),
    ERROR_COMMAND_BLOCKED("error-command-blocked", "&cThat command has been blocked during the tutorial."),

    WARNING_TUTORIAL_OTHER_CANCELLED("warning-tutorial-other-cancelled", "&eThe player (%username%) already had a tutorial going on. The old one (%tutorial%) has been cancelled."),

    TIP_EDITPOINT("tip-editpoint", "&7[&aTIP&7] If you want to edit an point, use /st editpoint"),

    // Misc
    WRONG_COMMAND_FORMAT("wrong-command-format", "&cWrong arguments, please use the command like this: &7"),
    UNKOWN_ARGUMENT("unkown-argument", "&cUnknown arg. Possible args: &7"),
    PLAYER_NOT_FOUND("player-not-found", "&cUnable to find that player on this server.");

    private static Config languageFile;
    private String path;
    private String defaultMessage;

    Lang(String path, String defaultMessage) {
        this.path = path;
        this.defaultMessage = defaultMessage;
    }

    public static void setFile(Config config) {
        languageFile = config;
    }

    public String getPath() {
        return path;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }

    public String getConfigMessage(String key) {
        return ChatColor.translateAlternateColorCodes('&', languageFile.get(key).toString());
    }

    @Override
    public String toString() {
        return getConfigMessage(path);
    }
}
