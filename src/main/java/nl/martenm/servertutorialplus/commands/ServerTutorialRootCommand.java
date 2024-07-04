package nl.martenm.servertutorialplus.commands;

import nl.martenm.servertutorialplus.commands.help.STFormatter;
import nl.martenm.servertutorialplus.commands.sub.NpcCommand;
import nl.martenm.servertutorialplus.commands.sub.PlayerCommand;
import nl.martenm.servertutorialplus.commands.sub.SetBlockCommand;
import nl.martenm.servertutorialplus.commands.sub.manage.ReloadPluginCommand;
import nl.martenm.servertutorialplus.commands.sub.manage.SaveCommand;
import nl.martenm.servertutorialplus.commands.sub.misc.CancelActionCommand;
import nl.martenm.servertutorialplus.commands.sub.misc.NextPointCommand;
import nl.martenm.servertutorialplus.commands.sub.points.*;
import nl.martenm.servertutorialplus.commands.sub.tutorials.*;
import nl.martenm.simplecommands.RootCommand;

public class ServerTutorialRootCommand extends RootCommand {

    public ServerTutorialRootCommand() {
        super("servertutorial", "servertutorialplus.command", false);
        setHelpFormatter(new STFormatter());

        addCommand(new CreateTutorialCommand());
        addCommand(new RemoveTutorialCommand());
        addCommand(new AddpointCommand());
        addCommand(new RemovePointCommand());
        addCommand(new EditTutorialCommand());
        addCommand(new EditPointCommand());
        addCommand(new EditAllPointsCommand());
        addCommand(new InfoCommand());
        addCommand(new PlayerCommand());
        addCommand(new PlayTutorialCommand());
        addCommand(new QuitTutorialCommand());
        addCommand(new PlayPointCommand());
        addCommand(new SetBlockCommand());
        addCommand(new NpcCommand());
        addCommand(new ReloadPluginCommand());
        addCommand(new SaveCommand());

        addCommand(new CancelActionCommand());
        addCommand(new NextPointCommand());
    }
}
