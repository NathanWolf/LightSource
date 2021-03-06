package ykt.BeYkeRYkt.LightSource.gui.menus;

import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

import ykt.BeYkeRYkt.LightSource.LightSource;
import ykt.BeYkeRYkt.LightSource.api.gui.Icon;
import ykt.BeYkeRYkt.LightSource.api.gui.Menu;

public class OptionsMenu extends Menu {

    public OptionsMenu() {
        super("optionsMenu", "Options", 18);

        Icon playerLight = LightSource.getAPI().getGUIManager().getIconFromId("playerLight");
        addItem(playerLight, 1);

        Icon entityLight = LightSource.getAPI().getGUIManager().getIconFromId("entityLight");
        addItem(entityLight, 2);

        Icon itemLight = LightSource.getAPI().getGUIManager().getIconFromId("itemLight");
        addItem(itemLight, 3);

        Icon burn = LightSource.getAPI().getGUIManager().getIconFromId("burnLight");
        addItem(burn, 4);

        Icon ignore = LightSource.getAPI().getGUIManager().getIconFromId("ignoreSaveUpdate");
        addItem(ignore, 5);

        Icon lightDamage = LightSource.getAPI().getGUIManager().getIconFromId("lightsourcedamage");
        addItem(lightDamage, 6);

        Icon back = LightSource.getAPI().getGUIManager().getIconFromId("back");
        addItem(back, 18);
    }

    @Override
    public void onOpenMenu(InventoryOpenEvent event) {
    }

    @Override
    public void onCloseMenu(InventoryCloseEvent event) {
    }
}