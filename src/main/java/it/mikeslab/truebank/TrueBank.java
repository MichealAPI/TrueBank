package it.mikeslab.truebank;

import it.mikeslab.truebank.banknote.BanknoteHandler;
import it.mikeslab.truebank.creditcard.CardTypeHandler;
import it.mikeslab.truebank.economy.AccountHandler;
import it.mikeslab.truebank.economy.BalanceHandler;
import it.mikeslab.truebank.economy.PlayerStatsHandler;
import it.mikeslab.truebank.economy.TransactionHandler;
import it.mikeslab.truebank.util.database.SQLiteDBUtil;
import it.mikeslab.truebank.util.language.Language;
import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
    private BalanceHandler balanceHandler;
    private TransactionHandler transactionsHandler;
    private PlayerStatsHandler playerStatsHandler;
    private CardTypeHandler cardTypeHandler;
    private BanknoteHandler banknoteHandler;


    @Override
    public void onEnable() {
        saveDefaultConfig();

        if(!setupEconomy()) {
            fatalError("Unable to interface with Vault Economy", new ProviderNotFoundException());
        }


        setupDatabase();
        setupHandlers();
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
        this.balanceHandler = new BalanceHandler(sqLiteDBUtil);
        this.transactionsHandler = new TransactionHandler(sqLiteDBUtil);

        this.playerStatsHandler = new PlayerStatsHandler();

        this.cardTypeHandler = new CardTypeHandler(this);

        this.banknoteHandler = new BanknoteHandler(this);
    }

    private void setupLanguages() {
        Language.initialize(this, getConfig().getString("language"));
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

    public void consoleRedError(String message) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[" + this.getName() + "] " + message);
    }




}
