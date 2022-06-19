package de.obey;
/*

    Author - Obey -> Mc-Kingdoms
       19.06.2022 / 01:19

*/

import de.obey.database.MySQL;
import de.obey.utils.ServerData;
import de.obey.utils.Util;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Startup extends JavaPlugin {

    @Getter
    private ExecutorService executorService = Executors.newCachedThreadPool();
    @Getter
    private ServerData serverData;
    @Getter
    private MySQL mySQL;

    @Override
    public void onEnable() {
        createDataFolder();
        loadServerData();
        loadMySQL();
    }

    @Override
    public void onDisable() {
        serverData.saveServerData();
    }

    private void createDataFolder(){
        if(!getDataFolder().exists()) {
            getDataFolder().mkdir();
            Util.console("§a |> §f§oCreated DataFolder");
        }
    }

    private void loadServerData(){
        serverData = new ServerData();
        Util.console( "§a |> §f§oLoaded ServerData ...");
    }

    private void loadMySQL(){
        mySQL = new MySQL(serverData.getData());
    }

    public static Startup getInstance(){
        return getPlugin(Startup.class);
    }
}
