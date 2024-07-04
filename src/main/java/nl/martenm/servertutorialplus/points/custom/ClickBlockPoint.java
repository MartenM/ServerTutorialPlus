package nl.martenm.servertutorialplus.points.custom;

import net.md_5.bungee.api.ChatColor;
import nl.martenm.servertutorialplus.ServerTutorialPlus;
import nl.martenm.servertutorialplus.helpers.Color;
import nl.martenm.servertutorialplus.helpers.Config;
import nl.martenm.servertutorialplus.helpers.PluginUtils;
import nl.martenm.servertutorialplus.helpers.dataholders.OldValuesPlayer;
import nl.martenm.servertutorialplus.managers.clickactions.IClickAction;
import nl.martenm.servertutorialplus.objects.ServerTutorial;
import nl.martenm.servertutorialplus.points.IPlayPoint;
import nl.martenm.servertutorialplus.points.IPointCallBack;
import nl.martenm.servertutorialplus.points.PointType;
import nl.martenm.servertutorialplus.points.ServerTutorialPoint;
import nl.martenm.servertutorialplus.points.editor.PointArg;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.List;

/**
 * @author MartenM
 * @since 17-1-2018.
 */
public class ClickBlockPoint extends ServerTutorialPoint {

    private BukkitTask animationTask;
    private Location clickableBlock;

    private boolean enableParticles;
    private Color particleColor;
    private boolean teleport;

    public ClickBlockPoint(ServerTutorialPlus plugin, Location loc) {
        super(plugin, loc, PointType.CLICK_BLOCK);
        particleColor = new Color(125, 255, 0);
        clickableBlock = loc.clone();
    }

    public ClickBlockPoint(ServerTutorialPlus plugin, Location loc, boolean enableParticles) {
        super(plugin, loc, PointType.CLICK_BLOCK);
        clickableBlock = loc.getBlock().getLocation();
        particleColor = new Color(125, 255, 0);
        this.enableParticles = enableParticles;
    }

    @Override
    public IPlayPoint createPlay(Player player, OldValuesPlayer oldValuesPlayer, IPointCallBack callBack) {
        return new IPlayPoint() {
            @Override
            public void start() {
                playDefault(player, oldValuesPlayer, teleport);

                if (clickableBlock == null) {
                    player.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "SKIPPED: " + ChatColor.RED + "Point has not been setup! Use the command " + ChatColor.YELLOW + "/st editpoint <id> <point> setblock" + ChatColor.RED + " to set the clickable block!");
                    callBack.finish();
                    return;
                }

                plugin.getClickManager().registerClickAction(player.getUniqueId(), new IClickAction() {
                    @Override
                    public void run(PlayerInteractEvent event) {
                        if (event.getClickedBlock() == null) return;
                        if (event.getClickedBlock().getType() == Material.AIR) return;
                        if (event.getHand() == EquipmentSlot.OFF_HAND) return;
                        if (!event.getClickedBlock().getLocation().equals(clickableBlock)) return;

                        event.setCancelled(true);
                        if (enableParticles) {
                            animationTask.cancel();
                        }
                        plugin.getClickManager().removeClickaction(player.getUniqueId());
                        callBack.finish();
                    }

                    @Override
                    public void run(PlayerInteractEntityEvent event) {
                        // No actions need to be taken.
                    }
                });

                if (enableParticles) {
                    animationTask = new BukkitRunnable() {
                        private List<Location> locs = null;

                        @Override
                        public void run() {
                            if (locs == null)
                                locs = PluginUtils.getHollowCube(clickableBlock, clickableBlock.clone().add(new Vector(1, 1, 1)), 0.25);
                            for (Location loc : locs) {
                                plugin.getProtocol().playRedstoneParticle(player, loc, particleColor);
                            }
                        }
                    }.runTaskTimer(plugin, 0, 2);
                }
            }

            @Override
            public void stop() {
                plugin.getClickManager().removeClickaction(player.getUniqueId());
                if (enableParticles) {
                    animationTask.cancel();
                }
            }
        };
    }

    @Override
    protected void saveCustomData(Config tutorialSaves, String key, String i) {
        tutorialSaves.set("tutorials." + key + ".points." + i + ".blocklocation", PluginUtils.fromLocation(clickableBlock));
        tutorialSaves.set("tutorials." + key + ".points." + i + ".particles", enableParticles);
        tutorialSaves.set("tutorials." + key + ".points." + i + ".colour", particleColor.toString());
        tutorialSaves.set("tutorials." + key + ".points." + i + ".teleport", teleport);
    }

    @Override
    protected void readCustomSaveData(Config tutorialSaves, String key, String i) {
        clickableBlock = PluginUtils.fromString(plugin, tutorialSaves.getString("tutorials." + key + ".points." + i + ".blocklocation")).getBlock().getLocation();
        particleColor = Color.fromString(tutorialSaves.getString("tutorials." + key + ".points." + i + ".colour"));
        enableParticles = tutorialSaves.getBoolean("tutorials." + key + ".points." + i + ".particles");
        teleport = tutorialSaves.getBoolean("tutorials." + key + ".points." + i + ".teleport");
    }

    @Override
    public List<PointArg> getArgs() {
        List<PointArg> args = super.getArgs();

        args.add(new PointArg("setblock") {
            @Override
            public boolean run(ServerTutorial serverTutorial, ServerTutorialPoint point, CommandSender sender, String[] args) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(ChatColor.RED + "This is a player only command!");
                    return false;
                }

                Player player = (Player) sender;
                player.sendMessage(ChatColor.YELLOW + "Click any block to set the block. Type " + ChatColor.RED + "/st cancel" + ChatColor.YELLOW + " to cancel the process.");

                plugin.getClickManager().registerClickAction(player.getUniqueId(), new IClickAction() {
                    @Override
                    public void run(PlayerInteractEvent event) {
                        if (event.getClickedBlock() == null) return;
                        if (event.getClickedBlock().getType() == Material.AIR) return;

                        event.setCancelled(true);
                        clickableBlock = event.getClickedBlock().getLocation();
                        player.sendMessage(ChatColor.GREEN + "Successfully set the block!");
                        plugin.getClickManager().removeClickaction(player.getUniqueId());
                    }

                    @Override
                    public void run(PlayerInteractEntityEvent event) {

                    }
                });
                return false;
            }
        });

        args.add(new PointArg("colour", new String[]{"color"}) {
            @Override
            public boolean run(ServerTutorial serverTutorial, ServerTutorialPoint point, CommandSender sender, String[] args) {
                if (args.length < 1) {
                    sender.sendMessage(ChatColor.RED + "Wrong usage. /st editpoint <t> <p> colour <red> <green> <blue> " + ChatColor.GRAY + "(RGB format, MAX is 255)");
                    return false;
                }

                try {
                    int red = Integer.parseInt(args[0]);
                    int green = Integer.parseInt(args[1]);
                    int blue = Integer.parseInt(args[2]);

                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&aTIP&8] &7If you set the values higher then the standard (255) RGB value, it will create a mix of colours!"));

                    particleColor.set(red, green, blue);
                } catch (NumberFormatException ex) {
                    sender.sendMessage(ChatColor.RED + "Invalid number. /st editpoint <t> <p> colour <red> <green> <blue> " + ChatColor.GRAY + "(RGB format, MAX is 255)");
                    return false;
                }

                return true;
            }
        });

        args.add(new PointArg("particles") {
            @Override
            public boolean run(ServerTutorial serverTutorial, ServerTutorialPoint point, CommandSender sender, String[] args) {
                if (args.length < 1) {
                    sender.sendMessage(ChatColor.RED + "Wrong usage. /st editpoint <t> <p> particles <TRUE/FALSE>");
                    return false;
                }

                try {
                    enableParticles = Boolean.valueOf(args[0]);
                } catch (NumberFormatException ex) {
                    sender.sendMessage(ChatColor.RED + "Wrong usage. /st editpoint <t> <p> particles <TRUE/FALSE>");
                    return false;
                }
                return true;
            }
        });

        args.add(new PointArg("teleport", new String[]{"tp"}) {
            @Override
            public boolean run(ServerTutorial serverTutorial, ServerTutorialPoint point, CommandSender sender, String[] args) {
                if (args.length < 1) {
                    sender.sendMessage(ChatColor.RED + "Wrong usage. /st editpoint <t> <p> particles <TRUE/FALSE>");
                    return false;
                }

                try {
                    teleport = Boolean.valueOf(args[0]);
                } catch (NumberFormatException ex) {
                    sender.sendMessage(ChatColor.RED + "Wrong usage. /st editpoint <t> <p> particles <TRUE/FALSE>");
                    return false;
                }
                return true;
            }
        });


        return args;
    }
}
