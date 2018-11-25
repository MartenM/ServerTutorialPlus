package nl.martenm.servertutorialplus.points.custom;

import nl.martenm.servertutorialplus.ServerTutorialPlus;
import nl.martenm.servertutorialplus.helpers.dataholders.OldValuesPlayer;
import nl.martenm.servertutorialplus.points.IPlayPoint;
import nl.martenm.servertutorialplus.points.IPointCallBack;
import nl.martenm.servertutorialplus.points.PointType;
import nl.martenm.servertutorialplus.points.ServerTutorialPoint;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;

/**
 * A point that is finished when the command /st next is used.
 * @author MartenM
 * @since 22-6-2018.
 */
public class CommandPoint extends ServerTutorialPoint {

    private static Map<UUID, IPointCallBack> waiting = new WeakHashMap<>();

    private static Map<UUID, String> commandTriggers = new WeakHashMap<>();

    public CommandPoint(ServerTutorialPlus plugin, Location loc) {
        super(plugin, loc, PointType.COMMAND);
    }

    @Override
    public IPlayPoint createPlay(Player player, OldValuesPlayer oldValuesPlayer, IPointCallBack callBack) {
        return new IPlayPoint() {
            @Override
            public void start() {
                waiting.put(player.getUniqueId(), callBack);
                commandTriggers.put(player.getUniqueId(), CommandPoint.super.triggerCommand);
                playDefault(player, oldValuesPlayer);
            }

            @Override
            public void stop() {
                waiting.remove(player.getUniqueId(), callBack);
                commandTriggers.remove(player.getUniqueId());
            }
        };
    }

    public static void handle(UUID uuid) {
        IPointCallBack callBack = waiting.get(uuid);
        if(callBack != null) {
            callBack.finish();
        }
    }

    public static boolean hasCommandTrigger(UUID uuid, String incomingCommand) {
        return commandTriggers.containsKey(uuid) && incomingCommand.startsWith(commandTriggers.get(uuid));
    }
}
