package it.mikeslab.truebank.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;

@Data
@RequiredArgsConstructor
public class RawLocation {

    private final double x, y, z;
    private final String worldName;

    public static RawLocation fromLocation(Location location) {
        return new RawLocation(
                location.getBlockX(),
                location.getBlockY(),
                location.getBlockZ(),
                location.getWorld().getName()
        );
    }


    public static boolean compare(RawLocation raw1, RawLocation raw2) {
        return raw1.equals(raw2);
    }


}
