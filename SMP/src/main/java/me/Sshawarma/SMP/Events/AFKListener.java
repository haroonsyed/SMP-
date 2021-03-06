package me.Sshawarma.SMP.Events;

import java.util.ArrayList;
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
	private ArrayList<String> afk = new ArrayList<String>();
	
	public String getAFKMessage(){
		
		String afkMessage = ChatColor.YELLOW + "" + ChatColor.ITALIC + "[ServerInfo]: AFKPLAYERS are " + afk.toString();
		
		return afkMessage;
		
	}
	
	@Override
	public void run() {
		


		//If locations matches last known location, add them to afk
		for(Player p : Bukkit.getOnlinePlayers()) {
			
			if(lastLocations.containsKey(p.getUniqueId())) {
				
				Location lastLoc = lastLocations.get(p.getUniqueId());
				
				if(lastLoc.equals(p.getLocation()) && !afk.contains(p.getDisplayName())) {
					afk.add(p.getDisplayName());
				}
				else {
					if(afk.contains(p.getDisplayName())) {
						afk.remove(p.getDisplayName());
					}
					lastLocations.put(p.getUniqueId(), p.getLocation());
				}
			}
			else {
				lastLocations.put(p.getUniqueId(), p.getLocation());
			}
			
		}
		
		if(afk.size() > 0) {
			Bukkit.broadcastMessage(getAFKMessage());
		}
		

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
