package it.mikeslab.truebank.creditcard;

import it.mikeslab.truebank.TrueBank;
import it.mikeslab.truebank.model.CardType;
import it.mikeslab.truebank.model.Icon;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class CardTypeHandler {

    /**
     * A map to store card types with their corresponding IDs.
     */
    public Map<Integer, CardType> cardTypeMap;
    private final TrueBank instance;

    public CardTypeHandler(TrueBank instance) {
        this.instance = instance;

        this.loadTypes();
    }

    /**
     * Loads card types from the plugin's configuration.
     */
    public void loadTypes() {
        this.cardTypeMap = new HashMap<>();

        ConfigurationSection section = instance.getConfig().getConfigurationSection("card-types");

        if (section == null || section.getKeys(false).isEmpty()) return;

        for (String key : section.getKeys(false)) {
            try {
                loadType(
                        Integer.parseInt(key),
                        section.getConfigurationSection(key)
                );
            } catch (NumberFormatException exception) {
                instance.fatalError(
                        "Text cannot be used as an identifier; only integers are allowed as IDs for CardTypes.",
                        exception);
            }
        }
    }

    private void loadType(int id, ConfigurationSection section) {
        Icon icon;

        try {
            icon = Icon.fromConfig(section.getConfigurationSection("icon"));
        } catch (NullPointerException e) {
            instance.logError(
                    Level.WARNING,
                    "Card Type with ID '" + id + "' has an invalid icon! Please, contact an Administrator and check our documentation. Skipping..."
            );
            return;
        }

        String name = section.getString("typeName");
        double creationCost = section.getDouble("typeCreationCost");

        // Daily limits
        double dailyTransferLimit = section.getDouble("dailyTransferLimit");
        double dailyWithdrawLimit = section.getDouble("dailyWithdrawLimit");
        double dailyDepositLimit = section.getDouble("dailyDepositLimit");

        // Global limits
        double depositLimit = section.getDouble("depositLimit");

        cardTypeMap.put(id, new CardType(id, icon, name, creationCost, dailyTransferLimit, dailyWithdrawLimit, dailyDepositLimit, depositLimit));
    }
}
