package it.mikeslab.truebank.util;

import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

@UtilityClass
public class PlayerUtil {

    public boolean give(Player player, ItemStack itemStack) {

        if(!hasAvailableSlot(player)) {
            return false;
        }

        player.getInventory().addItem(itemStack);
        return true;
    }


    public boolean hasAvailableSlot(Player player){
        Inventory inv = player.getInventory();
        for (ItemStack item: inv.getContents()) {
            if(item == null) {
                return true;
            }
        }
        return false;
    }



}
