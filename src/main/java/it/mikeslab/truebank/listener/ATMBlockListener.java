package it.mikeslab.truebank.listener;

import it.mikeslab.truebank.TrueBank;
import it.mikeslab.truebank.inventory.anvil.PINMenu;
import it.mikeslab.truebank.model.CreditCard;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

public class ATMBlockListener implements Listener {

    private final TrueBank instance;

    public ATMBlockListener(TrueBank instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onBlockInteract(PlayerInteractEvent event) {

        Block clickedBlock = event.getClickedBlock();

        if(clickedBlock == null || clickedBlock.getType().isAir()) return;

        Location location = clickedBlock.getLocation();
        int atmID = instance.getAtmHandler().isATM(location);

        // -1 stands for ATM not found.
        if(atmID == -1) return;

        // Check if item is a valid CreditCard

        ItemStack item = event.getItem();
        ItemMeta meta = item.getItemMeta();

        boolean isCreditCard = instance.getCreditCardHandler().isCreditCard(item);
        if(!isCreditCard) return;

        NamespacedKey holderUUIDKey = instance.getCreditCardHandler().holderUUIDKey;
        NamespacedKey cardNumberKey = instance.getCreditCardHandler().cardNumberKey;

        String itemHolderUUID = meta.getPersistentDataContainer().get(holderUUIDKey, PersistentDataType.STRING);
        String itemCardNumber = meta.getPersistentDataContainer().get(cardNumberKey, PersistentDataType.STRING);

        UUID holderUUID = UUID.fromString(itemHolderUUID);

        CreditCard holderCreditCard = instance.getAccountHandler().fromUUID(holderUUID);
        String validCardNumber = holderCreditCard.getCardNumber();

        // Checks whether the saved database holder's card number is the same as that found in the credit card ItemStack.
         if(!itemCardNumber.equals(validCardNumber)) return;

        new PINMenu(instance).showPinGui(event.getPlayer(), Bukkit.getOfflinePlayer(holderUUID), holderCreditCard.getSecurityPIN());
    }







}
