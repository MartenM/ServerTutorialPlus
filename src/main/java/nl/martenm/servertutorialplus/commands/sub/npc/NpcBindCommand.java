package nl.martenm.servertutorialplus.commands.sub.npc;

import nl.martenm.servertutorialplus.ServerTutorialPlus;
import nl.martenm.servertutorialplus.helpers.PluginUtils;
import nl.martenm.servertutorialplus.language.Lang;
import nl.martenm.servertutorialplus.objects.ServerTutorial;
import nl.martenm.servertutorialplus.objects.TutorialEntitySelector;
import nl.martenm.simplecommands.SimpleCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NpcBindCommand extends SimpleCommand {

    public NpcBindCommand() {
        super("bind", "Used to bind an existing entity.", null, true);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        ServerTutorialPlus plugin = ServerTutorialPlus.getInstance();

        Player player = (Player) sender;
        if (plugin.selectingNpc.containsKey(player.getUniqueId())) {
            plugin.selectingNpc.remove(player.getUniqueId());
            sender.sendMessage(Lang.NPC_SELECTION_CANCELLED.toString());
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage(Lang.WRONG_COMMAND_FORMAT + "/st npc bind <NPC id> <ServerTutorial>");
            return true;
        }

        ServerTutorial tutorial;
        tutorial = PluginUtils.getTutorial(plugin, args[1]);
        if (tutorial == null) {
            sender.sendMessage(Lang.TUTORIAL_ID_NOT_FOUND.toString());
            return true;
        }

        TutorialEntitySelector selector = new TutorialEntitySelector(player, tutorial, args[0]);
        plugin.selectingNpc.put(player.getUniqueId(), selector);
        player.sendMessage(Lang.NPC_SELECTION_MESSAGE.toString());
        return true;
    }
}
