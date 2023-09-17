package it.mikeslab.truebank.command.subcommand;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import it.mikeslab.truebank.Perms;
import it.mikeslab.truebank.TrueBank;
import it.mikeslab.truebank.inventory.CardTypeMenu;
import it.mikeslab.truebank.model.CardType;
import it.mikeslab.truebank.model.CreditCard;
import it.mikeslab.truebank.util.CardNumber;
import it.mikeslab.truebank.util.PlayerUtil;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@CommandAlias("bank")
public class SubCardCreator extends BaseCommand {

    private final TrueBank instance;

    public SubCardCreator(TrueBank instance) {
        this.instance = instance;
    }



    @Subcommand("create")
    @CommandPermission(Perms.CREATE_CREDIT_CARD)
    public void create(Player sender, Player target) {

        new CardTypeMenu(instance).showCardTypeMenuSelector(sender)
                .thenAccept(cardType -> {
                    sender.closeInventory(); // Closes the GUI

                    boolean hasEnoughMoney = instance.getEconomy().has(sender, cardType.getCreationCost());

                    if(!hasEnoughMoney) {
                        //todo: not enough money message
                        return;
                    }

                    generateCreditCardFor(sender, target, cardType);

                });






    }

    private void generateCreditCardFor(Player creator, Player target, CardType cardType) {

        if(!PlayerUtil.hasAvailableSlot(target)) {
            // todo send creator message that target hasn't any empty spaces inside his inventory
            return;
        }

        // Generate internal data
        CreditCard creditCard = CardNumber.generate(cardType.getId());
        instance.getAccountHandler().saveOrUpdateCreditCard(target.getUniqueId(), creditCard);

        // Generate ItemStack
        ItemStack creditCardItemStack = instance.getCreditCardHandler().generateCardItemStack(creditCard, target.getUniqueId());
        PlayerUtil.give(target, creditCardItemStack);

        instance.getEconomy().withdrawPlayer(creator, cardType.getCreationCost());



        // TODO: some peripherals messages and give pin book


    }








}
