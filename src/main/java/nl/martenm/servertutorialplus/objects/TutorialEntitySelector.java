package nl.martenm.servertutorialplus.objects;

import nl.martenm.servertutorialplus.ServerTutorialPlus;
import nl.martenm.servertutorialplus.language.Lang;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.UUID;

/**
 * This object is used to hold certain data when a player is trying to bind an entity as NPC.
 * @author MartenM
 */
public class TutorialEntitySelector {

    private Player player;
    private String npcId;
    private ServerTutorial tutorial;

    public TutorialEntitySelector(Player player, ServerTutorial tutorial, String npcId){
        this.player = player;
        this.npcId = npcId;
        this.tutorial = tutorial;
    }

    public Player getPlayer() {
        return player;
    }

    public String getNpcId() {
        return npcId;
    }

    public ServerTutorial getTutorial() {
        return tutorial;
    }

    public void create(ServerTutorialPlus plugin, PlayerInteractEntityEvent event){

        if(!(event.getRightClicked() instanceof LivingEntity)){
            player.sendMessage(Lang.NPC_INVALID_ENTITY.toString());
            return;
        }

        if(event.getRightClicked() instanceof HumanEntity){
            HumanEntity humanEntity = (HumanEntity) event.getRightClicked();

            Player target = plugin.getServer().getPlayer(event.getRightClicked().getUniqueId());
            if(target != null){
                player.sendMessage(Lang.NPC_PLAYER_SELECTED.toString());
                return;
            }
        }

        LivingEntity npc = (LivingEntity) event.getRightClicked();
        npc.setAI(false);
        npc.setCanPickupItems(false);
        npc.setGravity(false);
        npc.setCollidable(false);

        ArmorStand armorStand_1 = (ArmorStand) player.getWorld().spawnEntity(player.getLocation(), EntityType.ARMOR_STAND);
        armorStand_1.setGravity(false);

        if(Bukkit.getVersion().contains("1.8") || Bukkit.getVersion().contains("1.9") || Bukkit.getVersion().contains("1.10")){
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "  "));
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7(&c!&7) &eSpigot version < 1.11. Using alternative text method."));
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aTIP: &7Use /st npc set height <npc ID> to set the text heigh relative to the NPC."));
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "  "));
            armorStand_1.teleport(npc.getLocation().add(0, 0.45, 0));

            new BukkitRunnable(){
                @Override
                public void run() {
                    Location loc = armorStand_1.getLocation();

                    ArmorStand armorStand_2 = (ArmorStand) loc.getWorld().spawnEntity(loc.add(0, 0.45, 0), EntityType.ARMOR_STAND);
                    armorStand_2.setGravity(false);

                    armorStand_1.setMarker(true);
                    armorStand_2.setMarker(true);

                    armorStand_1.setVisible(false);
                    armorStand_2.setVisible(false);

                    armorStand_1.setCustomNameVisible(true);
                    armorStand_2.setCustomNameVisible(true);

                    armorStand_1.setCustomName(ChatColor.YELLOW + ChatColor.BOLD.toString() +"Right click!");
                    armorStand_2.setCustomName(ChatColor.GREEN +  "Tutorial");

                    armorStand_1.teleport(loc.add(0, -0.25, 0));

                    npc.setInvulnerable(true);
                    armorStand_1.setInvulnerable(true);
                    armorStand_2.setInvulnerable(true);

                    NPCInfo info = new NPCInfo(plugin, npcId, npc.getUniqueId(), new UUID[] {armorStand_1.getUniqueId(), armorStand_2.getUniqueId()}, tutorial.getId());
                    plugin.clickableNPCs.put(npc.getUniqueId(), info);

                    npc.teleport(npc.getLocation());
                    player.sendMessage(ChatColor.GREEN + "Successfully created a NPC with NPC ID: " + ChatColor.YELLOW + info.getId() + ChatColor.GREEN + " that plays server tutorial: " + ChatColor.YELLOW + info.getServerTutorialID());
                }
            }.runTaskLater(plugin, 1);

            if(Bukkit.getVersion().contains("1.8") || Bukkit.getVersion().contains("1.9") || Bukkit.getVersion().contains("1.10")) {
                new BukkitRunnable(){
                    Location loc = npc.getLocation();
                    @Override
                    public void run() {
                        try {
                            npc.teleport(loc);
                            npc.setVelocity(new Vector(0, 0, 0));
                        }
                        catch (Exception ex){
                            this.cancel();
                        }
                    }
                }.runTaskTimer(plugin, 0, 5);
            }

        }
        else{
            npc.addPassenger(armorStand_1);

            new BukkitRunnable(){
                @Override
                public void run() {
                    //Do stuff
                    Location loc = armorStand_1.getLocation();
                    npc.removePassenger(armorStand_1);

                    ArmorStand armorStand_2 = (ArmorStand) loc.getWorld().spawnEntity(loc.add(0, 0.45, 0), EntityType.ARMOR_STAND);
                    armorStand_2.setGravity(false);

                    armorStand_1.setMarker(true);
                    armorStand_2.setMarker(true);

                    armorStand_1.setVisible(false);
                    armorStand_2.setVisible(false);

                    armorStand_1.setCustomNameVisible(true);
                    armorStand_2.setCustomNameVisible(true);

                    armorStand_1.setCustomName(ChatColor.YELLOW + ChatColor.BOLD.toString() +"Right click!");
                    armorStand_2.setCustomName(ChatColor.GREEN +  "Tutorial");

                    armorStand_1.teleport(loc.add(0, -0.25, 0));

                    npc.setInvulnerable(true);
                    armorStand_1.setInvulnerable(true);
                    armorStand_2.setInvulnerable(true);

                    NPCInfo info = new NPCInfo(plugin, npcId, npc.getUniqueId(), new UUID[] {armorStand_1.getUniqueId(), armorStand_2.getUniqueId()}, tutorial.getId());
                    plugin.clickableNPCs.put(npc.getUniqueId(), info);

                    npc.teleport(npc.getLocation());
                    player.sendMessage(ChatColor.GREEN + "Successfully created a NPC with NPC ID: " + ChatColor.YELLOW + info.getId() + ChatColor.GREEN + " that plays server tutorial: " + ChatColor.YELLOW + info.getServerTutorialID());
                    //Do stuff
                }
            }.runTaskLater(plugin, 1);
        }

        plugin.selectingNpc.remove(player.getUniqueId());
    }
}
