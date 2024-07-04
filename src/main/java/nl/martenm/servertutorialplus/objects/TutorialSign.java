package nl.martenm.servertutorialplus.objects;

import org.bukkit.block.Block;

/**
 * Represents a tutorial sign.
 * Created by Marten on 10-3-2017.
 */
public class TutorialSign {

    public Block block;
    public String ServerTutorialId;

    public TutorialSign(Block block, String serverTutorialId) {
        this.block = block;
        this.ServerTutorialId = serverTutorialId;
    }
}
