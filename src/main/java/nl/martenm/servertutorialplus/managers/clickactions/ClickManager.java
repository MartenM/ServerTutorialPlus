package nl.martenm.servertutorialplus.managers.clickactions;

import nl.martenm.servertutorialplus.ServerTutorialPlus;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * The ClickAction manager handles all events that could have some sort of click action.
 * This includes PlayerInteractEvent and PlayerInteractEntityEvent. Both these events can get caught here and handled as they should be.
 *
 * @author MartenM
 * @since 17-1-2018.
 */
public class ClickManager {

    private Map<UUID, IClickAction> clickActions;

    private ServerTutorialPlus plugin;

    public ClickManager(ServerTutorialPlus plugin) {
        this.plugin = plugin;
        this.clickActions = new HashMap<>();
    }

    public boolean registerClickAction(UUID uuid, IClickAction action) {
        if (hasClickaction(uuid)) return false;
        clickActions.put(uuid, action);
        return true;
    }

    public void removeClickaction(UUID uuid) {
        clickActions.remove(uuid);
    }

    public boolean hasClickaction(UUID uuid) {
        return clickActions.containsKey(uuid);
    }

    public void handleClickAction(PlayerInteractEvent event) {
        if (!hasClickaction(event.getPlayer().getUniqueId())) return;
        clickActions.get(event.getPlayer().getUniqueId()).run(event);
    }

    public void handleClickAction(PlayerInteractEntityEvent event) {
        if (!hasClickaction(event.getPlayer().getUniqueId())) return;
        clickActions.get(event.getPlayer().getUniqueId()).run(event);
    }
}
