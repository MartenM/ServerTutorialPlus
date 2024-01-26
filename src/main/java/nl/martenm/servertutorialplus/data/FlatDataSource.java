package nl.martenm.servertutorialplus.data;

import nl.martenm.servertutorialplus.ServerTutorialPlus;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author MartenM
 * @since 24-12-2017.
 */
public class FlatDataSource implements DataSource {

    private ServerTutorialPlus plugin;
    public FlatDataSource(ServerTutorialPlus plugin){
        this.plugin = plugin;
    }

    @Override
    public List<String> getPlayedTutorials(UUID uuid) {
        return (List<String>) getDataFromUUID(uuid).getOrDefault("tutorials", new ArrayList<>());
    }

    @Override
    public String getQuitTutorial(UUID uuid) {
        return (String) getDataFromUUID(uuid).get("quit_tutorial");
    }

    private JSONObject getDataFromUUID(UUID uuid) {
        File hostlocation = new File(plugin.getDataFolder() + "/data/playerdata");
        hostlocation.mkdirs();

        JSONObject data = new JSONObject();

        File file = new File(plugin.getDataFolder() + "/data/playerdata/" + uuid + ".json");
        if(file.exists()) {
            JSONParser parser = new JSONParser();
            FileReader reader = null;

            try {
                reader = new FileReader(file.getPath());
                Object obj = parser.parse(reader);
                data = (JSONObject) obj;
            } catch (Exception ex) {
                ex.printStackTrace();

            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return data;
    }

    @Override
    public boolean addPlayedTutorial(UUID uuid, String id) {
        List<String> played = getPlayedTutorials(uuid);
        played.add(id);
        return changeDataInFile(uuid, "tutorials", played);
    }

    @Override
    public void setQuitTutorial(UUID uuid, String id) {
        changeDataInFile(uuid, "quit_tutorial", id);
    }

    public boolean changeDataInFile(UUID uuid, String key, Object value) {
        JSONObject data = getDataFromUUID(uuid);
        if (value == null) {
            data.remove(key);
        } else {
            data.put(key, value);
        }

        File file = new File(plugin.getDataFolder() + "/data/playerdata/" + uuid + ".json");

        FileWriter writer = null;
        try{
            writer = new FileWriter(file);
            writer.write(data.toJSONString());
        } catch (Exception ex){
            ex.printStackTrace();
            return false;
        } finally {
            if(writer != null){
                try {
                    writer.flush();
                    writer.close();
                } catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        }

        return true;
    }

    @Override
    public boolean removePlayedTutorial(UUID uuid, String id) {
        List<String> played = getPlayedTutorials(uuid);
        played.remove(id);
        return changeDataInFile(uuid, "tutorials", played);
    }

    @Override
    public boolean removeQuitTutorial(UUID uuid) {
        return changeDataInFile(uuid, "quit_tutorial", null);
    }

    @Override
    public boolean hasPlayedTutorial(UUID uuid, String id) {
        return getPlayedTutorials(uuid).contains(id);
    }
}
