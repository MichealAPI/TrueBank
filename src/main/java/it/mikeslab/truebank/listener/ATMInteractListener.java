package it.mikeslab.truebank.listener;

import it.mikeslab.truebank.TrueBank;
import it.mikeslab.truebank.inventory.anvil.PINMenu;
import it.mikeslab.truebank.model.CreditCard;
import it.mikeslab.truebank.model.Transaction;
import it.mikeslab.truebank.util.language.LangKey;
import it.mikeslab.truebank.util.language.Language;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

public class ATMInteractListener implements Listener {

    private final TrueBank instance;

    public ATMInteractListener(TrueBank instance) {
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

        boolean isCreditCard = instance.getCreditCardHandler().isCreditCard(item);
        if(isCreditCard) {
            processCreditCard(event);
            event.setCancelled(true);

            return;
        }

        boolean isBanknote = instance.getBanknoteHandler().isBanknote(item);
        if(isBanknote) {
            processBanknote(event);
            event.setCancelled(true);
        }

    }


    private void processBanknote(PlayerInteractEvent event) {

        Player subject = event.getPlayer();
        ItemStack itemStack = event.getItem();

        boolean hasAccount = instance.getAccountHandler().fromUUID(subject.getUniqueId()) != null;

        if(!hasAccount) {
            // todo: send message
            return;
        }

        ItemMeta meta = itemStack.getItemMeta();
        NamespacedKey banknoteNsK = instance.getBanknoteHandler().banknoteNamespacedKey;
        double banknoteValue = meta.getPersistentDataContainer().get(banknoteNsK, PersistentDataType.DOUBLE);

        int itemAmount = event.getItem().getAmount() - 1;
        event.getItem().setAmount(itemAmount);

        // Deposit

        instance.getEconomy().depositPlayer(subject, banknoteValue);
        // todo message & sound deposited

        // Transaction

        Transaction transaction = new Transaction(
                Language.getString(LangKey.TRANSACTION_BANKNOTE_DEPOSIT, false),
                banknoteValue,
                Language.getString(LangKey.TRANSACTION_BANK, false),
                subject.getName()
        );

        instance.getTransactionsHandler().insertTransaction(transaction);


    }


    private void processCreditCard(PlayerInteractEvent event) {

        ItemStack creditCardItem = event.getItem();
        Player subject = event.getPlayer();

        NamespacedKey holderUUIDKey = instance.getCreditCardHandler().holderUUIDKey;
        NamespacedKey cardNumberKey = instance.getCreditCardHandler().cardNumberKey;

        ItemMeta meta = creditCardItem.getItemMeta();

        String itemHolderUUID = meta.getPersistentDataContainer().get(holderUUIDKey, PersistentDataType.STRING);
        String itemCardNumber = meta.getPersistentDataContainer().get(cardNumberKey, PersistentDataType.STRING);

        UUID holderUUID = UUID.fromString(itemHolderUUID);

        CreditCard holderCreditCard = instance.getAccountHandler().fromUUID(holderUUID);
        String validCardNumber = holderCreditCard.getCardNumber();

        // Checks whether the saved database holder's card number is the same as that found in the credit card ItemStack.
        if(!itemCardNumber.equals(validCardNumber)) return;

        new PINMenu(instance).showPinGui(subject, Bukkit.getOfflinePlayer(holderUUID), holderCreditCard.getSecurityPIN());
    }







}
