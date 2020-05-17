package me.Sshawarma.Events;

import java.util.ArrayList;

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
	public void stopChestExplode(EntityExplodeEvent event) {
		//THE ISSUE WITH loc IS THAT IT IS LOCATION OF PLAYERDEATH, which is adding decimal stuff, NOT BLOCK, which is perfect integer.
		for(Block b : event.blockList()) {
			
		}
	}
	
	@EventHandler
	public void creeperStop(EntityExplodeEvent event) {
		
		//If command enables protection and entity is a creeper
		if(event.getEntityType() == EntityType.CREEPER && protection == true) {
			
			//Iterates through each block and records material and location
			int i=0;
			for(Block b : new ArrayList<Block>(event.blockList())) {
				
				Location loc = b.getLocation();
				Material mat = b.getType();

				if(b.getType() == Material.CHEST) {
					event.blockList().remove(i);
					i--;
				}
				
				if(mat!= Material.NETHER_PORTAL || mat != Material.CHEST) {
					
					new BukkitRunnable() {

						@Override
						public void run() {
							Bukkit.getServer().getWorld("world").getBlockAt(loc).setType(mat);		
						}
						
					}.runTaskLater(plugin, 200);
				}
				
				i++;
			}
			for(Block b : event.blockList()) {
				b.setType(event.getEntity().getLocation().add(0, 1, 0).getBlock().getType());
			}
		}
	}
}
	
