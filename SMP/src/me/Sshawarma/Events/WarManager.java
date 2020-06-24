package me.Sshawarma.Events;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitRunnable;

import me.Sshawarma.SMP.Main;
import me.ryanhamshire.GriefPrevention.GriefPrevention;

public class WarManager extends BukkitRunnable {
	
	PluginManager pm = Bukkit.getServer().getPluginManager();
	Plugin plugin = Main.getPlugin(Main.class);
	FileConfiguration config = plugin.getConfig();
	
	@Override
	public void run() {
		
		//Runs every minute. Checks if war should end for any given faction.
		for(String faction : config.getConfigurationSection("FactionSettings").getKeys(false)) {
			
			//Big number is 3 hours, reset back to non-war state
			if(config.getBoolean("FactionSettings." + faction + ".war.isWarring") && (System.currentTimeMillis() >= (config.getLong("FactionSettings." +faction + ".war.startTime") + 10800000))) {
				
				
				//Reset Config to Non-War Mode
				config.set("FactionSettings." + faction + ".war.isWarring", false);
				
				ArrayList<UUID> factionPlayers = new ArrayList<UUID>();
				//Get all players in the faction
				for(String p : config.getConfigurationSection("PlayerSettings").getKeys(false)) {
					
					if(config.getString("PlayerSettings." + p + ".faction").equals(faction)) {
						factionPlayers.add(UUID.fromString(p));
					}
					
				}
				
				for(UUID p : factionPlayers) {
					//Manage permissions or whatever for each player here.
					OfflinePlayer member = Bukkit.getOfflinePlayer(p);
					Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "lp user " + member.getName() + " permission set griefprevention.claims true");
					//Make sure that ignorclaims is in offmode
					if(GriefPrevention.instance.dataStore.getPlayerData(p).ignoreClaims) {
						if(Bukkit.getOfflinePlayer(p).isOnline()) {
							Bukkit.getServer().getPlayer(p).performCommand("ignoreclaims");
						}
						Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "lp user " + member.getName() + " permission set griefprevention.ignoreclaims false");
					}
				}
				
				
			}
		
		}
	}

}