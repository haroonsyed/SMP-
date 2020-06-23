package me.Sshawarma.Events;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitRunnable;

import me.Sshawarma.SMP.Main;

public class WarManager extends BukkitRunnable {
	
	PluginManager pm = Bukkit.getServer().getPluginManager();
	Plugin plugin = Main.getPlugin(Main.class);
	FileConfiguration config = plugin.getConfig();
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		//Runs every minute. Checks if war should end for any given faction.
		for(String faction : config.getConfigurationSection("FactionSettings").getKeys(false)) {
			
			//Big number is 3 hours, reset back to non-war state
			if(config.getBoolean("FactionSettings." + faction + ".war") && (System.currentTimeMillis() >= (config.getLong("FactionSettings." +faction + ".war.startTime") + 10800000))) {
				//Reset
				
				ArrayList<UUID> factionPlayers = new ArrayList<UUID>();
				//Get all players in the faction
				for(String p : config.getConfigurationSection("PlayerSettings").getKeys(false)) {
					
					if(config.getString("PlayerSettings." + p + ".faction").equals(faction)) {
						factionPlayers.add(UUID.fromString(p));
					}
					
				}
				
				//Reset
				for(UUID p : factionPlayers) {
					Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "lp user " + Bukkit.getPlayer(p).getDisplayName() + " permission set griefprevention.claims true");
					Bukkit.getPlayer(p).performCommand("untrust all");
					for(String id : config.getConfigurationSection("PlayerSettings").getKeys(false)) {
						if(config.getString("PlayerSettings." + id + ".faction").equals(faction)) {
							//Add check if player is online!
							Bukkit.getPlayer(p).performCommand("permissiontrust " + Bukkit.getPlayer(UUID.fromString(id)).getDisplayName());
						}
					}
				}
				
				
			}
		
		}
	}

}
