package nl.martenm.servertutorialplus.managers;

import net.md_5.bungee.api.ChatColor;
import nl.martenm.servertutorialplus.ServerTutorialPlus;
import nl.martenm.servertutorialplus.helpers.BukkitVersion;
import nl.martenm.servertutorialplus.helpers.Config;
import nl.martenm.servertutorialplus.helpers.SpigotUtils;
import nl.martenm.servertutorialplus.objects.NPCInfo;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class NPCManager extends AbstractManager {

    private Config npcSaves;
    private HashMap<UUID, NPCInfo> clickableNPCs;
    private int npcRetryTime = 5;

    private Map<UUID, NPCInfo> entityList = new HashMap<>();

    public NPCManager(ServerTutorialPlus plugin) {
        super(plugin);
        npcSaves = new Config(plugin, "NpcSaves");
        clickableNPCs = new HashMap<>();
        loadConfigValues();
    }

    public void loadConfigValues() {
        npcRetryTime = plugin.getConfig().getInt("npc.retry-time");
    }

    public NPCInfo getNPC(String ID) {
        if (ID == null) return null;
        for (NPCInfo info : clickableNPCs.values()) {
            if (info.getId().equalsIgnoreCase(ID)) {
                return info;
            }
        }
        return null;
    }

    public NPCInfo getByUUID(UUID uuid) {
        return this.clickableNPCs.get(uuid);
    }

    public List<NPCInfo> getNPCs() {
        return new ArrayList<>(this.clickableNPCs.values());
    }

    private void addEntities(NPCInfo info) {
        entityList.put(info.getNpcId(), info);
        entityList.put(info.getArmorstandIDs()[0], info);
        entityList.put(info.getArmorstandIDs()[1], info);
    }

    private void removeEntities(NPCInfo info) {
        entityList.remove(info.getNpcId());
        entityList.remove(info.getArmorstandIDs()[0]);
        entityList.remove(info.getArmorstandIDs()[1]);
    }

    public void loadNPCs() {
        if (!npcSaves.isConfigurationSection("npc")) {
            return;
        }

        for (String ID : npcSaves.getConfigurationSection("npc").getKeys(false)) {
            try {
                UUID npcUuid = UUID.fromString(npcSaves.getString("npc." + ID + ".UUID_npc"));
                UUID[] text = new UUID[2];
                text[0] = UUID.fromString(npcSaves.getString("npc." + ID + ".UUID_text1"));
                text[1] = UUID.fromString(npcSaves.getString("npc." + ID + ".UUID_text2"));
                String stID = npcSaves.getString("npc." + ID + ".servertutorial");
                Location loc = (Location) npcSaves.get("npc." + ID + ".loc");

                NPCInfo info = new NPCInfo(plugin, ID, npcUuid, text, stID, loc);
                clickableNPCs.put(npcUuid, info);
                addEntities(info);

                //Load chunk!
                if (!loc.getChunk().isLoaded()) {
                    loc.getChunk().load();
                }

                if (BukkitVersion.getInstance().versionEqualOrLower(1, 10)) {
                    logger.info("Activating keep in place for minecraft 1.8/1.9/1.10");
                    keepInPlace(npcUuid);
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.warning(" [!!] Could not load npc. Something in the NpcSaves.yml is messed up and prohibits the plugin from reading the data correctly!");
                logger.warning(" [!!] Revert any changes you have made if you have manually edited the config.");
            }
        }

    }

    public void saveNPCs() {
        npcSaves.set("npc", null);
        for (Map.Entry<UUID, NPCInfo> entry : clickableNPCs.entrySet()) {
            NPCInfo info = entry.getValue();
            npcSaves.set("npc." + info.getId() + ".UUID_npc", entry.getKey().toString());
            npcSaves.set("npc." + info.getId() + ".UUID_text1", info.getArmorstandIDs()[0].toString());
            npcSaves.set("npc." + info.getId() + ".UUID_text2", info.getArmorstandIDs()[1].toString());
            npcSaves.set("npc." + info.getId() + ".servertutorial", info.getServerTutorialID());
            try {
                npcSaves.set("npc." + info.getId() + ".loc", info.getLocation());
            } catch (NullPointerException ex) {
                logger.info("NPC: " + entry.getKey().toString() + " did not save a location.");
            }
        }

        npcSaves.save();
    }

    public void verifyCorrectSpawn(NPCInfo info, LivingEntity entity) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (entity.isDead()) {
                    deleteNPC(info);
                }
            }
        }.runTaskLater(plugin, 20);
    }

    public NPCInfo createNPC(EntityType entityType, Location location, String npcId, String tutorialId) {
        Entity entity_npc = location.getWorld().spawnEntity(location, entityType);
        if (!(entity_npc instanceof LivingEntity)) {
            entity_npc.remove();
            return null;
        }

        // Spawn all entities.
        LivingEntity npc = (LivingEntity) entity_npc;
        npc.setAI(false);
        npc.setCanPickupItems(false);
        npc.setGravity(false);
        npc.setCollidable(false);


        return createNPC(npc, location, npcId, tutorialId);
    }

    public NPCInfo createNPC(LivingEntity npc, Location location, String npcId, String tutorialId) {
        ArmorStand armorStand_1 = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        armorStand_1.setGravity(false);

        ArmorStand armorStand_2 = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        armorStand_2.setGravity(false);

        // Set properties of the armorstand and NPC.
        armorStand_1.setMarker(true);
        armorStand_2.setMarker(true);

        armorStand_1.setVisible(false);
        armorStand_2.setVisible(false);

        armorStand_1.setCustomNameVisible(true);
        armorStand_2.setCustomNameVisible(true);

        armorStand_1.setCustomName(ChatColor.YELLOW + ChatColor.BOLD.toString() + "Right click!");
        armorStand_2.setCustomName(ChatColor.GREEN + "Tutorial");

        npc.setInvulnerable(true);
        armorStand_1.setInvulnerable(true);
        armorStand_2.setInvulnerable(true);

        NPCInfo info = new NPCInfo(plugin, npcId, npc.getUniqueId(), new UUID[]{armorStand_1.getUniqueId(), armorStand_2.getUniqueId()}, tutorialId);

        // Configure heights.
        double npcHeight = npc.getHeight();
        new BukkitRunnable() {
            @Override
            public void run() {
                armorStand_1.teleport(npc.getLocation().add(0, npcHeight + 0.1, 0));
                armorStand_2.teleport(npc.getLocation().add(0, npcHeight + 0.35, 0));
            }
        }.runTaskLater(plugin, 5);

        if (BukkitVersion.getInstance().versionEqualOrLower(1, 10)) {
            keepInPlace(npc.getUniqueId());
        }
        verifyCorrectSpawn(info, npc);

        clickableNPCs.put(npc.getUniqueId(), info);
        saveNPCs();
        return info;
    }

    public void deleteNPC(NPCInfo info) {
        this.clickableNPCs.remove(info.getNpcId());
        despawnNPC(info);
        removeEntities(info);
    }

    /**
     * Despawn an NPC. Keeps the NPC in the NPC list.
     *
     * @param info
     */
    public void despawnNPC(NPCInfo info) {
        // The try catch are here to ensure that no matter what, we delete every single entity.

        try {
            SpigotUtils.getEntity(info.getNpcId()).remove();
        } catch (Exception ex) {
            logger.warning("Failed to remove the mob with UUID: " + info.getNpcId() + " (Hologram for NPC " + info.getId() + ")");
        }

        for (UUID uuid : info.getArmorstandIDs()) {
            try {
                SpigotUtils.getEntity(uuid).remove();
            } catch (Exception ex) {
                logger.warning("Failed to remove the armor stand with UUID: " + uuid + " (Hologram for NPC " + info.getId() + ") ");
            }
        }
    }

    /**
     * Legacy method to keep entities in place on older versions.
     *
     * @param npc
     */
    private void keepInPlace(UUID npc) {
        new BukkitRunnable() {
            Entity entity = SpigotUtils.getEntity(npc);

            @Override
            public void run() {
                if (entity == null || entity.isDead()) {
                    this.cancel();
                    return;
                }

                Location loc = entity.getLocation();

                try {
                    if (!plugin.isEnabled()) {
                        this.cancel();
                        return;
                    }

                    if (!loc.getChunk().isLoaded()) {
                        return;
                    }

                    Entity entity = SpigotUtils.getEntity(npc);
                    if (entity == null) {
                        return;
                    }

                    SpigotUtils.getEntity(npc).teleport(loc);
                    SpigotUtils.getEntity(npc).setVelocity(new org.bukkit.util.Vector(0, 0, 0));
                } catch (Exception ex) {
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 0, 5);
    }
}
