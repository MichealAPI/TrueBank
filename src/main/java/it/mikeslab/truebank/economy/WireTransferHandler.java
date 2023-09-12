package it.mikeslab.truebank.economy;

import it.mikeslab.truebank.TrueBank;
import it.mikeslab.truebank.model.Transaction;
import it.mikeslab.truebank.model.WireTransfer;
import it.mikeslab.truebank.util.language.LangKey;
import it.mikeslab.truebank.util.language.Language;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class WireTransferHandler {

    private final TrueBank instance;


    public WireTransferHandler(TrueBank instance) {
        this.instance = instance;
    }


    public void validateWireTransfer(WireTransfer wireTransfer) {

        Transaction transaction = new Transaction(
                Language.getString(LangKey.WIRETRANSFER_DESCRIPTION, false),
                wireTransfer.getAmount(),
                wireTransfer.getFromName(),
                wireTransfer.getToName());

        OfflinePlayer fromPlayer = Bukkit.getOfflinePlayer(wireTransfer.getFromUUID());
        OfflinePlayer toPlayer = Bukkit.getOfflinePlayer(wireTransfer.getToUUID());

        instance.getEconomy().withdrawPlayer(fromPlayer, wireTransfer.getAmount());
        instance.getEconomy().depositPlayer(toPlayer, wireTransfer.getAmount());

        instance.getTransactionsHandler().insertTransaction(transaction);
    }













}
