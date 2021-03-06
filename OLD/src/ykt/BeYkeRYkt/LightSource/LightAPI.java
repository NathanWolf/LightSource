package ykt.BeYkeRYkt.LightSource;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import ykt.BeYkeRYkt.LightSource.Light.Light;
import ykt.BeYkeRYkt.LightSource.NMS.NMSHandler_v1_7_R1;
import ykt.BeYkeRYkt.LightSource.NMS.NMSHandler_v1_7_R2;
import ykt.BeYkeRYkt.LightSource.NMS.NMSHandler_v1_7_R3;
import ykt.BeYkeRYkt.LightSource.NMS.NMSHandler_v1_7_R4;
import ykt.BeYkeRYkt.LightSource.NMS.NMSInterface;


public class LightAPI{	

    private String version = null;
    private static NMSInterface nms;
    private static ArrayList<Light> list = new ArrayList<Light>();

    public LightAPI(){
    	this.version = Bukkit.getBukkitVersion();
	    this.version = this.version.substring(0, 6);
	    this.version = this.version.replaceAll("-", "");
	    initNMS();
    }
    
    private void initNMS(){
    	if(this.version.startsWith("1.7.2")){
    		nms = new NMSHandler_v1_7_R1();
    		LightSource.getInstance().getLogger().info("Founded CraftBukkit/Spigot 1.7.2! Using NMSHandler for 1.7.2.");
    	}else if(this.version.startsWith("1.7.5")){
    		nms = new NMSHandler_v1_7_R2();
    		LightSource.getInstance().getLogger().info("Founded CraftBukkit/Spigot 1.7.5! Using NMSHandler for 1.7.5.");
    	}else if(this.version.startsWith("1.7.8") || this.version.startsWith("1.7.9")){
    		nms = new NMSHandler_v1_7_R3();
    		LightSource.getInstance().getLogger().info("Founded CraftBukkit/Spigot 1.7.8/9! Using NMSHandler for 1.7.8/9.");
    	}else if(this.version.startsWith("1.7.10")){
    		nms = new NMSHandler_v1_7_R4();
    		LightSource.getInstance().getLogger().info("Founded CraftBukkit/Spigot 1.7.10! Using NMSHandler for 1.7.10.");
    	}else{
    		LightSource.getInstance().getLogger().info("Sorry. Your MC server not supported this plugin.");
    		Bukkit.getPluginManager().disablePlugin(LightSource.getInstance());
    	}
    }
    
	public static Light checkEntityID(Entity entity){
		int index;
		for(index = LightAPI.getSources().size() - 1; index >= 0; --index){
		Light lights = LightAPI.getSources().get(index);
		if(entity.getEntityId() == lights.getOwner().getEntityId()){
			return lights;
		}
		}
		return null;
	}
	
	public NMSInterface getNMSHandler(){
		return nms;
	}
    
    /**
     * Create light with level at a location. 
     * @param loc - which block to update.
     * @param level - the new light level.
     * @param flag - Static light ?
     */
    public static void createLightSource(Location loc, int level, boolean flag) {
    	nms.createLightSource(loc, level, flag);
    }
    
    /**
     * Delete light with level at a location.
     * @param loc - which block to update.
     */
    public static void deleteLightSource(Location loc){
    	nms.deleteLightSource(loc);
    }
    
	/**
	* Gets all the chunks touching/diagonal to the chunk the location is in and updates players with them.
	* @param chunk - Bukkit chunk
	* @param loc - Location for update
	* @deprecated - prost))
	*/
    @Deprecated
    public static void updateChunk(Location loc, Chunk chunk) {
    	//nms.updateChunk(loc, chunk);
    }
    
    public static void initWorlds(){
    	nms.initWorlds();
    }
    
    public static void unloadWorlds(){
    	nms.unloadWorlds();
    }
    
	/**
	 * @return the list
	 */
	public static ArrayList<Light> getSources() {
		return list;
	}
	
	public static void addSource(Light light){
		getSources().add(light);
	}
	
	public static void removeSource(Light light){
		getSources().remove(light);
	}
	
	public static Light getSource(Location loc){
		for(Light light : getSources()){
			if(light.getLocation().getBlockX() == loc.getBlockX()
					&& light.getLocation().getBlockY() == loc.getBlockY()
					&& light.getLocation().getBlockZ() == loc.getBlockZ()){
				return light;
			}
		}
		return null;
	}
}