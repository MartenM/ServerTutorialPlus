package nl.martenm.servertutorialplus.points.editor;

/**
 * @author MartenM
 * @since 29-11-2017.
 */
public abstract class PointArg implements IPointArg {

    private String name;

    private String[] aliases;

    public PointArg(String name) {
        this.name = name;
        this.aliases = new String[]{};
    }

    public PointArg(String name, String[] aliases) {
        this.name = name;
        this.aliases = aliases;
    }

    public boolean isAlias(String string) {
        for (String alias : aliases) {
            if (string.equalsIgnoreCase(alias)) {
                return true;
            }
        }
        return false;
    }

    public String getName() {
        return name;
    }
}
