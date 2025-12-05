package nl.martenm.servertutorialplus.helpers.dataholders;

import org.bukkit.Sound;

/**
 * Created by Marten on 5-3-2017.
 */
public class PlayerSound {

    public PlayerSound(String soundKey, float pitch, float volume){
        this.soundKey = soundKey.toLowerCase();
        this.pitch = pitch;
        this.volume = volume;
    }

    public String soundKey;
    public float pitch;
    public float volume;

}
