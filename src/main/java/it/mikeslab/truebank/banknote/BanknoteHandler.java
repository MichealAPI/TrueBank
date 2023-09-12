package it.mikeslab.truebank.banknote;

import it.mikeslab.truebank.TrueBank;
import it.mikeslab.truebank.model.Banknote;
import it.mikeslab.truebank.model.Icon;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class BanknoteHandler {

    private final NamespacedKey namespacedKey;
    private final TrueBank instance;
    public List<Banknote> banknoteMap;


    public BanknoteHandler(TrueBank instance) {
        this.instance = instance;

        this.namespacedKey = new NamespacedKey(instance, "banknote");

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


    public double getBanknoteValue(ItemStack itemStack) {

        ItemMeta meta = itemStack.getItemMeta();

        if(meta == null) return -1;

        // -1 stands for no banknote found

        return meta.getPersistentDataContainer().getOrDefault(namespacedKey, PersistentDataType.DOUBLE, -1.0);
    }














}
