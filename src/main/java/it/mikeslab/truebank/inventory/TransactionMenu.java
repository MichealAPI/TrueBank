package it.mikeslab.truebank.inventory;

import de.themoep.inventorygui.GuiElementGroup;
import de.themoep.inventorygui.GuiPageElement;
import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import it.mikeslab.truebank.Perms;
import it.mikeslab.truebank.TrueBank;
import it.mikeslab.truebank.model.Transaction;
import it.mikeslab.truebank.util.ItemStackUtil;
import it.mikeslab.truebank.util.language.LangKey;
import it.mikeslab.truebank.util.language.Language;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class TransactionMenu {

    private final TrueBank instance;

    private final String[] setup = {
            "         ",
            " bbbbbbb ",
            " bbbbbbb ",
            " bbbbbbb ",
            "   a c   "
    };

    public TransactionMenu(TrueBank instance) {
        this.instance = instance;
    }

    public void showTransactionMenu(Player player, OfflinePlayer target) {

        String title = Language.getString(LangKey.TRANSACTION_MENU_TITLE, false, Map.of("%target%", target.getName()));

        InventoryGui gui = new InventoryGui(instance, title, setup);

        GuiElementGroup group = new GuiElementGroup('b');

        // Transactions are already sorted by date
        List<Transaction> targetTransactions = instance.getTransactionsHandler().getTransactions(target.getUniqueId());

        boolean isThirdPersonAdmin = !player.getUniqueId().equals(target.getUniqueId()) && !player.hasPermission(Perms.TRANSACTION_DELETE_OTHERS);
        boolean isTargetViewer = !isThirdPersonAdmin && !player.hasPermission(Perms.TRANSACTION_DELETE_OTHERS);

        for(Transaction transaction : targetTransactions) {

            group.addElement(new StaticGuiElement('b', fromTransaction(transaction, isThirdPersonAdmin, isTargetViewer), click -> {

                if(click.getType() != ClickType.RIGHT) return true;
                if(!isThirdPersonAdmin && !isTargetViewer) return true;

                instance.getTransactionsHandler().deleteTransactionByID(transaction.getId());
                showTransactionMenu(player, target);

                return true;
            }));
        }

        gui.addElement(group);

        // Pages (arrows)
        gui.addElement(new GuiPageElement('a', new ItemStack(Material.REDSTONE), GuiPageElement.PageAction.PREVIOUS, Language.getString(LangKey.PREVIOUS_PAGE, false)));
        gui.addElement(new GuiPageElement('c', new ItemStack(Material.ARROW), GuiPageElement.PageAction.NEXT, Language.getString(LangKey.NEXT_PAGE, false)));
        // --------------

        gui.setFiller(ItemStackUtil.getFiller(Material.GRAY_STAINED_GLASS_PANE));

        gui.setCloseAction(close -> false);

        gui.show(player);
    }


    private ItemStack fromTransaction(Transaction transaction, boolean isThirdPersonAdmin, boolean isTargetViewer) {

        // Formatting millis as date
        Date date = new Date(transaction.getCurrentMillis());
        DateFormat sdf = new SimpleDateFormat("dd/MM/yy");
        // -----

        String parsedAmount = instance.getEconomy().format(transaction.getAmount());

        String displayName = ChatColor.GREEN + sdf.format(date);

        List<String> lore = new ArrayList<>();

        lore.add(Language.getString(LangKey.TRANSACTION_HEADER, false));
        lore.add(" ");

        lore.add(Language.getString(LangKey.TRANSACTION_ID, false, Map.of("%id%", transaction.getId() + "")));
        lore.add(Language.getString(LangKey.TRANSACTION_AMOUNT, false, Map.of("%amount%", parsedAmount)));
        lore.add(Language.getString(LangKey.TRANSACTION_DESCRIPTION, false, Map.of("%description%", transaction.getDescription())));

        lore.add(Language.getString(LangKey.TRANSACTION_SENDER, false, Map.of("%sender%", transaction.getSender())));
        lore.add(Language.getString(LangKey.TRANSACTION_RECEIVER, false, Map.of("%receiver%", transaction.getReceiver())));

        lore.add(" ");


        addLoreIf(isThirdPersonAdmin || isTargetViewer, lore);


        return ItemStackUtil.create(Material.PAPER, displayName, lore);
    }

    private void addLoreIf(boolean condition, List<String> lore) {
        if (condition) {
            lore.add(Language.getString(LangKey.TRANSACTION_CLICK_DELETE, false));
            lore.add(" ");
        }
    }


}
