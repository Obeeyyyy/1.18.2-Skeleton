package de.obey.database;
/*

    Author - Obey -> Mc-Kingdoms
       19.06.2022 / 01:30

*/

import com.zaxxer.hikari.HikariDataSource;
import de.obey.utils.Util;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQL {

    @Getter
    private final HikariDataSource hikariDataSource;
    @Getter
    private boolean connected = false;
    private Connection connection;

    public MySQL(final JSONObject jsonObject){
        hikariDataSource = new HikariDataSource();

        if(!jsonObject.has("mysql.status")){

            jsonObject.put("mysql.status", false);
            jsonObject.put("mysql.username", "changeme");
            jsonObject.put("mysql.password", "changeme");
            jsonObject.put("mysql.database", "changeme");
            jsonObject.put("mysql.host", "changeme");
            jsonObject.put("mysql.port", "3306");

            Util.console("§a |> §f§oMySQL bitte daten angeben.");

            return;
        }

        if(!jsonObject.getBoolean("mysql.status")) {
            Util.console("§a |> §c§oMySQL - deaktiviert.");
            return;
        }

        hikariDataSource.setUsername(jsonObject.getString("mysql.username"));
        hikariDataSource.setPassword(jsonObject.getString("mysql.password"));
        hikariDataSource.setJdbcUrl("jdbc:mysql://" + jsonObject.getString("mysql.host") + ":" + jsonObject.getString("mysql.port") + "/" + jsonObject.getString("mysql.database") + "?autoReconnect=true");

        try {
            connection = hikariDataSource.getConnection();
            connected = true;
            Util.console("§a |> §f§oMySQL Verbindung wurde aufgebaut.");
        } catch (SQLException throwables) {
            Util.console("§4 !> §c§oMySQL Verbindung konnte nicht aufgebaut werden.");
        }

        if(connection == null)
            return;
    }

    public void execute(String update) {

        checkConnection();

        try {
            final Statement statement = connection.createStatement();

            statement.execute(update);
            statement.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public ResultSet getResultSet(String update){

        checkConnection();

        try {
            final Statement statement = connection.createStatement();

            return statement.executeQuery(update);
        } catch (SQLException throwables) {

            Util.console("§4ERROR <§f§oMYSQL> §c§o" + throwables.getMessage());

            Bukkit.getConsoleSender().sendMessage("§4ERROR : ResultSet kann nicht returnt werden");
        }

        return null;
    }

    public int getRows(final String table){
        final ResultSet resultSet = getResultSet("SELECT COUNT(*) FROM " + table);

        try {
            resultSet.next();
            return resultSet.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public String getString(final String table, final String what, final String type, final String typeValue) {

        checkConnection();

        try {
            final ResultSet resultSet = getResultSet("SELECT * FROM " + table + " WHERE " + type + "= '" + typeValue + "'");

            while (resultSet.next())
                return resultSet.getString(what);

            resultSet.close();

        } catch (SQLException ignored) {}

        return "null";
    }

    public long getLong(final String table, final String what, final String type, final String typeValue) {

        checkConnection();

        try {
            final ResultSet resultSet = getResultSet("SELECT * FROM " + table + " WHERE " + type + "= '" + typeValue + "'");

            while (resultSet.next())
                return resultSet.getLong(what);

            resultSet.close();

        } catch (SQLException ignored) {}

        return -111;
    }

    public double getDouble(final String table, final String what, final String type, final String typeValue) {

        checkConnection();

        try {
            final ResultSet resultSet = getResultSet("SELECT * FROM " + table + " WHERE " + type + "= '" + typeValue + "'");

            while (resultSet.next())
                return resultSet.getDouble(what);

        } catch (SQLException ignored) {}

        return -111;
    }

    private void checkConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = hikariDataSource.getConnection();
            } else {
                final Statement statement = connection.createStatement();
                final ResultSet resultSet = statement.executeQuery("SELECT 1");

                statement.close();

                if(resultSet != null)
                    resultSet.close();
            }
        } catch (SQLException throwables) {
            try {
                connection = hikariDataSource.getConnection();
            } catch (SQLException ignored) {}
        }
    }

}
