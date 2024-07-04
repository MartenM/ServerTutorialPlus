package nl.martenm.servertutorialplus.commands.sub;

import nl.martenm.servertutorialplus.ServerTutorialPlus;
import nl.martenm.servertutorialplus.language.Lang;
import nl.martenm.servertutorialplus.objects.TutorialSign;
import nl.martenm.simplecommands.SimpleCommand;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetBlockCommand extends SimpleCommand {


    public SetBlockCommand() {
        super("setblock", Lang.HELP_SETBLOCK.toString(), "+setblock", true);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        ServerTutorialPlus plugin = ServerTutorialPlus.getInstance();

        if (args.length < 1) {
            sender.sendMessage(Lang.WRONG_COMMAND_FORMAT + "/st setblock <id>");
            return true;
        }

        Player player = (Player) sender;
        Block block = player.getTargetBlock(null, 20);
        if (block.getType().equals(Material.AIR)) {
            sender.sendMessage(Lang.COMMAND_SETBLOCK_FAIL.toString());
            return true;
        }

        plugin.tutorialSigns.add(new TutorialSign(block, args[0]));
        player.sendMessage(Lang.COMMAND_SETBLOCK_SUCCES.toString().replace("%id%", args[0]).replace("%type%", block.getType().toString()));
        return true;
    }
}
