package it.mikeslab.truebank.model;

import it.mikeslab.truebank.economy.Statistic;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class PlayerStats {

    private final Map<Statistic, Double> statisticMap;

    public PlayerStats() {
        this.statisticMap = new HashMap<>();
    }


}
