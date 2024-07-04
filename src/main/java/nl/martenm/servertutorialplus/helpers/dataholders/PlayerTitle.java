package nl.martenm.servertutorialplus.helpers.dataholders;

/**
 * Created by Marten on 5-3-2017.
 */
public class PlayerTitle {

    public String title;
    public String subtitle;
    public int time;
    public int fadeIn;
    public int fadeOut;

    public PlayerTitle(String title, String subtitle, int fadeIn, int time, int fadeOut) {
        this.title = title;
        this.subtitle = subtitle;
        this.time = time;
        this.fadeIn = fadeIn;
        this.fadeOut = fadeOut;
    }

    public PlayerTitle() {
        this.title = "";
        this.subtitle = "";
        this.fadeIn = 20;
        this.fadeOut = 20;
        this.time = 40;
    }
}
