package nl.martenm.servertutorialplus.helpers.dataholders;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Created by Marten on 15-3-2017.
 */
public class OldValuesPlayer {

    private UUID uuid;
    private float original_flySpeed;
    private float original_walkSpeed;
    private boolean flying;
    private boolean allowFlight;
    private Location loc;
    private GameMode gamemode;

    public OldValuesPlayer(Player player) {
        this.uuid = player.getUniqueId();
        this.original_flySpeed = player.getFlySpeed();
        this.original_walkSpeed = player.getWalkSpeed();
        this.flying = player.isFlying();
        this.allowFlight = player.getAllowFlight();
        this.loc = player.getLocation();
        this.gamemode = player.getGameMode();
    }


    public float getOriginal_flySpeed() {
        return original_flySpeed;
    }

    public float getOriginal_walkSpeed() {
        return original_walkSpeed;
    }

    public UUID getUuid() {
        return uuid;
    }

    public boolean getFlying() {
        return flying;
    }

    public boolean isAllowFlight() {
        return allowFlight;
    }

    public Location getLoc() {
        return loc;
    }

    public GameMode getGamemode() {
        return gamemode;
    }

    public void restore(Player player) {
        player.setFlySpeed(getOriginal_flySpeed());
        player.setWalkSpeed(getOriginal_walkSpeed());
        player.setAllowFlight(isAllowFlight());
        player.setFlying(getFlying());
        player.setGameMode(getGamemode());
        player.teleport(getLoc());
    }
}
