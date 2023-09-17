package it.mikeslab.truebank;

import co.aikar.commands.BukkitCommandManager;
import it.mikeslab.truebank.atm.ATMHandler;
import it.mikeslab.truebank.banknote.BanknoteHandler;
import it.mikeslab.truebank.command.BankCommand;
import it.mikeslab.truebank.creditcard.CardTypeHandler;
import it.mikeslab.truebank.economy.AccountHandler;
import it.mikeslab.truebank.economy.CreditCardHandler;
import it.mikeslab.truebank.economy.PlayerStatsHandler;
import it.mikeslab.truebank.economy.TransactionHandler;
import it.mikeslab.truebank.listener.ATMBlockListener;
import it.mikeslab.truebank.listener.ATMInteractListener;
import it.mikeslab.truebank.util.database.SQLiteDBUtil;
import it.mikeslab.truebank.util.language.Language;
import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.nio.file.ProviderNotFoundException;
import java.util.logging.Level;

@Getter
public final class TrueBank extends JavaPlugin {

    private SQLiteDBUtil sqLiteDBUtil;

    private Economy economy;

    private AccountHandler accountHandler;
    private ATMHandler atmHandler;
    private TransactionHandler transactionsHandler;
    private PlayerStatsHandler playerStatsHandler;
    private CardTypeHandler cardTypeHandler;
    private CreditCardHandler creditCardHandler;
    private BanknoteHandler banknoteHandler;

    private FileConfiguration menuConfiguration;



    @Override
    public void onEnable() {
        saveDefaultConfig();

        if(!setupEconomy()) {
            fatalError("Unable to interface with Vault Economy", new ProviderNotFoundException());
        }


        setupDatabase();
        setupHandlers();
        setupListeners();
        setupCommands();
        setupLanguages();

        startCardDailyLimitsCountdown();


    }

    @Override
    public void onDisable() {
        this.sqLiteDBUtil.closeConnection();
    }

    private void setupDatabase() {
        this.sqLiteDBUtil = new SQLiteDBUtil(new File(this.getDataFolder(), "bank.db"));
        this.sqLiteDBUtil.createDefaultDatabaseTables();
    }

    private void setupHandlers() {
        this.accountHandler = new AccountHandler(sqLiteDBUtil, this);
        this.atmHandler = new ATMHandler(this);
        this.transactionsHandler = new TransactionHandler(sqLiteDBUtil);

        this.playerStatsHandler = new PlayerStatsHandler();

        this.cardTypeHandler = new CardTypeHandler(this);

        this.banknoteHandler = new BanknoteHandler(this);
        this.creditCardHandler = new CreditCardHandler(this);
    }

    private void setupListeners() {
        PluginManager pluginManager = Bukkit.getPluginManager();

        pluginManager.registerEvents(new ATMBlockListener(this), this);
        pluginManager.registerEvents(new ATMInteractListener(this), this);

    }

    private void setupLanguages() {
        Language.initialize(this, getConfig().getString("language"));

        // Inventories Language
        String inventoryLangPath = "inventories.yml";
        File inventoryLangFile = new File(getDataFolder(), inventoryLangPath);
        Language.generateFile(inventoryLangFile, "inventories.yml");

        this.menuConfiguration = YamlConfiguration.loadConfiguration(inventoryLangFile);
    }

    private void setupCommands() {

        BukkitCommandManager commandManager = new BukkitCommandManager(this);

        commandManager.enableUnstableAPI("help");

        commandManager.registerCommand(new BankCommand(this));

    }

    private void startCardDailyLimitsCountdown() {
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            this.getPlayerStatsHandler().reInitStatsMap();
        }, 0, 20*86400);
    }


    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }

    public void logError(Level logLevel, String message, Throwable t) {
        getLogger().log(logLevel, message, t);
    }

    public void logError(Level logLevel, String message) {
        logError(logLevel, message, null);
    }

    public void fatalError(String message, Exception ex) {
        logError(Level.SEVERE, message, ex);

        // disable plug-in
        this.getPluginLoader().disablePlugin(this);
    }




}
