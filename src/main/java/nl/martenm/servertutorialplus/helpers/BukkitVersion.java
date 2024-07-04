package nl.martenm.servertutorialplus.helpers;

import org.bukkit.Bukkit;

/**
 * Utility class to determine the server version and activate code based on this.
 */
public class BukkitVersion {

    private static BukkitVersion instance;
    int majorVersion;
    int minorVersion;
    int patchVersion;

    private BukkitVersion() {
        String versionString = Bukkit.getBukkitVersion();
        String[] split = versionString.split("-")[0].split("\\.");
        majorVersion = Integer.parseInt(split[0]);
        minorVersion = Integer.parseInt(split[1]);

        // 1.10 1.11 1.12, they don't have a patch version.
        if (split.length > 2) patchVersion = Integer.parseInt(split[2]);
        else patchVersion = 0;

        Bukkit.getLogger().info(String.format("Bukkit API version: %s.%s.%s", majorVersion, minorVersion, patchVersion));
    }

    public static BukkitVersion getInstance() {
        if (instance == null) {
            instance = new BukkitVersion();
        }
        return instance;
    }

    public boolean versionEqualOrHigher(int majorVersion) {
        return this.majorVersion >= majorVersion;
    }

    public boolean versionEqualOrHigher(int majorVersion, int minorVersion) {
        if (!versionEqualOrHigher(majorVersion)) return false;
        return this.minorVersion >= minorVersion;
    }

    public boolean versionEqualOrHigher(int majorVersion, int minorVersion, int patchVersion) {
        if (!versionEqualOrHigher(majorVersion, minorVersion)) return false;
        return this.patchVersion >= patchVersion;
    }

    public boolean versionEqualOrLower(int majorVersion) {
        return this.majorVersion <= majorVersion;
    }

    public boolean versionEqualOrLower(int majorVersion, int minorVersion) {
        if (!versionEqualOrLower(majorVersion)) return false;
        return this.minorVersion <= minorVersion;
    }

    public boolean versionEqualOrLower(int majorVersion, int minorVersion, int patchVersion) {
        if (!versionEqualOrLower(majorVersion, minorVersion)) return false;
        return this.patchVersion <= patchVersion;
    }
}
