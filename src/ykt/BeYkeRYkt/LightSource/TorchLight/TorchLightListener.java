package ykt.BeYkeRYkt.LightSource.TorchLight;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;

import ykt.BeYkeRYkt.LightSource.LightAPI;
import ykt.BeYkeRYkt.LightSource.LightSource;
import ykt.BeYkeRYkt.LightSource.nmsUtils.NMSInterface.LightType;

public class TorchLightListener implements Listener {

	private static HashMap<String, Location> pLocations = new HashMap<String, Location>();
	
	public static HashMap<String, Location>getLocations(){
		return pLocations;
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		ItemStack item = player.getItemInHand();
		if (LightSource.getInstance().getConfig().getBoolean("Worlds." + event.getPlayer().getWorld().getName()) || event.getPlayer().isOp()){
		// Checking player
		if (LightSource.getInstance().getHeadPlayers().contains(player.getName()))
			return;
		if (LightSource.getInstance().getTorchPlayers().contains(player.getName())) {
			if (item != null && ItemManager.isTorchLight(item)) {
				
			      if (!pLocations.containsKey(player.getName())) {
			            pLocations.put(player.getName(), event.getFrom());
			        }
				
			      double d = pLocations.get(player.getName()).distance(event.getTo());
			      if (d >= LightSource.getInstance().getConfig().getDouble("Radius-update")) {
					LightAPI.deleteLightSource(LightType.DYNAMIC, pLocations.get(player.getName()));
					LightAPI.createLightSource(LightType.DYNAMIC, event.getTo(),ItemManager.getLightLevel(item));
					pLocations.put(player.getName(), event.getTo());
			      }
			      
			}else if (item != null && !ItemManager.isTorchLight(item)|| item == null) {
				
				LightAPI.deleteLightSourceAndUpdate(LightType.DYNAMIC, pLocations.get(player.getName()));
				LightSource.getInstance().getTorchPlayers().remove(player.getName());
				pLocations.remove(player.getName());
				
				if (LightSource.getInstance().getConfig().getBoolean("Debug")) {
					LightSource.getInstance().getLogger()
							.info("Player removed from the group");
				}
			}
		}else{
			if (item != null && ItemManager.isTorchLight(item)) {
				
			      if (!pLocations.containsKey(player.getName())) {
			            pLocations.put(player.getName(), event.getFrom());
			        }
				
					LightAPI.createLightSource(LightType.DYNAMIC, event.getFrom(),ItemManager.getLightLevel(item));
					LightSource.getInstance().getTorchPlayers().add(player.getName());
					
					if (LightSource.getInstance().getConfig().getBoolean("Debug")) {
						LightSource.getInstance().getLogger()
								.info("Player added to the group");
					
				}
			}
		}
		}
	}

	@EventHandler
	public void onItemHeldChange(PlayerItemHeldEvent event) {
		Player player = event.getPlayer();
		ItemStack item = player.getInventory().getItem(event.getNewSlot());
		Location loc = player.getLocation();

		if (LightSource.getInstance().getConfig().getBoolean("Worlds." + event.getPlayer().getWorld().getName()) || event.getPlayer().isOp()){
		
		if (LightSource.getInstance().getHeadPlayers().contains(player.getName()))
			return;
		if (!LightSource.getInstance().getTorchPlayers().contains(player.getName())) {
			if (item != null && ItemManager.isTorchLight(item)) {
				LightAPI.createLightSource(LightType.DYNAMIC, loc, ItemManager.getLightLevel(item));
				pLocations.put(player.getName(), loc);
				LightSource.getInstance().getTorchPlayers().add(player.getName());

				if (LightSource.getInstance().getConfig().getBoolean("Debug")) {
					LightSource.getInstance().getLogger()
							.info("Player added to the group");
				}
			}

		} else if (LightSource.getInstance().getTorchPlayers().contains(player.getName())) {
			if (item != null && !ItemManager.isTorchLight(item) || item == null|| item.getType() == Material.AIR) {
				LightAPI.deleteLightSourceAndUpdate(LightType.DYNAMIC, pLocations.get(player.getName()));
				LightSource.getInstance().getTorchPlayers().remove(player.getName());
				pLocations.remove(player.getName());

				if (LightSource.getInstance().getConfig().getBoolean("Debug")) {
					LightSource.getInstance().getLogger()
							.info("Player removed from the group");
				}

			} else if (item != null && ItemManager.isTorchLight(item)) {
				LightAPI.deleteLightSource(LightType.DYNAMIC, pLocations.get(player.getName()));
				LightAPI.createLightSource(LightType.DYNAMIC, loc, ItemManager.getLightLevel(item));
				pLocations.put(player.getName(), loc);
			}
		}
	}
	}

	@EventHandler
	public void onPlayerChangeWorlds(PlayerChangedWorldEvent event) {
		if (LightSource.getInstance().getTorchPlayers().contains(event.getPlayer().getName())) {
			if(!pLocations.containsKey(event.getPlayer())) return;
			Location loc = pLocations.get(event.getPlayer().getName());
			LightAPI.deleteLightSourceAndUpdate(LightType.DYNAMIC, loc);
		}

	}

	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		if (LightSource.getInstance().getTorchPlayers().contains(event.getPlayer().getName())) {
			if(!pLocations.containsKey(event.getPlayer())) return;
			Location loc = pLocations.get(event.getPlayer().getName());
			LightAPI.deleteLightSourceAndUpdate(LightType.DYNAMIC, loc);
		}

	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		if (LightSource.getInstance().getTorchPlayers().contains(event.getEntity().getName())) {
			if(!pLocations.containsKey(event.getEntity())) return;
			Location loc = pLocations.get(event.getEntity().getName());
			LightAPI.deleteLightSourceAndUpdate(LightType.DYNAMIC, loc);
		}

	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		if (LightSource.getInstance().getTorchPlayers().contains(event.getPlayer().getName())) {
			if(!pLocations.containsKey(event.getPlayer())) return;
			Location loc = pLocations.get(event.getPlayer().getName());
			LightAPI.deleteLightSourceAndUpdate(LightType.DYNAMIC, loc);
			LightSource.getInstance().getTorchPlayers().remove(event.getPlayer().getName());
			pLocations.remove(event.getPlayer().getName());
		}

	}

	@EventHandler
	public void onPlayerKick(PlayerKickEvent event) {

		if (LightSource.getInstance().getTorchPlayers().contains(event.getPlayer().getName())) {
			if(!pLocations.containsKey(event.getPlayer())) return;
			Location loc = pLocations.get(event.getPlayer().getName());
			LightAPI.deleteLightSourceAndUpdate(LightType.DYNAMIC, loc);
			LightSource.getInstance().getTorchPlayers().remove(event.getPlayer().getName());
			pLocations.remove(event.getPlayer().getName());
		}

	}

	@EventHandler
	public void onPlayerBedEnter(PlayerBedEnterEvent event) {
		if (LightSource.getInstance().getTorchPlayers().contains(event.getPlayer().getName())) {
			if(!pLocations.containsKey(event.getPlayer())) return;
			Location loc = pLocations.get(event.getPlayer().getName());
			LightAPI.deleteLightSourceAndUpdate(LightType.DYNAMIC, loc);
		}
	}

	@EventHandler
	public void onPlayerDropLight(PlayerDropItemEvent event) {
		if (LightSource.getInstance().getTorchPlayers().contains(event.getPlayer().getName())) {
			if(event.getPlayer().getItemInHand().getAmount() < 1){
			if(!pLocations.containsKey(event.getPlayer())) return;
			Location loc = pLocations.get(event.getPlayer().getName());
			LightAPI.deleteLightSourceAndUpdate(LightType.DYNAMIC, loc);
			LightSource.getInstance().getTorchPlayers().remove(event.getPlayer().getName());
			}
		}
	}

}