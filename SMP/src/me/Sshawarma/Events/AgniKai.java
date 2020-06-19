package me.Sshawarma.Events;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.Sshawarma.SMP.Main;

public class AgniKai implements Listener{
	
	//Checks if player died in agni kai. If so, respawn with same amount of xp and back at the agni kai area.
	@EventHandler
	public void onDeathInAgniKai(PlayerDeathEvent event) {
		if(me.Sshawarma.Command.AgniKai.ogLocations.containsKey(event.getEntity().getUniqueId().toString())) {
			event.setKeepLevel(true);
			event.setKeepInventory(true);
			event.getDrops().clear();
		}
	}
	
	@EventHandler
	public void respawn(PlayerRespawnEvent event) {
		if(me.Sshawarma.Command.AgniKai.ogLocations.containsKey(event.getPlayer().getUniqueId().toString())) {
			Plugin plugin = Main.getPlugin(Main.class);
			FileConfiguration config = plugin.getConfig();
			Location loc = Bukkit.getWorld("world").getSpawnLocation();
			loc.setX(config.getDouble("AgniKai.Location.X"));
			loc.setY(config.getDouble("AgniKai.Location.Y"));
			loc.setZ(config.getDouble("AgniKai.Location.Z"));
			event.getPlayer().sendMessage("Respawning at AgniKai arena!");
			event.setRespawnLocation(loc);
			
			new BukkitRunnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					event.getPlayer().teleport(loc);
				}
				
			}.runTaskLater(plugin, 4);
			
		}
	}
	
}
