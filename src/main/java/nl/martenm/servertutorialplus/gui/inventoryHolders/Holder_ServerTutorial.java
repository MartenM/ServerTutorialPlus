package nl.martenm.servertutorialplus.gui.inventoryHolders;

import nl.martenm.servertutorialplus.objects.ServerTutorial;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

/**
 * Created by Marten on 23-3-2017.
 */
public class Holder_ServerTutorial implements InventoryHolder {
    @Override
    public Inventory getInventory() {
        return null;
    }

    public Holder_ServerTutorial(ServerTutorial serverTutorial, String id, int page){
        this.serverTutorial = serverTutorial;
        this.id = id;
        this.page = page;
    }

    private ServerTutorial serverTutorial;
    private String id;
    private int page;

    public ServerTutorial getServerTutorial() {
        return serverTutorial;
    }

    public int getPage() {
        return page;
    }

    public String getMode() {
        return id;
    }
}
