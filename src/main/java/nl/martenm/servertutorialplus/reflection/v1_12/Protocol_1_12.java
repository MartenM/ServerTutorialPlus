package nl.martenm.servertutorialplus.reflection.v1_12;

import nl.martenm.servertutorialplus.helpers.Color;
import nl.martenm.servertutorialplus.reflection.IProtocol;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

/**
 * @author MartenM
 * @since 27-8-2018.
 */
public class Protocol_1_12 implements IProtocol {

    /*
        This is the default. We will be using the 1.12 version of playing the redstone particle here.

     */

    @Override
    public void playRedstoneParticle(Player player, Location location, Color color) {
        player.spawnParticle(Particle.REDSTONE, location, 0, (double) color.getRed() / 255, (double) color.getGreen() / 255, (double) color.getBlue() / 255, 1);
    }
}
