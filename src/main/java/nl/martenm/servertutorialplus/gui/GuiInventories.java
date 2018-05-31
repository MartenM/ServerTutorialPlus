package nl.martenm.servertutorialplus.gui;

import nl.martenm.servertutorialplus.ServerTutorialPlus;
import nl.martenm.servertutorialplus.gui.inventoryHolders.Holder_MainMenu;
import nl.martenm.servertutorialplus.gui.inventoryHolders.Holder_ServerTutorial;
import nl.martenm.servertutorialplus.helpers.PluginUtils;
import nl.martenm.servertutorialplus.objects.ServerTutorial;
import nl.martenm.servertutorialplus.points.ServerTutorialPoint;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marten on 21-3-2017.
 */
public class GuiInventories {

    public static void openMainMenu(ServerTutorialPlus plugin, Player player){
        ItemStack option1 = createStack(Material.APPLE, ChatColor.GREEN + "Server tutorials", new ArrayList<>());
        ItemStack option2 = createStack(Material.BEDROCK, ChatColor.GREEN + "§6Soon...", new ArrayList<>());

        Inventory menu = Bukkit.createInventory(new Holder_MainMenu("main menu"), 45, "Main menu");

        menu.setItem(21, option1);
        menu.setItem(23, option2);

        menu = FilledInventory(menu);

        player.openInventory(menu);
    }

    public static void openTutorialMenu(ServerTutorialPlus plugin, Player player, int page){

        Inventory menu = Bukkit.createInventory(new Holder_MainMenu("server tutorials", page), 45, ChatColor.DARK_GREEN + "Server Tutorials" + ChatColor.RESET + " | " + ChatColor.DARK_GRAY + " page: " + (page + 1));

        ItemStack PAIN = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 13);
        ItemMeta PAINMETA = PAIN.getItemMeta();
        PAINMETA.setDisplayName(" ");
        PAIN.setItemMeta(PAINMETA);
        PAIN.setItemMeta(PAINMETA);

        for(int i = 0; i < 9; i++){
            menu.setItem(i + 27, PAIN);
        }

        for(int i = page * 27 ; i < plugin.serverTutorials.size(); i++){
            ServerTutorial serverTutorial = plugin.serverTutorials.get(i);

            ItemStack item = new ItemStack(Material.EMPTY_MAP);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(serverTutorial.getId());
            List<String> lore = new ArrayList<>();

            lore.add(ChatColor.DARK_GRAY + "-----------");
            lore.add(formatInfo("Points", serverTutorial.points.size() + ""));
            lore.add(formatInfo("Plays", serverTutorial.plays + ""));
            lore.add(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "Click for info");
            meta.setLore(lore);

            item.setItemMeta(meta);

            menu.setItem(i - page * 27, item);

            if(i >= (page * 27 + 26)){
                break;
            }
        }

        if(plugin.serverTutorials.size() > ((page + 1 )* 27)){
            menu.setItem(41, createStack(Material.ARROW, ChatColor.YELLOW + "Next", new ArrayList<>()));
        }
        if(page != 0){
            menu.setItem(39, createStack(Material.ARROW,  ChatColor.YELLOW + "Previous", new ArrayList<>()));
        }

        menu.setItem(36, createStack(Material.SPECTRAL_ARROW, ChatColor.YELLOW + "Back", new ArrayList<>()));
        menu.setItem(40, createStack(Material.BARRIER, ChatColor.RED + "Close menu", new ArrayList<>()));

        //menu = FilledInventory(menu);

        player.openInventory(menu);
    }

    public static void openIDTutorialMenu(ServerTutorial serverTutorial, ServerTutorialPlus plugin, Player player, int page){

        Inventory menu = Bukkit.createInventory(new Holder_ServerTutorial(serverTutorial, "tutorial", page), 45, ChatColor.DARK_GREEN + "Server Tutorial " + ChatColor.YELLOW + serverTutorial.getId() + ChatColor.RESET + " | " + ChatColor.DARK_GRAY + " page: " + (page + 1));

        ItemStack PAIN = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 13);
        ItemMeta PAINMETA = PAIN.getItemMeta();
        PAINMETA.setDisplayName(" ");
        PAIN.setItemMeta(PAINMETA);
        PAIN.setItemMeta(PAINMETA);

        for(int i = 0; i < 9; i++){
            menu.setItem(i + 27, PAIN);
        }

        for(int i = page * 27 ; i < serverTutorial.points.size(); i++){
            ServerTutorialPoint tutorialPoint = serverTutorial.points.get(i);

            ItemStack item = new ItemStack(Material.SNOW_BALL);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(i + 1 + "");
            List<String> lore = new ArrayList<>();

            Location rounded = tutorialPoint.getLoc();
            rounded.setY(Math.round(rounded.getY()));
            rounded.setX(Math.round(rounded.getX()));
            rounded.setZ(Math.round(rounded.getZ()));
            rounded.setYaw(Math.round(rounded.getYaw()));
            rounded.setPitch(Math.round(rounded.getPitch()));

            lore.add(ChatColor.DARK_GRAY + "-----------");
            lore.add(formatInfo("Location", PluginUtils.fromLocation(rounded)));
            lore.add(formatInfo("Time", tutorialPoint.getTime() + ""));
            meta.setLore(lore);

            item.setItemMeta(meta);

            menu.setItem(i - page * 27, item);

            if(i >= (page * 27 + 26)){
                break;
            }
        }

        if(serverTutorial.points.size() > ((page + 1 )* 27)){
            menu.setItem(41, createStack(Material.ARROW, ChatColor.YELLOW + "Next", new ArrayList<>()));
        }
        if(page != 0){
            menu.setItem(39, createStack(Material.ARROW,  ChatColor.YELLOW + "Previous", new ArrayList<>()));
        }

        menu.setItem(36, createStack(Material.SPECTRAL_ARROW, ChatColor.YELLOW + "Back", new ArrayList<>()));
        menu.setItem(40, createStack(Material.BARRIER, ChatColor.RED + "Close menu", new ArrayList<>()));


        player.openInventory(menu);
    }

    private static ItemStack createStack(Material material, String name, List<String> lore){
        ItemStack stack = new ItemStack(material);
        ItemMeta stackmeta = stack.getItemMeta();
        stackmeta.setDisplayName(name);
        stackmeta.setLore(lore);
        stack.setItemMeta(stackmeta);
        return stack;
    }

    private static Inventory FilledInventory(Inventory inv){
        ItemStack pane = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 7);
        ItemMeta meta = pane.getItemMeta();
        meta.setDisplayName(" ");
        pane.setItemMeta(meta);

        for(int i = 0; i < inv.getSize(); i++){
            if(inv.getItem(i) == null){
                inv.setItem(i, pane);
            }
        }
        return inv;
    }

    private static String formatInfo(String command, String description){
        return ChatColor.GREEN + "  " + command + ChatColor.DARK_GRAY + " : " + ChatColor.YELLOW + description;
    }
}
