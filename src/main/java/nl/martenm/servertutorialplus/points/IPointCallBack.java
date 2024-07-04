package nl.martenm.servertutorialplus.points;

/**
 * Point callback. Parsed to the points to give them the ability to finish the point.
 * The ServerTutorialController will handle this and start the next point.
 *
 * @author MartenM
 * @since 23-11-2017.
 */
public interface IPointCallBack {
    void finish();
}
