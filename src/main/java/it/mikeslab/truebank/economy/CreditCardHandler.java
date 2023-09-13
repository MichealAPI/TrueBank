package it.mikeslab.truebank.economy;

import it.mikeslab.truebank.TrueBank;
import it.mikeslab.truebank.model.CardType;
import it.mikeslab.truebank.model.CreditCard;
import it.mikeslab.truebank.util.ItemStackUtil;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

public class CreditCardHandler {

    private final TrueBank instance;
    public final NamespacedKey
            cardNumberKey,
            holderUUIDKey;

    public CreditCardHandler(TrueBank instance) {
        this.instance = instance;

        this.cardNumberKey = new NamespacedKey(instance, "creditcard");
        this.holderUUIDKey = new NamespacedKey(instance, "holderuuid");
    }


    public ItemStack generateCreditCard(CreditCard creditCard, UUID holderUUID) {

        int cardTypeID = creditCard.getCardTypeID();
        CardType cardType = instance.getCardTypeHandler().cardTypeMap.get(cardTypeID);

        ItemStack itemStack = ItemStackUtil.fromIcon(cardType.getIcon());
        ItemMeta meta = itemStack.getItemMeta();

        meta.getPersistentDataContainer().set(holderUUIDKey, PersistentDataType.STRING, holderUUID + "");
        meta.getPersistentDataContainer().set(cardNumberKey, PersistentDataType.STRING, creditCard.getCardNumber());

        itemStack.setItemMeta(meta);

        return itemStack;
    }



    public boolean isCreditCard(ItemStack itemStack) {

        if(itemStack == null || itemStack.getItemMeta() == null) return false;

        ItemMeta meta = itemStack.getItemMeta();

        boolean hasHolderUUID = meta.getPersistentDataContainer().has(holderUUIDKey, PersistentDataType.STRING);
        boolean hasCardNumber = meta.getPersistentDataContainer().has(cardNumberKey, PersistentDataType.STRING);

        return hasCardNumber && hasHolderUUID;
    }










}
