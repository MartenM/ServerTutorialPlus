package nl.martenm.servertutorialplus.gui.inventoryHolders;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

/**
 * Created by Marten on 21-3-2017.
 */
public class Holder_MainMenu implements InventoryHolder {

    public Holder_MainMenu(String id, int page){
        this.ID = id;
        this.page = page;
    }

    public Holder_MainMenu(String id){
        this.ID = id;
    }

    @Override
    public Inventory getInventory() {
        return null;
    }

    private String ID;
    public String getID(){
        return this.ID;
    }

    private int page;
    public int getPage(){
        return this.page;
    }

}
