package nl.martenm.servertutorialplus.managers;

import nl.martenm.servertutorialplus.ServerTutorialPlus;

import java.util.logging.Logger;

public abstract class AbstractManager {

    protected ServerTutorialPlus plugin;
    protected Logger logger;

    public AbstractManager(ServerTutorialPlus plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
    }

}
