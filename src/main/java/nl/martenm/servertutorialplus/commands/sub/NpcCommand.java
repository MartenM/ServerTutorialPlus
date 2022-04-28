package nl.martenm.servertutorialplus.commands.sub;

import nl.martenm.servertutorialplus.commands.sub.npc.*;
import nl.martenm.servertutorialplus.language.Lang;
import nl.martenm.simplecommands.RootCommand;

public class NpcCommand extends RootCommand {

    public NpcCommand() {
        super("npc", Lang.HELP_NPC.toString(), "+npc", false);
        addCommand(new NpcAddCommand());
        addCommand(new NpcBindCommand());
        addCommand(new NpcInfoCommand());
        addCommand(new NpcRemoveCommand());
        addCommand(new NpcTextCommand());
        addCommand(new NpcTextHeightCommand());
    }
}
