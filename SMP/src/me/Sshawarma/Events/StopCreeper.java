package me.Sshawarma.Events;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.Sshawarma.SMP.Main;

public class StopCreeper implements Listener{
	
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
		
		if(event.getEntityType() == EntityType.CREEPER && protection == true) {
			for(Block b : event.blockList()) {
				new BukkitRunnable() {

					@Override
					public void run() {
						if(b.getType()!= Material.CHEST) {
							Bukkit.getServer().getWorld("world").getBlockAt(b.getLocation()).setType(b.getType());
						}
						else {
							Chest chest = (Chest) b;
							Bukkit.getServer().getWorld("world").getBlockAt(b.getLocation()).setType(Material.CHEST);
							Chest newChest = (Chest) Bukkit.getServer().getWorld("world").getBlockAt(b.getLocation());
							newChest.getInventory().setContents(chest.getInventory().getContents());
						}

					}
					
				}.runTaskLaterAsynchronously(plugin, 20);
			}
		}
	}
	
}
