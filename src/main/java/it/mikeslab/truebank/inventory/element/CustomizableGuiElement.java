package it.mikeslab.truebank.inventory.element;

import de.themoep.inventorygui.StaticGuiElement;
import it.mikeslab.truebank.util.color.ChatColor;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class CustomizableGuiElement {
    private final FileConfiguration menuConfiguration;

    public StaticGuiElement create(Character character, String inventoryName, String elementName) {
        ConfigurationSection section = menuConfiguration.getConfigurationSection(inventoryName + "." + elementName);
        Material material = Material.valueOf(section.getString("material"));
        ItemStack itemStack = new ItemStack(material);

        return createElement(character, itemStack, section);
    }

    private StaticGuiElement createElement(Character character, ItemStack itemStack, ConfigurationSection section) {
        List<String> meta = new ArrayList<>();
        String displayName = ChatColor.color(section.getString("displayName"));
        List<String> lore = ChatColor.color(section.getStringList("lore"));

        meta.add(displayName);
        meta.addAll(lore);

        return new StaticGuiElement(character, itemStack, meta.toArray(new String[0]));
    }

}
