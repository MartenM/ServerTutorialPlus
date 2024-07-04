package nl.martenm.servertutorialplus.reflection;

import nl.martenm.servertutorialplus.helpers.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * This class helps to provide support for multiple versions.
 *
 * @author MartenM
 * @since 27-8-2018.
 */
public interface IProtocol {

    void playRedstoneParticle(Player player, Location location, Color color);

}
