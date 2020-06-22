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
	public void creeperStop(EntityExplodeEvent event) {
		
		//If command enables protection and entity is a creeper
		if(event.getEntityType() == EntityType.CREEPER && protection == true) {
			
			//Optimize cpu usage. Not as cool but I don't wanna bother with decreased performance.
			event.blockList().clear();
			
		}
	}
}
	
