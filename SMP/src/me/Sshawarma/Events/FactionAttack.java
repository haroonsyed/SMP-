package me.Sshawarma.Events;

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
	
	@EventHandler
	public void onAttack(EntityDamageByEntityEvent event) {
		
		if(event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
			
			String faction1 = plugin.getConfig().getString("PlayerSettings." + event.getDamager().getName() + ".faction");
			String faction2 = plugin.getConfig().getString("PlayerSettings." + event.getEntity().getName() + ".faction");
			FileConfiguration config = plugin.getConfig();
			//If the two are from different factions, check if one is peaceful. If so, then cancel the attack.
			if(!faction1.equalsIgnoreCase(faction2) && (config.getString("FactionSettings." + faction1 + "peaceful").equals("true") || config.getString("FactionSettings." + faction2 + "peaceful").equals("true"))){
				event.setCancelled(true);
				event.getDamager().sendMessage(ChatColor.DARK_RED + "Player is in peaceful faction: " + plugin.getConfig().getString(event.getEntity().getName() + ".faction") + "! Your's is: " + plugin.getConfig().getString(event.getDamager().getName() + ".faction") + ".");
			}
			//If friendly fire is turned off
			if(faction1.equalsIgnoreCase(faction2) && config.getString("FactionSettings" + faction1 + "friendlyFire").equalsIgnoreCase("false")) {
				event.setCancelled(true);
				event.getDamager().sendMessage(ChatColor.DARK_RED + "FriendlyFire is off!");
			}
		}
	}
	
}
