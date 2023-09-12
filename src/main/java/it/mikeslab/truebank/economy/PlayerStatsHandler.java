package it.mikeslab.truebank.economy;

import it.mikeslab.truebank.model.PlayerStats;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerStatsHandler {

    private Map<UUID, PlayerStats> uuidPlayerStatsMap;

    public PlayerStatsHandler() {
        this.reInitStatsMap();
    }

    public void reInitStatsMap() {
        this.uuidPlayerStatsMap = new HashMap<>();
    }

    public void update(UUID playerUUID, Statistic statistic, double value) {

        // Gets player's stats from uuidStatsMap, if nothing is found, returns a default all-zero values PlayerStats instance
        PlayerStats playerStats = uuidPlayerStatsMap.getOrDefault(playerUUID, new PlayerStats());

        // Current statistic value
        double currentValue = playerStats.getStatisticMap().getOrDefault(statistic, 0.0);

        // Increment / Decrement specified value
        playerStats.getStatisticMap().put(statistic, currentValue + value);

        // Updates uuidMap values
        uuidPlayerStatsMap.put(playerUUID, playerStats);
    }



}
