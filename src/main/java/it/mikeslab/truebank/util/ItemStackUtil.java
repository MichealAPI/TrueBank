package it.mikeslab.truebank.util;

import it.mikeslab.truebank.model.Icon;
import it.mikeslab.truebank.util.color.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ItemStackUtil {


    public static ItemStack fromIcon(Icon icon) {
        return create(icon.getMaterial(), icon.getDisplayName(), icon.getLore(), icon.getCustomModelData());
    }



    public static ItemStack create(Material material, String displayName, List<String> lore) {

        ItemStack itemStack = new ItemStack(material);
        ItemMeta meta = itemStack.getItemMeta();

        meta.setDisplayName(ChatColor.color(displayName));
        meta.setLore(ChatColor.color(lore));

        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public static ItemStack create(Material material, String displayName, List<String> lore, int modelData) {

        ItemStack itemStack = create(material, displayName, lore);
        ItemMeta meta = itemStack.getItemMeta();

        meta.setCustomModelData(modelData);

        itemStack.setItemMeta(meta);
        return itemStack;
    }


    public static ItemStack getFiller(Material material) {

        ItemStack itemStack = new ItemStack(material);
        ItemMeta meta = itemStack.getItemMeta();

        meta.setDisplayName(" ");

        itemStack.setItemMeta(meta);

        return itemStack;
    }





}
