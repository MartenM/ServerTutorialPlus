package nl.martenm.servertutorialplus.commands.sub.tutorials;

import nl.martenm.servertutorialplus.ServerTutorialPlus;
import nl.martenm.servertutorialplus.language.Lang;
import nl.martenm.servertutorialplus.objects.TutorialController;
import nl.martenm.simplecommands.SimpleCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class QuitTutorialCommand extends SimpleCommand {

    public QuitTutorialCommand() {
        super("quit", Lang.HELP_QUIT.toString(), "+quit", true);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        ServerTutorialPlus plugin = ServerTutorialPlus.getInstance();

        Player player = (Player) sender;

        if (plugin.inTutorial.containsKey(player.getUniqueId())) {
            TutorialController tc = plugin.inTutorial.get(player.getUniqueId());
            tc.cancel(true);
            player.sendMessage(Lang.COMMAND_SUCCESFULLY_LEFT.toString());
            return true;
        }

        player.sendMessage(Lang.COMMAND_QUIT_NOTIN.toString());
        return true;
    }
}
