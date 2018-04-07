package nl.martenm.servertutorialplus.events;

import nl.martenm.servertutorialplus.MainClass;
import nl.martenm.servertutorialplus.gui.GuiInventories;
import nl.martenm.servertutorialplus.gui.inventoryHolders.Holder_MainMenu;
import nl.martenm.servertutorialplus.gui.inventoryHolders.Holder_ServerTutorial;
import nl.martenm.servertutorialplus.helpers.PluginUtils;
import nl.martenm.servertutorialplus.language.Lang;
import nl.martenm.servertutorialplus.objects.ServerTutorial;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Inventory click event. Used for handling the GUIs
 * @author MartenM
 */
public class OnInventoryClickEvent implements Listener{

    private MainClass plugin;
    public OnInventoryClickEvent(MainClass plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event){

        if(!(event.getWhoClicked() instanceof Player)){
            return;
        }

        if(event.getInventory().getHolder() instanceof Holder_MainMenu){
            //region holder 1
            Player player = (Player) event.getWhoClicked();
            event.setCancelled(true);

            if(event.getCurrentItem() != null){
                //Main menu
                ItemStack item = event.getCurrentItem();
                if(item.getItemMeta() == null){
                    return;
                }

                if(ChatColor.stripColor(event.getInventory().getName()).equalsIgnoreCase("Main Menu")){
                    if(ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("Server Tutorials")){
                        GuiInventories.openTutorialMenu(plugin, player, 0);
                    }
                }

                else if(((Holder_MainMenu) event.getInventory().getHolder()).getID().equalsIgnoreCase("server tutorials")){
                    if(item.getType() == Material.BARRIER){
                        player.closeInventory();
                    }

                    else if(ChatColor.stripColor(item.getItemMeta().getDisplayName()).equalsIgnoreCase("next")){
                        GuiInventories.openTutorialMenu(plugin, player, ((Holder_MainMenu) event.getInventory().getHolder()).getPage() + 1);
                    } else if(ChatColor.stripColor(item.getItemMeta().getDisplayName()).equalsIgnoreCase("previous")){
                        GuiInventories.openTutorialMenu(plugin, player, ((Holder_MainMenu) event.getInventory().getHolder()).getPage() - 1);
                    } else if(ChatColor.stripColor(item.getItemMeta().getDisplayName()).equalsIgnoreCase("back")){
                        GuiInventories.openMainMenu(plugin, player);
                    }

                    if(item.getType() == Material.EMPTY_MAP){
                        ServerTutorial st = PluginUtils.getTutorial(plugin, ChatColor.stripColor(item.getItemMeta().getDisplayName()));
                        if(st == null){
                            player.closeInventory();
                            player.sendMessage(Lang.ERROR_FAILED_FINDING_TUTORIAL.toString());
                        }
                        GuiInventories.openIDTutorialMenu(st, plugin, player, 0);
                    }
                }
            }
            //endregion
        }

        if(event.getInventory().getHolder() instanceof Holder_ServerTutorial){
            //region inventory servertutorial
            event.setCancelled(true);
            if(event.getCurrentItem() != null) {
                ItemStack item = event.getCurrentItem();
                if (item.getItemMeta() == null) {
                    return;
                }

                Player player = (Player) event.getWhoClicked();

                if(item.getType() == Material.BARRIER){
                    player.closeInventory();
                }

                else if(ChatColor.stripColor(item.getItemMeta().getDisplayName()).equalsIgnoreCase("next")){
                    GuiInventories.openIDTutorialMenu(((Holder_ServerTutorial) event.getInventory().getHolder()).getServerTutorial(), plugin, player, ((Holder_ServerTutorial) event.getInventory().getHolder()).getPage() + 1);
                } else if(ChatColor.stripColor(item.getItemMeta().getDisplayName()).equalsIgnoreCase("previous")){
                    GuiInventories.openIDTutorialMenu(((Holder_ServerTutorial) event.getInventory().getHolder()).getServerTutorial(), plugin, player, ((Holder_ServerTutorial) event.getInventory().getHolder()).getPage() - 1);
                } else if(ChatColor.stripColor(item.getItemMeta().getDisplayName()).equalsIgnoreCase("back")){
                    GuiInventories.openTutorialMenu(plugin, player, 0);
                }



                //stuff
            }
            //endregion
        }




    }

}
