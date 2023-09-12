package it.mikeslab.truebank.atm;

import it.mikeslab.truebank.TrueBank;
import it.mikeslab.truebank.model.RawLocation;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;

public class ATMHandler {

    private Map<Integer, RawLocation> locationMap;
    private final TrueBank instance;

    public ATMHandler(TrueBank instance) {
        this.instance = instance;

        loadATMs();
    }

    public void loadATMs() {
        this.locationMap = new HashMap<>();

        ConfigurationSection section = instance.getConfig().getConfigurationSection("atms");

        if (section == null || section.getKeys(false).isEmpty()) return;

        for(String key : section.getKeys(false)) {
            loadATM(Integer.parseInt(key), section.getConfigurationSection(key));
        }

    }

    public void loadATM(Integer id, ConfigurationSection section) {
        RawLocation rawLocation = new RawLocation(
                section.getDouble("x"),
                section.getDouble("y"),
                section.getDouble("z"),
                section.getString("world")
        );

        locationMap.put(id, rawLocation);
    }


    public void createATM(Location location) {

        int id = generateUniqueATMId();

        instance.getConfig().set("atms." + id + "world", location.getWorld().getName());
        instance.getConfig().set("atms." + id + "x", location.getBlockX());
        instance.getConfig().set("atms." + id + "y", location.getBlockY());
        instance.getConfig().set("atms." + id + "z", location.getBlockZ());

        instance.saveConfig();

        loadATM(id, instance.getConfig().getConfigurationSection("atms." + id));
    }


    public void deleteATM(int id) {
        locationMap.remove(id);
    }

    public int isATM(Location location) {

        RawLocation clickedRawLocation = RawLocation.fromLocation(location);

        for(Map.Entry<Integer, RawLocation> atmMapEntries : locationMap.entrySet()) {

            if(RawLocation.compare(atmMapEntries.getValue(), clickedRawLocation)) {
                return atmMapEntries.getKey();
            }

        }

        return -1;
    }


    private int generateUniqueATMId() {
        int id = 1; // Start with an initial ID

        // Keep incrementing the ID until it is unique
        while (locationMap.containsKey(id)) {
            id++;
        }

        return id;
    }




}
