package nl.martenm.servertutorialplus.helpers;

/**
 * Simple colour class for particle colours.
 *
 * @author MartenM
 * @since 27-11-2017.
 */

public class Color {
    private int red;
    private int green;
    private int blue;

    public Color(int red, int green, int blue) {
        if (red == 0) {
            this.red = 1;
        } else this.red = red;

        this.green = green;
        this.blue = blue;
    }

    public static Color fromString(String input) {
        try {
            String[] data = input.split(" ");
            return new Color(Integer.parseInt(data[0]), Integer.parseInt(data[1]), Integer.parseInt(data[2]));
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("[!!] Invalid colour from string!");
            return new Color(255, 0, 0);
        }
    }

    public int getRed() {
        return red;
    }

    public void setRed(int red) {
        if (red == 0) {
            this.red = 1;
        } else this.red = red;
    }

    public int getGreen() {
        return green;
    }

    public void setGreen(int green) {
        this.green = green;
    }

    public int getBlue() {
        return blue;
    }

    public void setBlue(int blue) {
        this.blue = blue;
    }

    public void set(int red, int green, int blue) {
        setRed(red);
        setGreen(green);
        setBlue(blue);
    }

    @Override
    public String toString() {
        return red + " " + green + " " + blue;
    }

}
