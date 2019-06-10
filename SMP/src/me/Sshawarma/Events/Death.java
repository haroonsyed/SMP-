package me.Sshawarma.Events;

import java.util.Collection;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import net.md_5.bungee.api.ChatColor;

public class Death implements Listener{
	
	//Keep track of active beacons
	private HashMap<Location, Boolean> beacons = new HashMap<Location, Boolean>();
	
	@EventHandler
	public void placeBeacon(PlayerDeathEvent event) {
		if(event.getEntity() instanceof Player) {
			
			//Variables
			Player player = event.getEntity();
			Location dLoc = event.getEntity().getLocation();
			
			//Moves standard death message to before coordinate message
			for (Player onlinePlayers : Bukkit.getOnlinePlayers()){
				onlinePlayers.playSound(onlinePlayers.getLocation(), Sound.ENTITY_VILLAGER_DEATH, 10, 1);
			}
			String deathMessage = ChatColor.DARK_RED + "" + ChatColor.BOLD +event.getDeathMessage().toString();
			event.setDeathMessage(null);
			Bukkit.getServer().broadcastMessage(deathMessage);
			
			//Tells Player Where their death occurred
			Bukkit.getServer().broadcastMessage(ChatColor.BOLD + "" + ChatColor.WHITE + player.getDisplayName() + "'s death coordinates are: " + ChatColor.LIGHT_PURPLE + event.getEntity().getLocation().getBlockX() + 
				ChatColor.GREEN + " " + event.getEntity().getLocation().getBlockY() +ChatColor.BLUE + " " + event.getEntity().getLocation().getBlockZ() + ChatColor.WHITE + " in " + event.getEntity().getWorld().getName());

			//WIP Replaces death block with active beacon
			
			
		}
		
	}
}
