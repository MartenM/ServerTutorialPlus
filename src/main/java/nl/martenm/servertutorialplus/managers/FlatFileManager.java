package nl.martenm.servertutorialplus.managers;

import com.google.gson.JsonParser;
import nl.martenm.servertutorialplus.ServerTutorialPlus;
import nl.martenm.servertutorialplus.helpers.PluginUtils;
import nl.martenm.servertutorialplus.helpers.dataholders.OldValuesPlayer;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.UUID;

/**
 * Created by Marten on 7-6-2017.
 */
@SuppressWarnings("ALL")
public class FlatFileManager {

    public static JSONObject getPlayerData(ServerTutorialPlus plugin, UUID uuid) {
        File hostlocation = new File(plugin.getDataFolder() + "/playerdata");
        hostlocation.mkdirs();

        File file = new File(plugin.getDataFolder() + "/playerdata/" + uuid + ".json");
        if (file.exists()) {
            JsonParser parser = new JsonParser();
            JSONObject data = null;
            try {
                FileReader reader = new FileReader(file.getPath());
                Object obj = parser.parse(reader);
                data = (JSONObject) obj;
                reader.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
            return data;
        } else {
            //Nothing we only get data that exists ;p
        }
        return null;
    }

    public static void setPlayerData(ServerTutorialPlus plugin, Player player, JSONObject object) {
        if (object == null) return;
        new BukkitRunnable() {
            @Override
            public void run() {
                plugin.getLogger().info("Restoring player status for player: " + player.getName());
                Double dd = (Double) object.get("walkspeed");
                player.setWalkSpeed(dd.floatValue());
                player.setAllowFlight((Boolean) object.get("isAllowedFlight"));
                player.setFlying((boolean) object.get("isFlying"));
                Double ff = (Double) object.get("flyspeed");
                player.setFlySpeed(ff.floatValue());
                player.teleport(PluginUtils.fromString(plugin, (String) object.get("location")));
                player.setGameMode(GameMode.valueOf(object.get("gamemode").toString()));
            }
        }.runTask(plugin);
    }

    public static void deleteFile(ServerTutorialPlus plugin, UUID uuid) {
        File file = new File(plugin.getDataFolder() + "/playerdata/" + uuid + ".json");
        if (file.exists()) {
            file.delete();
        } else {
            System.out.println("[Server Tutorial Plus] Error, file not found.");
        }
    }

    public static void saveJson(ServerTutorialPlus plugin, OldValuesPlayer info) {
        File hostlocation = new File(plugin.getDataFolder() + "/playerdata");
        hostlocation.mkdirs();

        JSONObject data = new JSONObject();
        data.put("isFlying", info.getFlying());
        data.put("isAllowedFlight", info.isAllowFlight());
        data.put("flyspeed", info.getOriginal_flySpeed());
        data.put("walkspeed", info.getOriginal_walkSpeed());
        data.put("location", PluginUtils.fromLocation(info.getLoc()));
        data.put("gamemode", info.getGamemode().toString());

        File file = new File(plugin.getDataFolder() + "/playerdata/" + info.getUuid() + ".json");

        FileWriter writer = null;
        try {
            writer = new FileWriter(file);
            writer.write(data.toJSONString());
            System.out.println("[Server Tutorial Plus] A player left while in the tutorial. Old data has been saved.");
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.flush();
                    writer.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
