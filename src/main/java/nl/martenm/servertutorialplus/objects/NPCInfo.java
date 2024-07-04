package nl.martenm.servertutorialplus.objects;

import nl.martenm.servertutorialplus.ServerTutorialPlus;
import nl.martenm.servertutorialplus.helpers.SpigotUtils;
import org.bukkit.Location;

import java.util.UUID;

/**
 * This class holds info about NPCs.
 * It also contains the UUIDs of the armorstands (holograms)
 *
 * @author MartenM
 */
public class NPCInfo {

    private ServerTutorialPlus plugin;

    private String id;

    private UUID npcId;
    private UUID[] armorstandIDs;
    private String serverTutorialID;

    private Location location;

    public NPCInfo(ServerTutorialPlus plugin, String id, UUID npcId, UUID[] armorstandIDs, String serverTutorialID) {
        this.plugin = plugin;
        this.id = id;
        this.npcId = npcId;
        this.armorstandIDs = armorstandIDs;
        this.serverTutorialID = serverTutorialID;

        try {
            location = SpigotUtils.getEntity(npcId).getLocation();
        } catch (Exception ex) {
            System.out.println(ex.getStackTrace());
            //Welp what happend!
        }
    }

    public NPCInfo(ServerTutorialPlus plugin, String id, UUID npcId, UUID[] armorstandIDs, String serverTutorialID, Location location) {
        this.plugin = plugin;
        this.id = id;
        this.npcId = npcId;
        this.armorstandIDs = armorstandIDs;
        this.serverTutorialID = serverTutorialID;

        this.location = location;
    }

    public String getId() {
        return id;
    }

    public UUID getNpcId() {
        return npcId;
    }

    public UUID[] getArmorstandIDs() {
        return armorstandIDs;
    }

    public void setArmorstandIDs(UUID[] armorstandIDs) {
        this.armorstandIDs = armorstandIDs;
    }

    public String getServerTutorialID() {
        return serverTutorialID;
    }

    public Location getLocation() {
        return location;
    }
}
