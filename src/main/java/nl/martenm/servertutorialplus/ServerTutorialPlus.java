package nl.martenm.servertutorialplus;

import nl.martenm.servertutorialplus.api.ServerTutorialApi;
import nl.martenm.servertutorialplus.commands.ServerTutorialCommands;
import nl.martenm.servertutorialplus.data.DataSource;
import nl.martenm.servertutorialplus.data.FlatDataSource;
import nl.martenm.servertutorialplus.data.MySqlDataSource;
import nl.martenm.servertutorialplus.events.*;
import nl.martenm.servertutorialplus.helpers.Config;
import nl.martenm.servertutorialplus.helpers.MetricsLite;
import nl.martenm.servertutorialplus.helpers.PluginUtils;
import nl.martenm.servertutorialplus.helpers.SpigotUtils;
import nl.martenm.servertutorialplus.language.Lang;
import nl.martenm.servertutorialplus.managers.FlatFileManager;
import nl.martenm.servertutorialplus.managers.clickactions.ClickManager;
import nl.martenm.servertutorialplus.objects.*;
import nl.martenm.servertutorialplus.points.PointType;
import nl.martenm.servertutorialplus.points.ServerTutorialPoint;
import nl.martenm.servertutorialplus.points.custom.CheckPoint;
import nl.martenm.servertutorialplus.points.custom.ClickBlockPoint;
import nl.martenm.servertutorialplus.points.custom.CommandPoint;
import nl.martenm.servertutorialplus.points.custom.TimedPoint;
import nl.martenm.servertutorialplus.reflection.IProtocol;
import nl.martenm.servertutorialplus.reflection.V1_13.Protocol_1_13_V1;
import nl.martenm.servertutorialplus.reflection.v1_12.Protocol_1_12;
import nl.martenm.servertutorialplus.reflection.v1_14.Protocol_1_14_V1;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONObject;

import java.io.File;
import java.util.*;
import java.util.logging.Logger;

/**
 * ServerTutorialPlus
 * Created by Marten on 5-3-2017.
 */
public class ServerTutorialPlus extends JavaPlugin{

    public Logger logger;

    public List<ServerTutorial> serverTutorials;
    public List<TutorialSign> tutorialSigns;
    public List<UUID> blockPlayers;
    public List<UUID> lockedPlayers;
    public List<UUID> lockedViews;

    public HashMap<UUID, TutorialController> inTutorial;
    public HashMap<UUID, NPCInfo> clickableNPCs;
    public HashMap<UUID, TutorialEntitySelector> selectingNpc;

    private IProtocol protocol;

    public Config tutorialSaves;
    public Config signSaves;
    public Config npcSaves;
    private Config languageFile;

    public boolean enabled;
    public boolean placeholderAPI;

    private DataSource dataSource;

    private ClickManager clickManager;

    protected ServerTutorialPlus instance;

    public void onEnable(){
        instance = this;

        logger = getLogger();
        logger.info("Enabling server tutorial...");
        createLanguageFiles();

        serverTutorials = new ArrayList<>();
        inTutorial = new HashMap<>();
        clickableNPCs = new HashMap<>();
        selectingNpc = new HashMap<>();
        tutorialSaves = new Config(this, "tutorialsaves");
        signSaves = new Config(this, "blockSaves");
        npcSaves = new Config(this, "NpcSaves");
        tutorialSigns = new ArrayList<>();
        lockedPlayers = new ArrayList<>();
        lockedViews = new ArrayList<>();
        blockPlayers = new ArrayList<>();
        clickManager = new ClickManager(this);

        MetricsLite metricsLite = new MetricsLite(this);

        registerCommands();
        registerConfigs();
        registerEvents();

        //DATASOURCE
        if(getConfig().getBoolean("datasource.mysql.enabled")){
            logger.info("Using MySql as datasource...");

            dataSource = new MySqlDataSource(this);

        }else{
            logger.info("Using FlatFile as datasource...");

            dataSource = new FlatDataSource(this);

        }

        setupProtocol();

        loadTutorials();
        loadSigns();
        loadNPCs();

        if(Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")){
            logger.info("PlaceholderAPI has been found!");
            placeholderAPI = true;
        } else {
            logger.info("PlaceholderAPI was not found!");
            placeholderAPI = false;
        }

        if(getServer().getOnlinePlayers().size() > 0){
            for(int i = 0; i < 2; i++){
                logger.warning("");
            }
            logger.warning("[!!!] Reload detected. Getting file data for players...");
            for(int i = 0; i < 2; i++){
                logger.warning("");
            }

            for(Player player : getServer().getOnlinePlayers()){
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        JSONObject object = FlatFileManager.getPlayerData(instance, player.getUniqueId());
                        if(object == null){
                            this.cancel();
                            return;
                        }
                        FlatFileManager.setPlayerData(instance, player.getPlayer(), object);
                        FlatFileManager.deleteFile(instance, player.getPlayer().getUniqueId());
                    }
                }.runTaskAsynchronously(this);
            }
        }

        enabled = true;
        logger.info("Servertutorial enabled successfully!");
    }

    public void onDisable(){
        enabled = false;
        logger.info("Disabling server tutorial...");

        //Try to safely disable the current tutorials.
        for(TutorialController tc : inTutorial.values()){
            tc.cancel(true);
            FlatFileManager.saveJson(this, tc.getOldValuesPlayer());
        }
        inTutorial.clear();

        //save tutorials.
        saveTutorials();
        saveSigns();
        saveNPCs();

        logger.info("Successfully disabled server tutorial! Thanks for using this plugin.");
    }

    private void registerConfigs(){
        getConfig().options().copyHeader(true);
        getConfig().options().copyDefaults(true);
        saveConfig();
    }

    private void registerCommands(){
        getCommand("servertutorial").setExecutor(new ServerTutorialCommands(this));
    }

    private void registerEvents(){
        PluginManager pm = getServer().getPluginManager();
        if(Bukkit.getVersion().contains("1.8")){
            pm.registerEvents(new OnPlayerInteractEventV1_8(this), this);
            pm.registerEvents(new OnPlayerInteractEntityEventV1_8(this), this);
        } else {
            pm.registerEvents(new OnPlayerInteractEvent(this), this);
            pm.registerEvents(new OnPlayerInteractEntityEvent(this), this);
        }
        pm.registerEvents(new OnPlayerJoinEvent(this), this);
        pm.registerEvents(new OnBlockBreakEvent(this), this);
        pm.registerEvents(new onPlayerMoveEvent(this), this);
        pm.registerEvents(new OnPlayerQuitEvent(this), this);
        pm.registerEvents(new OnInventoryClickEvent(this), this);
        pm.registerEvents(new OnPlayerToggleFlight(this), this);
        pm.registerEvents(new OnCommandPreprocessEvent(this), this);
        pm.registerEvents(new ChatEventListener(this), this);
    }

    public void loadTutorials(){

        if(!tutorialSaves.isConfigurationSection("tutorials")){
            return;
        }

        for(String ID : tutorialSaves.getConfigurationSection("tutorials").getKeys(false)){
            try {

                logger.info("Loading server tutorial: " + ID);
                ServerTutorial serverTutorial = new ServerTutorial(ID);
                serverTutorial.plays = tutorialSaves.getInt("tutorials." + ID + ".stats.plays");
                serverTutorial.invisiblePlayer = tutorialSaves.getBoolean("tutorials." + ID + ".invisible");
                serverTutorial.setNeedsPermission(tutorialSaves.getBoolean("tutorials." + ID + ".permission"));
                serverTutorial.setChatBlocked(tutorialSaves.getBoolean("tutorials." + ID + ".block-chat"));
                serverTutorial.setBlocksCommands(tutorialSaves.getBoolean("tutorials." + ID + ".blocks-commands"));
                serverTutorial.setRewards(tutorialSaves.getStringList("tutorials." + ID + ".rewards"));
                serverTutorial.setCommandWhiteList(tutorialSaves.getStringList("tutorials." + ID + ".command-whitelist"));

                if (tutorialSaves.isConfigurationSection("tutorials." + ID + ".points")) {

                    for (String key : tutorialSaves.getConfigurationSection("tutorials." + ID + ".points").getKeys(false)) {
                        try {
                            PointType type;
                            try{
                                type = PointType.valueOf(tutorialSaves.getString("tutorials." + ID + ".points." + key + ".type"));
                            } catch (Exception ex){
                                type = PointType.TIMED;
                            }

                            ServerTutorialPoint point = null;
                            switch (type) {
                                case TIMED:
                                    point = new TimedPoint(this, PluginUtils.fromString(this, tutorialSaves.getString("tutorials." + ID + ".points." + key + ".location")));
                                    break;
                                case CHECKPOINT:
                                    point = new CheckPoint(this, PluginUtils.fromString(this, tutorialSaves.getString("tutorials." + ID + ".points." + key + ".location")));
                                    break;
                                case CLICK_BLOCK:
                                    point = new ClickBlockPoint(this, PluginUtils.fromString(this, tutorialSaves.getString("tutorials." + ID + ".points." + key + ".location")));
                                    break;
                                case COMMAND:
                                    point = new CommandPoint(this, PluginUtils.fromString(this, tutorialSaves.getString("tutorials." + ID + ".points." + key + ".location")));
                                    break;
                            }

                            point.readSaveData(tutorialSaves, ID, key);
                            serverTutorial.points.add(point);
                        }
                        catch (Exception e){
                            e.printStackTrace();
                            logger.warning(" [!!] Could not a point from server tutorial " + ID + " point index " + key + ".");
                            logger.warning(" [!!] Something in the tutorialsaves.yml is messed up and prohibits the plugin from reading the data correctly!");
                            logger.warning(" [!!] Revert any changes you have made if you have manually edited the config.");
                        }
                    }
                }
                serverTutorials.add(serverTutorial);
            }
            catch (Exception e){
                e.printStackTrace();
                logger.warning(" [!!] Could not load server tutorial " + ID + ". Something in the tutorialsaves.yml is messed up and prohibits the plugin from reading the data correctly!");
                logger.warning(" [!!] Revert any changes you have made if you have manually edited the config.");
            }
        }
    }

    public void saveTutorials(){
        for(ServerTutorial serverTutorial : serverTutorials){
            tutorialSaves.set("tutorials." + serverTutorial.getId(), null);

            tutorialSaves.set("tutorials." + serverTutorial.getId() + ".stats.plays", serverTutorial.plays);
            tutorialSaves.set("tutorials." + serverTutorial.getId() + ".invisible", serverTutorial.invisiblePlayer);
            tutorialSaves.set("tutorials." + serverTutorial.getId() + ".permission", serverTutorial.getNeedsPermission());
            tutorialSaves.set("tutorials." + serverTutorial.getId() + ".block-chat", serverTutorial.isChatBlocked());
            tutorialSaves.set("tutorials." + serverTutorial.getId() + ".blocks-commands", serverTutorial.isBlockingCommands());

            tutorialSaves.set("tutorials." + serverTutorial.getId() + ".rewards", serverTutorial.getRewards());
            tutorialSaves.set("tutorials." + serverTutorial.getId() + ".command-whitelist", serverTutorial.getCommandWhiteList());

            for(int i = 0; i < serverTutorial.points.size(); i++){
                ServerTutorialPoint tutorialPoint = serverTutorial.points.get(i);
                tutorialPoint.saveData(tutorialSaves, serverTutorial.getId(), i + "");
            }
        }
        tutorialSaves.save();
    }

    public void loadSigns(){
        if(!signSaves.isConfigurationSection("signs")){
            return;
        }

        for(String key : signSaves.getConfigurationSection("signs").getKeys(false)) {
            try{
                Block block = PluginUtils.fromString(this, signSaves.getString("signs." + key + ".location")).getBlock();
                String ID = signSaves.getString("signs." + key + ".servertutorialplus");
                tutorialSigns.add(new TutorialSign(block, ID));
            } catch (Exception e){
                e.printStackTrace();
                logger.warning(" [!!] Could not load bound block. Something in the blocksaves.yml is messed up and prohibits the plugin from reading the data correctly!");
                logger.warning(" [!!] Revert any changes you have made if you have manually edited the config.");
            }

        }

    }

    public void saveSigns(){
        signSaves.set("signs", null);
        for(int i = 0; i < tutorialSigns.size(); i++){
            TutorialSign ts = tutorialSigns.get(i);
            signSaves.set("signs." + i + ".location", PluginUtils.fromLocation(ts.block.getLocation()));
            signSaves.set("signs." + i + ".servertutorialplus", ts.ServerTutorialId);
        }
        signSaves.save();
    }

    public void saveNPCs(){
        npcSaves.set("npc", null);
        for(Map.Entry<UUID, NPCInfo> entry : clickableNPCs.entrySet()){
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

    public void loadNPCs(){
        if(!npcSaves.isConfigurationSection("npc")){
            return;
        }

        for(String ID : npcSaves.getConfigurationSection("npc").getKeys(false)){
            try {
                UUID npcUuid = UUID.fromString(npcSaves.getString("npc." + ID + ".UUID_npc"));
                UUID[] text = new UUID[2];
                text[0] = UUID.fromString(npcSaves.getString("npc." + ID + ".UUID_text1"));
                text[1] = UUID.fromString(npcSaves.getString("npc." + ID + ".UUID_text2"));
                String stID = npcSaves.getString("npc." + ID + ".servertutorial");
                Location loc;

                try {
                    loc = (Location) npcSaves.get("npc." + ID + ".loc");
                } catch (Exception ex) {
                    loc = null;
                } //Nothing serious

                //Load chunk!
                if(!loc.getChunk().isLoaded()){
                    loc.getChunk().load();
                }

                if(getConfig().getBoolean("npc.remove-invalid")) {
                    if (SpigotUtils.getEntity(npcUuid) == null) {
                        //region not found

                        logger.warning(" [!!] Could not find the mob / npc. Retrying in " + getConfig().getInt("npc.retry-time") + " seconds. (Using location as well now)");

                        final Location location = loc;
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                Entity npc;

                                if (!location.getChunk().isLoaded()) {
                                    location.getChunk().load();
                                }

                                npc = SpigotUtils.getEntity(npcUuid);
                                if (npc != null) {
                                    logger.info(" [!] Found the NPC after searching it's UUID again. Enabling it right now... (NpcId: " + ID + ")");
                                    NPCInfo info = new NPCInfo(instance, ID, npc.getUniqueId(), text, stID, location);
                                    clickableNPCs.put(npc.getUniqueId(), info);
                                    return;
                                } else if (location != null) {
                                    Collection entities = location.getWorld().getNearbyEntities(location, 1, 1, 1);
                                    if (entities.size() != 0) {
                                        if (entities.size() > 1) {
                                            logger.warning(" [!!] More then 1 entity found at the location! Wrong entity might be selected!");
                                        }

                                        for (Object e : entities) {
                                            if (e instanceof LivingEntity) {
                                                npc = (Entity) e;
                                                break;
                                            }
                                        }

                                        if (npc != null) {
                                            NPCInfo info = new NPCInfo(instance, ID, npc.getUniqueId(), text, stID, location);
                                            clickableNPCs.put(npc.getUniqueId(), info);
                                            logger.info(" [!] Found the NPC after searching on its location. Enabling it right now... (NpcId: " + ID + ")");
                                            return;
                                        }
                                        //Fall through to deletion.
                                    }
                                }

                                try {
                                    SpigotUtils.getEntity(text[0]).remove();
                                } catch (Exception ex){
                                    logger.warning("Failed to remove the armor stand with UUID: " + text[0].toString() + " (Hologram 1 for NPC " + ID);
                                }
                                try {
                                    SpigotUtils.getEntity(text[1]).remove();
                                } catch (Exception ex) {
                                    logger.warning("Failed to remove the armor stand with UUID: " + text[0].toString() + " (Hologram 1 for NPC " + ID);
                                }

                                logger.warning(" [!!] Could not find the mob / npc using the location! Deleting the NPC.");
                            }
                        }.runTaskLater(this, getConfig().getInt("npc.retry-time") * 20);
                        continue;

                        //endregion
                    }
                }

                NPCInfo info = new NPCInfo(instance, ID, npcUuid, text, stID, loc);
                clickableNPCs.put(npcUuid, info);
                //SpigotUtils.getEntity(npc).teleport(loc);

                if (Bukkit.getVersion().contains("1.8") || Bukkit.getVersion().contains("1.9") || Bukkit.getVersion().contains("1.10")) {
                    new BukkitRunnable() {

                        Location loc = SpigotUtils.getEntity(npcUuid).getLocation();

                        @Override
                        public void run() {
                            try {
                                if (!isEnabled()) {
                                    this.cancel();
                                    return;
                                }

                                if(!loc.getChunk().isLoaded()){
                                    loc.getChunk().load();
                                }

                                if(SpigotUtils.getEntity(npcUuid) == null){
                                    return;
                                }

                                SpigotUtils.getEntity(npcUuid).teleport(loc);
                                SpigotUtils.getEntity(npcUuid).setVelocity(new org.bukkit.util.Vector(0, 0, 0));
                            } catch (Exception ex) {
                                this.cancel();
                            }
                        }
                    }.runTaskTimer(this, 0, 5);
                }
            }
            catch (Exception e){
                e.printStackTrace();
                logger.warning(" [!!] Could not load npc. Something in the NpcSaves.yml is messed up and prohibits the plugin from reading the data correctly!");
                logger.warning(" [!!] Revert any changes you have made if you have manually edited the config.");
            }
        }
    }

    private void createLanguageFiles(){

        File folder = new File(getDataFolder() + "/language");
        folder.mkdirs();

        if(folder.listFiles().length == 0){
            //TODO: Create default language files.
        }

        languageFile = new Config(this, "/language/" + getConfig().getString("language"));

        for(Lang l : Lang.values()){
            languageFile.addDefault(l.getPath(), l.getDefaultMessage());
        }

        languageFile.options().copyDefaults(true);
        languageFile.save();

        Lang.setFile(languageFile);
    }

    public ServerTutorialApi getApi(){
        return new ServerTutorialApi(this);
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public ClickManager getClickManager() {
        return clickManager;
    }

    public void setupProtocol() {
        String version = Bukkit.getServer().getClass().getPackage().getName();
        String formmatedVersion = version.substring(version.lastIndexOf(".") + 1);

        switch (formmatedVersion) {
            case "v1_13_R2":
            case "v1_13_R1":
                protocol = new Protocol_1_13_V1();
                break;
            case "v1_14_R1":
            default:
                protocol = new Protocol_1_14_V1();
                break;
        }

        getLogger().info("Using protocol: " + protocol.getClass().getName());
    }

    public IProtocol getProtocol() {
        return protocol;
    }
}
