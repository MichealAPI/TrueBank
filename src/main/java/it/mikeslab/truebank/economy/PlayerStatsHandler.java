package it.mikeslab.truebank.economy;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerStatsHandler {

    public Map<UUID, Map<Statistic, Double>> uuidPlayerStatsMap;

    public PlayerStatsHandler() {
        this.reInitStatsMap();
    }

    public void reInitStatsMap() {
        this.uuidPlayerStatsMap = new HashMap<>();
    }

    public void update(UUID playerUUID, Statistic statistic, double value) {

        // Gets player's stats from uuidStatsMap, if nothing is found, returns a default all-zero values PlayerStats instance
        Map<Statistic, Double> playerStats = uuidPlayerStatsMap.getOrDefault(playerUUID, new HashMap<>());

        // Current statistic value
        double currentValue = playerStats.getOrDefault(statistic, 0.0);

        // Increment / Decrement specified value
        playerStats.put(statistic, currentValue + value);

        // Updates uuidMap values
        uuidPlayerStatsMap.put(playerUUID, playerStats);
    }



}
