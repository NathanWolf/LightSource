package ykt.BeYkeRYkt.LightSource.gui.menus;

import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

import ykt.BeYkeRYkt.LightSource.LightSource;
import ykt.BeYkeRYkt.LightSource.api.gui.Icon;
import ykt.BeYkeRYkt.LightSource.api.gui.Menu;

public class MainMenu extends Menu {

    public MainMenu() {
        super("mainMenu", LightSource.getInstance().getName(), 18);

        Icon items = LightSource.getAPI().getGUIManager().getIconFromId("items");
        addItem(items, 1);

        Icon worlds = LightSource.getAPI().getGUIManager().getIconFromId("worlds");
        addItem(worlds, 2);

        Icon update = LightSource.getAPI().getGUIManager().getIconFromId("checkUpdate");
        addItem(update, 3);

        Icon options = LightSource.getAPI().getGUIManager().getIconFromId("options");
        addItem(options, 4);

        Icon about = LightSource.getAPI().getGUIManager().getIconFromId("about");
        addItem(about, 18);
    }

    @Override
    public void onOpenMenu(InventoryOpenEvent event) {
    }

    @Override
    public void onCloseMenu(InventoryCloseEvent event) {
    }
}