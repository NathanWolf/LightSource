package ykt.BeYkeRYkt.LightSource.gui.icons;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import ykt.BeYkeRYkt.LightSource.LightSource;
import ykt.BeYkeRYkt.LightSource.api.LightAPI;
import ykt.BeYkeRYkt.LightSource.api.gui.Icon;
import ykt.BeYkeRYkt.LightSource.api.gui.Menu;
import ykt.BeYkeRYkt.LightSource.editor.PlayerEditor;

public class ChangeName extends Icon {

    public ChangeName() {
        super("editorName", Material.NAME_TAG);
        setName(ChatColor.AQUA + "Change name");
        getLore().add("");
        getLore().add(ChatColor.WHITE + "Current name: ");
        getLore().add(ChatColor.GOLD + "Click item for edit...");
    }

    @Override
    public void onItemClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        LightSource.getAPI().log(player, "Enter new name (Chat)");

        PlayerEditor editor = LightAPI.getEditorManager().getEditor(player.getName());
        editor.setStage(1);
        player.closeInventory();
    }

    @Override
    public void onMenuOpen(Menu menu, Player player) {
        PlayerEditor editor = LightAPI.getEditorManager().getEditor(player.getName());
        getLore().set(1, ChatColor.WHITE + "Current name: " + ChatColor.GREEN + editor.getItem().getName());
    }

}