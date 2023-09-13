package it.mikeslab.truebank.inventory.general;

import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import it.mikeslab.truebank.TrueBank;
import it.mikeslab.truebank.inventory.element.CustomizableGuiElement;
import it.mikeslab.truebank.util.ItemStackUtil;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

public class ConfirmMenu {
    private final FileConfiguration menuConfiguration;
    private final TrueBank instance;

    private final String[] setup = {
            "         ",
            "   a b   ",
            "         ",
    };


    public ConfirmMenu(TrueBank instance) {
        this.instance = instance;
        this.menuConfiguration = instance.getMenuConfiguration();
    }


    public CompletableFuture<Boolean> show(Player player, String title) {
        final String inventoryName = "confirm-menu";

        CompletableFuture<Boolean> completableFuture = new CompletableFuture<>();

        InventoryGui inventoryGui = new InventoryGui(instance, title, setup);

        inventoryGui.setFiller(ItemStackUtil.getFiller(Material.GRAY_STAINED_GLASS_PANE));

        StaticGuiElement confirmElement = new CustomizableGuiElement(menuConfiguration).create('a', inventoryName, "confirm");
        StaticGuiElement cancelElement = new CustomizableGuiElement(menuConfiguration).create('b', inventoryName, "cancel");

        confirmElement.setAction(click -> {
            click.getGui().close();
            completableFuture.complete(true);
            return true;
        });


        cancelElement.setAction(click -> {
            click.getGui().close();
            completableFuture.complete(false);
            return true;
        });

        inventoryGui.addElement(confirmElement);
        inventoryGui.addElement(cancelElement);

        inventoryGui.setCloseAction(close -> {
            completableFuture.complete(false);
            return true;
        });

        inventoryGui.show(player);
        return completableFuture;
    }


}
