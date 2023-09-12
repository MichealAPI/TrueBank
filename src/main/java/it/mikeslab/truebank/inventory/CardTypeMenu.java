package it.mikeslab.truebank.inventory;

import de.themoep.inventorygui.GuiElementGroup;
import de.themoep.inventorygui.GuiPageElement;
import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import it.mikeslab.truebank.TrueBank;
import it.mikeslab.truebank.model.CardType;
import it.mikeslab.truebank.model.Icon;
import it.mikeslab.truebank.util.ItemStackUtil;
import it.mikeslab.truebank.util.language.LangKey;
import it.mikeslab.truebank.util.language.Language;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class CardTypeMenu {

    private final TrueBank instance;

    private final String[] menuSetup = {
            "         ",
            "abbbbbbbc",
            "         "
    };



    public CardTypeMenu(TrueBank instance) {
        this.instance = instance;
    }


    public CompletableFuture<CardType> showCardTypeMenuSelector(Player player) {
        CompletableFuture<CardType> future = new CompletableFuture<>();

        InventoryGui gui = new InventoryGui(instance, Language.getString(LangKey.CARDTYPE_SELECTOR_TITLE, false), menuSetup);

        GuiElementGroup elementGroup = new GuiElementGroup('b');
        Collection<CardType> cardTypes = instance.getCardTypeHandler().cardTypeMap.values();

        for(CardType cardType : cardTypes) {

            String formattedCost = instance.getEconomy().format(cardType.getCreationCost());

            Icon icon = cardType.getIcon();

            icon.getLore().add(" ");
            icon.getLore().add(Language.getString(LangKey.CARDTYPE_PRICE, false, Map.of("%price%", formattedCost)));
            icon.getLore().add(Language.getString(LangKey.CARDTYPE_CLICK_SELECT, false));
            icon.getLore().add(" ");

            ItemStack cardTypeItemStack = ItemStackUtil.fromIcon(icon);

            elementGroup.addElement(new StaticGuiElement('b', cardTypeItemStack, click -> {
                future.complete(cardType);
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






}
