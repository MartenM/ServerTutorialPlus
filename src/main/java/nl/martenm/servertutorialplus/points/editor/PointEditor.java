package nl.martenm.servertutorialplus.points.editor;

import nl.martenm.servertutorialplus.language.Lang;
import nl.martenm.servertutorialplus.objects.ServerTutorial;
import nl.martenm.servertutorialplus.points.ServerTutorialPoint;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author MartenM
 * @since 29-11-2017.
 */
public class PointEditor {

    private List<PointArg> arguments;

    private PointEditor() {
        arguments = new ArrayList<>();
    }

    public static PointEditor getPointeditor(ServerTutorialPoint point) {
        PointEditor editor = new PointEditor();
        editor.arguments.addAll(point.getArgs());


        return editor;
    }

    public boolean execute(ServerTutorial tutorial, ServerTutorialPoint point, CommandSender sender, String[] args) {

        for (PointArg argument : arguments) {
            if (argument.getName().equalsIgnoreCase(args[2]) || argument.isAlias(args[2])) {

                String[] editorArgs = Arrays.copyOfRange(args, 3, args.length);
                if (argument.run(tutorial, point, sender, editorArgs)) {
                    sender.sendMessage(Lang.SETTING_EDITED.toString().replace("%setting%", argument.getName()));
                    return true;
                } else {
                    return false;
                }
            }
        }

        String possible = "";
        for (PointArg argument : arguments) {
            possible += argument.getName() + ", ";
        }
        possible = point.getArgsString();

        sender.sendMessage(Lang.UNKOWN_ARGUMENT + possible);
        return false;
    }
}
