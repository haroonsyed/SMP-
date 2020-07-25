package me.Sshawarma.Events;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.ChatColor;

public class AFKListener extends BukkitRunnable{
	
	private HashMap<UUID, Location> lastLocations = new HashMap<UUID, Location>();
	
	@Override
	public void run() {
		
		for(Player p : Bukkit.getOnlinePlayers()) {
			
			if(lastLocations.containsKey(p.getUniqueId())) {
				if(lastLocations.get(p.getUniqueId()) == p.getLocation().getBlock().getLocation()) {
					Bukkit.getServer().broadcastMessage(ChatColor.YELLOW + "" + ChatColor.ITALIC + "[ServerInfo]: Player " + p.getDisplayName() + " is AFK!");
				}
				else {
					lastLocations.put(p.getUniqueId(), p.getLocation().getBlock().getLocation());
				}
			}
			else {
				lastLocations.put(p.getUniqueId(), p.getLocation().getBlock().getLocation());
			}
			
		}
		
		//Called every minute or so, so two for loops does not hit efficiency
		//Removes offline players
		
		
		Iterator<Entry<UUID, Location>> it = lastLocations.entrySet().iterator();
		
		while(it.hasNext()) {
			
			Map.Entry<UUID, Location> data = it.next();
			if(!Bukkit.getOfflinePlayer(data.getKey()).isOnline()) {
				it.remove();
			}
			
		}
		
		
	}
	
	
	
}
