package it.mikeslab.truebank.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;

import java.util.List;

@Data
@RequiredArgsConstructor
public class Icon {

    private final Material material;

    private final String displayName;
    private final List<String> lore;

    private final int customModelData;

}
