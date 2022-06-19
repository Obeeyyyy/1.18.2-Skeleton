package de.obey.utils;
/*

    Author - Obey -> Mc-Kingdoms
       14.11.2021 / 18:08

*/


import de.obey.Startup;
import lombok.Getter;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

public class ServerData {

    @Getter
    private JSONObject data;

    public ServerData(){
        loadServerData();
    }

    public void loadServerData(){
        final File file = new File(Startup.getInstance().getDataFolder().getPath() + "/serverData.json");

        if(!file.exists()){
            try {
                file.createNewFile();

                data = new JSONObject();

                data.put("prefix", "&6Kingdoms&7 ");
                data.put("whitelist", true);

                data.put("mysql.host", "mysql-mariadb-19-104.zap-hosting.com");
                data.put("mysql.password", "ORQnlZiwW5W9nCKL");
                data.put("mysql.username", "zap50270-2");
                data.put("mysql.database", "zap50270-2");
                data.put("mysql.port", "3306");
                data.put("mysql.status", true);

                saveServerData();
            } catch (IOException ignored) {}

        }

        if(data == null) {
            try {
                data = Util.loadJSONObject(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveServerData(){
        Startup.getInstance().getExecutorService().submit(() -> {
            final File file = new File(Startup.getInstance().getDataFolder().getPath() + "/serverData.json");

            if(!file.exists()){
                Util.console("§a |> §c§oDid not save ServerData ...");
                return;
            }

            Util.saveJSONObject(data, file);
            Util.console("§a |> §f§oSaved ServerData ...");
        });
    }
}
