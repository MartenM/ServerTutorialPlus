package nl.martenm.servertutorialplus.data;

import java.util.List;
import java.util.UUID;

/**
 * Custom data source object for Server Tutorial Plus.
 *
 * @author MartenM
 * @since 24-12-2017.
 */
public interface DataSource {

    List<String> getPlayedTutorials(UUID uuid);

    boolean addPlayedTutorial(UUID uuid, String id);

    boolean removePlayedTutorial(UUID uuid, String id);

    boolean hasPlayedTutorial(UUID uuid, String id);
}
