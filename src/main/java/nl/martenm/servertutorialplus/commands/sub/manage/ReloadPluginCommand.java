package nl.martenm.servertutorialplus.commands.sub.manage;

import nl.martenm.servertutorialplus.ServerTutorialPlus;
import nl.martenm.servertutorialplus.helpers.Config;
import nl.martenm.servertutorialplus.language.Lang;
import nl.martenm.servertutorialplus.objects.TutorialController;
import nl.martenm.simplecommands.SimpleCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class ReloadPluginCommand extends SimpleCommand {

    public ReloadPluginCommand() {
        super("reload", Lang.HELP_RELOAD.toString(), "+reload", false);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        ServerTutorialPlus plugin = ServerTutorialPlus.getInstance();

        for (TutorialController tc : plugin.inTutorial.values()) {
            tc.cancel(true);
            tc.getPlayer().sendMessage(Lang.RELOAD_STOPTUTORIAL.toString());
        }

        plugin.enabled = false;

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
    }
}
