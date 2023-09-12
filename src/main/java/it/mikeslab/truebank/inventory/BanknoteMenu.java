package it.mikeslab.truebank.inventory;

import de.themoep.inventorygui.GuiElementGroup;
import de.themoep.inventorygui.GuiPageElement;
import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import it.mikeslab.truebank.TrueBank;
import it.mikeslab.truebank.model.Banknote;
import it.mikeslab.truebank.model.Icon;
import it.mikeslab.truebank.util.ItemStackUtil;
import it.mikeslab.truebank.util.language.LangKey;
import it.mikeslab.truebank.util.language.Language;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class BanknoteMenu {

    private final TrueBank instance;

    private final String[] menuSetup = {
            "         ",
            "abbbbbbbc",
            "    d    "
    };



    public BanknoteMenu(TrueBank instance) {
        this.instance = instance;
    }


    public CompletableFuture<Banknote> showBanknoteSelector(Player player, ItemStack creditCardItemStack) {
        CompletableFuture<Banknote> future = new CompletableFuture<>();

        InventoryGui gui = new InventoryGui(instance, Language.getString(LangKey.BANKNOTE_SELECTOR_TITLE, false), menuSetup);

        // Balance Item
        OfflinePlayer cardHolderOfflinePlayer = getOfflinePlayerFromCreditCard(creditCardItemStack);

        if(!cardHolderOfflinePlayer.hasPlayedBefore()) {
            future.completeExceptionally(new NullPointerException());
        }

        double balance = instance.getEconomy().getBalance(cardHolderOfflinePlayer);
        String formattedBalance = instance.getEconomy().format(balance);

        String balanceDisplayName = Language.getString(LangKey.BALANCE, false);
        String balanceLore = Language.getString(LangKey.BALANCE_LORE, false, Map.of("%balance%", formattedBalance));

        gui.addElement('d', ItemStackUtil.create(Material.SUNFLOWER, balanceDisplayName, List.of(balanceLore)));

        // -------------


        GuiElementGroup elementGroup = new GuiElementGroup('b');
        List<Banknote> banknotes = instance.getBanknoteHandler().banknoteMap;

        for(Banknote banknote : banknotes) {

            String formattedValue = instance.getEconomy().format(banknote.getValue());

            Icon icon = banknote.getIcon();

            icon.getLore().add(" ");
            icon.getLore().add(Language.getString(LangKey.BANKNOTE_VALUE, false, Map.of("%value%", formattedValue)));
            icon.getLore().add(Language.getString(LangKey.BANKNOTE_CLICK_SELECT, false));
            icon.getLore().add(" ");

            ItemStack banknoteItemStack = ItemStackUtil.fromIcon(icon);

            elementGroup.addElement(new StaticGuiElement('b', banknoteItemStack, click -> {
                future.complete(banknote);
                return true;
            }));

        }

        gui.addElement(elementGroup);



        // Pages (arrows)
        gui.addElement(new GuiPageElement('a', new ItemStack(Material.REDSTONE), GuiPageElement.PageAction.PREVIOUS, Language.getString(LangKey.PREVIOUS_PAGE, false)));
        gui.addElement(new GuiPageElement('c', new ItemStack(Material.ARROW), GuiPageElement.PageAction.NEXT, Language.getString(LangKey.NEXT_PAGE, false)));
        // --------------

        gui.setFiller(ItemStackUtil.getFiller(Material.GRAY_STAINED_GLASS_PANE));

        gui.show(player);

        return future;
    }




    private OfflinePlayer getOfflinePlayerFromCreditCard(ItemStack itemStack) {
        //todo update namespaced
        String uuidString = itemStack.getItemMeta()
                .getPersistentDataContainer()
                .get(null, PersistentDataType.STRING);

        return Bukkit.getOfflinePlayer(UUID.fromString(uuidString));
    }




}
