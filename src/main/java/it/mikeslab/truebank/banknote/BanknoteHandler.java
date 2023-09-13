package it.mikeslab.truebank.banknote;

import it.mikeslab.truebank.TrueBank;
import it.mikeslab.truebank.model.Banknote;
import it.mikeslab.truebank.model.Icon;
import it.mikeslab.truebank.util.ItemStackUtil;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class BanknoteHandler {

    private final NamespacedKey banknoteNamespacedKey;
    private final TrueBank instance;
    public List<Banknote> banknoteMap;


    public BanknoteHandler(TrueBank instance) {
        this.instance = instance;

        this.banknoteNamespacedKey = new NamespacedKey(instance, "banknote");

        loadBanknotes();
    }


    public void loadBanknotes() {
        this.banknoteMap = new ArrayList<>();

        ConfigurationSection section = instance.getConfig().getConfigurationSection("banknotes");

        if (section == null || section.getKeys(false).isEmpty()) return;

        for(String key : section.getKeys(false)) {
            loadBanknote(section.getConfigurationSection(key));
        }


        // Sorting by ascending order
        banknoteMap.sort(Comparator.comparingDouble(Banknote::getValue));
    }


    private void loadBanknote(ConfigurationSection section) {

        Icon icon = Icon.fromConfig(section.getConfigurationSection("icon"));
        double value = section.getDouble("value");

        banknoteMap.add(new Banknote(value, icon));
    }


    public ItemStack fromBanknote(Banknote banknote) {
        ItemStack itemStack = ItemStackUtil.fromIcon(banknote.getIcon());
        ItemMeta meta = itemStack.getItemMeta();

        meta.getPersistentDataContainer().set(banknoteNamespacedKey, PersistentDataType.DOUBLE, banknote.getValue());

        itemStack.setItemMeta(meta);

        return itemStack;
    }


    public boolean isBanknote(ItemStack itemStack) {

        if(itemStack == null || itemStack.getItemMeta() == null) return false;

        ItemMeta meta = itemStack.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();

        return pdc.has(banknoteNamespacedKey, PersistentDataType.DOUBLE);
    }













}
