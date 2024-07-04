package nl.martenm.servertutorialplus.points;

/**
 * Enum for all the point types.
 *
 * @author MartenM
 * @since 24-11-2017.
 */
//TODO: Make it so that points are identified by an type String so other people could possible create addons (as seperate points).
public enum PointType {
    TIMED,
    CHECKPOINT,
    CLICK_BLOCK,
    COMMAND,
}
