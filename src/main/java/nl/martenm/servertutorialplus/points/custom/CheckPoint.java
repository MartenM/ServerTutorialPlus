package nl.martenm.servertutorialplus.points.custom;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import nl.martenm.servertutorialplus.ServerTutorialPlus;
import nl.martenm.servertutorialplus.helpers.Color;
import nl.martenm.servertutorialplus.helpers.Config;
import nl.martenm.servertutorialplus.helpers.PluginUtils;
import nl.martenm.servertutorialplus.helpers.dataholders.OldValuesPlayer;
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
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.List;

/**
 * @author MartenM
 * @since 25-11-2017.
 */
public class CheckPoint extends ServerTutorialPoint {

    private AnimationType animationType;
    private Color color;
    private Color guideColor;
    private double distance;
    private boolean repeatActionbar;
    private boolean drawGuide;

    public CheckPoint(ServerTutorialPlus plugin, Location loc) {
        super(plugin, loc, PointType.CHECKPOINT);
        animationType = AnimationType.CIRCLE;
        color = new Color(244, 153, 0);
        guideColor = new Color(244, 244, 244);
        repeatActionbar = true;
        distance = 1;
    }

    @Override
    public IPlayPoint createPlay(Player player, OldValuesPlayer oldValuesPlayer, IPointCallBack callBack) {
        return new IPlayPoint() {

            BukkitTask checker;

            @Override
            public void start() {
                playDefault(player, oldValuesPlayer, false);
                checker = new BukkitRunnable() {
                    int count;

                    @Override
                    public void run() {
                        if (loc.getWorld() == player.getWorld()) {
                            if (loc.distance(player.getLocation()) < distance) {
                                stop();
                                callBack.finish();
                            }

                            switch (animationType) {
                                case CIRCLE:
                                    for (int i = 0; i < 20; i++) {
                                        double radians = Math.toRadians(18 * i);
                                        double x = Math.sin(radians) * 0.7;
                                        double z = Math.cos(radians) * 0.7;

                                        for (int d = 0; d < 5; d++) {
                                            plugin.getProtocol().playRedstoneParticle(player, new Location(
                                                            loc.getWorld(),
                                                            loc.getX() + x,
                                                            loc.getY() + count * 0.1,
                                                            loc.getZ() + z),
                                                    color);

                                        }
                                    }

                                    if (count > 20) count = 0;
                                    else count++;
                                    break;
                                case LINE:
                                    for (int y = 0; y < 20; y++) {
                                        plugin.getProtocol().playRedstoneParticle(player, new Location(
                                                        loc.getWorld(),
                                                        loc.getX(),
                                                        loc.getY() + y * 0.1, loc.getZ()),
                                                color);
                                    }
                                    break;
                            }

                            if (drawGuide) {
                                Location playerLocation = player.getLocation();
                                Vector direction = loc.toVector().subtract(playerLocation.toVector());
                                direction.normalize();

                                for (double i = 2.0; i < 3; i += 0.1) {
                                    direction.multiply(i);
                                    playerLocation.add(direction);

                                    for (double a = 0; a < 2; a++) {
                                        double y = playerLocation.getY();

                                        for (int attempts = 0; attempts < 5; attempts++) {
                                            if (playerLocation.getWorld().getBlockAt(playerLocation.getBlockX(), (int) y, playerLocation.getBlockZ()).getType() != Material.AIR) {
                                                if (playerLocation.getWorld().getBlockAt(playerLocation.getBlockX(), (int) y + attempts, playerLocation.getBlockZ()).getType() == Material.AIR) {
                                                    y = y + attempts;
                                                    break;
                                                }
                                            }
                                        }

                                        plugin.getProtocol().playRedstoneParticle(player, new Location(loc.getWorld(),
                                                        playerLocation.getX(),
                                                        y + 0.1,
                                                        playerLocation.getZ()),
                                                color);
                                    }

                                    playerLocation.subtract(direction);
                                    direction.normalize();
                                }
                            }
                        }

                        if (repeatActionbar) {
                            if (message_actionBar != null) {
                                //NeedsReflection.sendActionBar(player, PluginUtils.replaceVariables(plugin.placeholderAPI, player, message_actionBar));
                                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(PluginUtils.replaceVariables(plugin.placeholderAPI, player, message_actionBar)));
                            }
                        }
                    }

                }.runTaskTimer(plugin, 2, 2);
            }

            @Override
            public void stop() {
                if (checker != null) {
                    checker.cancel();
                }
            }
        };
    }

    @Override
    protected void saveCustomData(Config tutorialSaves, String key, String i) {
        tutorialSaves.set("tutorials." + key + ".points." + i + ".animation", animationType.toString());
        tutorialSaves.set("tutorials." + key + ".points." + i + ".colour", color.toString());
        tutorialSaves.set("tutorials." + key + ".points." + i + ".distance", distance);
        tutorialSaves.set("tutorials." + key + ".points." + i + ".repeatActionbar", repeatActionbar);
        tutorialSaves.set("tutorials." + key + ".points." + i + ".drawguide", drawGuide);
        tutorialSaves.set("tutorials." + key + ".points." + i + ".guidecolour", guideColor.toString());
    }

    @Override
    protected void readCustomSaveData(Config tutorialSaves, String key, String i) {
        this.animationType = AnimationType.valueOf(tutorialSaves.getString("tutorials." + key + ".points." + i + ".animation"));
        color = Color.fromString(tutorialSaves.getString("tutorials." + key + ".points." + i + ".colour"));
        distance = tutorialSaves.getDouble("tutorials." + key + ".points." + i + ".distance");
        repeatActionbar = tutorialSaves.getBoolean("tutorials." + key + ".points." + i + ".repeatActionbar");

        drawGuide = tutorialSaves.getBoolean("tutorials." + key + ".points." + i + ".drawguide");
        guideColor = Color.fromString(tutorialSaves.getString("tutorials." + key + ".points." + i + ".guidecolour"));
    }

    @Override
    public List<PointArg> getArgs() {
        List<PointArg> args = super.getArgs();

        // Remove these as they can cause issues when configured.
        args.removeIf(arg -> arg.getName().equalsIgnoreCase("lockplayer"));
        args.removeIf(arg -> arg.getName().equalsIgnoreCase("lockview"));

        args.add(new PointArg("animation") {
            @Override
            public boolean run(ServerTutorial serverTutorial, ServerTutorialPoint point, CommandSender sender, String[] args) {
                String animations = "";
                for (AnimationType type : AnimationType.values()) {
                    animations += type.toString() + ", ";
                }
                animations = animations.substring(0, animations.length() - 2);

                if (args.length < 1) {
                    sender.sendMessage(ChatColor.RED + "Wrong usage. /st editpoint <t> <p> animation " + ChatColor.GRAY + "<" + animations + ">");
                    return false;
                }

                try {
                    animationType = AnimationType.valueOf(args[0]);
                    return true;
                } catch (Exception ex) {
                    plugin.getLogger().warning("Invalid animation type. Using default (TIMED) instead.");
                }

                sender.sendMessage(ChatColor.RED + "Wrong usage. /st editpoint <t> <p> animation " + ChatColor.GRAY + "<" + animations + ">");
                return false;
            }
        });

        args.add(new PointArg("distance") {
            @Override
            public boolean run(ServerTutorial serverTutorial, ServerTutorialPoint point, CommandSender sender, String[] args) {
                if (args.length < 1) {
                    sender.sendMessage(ChatColor.RED + "Wrong usage. /st editpoint <t> <p> distance <value>");
                    return false;
                }

                try {
                    distance = Double.parseDouble(args[0]);
                } catch (NumberFormatException ex) {
                    sender.sendMessage(ChatColor.RED + "Invalid number. /st editpoint <t> <p> distance <value>");
                    return false;
                }

                return true;
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

                    color.set(red, green, blue);
                } catch (NumberFormatException ex) {
                    sender.sendMessage(ChatColor.RED + "Invalid number. /st editpoint <t> <p> colour <red> <green> <blue> " + ChatColor.GRAY + "(RGB format, MAX is 255)");
                    return false;
                }

                return true;
            }
        });

        args.add(new PointArg("guidecolour", new String[]{"guidecolor"}) {
            @Override
            public boolean run(ServerTutorial serverTutorial, ServerTutorialPoint point, CommandSender sender, String[] args) {
                if (args.length < 1) {
                    sender.sendMessage(ChatColor.RED + "Wrong usage. /st editpoint <t> <p> guidecolour <red> <green> <blue> " + ChatColor.GRAY + "(RGB format, MAX is 255)");
                    return false;
                }

                try {
                    int red = Integer.parseInt(args[0]);
                    int green = Integer.parseInt(args[1]);
                    int blue = Integer.parseInt(args[2]);

                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&aTIP&8] &7If you set the values higher then the standard (255) RGB value, it will create a mix of colours!"));

                    guideColor.set(red, green, blue);
                } catch (NumberFormatException ex) {
                    sender.sendMessage(ChatColor.RED + "Invalid number. /st editpoint <t> <p> guidecolor <red> <green> <blue> " + ChatColor.GRAY + "(RGB format, MAX is 255)");
                    return false;
                }

                return true;
            }
        });

        args.add(new PointArg("repeatActionbar") {
            @Override
            public boolean run(ServerTutorial serverTutorial, ServerTutorialPoint point, CommandSender sender, String[] args) {
                if (args.length < 1) {
                    sender.sendMessage(ChatColor.RED + "Wrong usage. /st editpoint <t> <p> repeatActionbar <TRUE/FALSE>");
                    return false;
                }

                try {
                    repeatActionbar = Boolean.valueOf(args[0]);
                } catch (NumberFormatException ex) {
                    sender.sendMessage(ChatColor.RED + "Wrong usage. /st editpoint <t> <p> repeatActionbar <TRUE/FALSE>");
                    return false;
                }
                return true;
            }
        });

        args.add(new PointArg("drawguide") {
            @Override
            public boolean run(ServerTutorial serverTutorial, ServerTutorialPoint point, CommandSender sender, String[] args) {
                if (args.length < 1) {
                    sender.sendMessage(ChatColor.RED + "Wrong usage. /st editpoint <t> <p> drawguider <TRUE/FALSE>");
                    return false;
                }

                try {
                    drawGuide = Boolean.valueOf(args[0]);
                } catch (NumberFormatException ex) {
                    sender.sendMessage(ChatColor.RED + "Wrong usage. /st editpoint <t> <p> drawguider <TRUE/FALSE>");
                    return false;
                }
                return true;
            }
        });

        return args;
    }

    public AnimationType getAnimationType() {
        return animationType;
    }

    public void setAnimationType(AnimationType animationType) {
        this.animationType = animationType;
    }

    public boolean hasDrawGuider() {
        return drawGuide;
    }

    public void setDrawGuide(boolean drawGuide) {
        this.drawGuide = drawGuide;
    }

    public enum AnimationType {
        CIRCLE,
        LINE
    }
}
