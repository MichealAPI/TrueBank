package it.mikeslab.truebank.listener;

import it.mikeslab.truebank.TrueBank;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class ATMBlockListener implements Listener {

    private final TrueBank instance;

    public ATMBlockListener(TrueBank instance) {
        this.instance = instance;
    }


    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {

        // todo: Check for editor mode

        Location blockLocation = event.getBlock().getLocation();
        instance.getAtmHandler().createATM(blockLocation);

        // todo: inserted message

    }


}
