package it.mikeslab.truebank.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;

@Data
@RequiredArgsConstructor
public class Icon {

    private final Material material;

    private final String displayName;
    private final List<String> lore;

    private final int customModelData;

    public static Icon fromConfig(ConfigurationSection section) {
        return new Icon(
                Material.getMaterial(section.getString("material")),
                section.getString("displayName"),
                section.getStringList("lore"),
                section.getInt("modelData", -1));
    }



}
