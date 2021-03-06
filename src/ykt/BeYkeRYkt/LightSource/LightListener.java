package ykt.BeYkeRYkt.LightSource;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import ykt.BeYkeRYkt.LightSource.api.LightAPI;
import ykt.BeYkeRYkt.LightSource.api.gui.Icon;
import ykt.BeYkeRYkt.LightSource.api.gui.Menu;
import ykt.BeYkeRYkt.LightSource.api.items.ItemManager;
import ykt.BeYkeRYkt.LightSource.api.items.LightItem;
import ykt.BeYkeRYkt.LightSource.api.sources.Source;
import ykt.BeYkeRYkt.LightSource.api.sources.Source.ItemType;
import ykt.BeYkeRYkt.LightSource.editor.PlayerCreator;
import ykt.BeYkeRYkt.LightSource.editor.PlayerEditor;
import ykt.BeYkeRYkt.LightSource.nbt.comphenix.AttributeStorage;
import ykt.BeYkeRYkt.LightSource.sources.ItemSource;
import ykt.BeYkeRYkt.LightSource.sources.PlayerSource;
import de.albionco.updater.Response;
import de.albionco.updater.Updater;
import de.albionco.updater.Version;

public class LightListener implements Listener {

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (event.isCancelled())
            return;
        Player player = event.getPlayer();
        if (LightAPI.getEditorManager().isEditor(player.getName())) {
            PlayerEditor editor = LightAPI.getEditorManager().getEditor(player.getName());
            event.setCancelled(true);

            if (editor.getStage() == 1) {// Change name
                String message = event.getMessage();

                if (message.equalsIgnoreCase("null") || message.equalsIgnoreCase("no")) {
                    message = null;
                }
                editor.getItem().setName(message);
                LightSource.getAPI().log(player, "Name changed to " + ChatColor.AQUA + message);
            } else if (editor.getStage() == 2) {// Change burn time
                String message = event.getMessage();

                try {
                    int time = Integer.parseInt(message);
                    editor.getItem().setMaxBurnTime(time);
                    LightSource.getAPI().log(player, "Burn time changed to " + ChatColor.AQUA + time + ChatColor.WHITE + " seconds!");
                } catch (Exception e) {
                    LightSource.getAPI().log(player, ChatColor.RED + "Please enter numbers");
                    return;
                }
            } else if (editor.getStage() == 3) {// Change light level
                String message = event.getMessage();

                try {
                    int level = Integer.parseInt(message);
                    if (level > 15) { // User, You are a crazy ?!
                        level = 15;
                    }
                    editor.getItem().setMaxLevelLight(level);
                    LightSource.getAPI().log(player, "Light level changed to " + ChatColor.AQUA + level + ChatColor.WHITE + " / 15.");
                } catch (Exception e) {
                    LightSource.getAPI().log(player, ChatColor.RED + "Please enter numbers");
                    return;
                }
            }

            Menu menu = LightSource.getAPI().getGUIManager().getMenuFromId("editorMenu");
            LightSource.getAPI().getGUIManager().openMenu(player, menu);
            editor.setStage(0);
            return;
        }

        if (LightAPI.getEditorManager().isCreator(player.getName())) {
            PlayerCreator creator = LightAPI.getEditorManager().getCreator(player.getName());
            event.setCancelled(true);

            if (creator.getStage() == 0) {// create item ID
                String id = event.getMessage();
                if (ItemManager.getLightItem(id) != null) {
                    LightSource.getAPI().log(player, ChatColor.RED + "This ID is exists. try new id.");
                    return;
                } else {
                    creator.setID(id);
                    LightSource.getAPI().log(player, "Enter item material (You can see BukkitAPI documentation or use Essentials comamnd /dura).");
                    creator.setStage(1);
                    return;
                }
            } else if (creator.getStage() == 1) {// create Material
                String material = event.getMessage().toUpperCase();
                if (Material.getMaterial(material) != null) {
                    creator.setMaterial(Material.getMaterial(material));
                    creator.setStage(0);

                    LightItem item = new LightItem(creator.getID(), null, Material.getMaterial(material), 15, 60);
                    ItemManager.addLightSource(item, item.getId());

                    PlayerEditor editor = new PlayerEditor(player.getName(), item);
                    LightAPI.getEditorManager().removeCreator(creator);
                    LightAPI.getEditorManager().addEditor(editor);

                    LightSource.getAPI().log(player, ChatColor.DARK_AQUA + "Refreshing GUI Manager...");
                    LightSource.getAPI().getGUIManager().refresh();
                } else {
                    LightSource.getAPI().log(player, ChatColor.RED + "Material is not found. Try again :(");
                    return;
                }
            }
            Menu menu = LightSource.getAPI().getGUIManager().getMenuFromId("editorMenu");
            LightSource.getAPI().getGUIManager().openMenu(player, menu);
            return;
        }
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (event.isCancelled())
            return;
        Inventory inventory = event.getInventory();

        if (inventory.getTitle() == null)
            return;
        if (LightSource.getAPI().getGUIManager().isMenu(inventory.getTitle())) {
            Menu menu = LightSource.getAPI().getGUIManager().getMenuFromName(inventory.getTitle());
            menu.onOpenMenu(event);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Inventory inventory = event.getInventory();

        if (inventory.getTitle() == null)
            return;
        if (LightSource.getAPI().getGUIManager().isMenu(inventory.getTitle())) {
            Menu menu = LightSource.getAPI().getGUIManager().getMenuFromName(inventory.getTitle());
            menu.onCloseMenu(event);
        }
    }

    @EventHandler
    public void onItemClick(InventoryClickEvent event) {
        if (event.isCancelled())
            return;
        ItemStack item = event.getCurrentItem();
        Inventory inventory = event.getInventory();

        if (item == null)
            return;
        if (inventory.getTitle() == null)
            return;
        if (LightSource.getAPI().getGUIManager().isMenu(inventory.getTitle())) {
            if (LightSource.getAPI().getGUIManager().isIcon(item)) {
                if (!item.getItemMeta().hasDisplayName())
                    return;
                Icon icon = LightSource.getAPI().getGUIManager().getIconFromName(item.getItemMeta().getDisplayName());
                icon.onItemClick(event);
            }
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDropLight(PlayerDropItemEvent event) {
        if (event.isCancelled())
            return;
        for (Source light : LightAPI.getSourceManager().getSourceList()) {
            if (light.getOwner().getType() == EntityType.PLAYER) {
                Player player = (Player) light.getOwner();
                if (player.getName().equals(event.getPlayer())) {
                    if (player.getItemInHand().getAmount() <= 1) {
                        LightAPI.deleteLight(light.getLocation(), false);
                    }
                    ItemSource itemsource = new ItemSource(event.getItemDrop(), light.getItem());
                    LightAPI.getSourceManager().addSource(itemsource);
                }
            }
        }
    }

    @EventHandler
    public void onItemHeldChange(PlayerItemHeldEvent event) {
        if (event.isCancelled())
            return;
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItem(event.getNewSlot());
        Location loc = player.getLocation();

        if (LightSource.getInstance().getDB().getWorld(event.getPlayer().getWorld().getName()) && LightSource.getInstance().getDB().isPlayerLight()) {

            if (item != null && ItemManager.isLightSource(item)) {
                LightItem lightItem = ItemManager.getLightItem(item);

                // AttributeStorage storage = AttributeStorage.newTarget(item,
                // ItemManager.TIME_ID);
                // if (storage.getData(null) != null) {
                // int time = Integer.parseInt(storage.getData(null));
                // lightItem.setBurnTime(time, true);
                // }

                if (LightAPI.getSourceManager().getSource(player) == null) {
                    PlayerSource light = new PlayerSource(player, loc, lightItem, ItemType.HAND, item);

                    AttributeStorage storage = AttributeStorage.newTarget(item, ItemManager.TIME_ID);
                    if (storage.getData(null) != null) {
                        int time = Integer.parseInt(storage.getData(null));
                        // lightItem.setBurnTime(time, true);
                        light.setBurnTime(time, true);
                    }

                    LightAPI.getSourceManager().addSource(light);
                } else {
                    Source source = LightAPI.getSourceManager().getSource(player);

                    // Save old item nbt
                    AttributeStorage saveStorage = AttributeStorage.newTarget(source.getItemStack(), ItemManager.TIME_ID);
                    saveStorage.setData(String.valueOf(source.getBurnTime()));

                    // set new item
                    source.setItemStack(item);
                    source.setItem(lightItem);
                }
            } else if (item == null || item != null && !ItemManager.isLightSource(item)) {
                if (LightAPI.getSourceManager().getSource(player) != null) {
                    Source source = LightAPI.getSourceManager().getSource(player);
                    LightAPI.deleteLight(source.getLocation(), true);
                }
            }
        }
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        for (Source light : LightAPI.getSourceManager().getSourceList()) {
            if (event.getChunk().getX() == light.getLocation().getChunk().getX() && event.getChunk().getZ() == light.getLocation().getChunk().getZ()) {
                LightAPI.deleteLight(light.getLocation(), true);
            }
        }
    }

    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent event) {
        if (event.isCancelled())
            return;
        for (Source light : LightAPI.getSourceManager().getSourceList()) {
            if (event.getChunk().getX() == light.getLocation().getChunk().getX() && event.getChunk().getZ() == light.getLocation().getChunk().getZ()) {
                LightAPI.deleteLight(light.getLocation(), true);
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (LightAPI.getSourceManager().getSource(event.getPlayer()) != null) {
            Source light = LightAPI.getSourceManager().getSource(event.getPlayer());
            LightAPI.deleteLight(light.getLocation(), false);
            LightAPI.getSourceManager().removeSource(light);
        }
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        if (event.isCancelled())
            return;
        if (LightAPI.getSourceManager().getSource(event.getPlayer()) != null) {
            Source light = LightAPI.getSourceManager().getSource(event.getPlayer());
            LightAPI.deleteLight(light.getLocation(), false);
            LightAPI.getSourceManager().removeSource(light);
        }
    }

    @EventHandler
    public void onPlayerBedEnter(PlayerBedEnterEvent event) {
        if (event.isCancelled())
            return;
        if (LightAPI.getSourceManager().getSource(event.getPlayer()) != null) {
            Source light = LightAPI.getSourceManager().getSource(event.getPlayer());
            LightAPI.deleteLight(light.getLocation(), false);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (LightAPI.getSourceManager().getSource(event.getEntity()) != null) {
            Source light = LightAPI.getSourceManager().getSource(event.getEntity());
            LightAPI.deleteLight(light.getLocation(), false);
        }
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if (event.isCancelled())
            return;
        if (LightAPI.getSourceManager().getSource(event.getPlayer()) != null) {
            Source light = LightAPI.getSourceManager().getSource(event.getPlayer());
            LightAPI.deleteLight(light.getLocation(), true);
        }
    }

    @EventHandler
    public void onPlayerChangeWorlds(PlayerChangedWorldEvent event) {
        if (LightAPI.getSourceManager().getSource(event.getPlayer()) != null) {
            Source light = LightAPI.getSourceManager().getSource(event.getPlayer());
            LightAPI.deleteLight(light.getLocation(), true);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.isCancelled())
            return;
        if (LightSource.getInstance().getDB().isLightSourceDamage()) {
            LivingEntity entity = (LivingEntity) event.getEntity();
            if (event.getDamager().getType().isAlive()) {
                LivingEntity damager = (LivingEntity) event.getDamager();
                if (ItemManager.isLightSource(damager.getEquipment().getItemInHand())) {
                    int fire = LightSource.getInstance().getDB().getDamageFire() * 20;
                    entity.setFireTicks(fire);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        if (player.isOp() || player.hasPermission("ls.admin")) {
            Bukkit.getScheduler().runTaskLaterAsynchronously(LightSource.getInstance(), new Runnable() {

                @Override
                public void run() {
                    Version version = Version.parse(LightSource.getInstance().getDescription().getVersion());
                    String repo = "BeYkeRYkt/LightSource";

                    Updater updater;
                    try {
                        updater = new Updater(version, repo);

                        Response response = updater.getResult();
                        if (response == Response.SUCCESS) {
                            LightSource.getAPI().log(player, ChatColor.GREEN + "New update is available: " + ChatColor.YELLOW + updater.getLatestVersion() + ChatColor.GREEN + "!");
                            LightSource.getAPI().log(player, ChatColor.GREEN + "Changes: ");
                            player.sendMessage(updater.getChanges());// for
                                                                     // normal
                                                                     // view
                            player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 20);
        }
    }
}