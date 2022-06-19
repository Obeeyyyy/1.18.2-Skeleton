package net.traxfight.backend.utils;
/*

    Author - Obey -> Kingdoms
       29.11.2021 / 17:27

*/


import com.google.common.collect.Maps;
import net.traxfight.backend.Trax;
import org.bukkit.Location;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class LocationData {

    private final Map<String, Location> locations = Maps.newConcurrentMap();
    private JSONObject jsonObject;

    public LocationData(){
        loadLocations();
    }

    public Location getLocation(final String name){
        if(!locations.containsKey(name))
            return null;

        return locations.get(name);
    }

    private void loadLocations(){
        final File file = new File(Trax.getInstance().getDataFolder().getPath() + "/locations.json");

        if(!file.exists()) {
            try {
                file.createNewFile();

                Util.saveJSONObject(new JSONObject(), file);

                Util.console(" §a|> §f§oCreated Locationfile ...");

                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            jsonObject = Util.loadJSONObject(file);
            jsonObject.keySet().forEach(this::loadLocation);
        } catch (IOException e) {}

        Util.console(" §a|> §f§oLoaded Locations ...");
    }

    private void loadLocation(final String key){
        // world#x#y#z#yaw#pitch
        locations.put(key, Util.getLocationFromString(jsonObject.getString(key)));
    }

    public void setLocation(final String name, final Location location){
        locations.put(name, location);
        jsonObject.put(name, Util.getStringFromLocation(location));
        Util.saveJSONObject(jsonObject, new File(Trax.getInstance().getDataFolder().getPath() + "/locations.json"));
    }
}
