package nl.martenm.servertutorialplus.data;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import nl.martenm.servertutorialplus.ServerTutorialPlus;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author MartenM
 * @since 24-12-2017.
 */
public class MySqlDataSource implements DataSource {

    private ServerTutorialPlus plugin;
    private HikariConfig config = new HikariConfig();
    private HikariDataSource mySql;

    public MySqlDataSource(ServerTutorialPlus plugin) {
        this.plugin = plugin;
        setup();
    }


    public boolean setup() {
        String host = plugin.getConfig().getString("datasource.mysql.host");
        String database = plugin.getConfig().getString("datasource.mysql.database");
        int port = plugin.getConfig().getInt("datasource.mysql.port");

        config = new HikariConfig();
        config.setUsername(plugin.getConfig().getString("datasource.mysql.username"));
        config.setPassword(plugin.getConfig().getString("datasource.mysql.password"));

        config.setJdbcUrl(String.format("jdbc:mysql://%s:%s/%s", host, port, database));

        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        mySql = new HikariDataSource(config);

        plugin.getLogger().info("Creating Tutorial_Players table.");
        if (!simpleSqlUpdate("CREATE TABLE IF NOT EXISTS Tutorial_Players " +
                "(uuid VARCHAR(64) not NULL, " +
                " tutorial VARCHAR(255), " +
                " PRIMARY KEY ( uuid, tutorial))")) {
            return false;
        }

        return true;
    }

    public boolean simpleSqlUpdate(String sql) {
        Connection connection = null;
        Statement statement = null;

        try {
            connection = mySql.getConnection();

            statement = connection.createStatement();
            statement.executeUpdate(sql);

        } catch (Exception ex) {
            plugin.getLogger().warning("[!!!] Error while performing an SQL query!");
            ex.printStackTrace();
            return false;
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    @Override
    public List<String> getPlayedTutorials(UUID uuid) {
        List<String> tutorials = new ArrayList<>();

        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;

        try {
            connection = mySql.getConnection();
            statement = connection.createStatement();

            result = statement.executeQuery("select distinct tutorial from Tutorial_Players where uuid='" + uuid + "'");

            while (result.next()) {
                tutorials.add(result.getString(1));
            }

        } catch (Exception ex) {
            plugin.getLogger().warning("[!!!] An error occurred while to get a players played tutorials...");
            ex.printStackTrace();
            return null;
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (result != null) {
                try {
                    result.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return tutorials;
    }

    @Override
    public boolean addPlayedTutorial(UUID uuid, String id) {
        return simpleSqlUpdate("insert into Tutorial_Players (uuid, tutorial) VALUES " + String.format("('%s', '%s')", uuid, id));
    }

    @Override
    public boolean removePlayedTutorial(UUID uuid, String id) {
        return simpleSqlUpdate("delete from tutorial_players where uuid='" + uuid + "' AND tutorial='" + id + "'");
    }

    @Override
    public boolean hasPlayedTutorial(UUID uuid, String id) {
        List<String> tutorials = getPlayedTutorials(uuid);
        if (tutorials == null) {
            //Replay later for the rewards!
            return true;
        }

        return tutorials.contains(id);
    }
}
