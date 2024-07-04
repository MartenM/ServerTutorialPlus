package nl.martenm.servertutorialplus.points;

import com.cryptomorin.xseries.messages.Titles;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import nl.martenm.servertutorialplus.ServerTutorialPlus;
import nl.martenm.servertutorialplus.helpers.Config;
import nl.martenm.servertutorialplus.helpers.PluginUtils;
import nl.martenm.servertutorialplus.helpers.dataholders.FireWorkInfo;
import nl.martenm.servertutorialplus.helpers.dataholders.OldValuesPlayer;
import nl.martenm.servertutorialplus.helpers.dataholders.PlayerSound;
import nl.martenm.servertutorialplus.helpers.dataholders.PlayerTitle;
import nl.martenm.servertutorialplus.points.editor.PointArg;
import nl.martenm.servertutorialplus.points.editor.args.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

/**
 * The default abstract class for a ServerTutorialPoint.
 *
 * @author MartenM
 * @since 22-11-2017.
 */
public abstract class ServerTutorialPoint {

    protected ServerTutorialPlus plugin;
    protected PointType type;

    protected Location loc;
    protected List<String> message_chat;
    protected List<String> commands;
    protected List<FireWorkInfo> fireworks;
    protected String message_actionBar;
    protected PlayerTitle titleInfo;
    protected PlayerSound soundInfo;
    protected List<PotionEffect> pointionEffects;
    protected boolean lockPlayer;
    protected boolean lockView;
    protected double time;
    protected boolean flying;

    public ServerTutorialPoint(ServerTutorialPlus plugin, Location loc, PointType type) {
        this.plugin = plugin;
        this.loc = loc;
        this.type = type;
        this.time = 2;
        this.message_chat = new ArrayList<>();
        this.commands = new ArrayList<>();
        this.fireworks = new ArrayList<>();
        this.lockPlayer = false;
        this.lockView = false;
        this.pointionEffects = new ArrayList<>();
    }

    public static String getArgsString(List<PointArg> args) {
        String s = "";
        for (PointArg arg : args) {
            s += arg.getName() + " / ";
        }

        return s.substring(0, s.length() - 3);
    }

    /**
     * The method create the playable point.
     *
     * @param player          The targeted player.
     * @param oldValuesPlayer Old values of the player before starting the tutorial / point.
     * @param callBack        The callback to the controller used to complete the point.
     */
    public IPlayPoint createPlay(Player player, OldValuesPlayer oldValuesPlayer, IPointCallBack callBack) {
        return new IPlayPoint() {

            BukkitTask timerTask = null;

            @Override
            public void start() {
                playDefault(player, oldValuesPlayer, true);

                timerTask = new BukkitRunnable() {
                    @Override
                    public void run() {
                        callBack.finish();
                    }
                }.runTaskLater(plugin, (long) (20 * time));
            }

            @Override
            public void stop() {
                if (timerTask != null) timerTask.cancel();
            }
        };
    }

    /**
     * The very basic logic of a point that should be applied to every point.
     * This includes for example, lockplayer, lockview, time, titles, sounds, etc...
     *
     * @param player          The targeted player.
     * @param oldValuesPlayer Old values of the player before starting the tutorial / point.
     */
    protected void playDefault(Player player, OldValuesPlayer oldValuesPlayer, boolean teleport) {
        if (teleport) player.teleport(loc);

        for (String message : message_chat) {
            player.sendMessage(PluginUtils.replaceVariables(plugin.placeholderAPI, player, message));
        }

        //region lockplayer
        if (lockPlayer) {
            if (!plugin.lockedPlayers.contains(player.getUniqueId())) {
                plugin.lockedPlayers.add(player.getUniqueId());
                player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, (int) (time * 20), 128, false, false));
                player.setFlySpeed(0.0f);
                player.setWalkSpeed(0.0f);
            }
        } else {
            if (plugin.lockedPlayers.contains(player.getUniqueId())) {
                plugin.lockedPlayers.remove(player.getUniqueId());
                player.setFlySpeed(oldValuesPlayer.getOriginal_flySpeed());
                player.setWalkSpeed(oldValuesPlayer.getOriginal_walkSpeed());
            }
        }
        //endregion

        //region lockView
        if (lockView) {
            if (!plugin.lockedViews.contains(player.getUniqueId())) {
                plugin.lockedViews.add(player.getUniqueId());
            }
        } else {
            plugin.lockedViews.remove(player.getUniqueId());
        }
        //endregion

        //region flying
        if (flying) {
            if (!player.isFlying()) {
                if (!player.getAllowFlight()) {
                    player.setAllowFlight(true);
                }
                player.setFlying(true);
            }
        } else {
            if (player.isFlying()) {
                player.setFlying(false);
                player.setAllowFlight(oldValuesPlayer.isAllowFlight());
            }
        }
        //endregion

        //region actionbar
        if (message_actionBar != null) {
            //NeedsReflection.sendActionBar(player, PluginUtils.replaceVariables(plugin.placeholderAPI, player, message_actionBar));
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(PluginUtils.replaceVariables(plugin.placeholderAPI, player, message_actionBar)));
        }
        //endregion

        //region commands
        for (String command : commands) {
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), PluginUtils.replaceVariables(plugin.placeholderAPI, player, command));
        }
        //endregion

        //region fireworks
        if (fireworks != null) {
            for (FireWorkInfo fireWorkInfo : fireworks) {
                Firework firework = (Firework) player.getWorld().spawnEntity(fireWorkInfo.getLoc(), EntityType.FIREWORK);
                firework.setFireworkMeta(fireWorkInfo.getFireworkMeta());
            }
        }
        //endregion

        //region potionEffects
        if (pointionEffects != null) {
            for (PotionEffect effect : pointionEffects) {
                player.addPotionEffect(effect, false);
            }
        }
        //endregion

        if (titleInfo != null) {
            Titles.sendTitle(player, titleInfo.fadeIn, titleInfo.time, titleInfo.fadeOut, PluginUtils.replaceVariables(plugin.placeholderAPI, player, titleInfo.title), PluginUtils.replaceVariables(plugin.placeholderAPI, player, titleInfo.subtitle));
        }

        if (soundInfo != null) {
            // entity, string, volume, pitch <-- I forget that all the damm time.
            player.playSound(player, soundInfo.sound, soundInfo.volume, soundInfo.pitch);
        }
    }

    public void readSaveData(Config tutorialSaves, String ID, String i) {
        //Normal saving
        time = tutorialSaves.getDouble("tutorials." + ID + ".points." + i + ".time");

        message_chat = tutorialSaves.getStringList("tutorials." + ID + ".points." + i + ".messages");
        commands = tutorialSaves.getStringList("tutorials." + ID + ".points." + i + ".commands");

        message_actionBar = tutorialSaves.getString("tutorials." + ID + ".points." + i + ".actionbar");
        lockPlayer = tutorialSaves.getBoolean("tutorials." + ID + ".points." + i + ".locplayer");
        lockView = tutorialSaves.getBoolean("tutorials." + ID + ".points." + i + ".locview");
        flying = tutorialSaves.getBoolean("tutorials." + ID + ".points." + i + ".setFly");
        /*
           Fire work meta!
        */

        if (tutorialSaves.isConfigurationSection("tutorials." + ID + ".points." + i + ".title")) {
            String title = tutorialSaves.getString("tutorials." + ID + ".points." + i + ".title.title");
            String subtitle = tutorialSaves.getString("tutorials." + ID + ".points." + i + ".title.subtitle");
            int fadein = tutorialSaves.getInt("tutorials." + ID + ".points." + i + ".title.fade-in");
            int stay = tutorialSaves.getInt("tutorials." + ID + ".points." + i + ".title.stay");
            int fadeout = tutorialSaves.getInt("tutorials." + ID + ".points." + i + ".title.fade-out");

            titleInfo = new PlayerTitle(title, subtitle, fadein, stay, fadeout);
        }

        if (tutorialSaves.isConfigurationSection("tutorials." + ID + ".points." + i + ".sound")) {
            String sound = tutorialSaves.getString("tutorials." + ID + ".points." + i + ".sound.sound");
            float pitch = Float.parseFloat(tutorialSaves.getString("tutorials." + ID + ".points." + i + ".sound.pitch"));
            float volume = Float.parseFloat(tutorialSaves.getString("tutorials." + ID + ".points." + i + ".sound.volume"));
            soundInfo = new PlayerSound(sound, pitch, volume);
        }

        if (tutorialSaves.isConfigurationSection("tutorials." + ID + ".points." + i + ".fireworks")) {
            List<FireWorkInfo> infos = new ArrayList<>();
            for (String index : tutorialSaves.getConfigurationSection("tutorials." + ID + ".points." + i + ".fireworks").getKeys(false)) {
                FireworkMeta meta = (FireworkMeta) tutorialSaves.get("tutorials." + ID + ".points." + i + ".fireworks." + index + ".meta");
                // "tutorials." + ID + ".points." + i + ".fireworks." + index + ".location"
                // "tutorials." + ID + ".points." + i + ".fireworks." + index + ".meta"
                Location loc = PluginUtils.fromString(plugin, tutorialSaves.getString("tutorials." + ID + ".points." + i + ".fireworks." + index + ".location"));
                infos.add(new FireWorkInfo(loc, meta));
            }
            fireworks = infos;
        }

        if (tutorialSaves.isConfigurationSection("tutorials." + ID + ".points." + i + ".potioneffects")) {
            List<PotionEffect> infos = new ArrayList<>();
            for (String index : tutorialSaves.getConfigurationSection("tutorials." + ID + ".points." + i + ".potioneffects").getKeys(false)) {

                PotionEffectType type = PotionEffectType.getByName(tutorialSaves.getString("tutorials." + ID + ".points." + i + ".potioneffects." + index + ".type"));
                int duration = tutorialSaves.getInt("tutorials." + ID + ".points." + i + ".potioneffects." + index + ".time");
                int amplifier = tutorialSaves.getInt("tutorials." + ID + ".points." + i + ".potioneffects." + index + ".amplifier");
                boolean isAmbient = tutorialSaves.getBoolean("tutorials." + ID + ".points." + i + ".potioneffects." + index + ".ambient");
                boolean show_particles = tutorialSaves.getBoolean("tutorials." + ID + ".points." + i + ".potioneffects." + index + ".show_particles");

                infos.add(new PotionEffect(type, duration, amplifier, isAmbient, show_particles));
            }
            pointionEffects = infos;
        }

        readCustomSaveData(tutorialSaves, ID, i);
    }

    protected void readCustomSaveData(Config tutorialSaves, String key, String i) {

    }

    public void saveData(Config tutorialSaves, String key, String i) {
        tutorialSaves.set("tutorials." + key + ".points." + i + ".type", type.toString());
        tutorialSaves.set("tutorials." + key + ".points." + i + ".location", PluginUtils.fromLocation(loc));
        tutorialSaves.set("tutorials." + key + ".points." + i + ".time", time);
        tutorialSaves.set("tutorials." + key + ".points." + i + ".locplayer", lockPlayer);
        tutorialSaves.set("tutorials." + key + ".points." + i + ".locview", lockView);
        tutorialSaves.set("tutorials." + key + ".points." + i + ".messages", message_chat);
        tutorialSaves.set("tutorials." + key + ".points." + i + ".actionbar", message_actionBar);
        tutorialSaves.set("tutorials." + key + ".points." + i + ".commands", commands);
        if (flying) tutorialSaves.set("tutorials." + key + ".points." + i + ".setFly", flying);

        if (titleInfo != null) {
            tutorialSaves.set("tutorials." + key + ".points." + i + ".title.title", titleInfo.title);
            tutorialSaves.set("tutorials." + key + ".points." + i + ".title.subtitle", titleInfo.subtitle);
            tutorialSaves.set("tutorials." + key + ".points." + i + ".title.fade-in", titleInfo.fadeIn);
            tutorialSaves.set("tutorials." + key + ".points." + i + ".title.stay", titleInfo.time);
            tutorialSaves.set("tutorials." + key + ".points." + i + ".title.fade-out", titleInfo.fadeOut);
        }

        if (soundInfo != null) {
            tutorialSaves.set("tutorials." + key + ".points." + i + ".sound.sound", soundInfo.sound);
            tutorialSaves.set("tutorials." + key + ".points." + i + ".sound.pitch", soundInfo.pitch);
            tutorialSaves.set("tutorials." + key + ".points." + i + ".sound.volume", soundInfo.volume);
        }

        if (fireworks != null) {
            for (int fire = 0; fire < fireworks.size(); fire++) {
                FireWorkInfo info = fireworks.get(fire);
                tutorialSaves.set("tutorials." + key + ".points." + i + ".fireworks." + fire + ".location", PluginUtils.fromLocation(info.getLoc()));
                tutorialSaves.set("tutorials." + key + ".points." + i + ".fireworks." + fire + ".meta", info.getFireworkMeta());
            }
        }

        if (pointionEffects != null) {
            for (int effect = 0; effect < pointionEffects.size(); effect++) {
                PotionEffect info = pointionEffects.get(effect);
                tutorialSaves.set("tutorials." + key + ".points." + i + ".potioneffects." + effect + ".type", info.getType().getName());
                tutorialSaves.set("tutorials." + key + ".points." + i + ".potioneffects." + effect + ".time", info.getDuration());
                tutorialSaves.set("tutorials." + key + ".points." + i + ".potioneffects." + effect + ".amplifier", info.getAmplifier());
                tutorialSaves.set("tutorials." + key + ".points." + i + ".potioneffects." + effect + ".ambient", info.isAmbient());
                tutorialSaves.set("tutorials." + key + ".points." + i + ".potioneffects." + effect + ".show_particles", info.hasParticles());
            }
        }

        saveCustomData(tutorialSaves, key, i);
    }

    protected void saveCustomData(Config tutorialSaves, String key, String i) {

    }

    public List<PointArg> getArgs() {
        List<PointArg> args = new ArrayList<>();
        args.add(new TimeArg());
        args.add(new LocationArg());
        args.add(new FlyArg());
        args.add(new LockPlayerArg());
        args.add(new LockViewArg());
        args.add(new MessagesArg());
        args.add(new CommandsArg());
        args.add(new ActionbarArg());
        args.add(new FireworkArg());
        args.add(new PotionEffectArg());
        args.add(new SoundArg());
        args.add(new TitleArg());
        return args;
    }

    public String getArgsString() {
        List<PointArg> args = getArgs();
        String s = "";
        for (PointArg arg : args) {
            s += arg.getName() + " / ";
        }

        return s.substring(0, s.length() - 3) + " / switch / infront";
    }

    //Getters and setters
    public Location getLoc() {
        return loc;
    }

    public void setLoc(Location loc) {
        this.loc = loc;
    }

    public String getMessage_actionBar() {
        return message_actionBar;
    }

    public void setMessage_actionBar(String message_actionBar) {
        this.message_actionBar = message_actionBar;
    }

    public PlayerTitle getTitleInfo() {
        return titleInfo;
    }

    public void setTitleInfo(PlayerTitle titleInfo) {
        this.titleInfo = titleInfo;
    }

    public List<String> getMessage_chat() {
        return message_chat;
    }

    public void setMessage_chat(List<String> message_chat) {
        this.message_chat = message_chat;
    }

    public List<String> getCommands() {
        return commands;
    }

    public void setCommands(List<String> commands) {
        this.commands = commands;
    }

    public List<FireWorkInfo> getFireworks() {
        return fireworks;
    }

    public void setFireworks(List<FireWorkInfo> fireworks) {
        this.fireworks = fireworks;
    }

    public List<PotionEffect> getPointionEffects() {
        return pointionEffects;
    }

    public void setPointionEffects(List<PotionEffect> pointionEffects) {
        this.pointionEffects = pointionEffects;
    }

    public PlayerSound getSoundInfo() {
        return soundInfo;
    }

    public void setSoundInfo(PlayerSound soundInfo) {
        this.soundInfo = soundInfo;
    }

    public boolean isLockPlayer() {
        return lockPlayer;
    }

    public void setLockPlayer(boolean lockPlayer) {
        this.lockPlayer = lockPlayer;
    }

    public boolean isLockView() {
        return lockView;
    }

    public void setLockView(boolean lockView) {
        this.lockView = lockView;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public boolean isSetFlying() {
        return flying;
    }

    public void setFlying(boolean setFlying) {
        this.flying = setFlying;
    }
}
