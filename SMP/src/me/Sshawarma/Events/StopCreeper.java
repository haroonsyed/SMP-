package me.Sshawarma.Events;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.Sshawarma.SMP.Main;

public class StopCreeper implements Listener{
	
	
	//Methods ot add command to disable creeper protection
	Plugin plugin = Main.getPlugin(Main.class);
	
	private static boolean protection = true;	
	
	public static Boolean getProtection() {
		return protection;
	}
	
	public static void setProtection(Boolean state) {
		protection = state;
	}
	
	@EventHandler
	public void creeperStop(EntityExplodeEvent event) {
		
		//If command enables protection and entity is a creeper
		if(event.getEntityType() == EntityType.CREEPER && protection == true) {
			
			//Iterates through each block and records material and location
			for(Block b : event.blockList()) {
				
				
				Location loc = b.getLocation();
				Material mat = b.getType();
				
				
				//If non chest, it will Simply Replace that block. Runnable is for delay
				
				if(mat!= Material.NETHER_PORTAL) {
					
					new BukkitRunnable() {

						@Override
						public void run() {
							
							Bukkit.getServer().getWorld("world").getBlockAt(loc).setType(mat);
							
						}
						
					}.runTaskLater(plugin, 200);
					
				}
				
				//Else it will get the chest contents and replace them
				//Disabled for now because doublechests dont seem to work with this
				
				/*else {
					Chest chest = (Chest) b.getState();
					ItemStack[] inven = chest.getSnapshotInventory().getContents();
					chest.getInventory().clear();
					new BukkitRunnable() {

						@Override
						public void run() {
							
							Bukkit.getServer().getWorld("world").getBlockAt(loc).setType(Material.CHEST);
							Chest newChest = (Chest) Bukkit.getServer().getWorld("world").getBlockAt(loc).getState();
							newChest.getInventory().setContents(inven);
							
						}
						
					}.runTaskLater(plugin, 200);
					
				
				}
				
				*/
				b.setType(Material.AIR);
				
			}
					

		}
	}
}
	
