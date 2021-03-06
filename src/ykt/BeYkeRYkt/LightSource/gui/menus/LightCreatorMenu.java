package ykt.BeYkeRYkt.LightSource.gui.menus;

import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

import ykt.BeYkeRYkt.LightSource.LightSource;
import ykt.BeYkeRYkt.LightSource.api.gui.Icon;
import ykt.BeYkeRYkt.LightSource.api.gui.Menu;

public class LightCreatorMenu extends Menu {

    public LightCreatorMenu() {
        super("lc_mainMenu", "Light Static Creator", 18);

        Icon create = LightSource.getAPI().getGUIManager().getIconFromId("lc_create");
        addItem(create, 1);

        Icon delete = LightSource.getAPI().getGUIManager().getIconFromId("lc_delete");
        addItem(delete, 2);
    }

    @Override
    public void onOpenMenu(InventoryOpenEvent event) {
    }

    @Override
    public void onCloseMenu(InventoryCloseEvent event) {
    }
}