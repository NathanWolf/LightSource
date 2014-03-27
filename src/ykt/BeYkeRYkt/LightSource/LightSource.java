package ykt.BeYkeRYkt.LightSource;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import ykt.BeYkeRYkt.LightSource.GUIMenu.GUIListener;
import ykt.BeYkeRYkt.LightSource.HeadLamp.HeadLampListener;
import ykt.BeYkeRYkt.LightSource.HeadLamp.HeadManager;
import ykt.BeYkeRYkt.LightSource.OtherListeners.EntityListener;
import ykt.BeYkeRYkt.LightSource.OtherListeners.ItemLightListener;
import ykt.BeYkeRYkt.LightSource.TorchLight.ItemManager;
import ykt.BeYkeRYkt.LightSource.TorchLight.TorchLightListener;

public class LightSource extends JavaPlugin {

	private static LightSource plugin;
	private ArrayList<Player> torch = new ArrayList<Player>();
	private ArrayList<Player> head = new ArrayList<Player>();
	private ItemManager im;
	private HeadManager hm;
	private LightAPI api;
	private boolean gui;

	// Enable
	@Override
	public void onEnable() {
		this.plugin = this;
		this.api = new LightAPI();
		this.hm = new HeadManager();
		this.im = new ItemManager();

		PluginDescriptionFile pdfFile = getDescription();

		try {
			FileConfiguration fc = getConfig();
			if (!new File(getDataFolder(), "config.yml").exists()) {
				fc.options().header(
						"LightSource v" + pdfFile.getVersion()
								+ " Configuration" + "\nHave fun :3"
								+ "\nby BeYkeRYkt");
				fc.addDefault("Debug", false);
				fc.addDefault("Enable-GUI", false);
				fc.addDefault("Advanced-Listener.TorchLight", false);
				fc.addDefault("Advanced-Listener.Entity", false);

				List<World> worlds = getServer().getWorlds();
				for (World world : worlds) {
					fc.addDefault("Worlds." + world.getName(), true);
				}
				fc.options().copyDefaults(true);
				saveConfig();
				fc.options().copyDefaults(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		createExampleTorch();
		createExampleHead();

		getItemManager().loadItems();
		getHeadManager().loadItems();

		Bukkit.getPluginManager().registerEvents(new TorchLightListener(), this);
		Bukkit.getPluginManager().registerEvents(new HeadLampListener(), this);

		// Advanced Item listener
		if (this.getConfig().getBoolean("Advanced-Listener.TorchLight")) {

			if (Bukkit.getPluginManager().getPlugin("BKCommonLib").isEnabled()) {
				this.getLogger().info(
						"Found BKCommonLib! Enabling Advanced item listener!");
				Bukkit.getPluginManager().registerEvents(
						new ItemLightListener(), this);
			} else {
				this.getLogger()
						.info("To work needed BKCommonLib. Advanced listener does not include.");
			}

		}

		// Advanced Entity listener
		if (this.getConfig().getBoolean("Advanced-Listener.Entity")) {

			if (Bukkit.getPluginManager().getPlugin("BKCommonLib").isEnabled()) {
				this.getLogger()
						.info("Found BKCommonLib! Enabling Advanced entity listener!");
				Bukkit.getPluginManager().registerEvents(new EntityListener(),
						this);
			} else {
				this.getLogger()
						.info("To work needed BKCommonLib. Advanced listener does not include.");
			}

		}

		this.gui = getConfig().getBoolean("Enable-GUI");	
		if(gui){
			Bukkit.getPluginManager().registerEvents(new GUIListener(), this);
		}
		
		getCommand("ls").setExecutor(new MainCommand());
		this.getLogger().info( pdfFile.getName() + " version " + pdfFile.getVersion() + " is now enabled. Have fun.");

	}

	public void createExampleTorch() {
		try {
			FileConfiguration fc = getItemManager().getConfig()
					.getSourceConfig();
			if (!new File(getDataFolder(), "TorchLight.yml").exists()) {

				fc.addDefault("DEMO.name", null);
				fc.addDefault("DEMO.material", "TORCH");
				fc.addDefault("DEMO.lightlevel", 14);

				fc.addDefault("Redstone.name", "REDSTONE_TEST");
				fc.addDefault("Redstone.material", "REDSTONE_TORCH_ON");
				fc.addDefault("Redstone.lightlevel", 8);
				
				fc.addDefault("COLOR_TEST.name", "&4TEST");
				fc.addDefault("COLOR_TEST.material", "LAVA_BUCKET");
				fc.addDefault("COLOR_TEST.lightlevel", 10);
				
				fc.options().copyDefaults(true);
				getItemManager().getConfig().saveSourceConfig();
				fc.options().copyDefaults(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void createExampleHead() {
		try {
			FileConfiguration fc = getHeadManager().getConfig()
					.getSourceConfig();
			if (!new File(getDataFolder(), "HeadLamp.yml").exists()) {

				fc.addDefault("DEMO.name", null);
				fc.addDefault("DEMO.material", "GLOWSTONE");
				fc.addDefault("DEMO.lightlevel", 10);

				fc.options().copyDefaults(true);
				getHeadManager().getConfig().saveSourceConfig();
				fc.options().copyDefaults(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onDisable() {
		
		for(Player players : Bukkit.getOnlinePlayers()){
			LightAPI.deleteLightSourceAndUpdate(players.getLocation().getBlock().getLocation());
		}
		
		HandlerList.unregisterAll(this);
		
		this.torch.clear();
		this.head.clear();
		this.api = null;
		this.gui = false;
		
		hm.getList().clear();
		im.getList().clear();
		
		this.hm = null;
		this.im = null;
	}

	// Plugin method
	public static LightSource getInstance() {
		return plugin;
	}

	public ArrayList<Player> getTorchPlayers() {
		return torch;
	}

	public ArrayList<Player> getHeadPlayers() {
		return head;
	}

	public HeadManager getHeadManager() {
		return hm;
	}

	public ItemManager getItemManager() {
		return im;
	}

	public LightAPI getAPI() {
		return api;
	}
	// End

	public boolean getGUI() {
		return gui;
	}

}