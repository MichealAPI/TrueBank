package it.mikeslab.truebank.inventory.anvil;

import it.mikeslab.truebank.TrueBank;
import it.mikeslab.truebank.economy.Statistic;
import it.mikeslab.truebank.inventory.BanknoteMenu;
import it.mikeslab.truebank.model.Banknote;
import it.mikeslab.truebank.model.CardType;
import it.mikeslab.truebank.util.PlayerUtil;
import it.mikeslab.truebank.util.language.LangKey;
import it.mikeslab.truebank.util.language.Language;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class PINMenu {

    private final TrueBank instance;

    public PINMenu(TrueBank instance) {
        this.instance = instance;
    }

    public void showPinGui(Player subject, OfflinePlayer targetOfflinePlayer, int correctPin) {

        new AnvilGUI.Builder()
                .onClick((slot, stateSnapshot) -> {
                    if(slot != AnvilGUI.Slot.OUTPUT) {
                        return Collections.emptyList();
                    }

                    if(stateSnapshot.getText().equalsIgnoreCase(correctPin + "")) {

                        // Opens atm
                        this.openBanknoteMenu(subject, targetOfflinePlayer);


                        return List.of(AnvilGUI.ResponseAction.close());
                    } else {
                        return List.of(AnvilGUI.ResponseAction.replaceInputText(Language.getString(LangKey.WRONG_PIN, false)));
                    }
                })
                .text("1234")
                .title(Language.getString(LangKey.PIN_GUI_TITLE, false))
                .plugin(instance)
                .open(subject);



    }

    private void openBanknoteMenu(Player subject, OfflinePlayer targetOfflinePlayer) {

        new BanknoteMenu(instance).showBanknoteSelector(subject, targetOfflinePlayer).thenAccept(banknote -> {

            if(banknote == null) {
                subject.closeInventory();
                return;
            }

            if(!instance.getEconomy().has(targetOfflinePlayer, banknote.getValue())) return;

            UUID holderUUID = targetOfflinePlayer.getUniqueId();

            // Each card has a daily limit on Transfers, Withdraws and Deposits. Here's the check.
            int cardTypeID = instance.getAccountHandler().fromUUID(holderUUID).getCardTypeID();
            CardType cardType = instance.getCardTypeHandler().cardTypeMap.get(cardTypeID);

            double dailyWithdrawLimit = cardType.getDailyWithdrawLimit();
            double dailyWithdrawReached = instance
                    .getPlayerStatsHandler()
                    .uuidPlayerStatsMap
                    .get(holderUUID)
                    .get(Statistic.WITHDREW);

            // Checking limit
            if((dailyWithdrawReached + banknote.getValue()) > dailyWithdrawLimit) {
                subject.sendMessage(Language.getString(LangKey.HOLDER_WITHDRAW_LIMIT_REACHED, true));
                return;
            }

            this.giveBanknote(subject, targetOfflinePlayer, banknote);
        });
    }



    private void giveBanknote(Player subject, OfflinePlayer targetOfflinePlayer, Banknote banknote) {

        ItemStack banknoteItemStack = instance.getBanknoteHandler().fromBanknote(banknote);

        if(PlayerUtil.give(subject, banknoteItemStack)) {
            instance.getEconomy().withdrawPlayer(targetOfflinePlayer, banknote.getValue());
        } else {
            subject.sendMessage(Language.getString(LangKey.INVENTORY_FULL, true));
        }

        updateLimits(targetOfflinePlayer, banknote.getValue());
    }


    private void updateLimits(OfflinePlayer targetOfflinePlayer, double amount) {
        instance.getPlayerStatsHandler().update(targetOfflinePlayer.getUniqueId(), Statistic.WITHDREW, amount);
    }


}
