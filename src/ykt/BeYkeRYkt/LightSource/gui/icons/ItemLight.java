package ykt.BeYkeRYkt.LightSource.gui.icons;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import ykt.BeYkeRYkt.LightSource.LightSource;
import ykt.BeYkeRYkt.LightSource.api.gui.Icon;
import ykt.BeYkeRYkt.LightSource.api.gui.Menu;

public class ItemLight extends Icon {

    public ItemLight() {
        super("itemLight", Material.GLOWSTONE_DUST);
        setName(ChatColor.GREEN + "ItemLight");
        getLore().add("");
        getLore().add(ChatColor.DARK_RED + "WARNING!");
        getLore().add(ChatColor.RED + "This option can cause colossal lags!");
        getLore().add(ChatColor.GOLD + "Status: ");
        getLore().add(String.valueOf(LightSource.getInstance().getDB().isItemLight()));
    }

    @Override
    public void onItemClick(InventoryClickEvent event) {
        if (!LightSource.getInstance().getDB().isItemLight()) {
            LightSource.getInstance().getDB().setItemLight(true);
        } else {
            LightSource.getInstance().getDB().setItemLight(false);
        }
        // replace
        getLore().set(4, String.valueOf(LightSource.getInstance().getDB().isItemLight()));

        Player player = (Player) event.getWhoClicked();
        player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 1, 1);
        Menu menu = LightSource.getAPI().getGUIManager().getMenuFromId("optionsMenu");
        LightSource.getAPI().getGUIManager().openMenu(player, menu);
    }
}