package me.Sshawarma.Events;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.Plugin;

import me.Sshawarma.SMP.Main;
import net.md_5.bungee.api.ChatColor;

public class FactionAttack implements Listener{
	
	Plugin plugin = Main.getPlugin(Main.class);
	FileConfiguration config = plugin.getConfig();
	
	@EventHandler
	public void onAttack(EntityDamageByEntityEvent event) {
		
		if(event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
			
			String faction1 = plugin.getConfig().getString("PlayerSettings." + ((Player)event.getDamager()).getUniqueId().toString() + ".faction");
			String faction2 = plugin.getConfig().getString("PlayerSettings." + ((Player)event.getEntity()).getUniqueId().toString() + ".faction");

			//If the two are from different factions, check if one is peaceful. If so, then cancel the attack.
			if(!faction1.equalsIgnoreCase(faction2) && (config.getString("FactionSettings." + faction1 + ".peaceful").equals("true") || config.getString("FactionSettings." + faction2 + ".peaceful").equals("true"))){
				//AgniKai check. If one is in agnikai then both are, so just check one.
				HashMap<String, Location> agniKaiPlayers = me.Sshawarma.Command.AgniKai.ogLocations;
				if(!agniKaiPlayers.containsKey(event.getDamager().getUniqueId().toString())) {
					event.setCancelled(true);
					event.getDamager().sendMessage(ChatColor.DARK_RED + "Player is in peaceful faction: " + faction2 + "! Your's is: " + faction1 + ".");
				}
			}
			//If friendly fire is turned off
			if(faction1.equalsIgnoreCase(faction2) && config.getString("FactionSettings." + faction1 + ".friendlyFire").equalsIgnoreCase("false")) {
				event.setCancelled(true);
				event.getDamager().sendMessage(ChatColor.DARK_RED + "FriendlyFire is off!");
			}
		}
	}
	
}
