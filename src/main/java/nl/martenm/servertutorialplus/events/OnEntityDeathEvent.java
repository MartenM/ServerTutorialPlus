package nl.martenm.servertutorialplus.events;

import nl.martenm.servertutorialplus.ServerTutorialPlus;
import nl.martenm.servertutorialplus.objects.NPCInfo;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class OnEntityDeathEvent implements Listener {

    private ServerTutorialPlus plugin;

    public OnEntityDeathEvent(ServerTutorialPlus plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDeathEvent(EntityDeathEvent event) {
        NPCInfo info = plugin.getNpcManager().getByUUID(event.getEntity().getUniqueId());
        if (info == null) return;

        plugin.getLogger().warning("[!!!] An NPC has been killed! It has been removed from the NPC list.");
        plugin.getNpcManager().deleteNPC(info);
    }

}
