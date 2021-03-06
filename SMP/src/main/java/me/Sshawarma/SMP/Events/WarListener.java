package me.Sshawarma.SMP.Events;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.Sshawarma.SMP.Main.Main;
import me.ryanhamshire.GriefPrevention.DataStore;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import net.md_5.bungee.api.ChatColor;

public class WarListener implements Listener{
	
	Plugin plugin = Main.getPlugin(Main.class);
	FileConfiguration config = plugin.getConfig();
	DataStore datastore = null;
		
	@EventHandler
	public void isWarModeAllowed(PlayerMoveEvent event) {
		
		//Check player has moved.
		if(event.getTo().getBlockX() == event.getFrom().getBlockX() && event.getTo().getBlockY() == event.getFrom().getBlockY() && event.getTo().getBlockZ() == event.getFrom().getBlockZ()) {
			return;
		}
		

		
		//Else do all this
		Player player = event.getPlayer();
		datastore = GriefPrevention.instance.dataStore;
		
		boolean isWarringFaction = config.getBoolean("FactionSettings." + config.getString("PlayerSettings." + event.getPlayer().getUniqueId().toString() + ".faction") + ".war.isWarring");
		boolean ignoringClaims = datastore.getPlayerData(player.getUniqueId()).ignoreClaims;
		
		//Check if warmode is on
		if(!isWarringFaction) {
			return;
		}
		
		//Check if player is not in a claim
		if(datastore.getClaimAt(player.getLocation(), true, null) == null) {
			
			if(isWarringFaction && ignoringClaims == true) {
				//Only runs once because he wont have permission after
				player.performCommand("ignoreclaims");
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getDisplayName() + " permission set griefprevention.ignoreclaims false");
			}
			
			return;
		}
		
		
		UUID owner = datastore.getClaimAt(player.getLocation(), true, null).ownerID;
		
		//Admin Claims
		if(owner == null) {
			return;
		}
		
		String ownerFaction = config.getString("PlayerSettings." + owner.toString() + ".faction");
		String oFChatColor = config.getString("FactionSettings." + ownerFaction + ".color");
		boolean isInWarringTerritory = config.getBoolean("FactionSettings." + ownerFaction + ".war.isWarring");
		boolean haveAccess = false;
		
		//Sees if player can build in claim normally
		if(datastore.getClaimAt(player.getLocation(), true, null).allowAccess(player) == null) {
					haveAccess = true;
					//Do nothing if they already have access
					return;
		}
		
		//USE FROM AND TO to track when the enter.
		if(isInWarringTerritory && isWarringFaction && haveAccess == false && ignoringClaims == false) {
			player.sendMessage(ChatColor.RED + "[WAR]" + ChatColor.ITALIC + "You are in warring faction's territory " + ChatColor.RESET + ""  + ChatColor.translateAlternateColorCodes('&', oFChatColor) + "" + ChatColor.BOLD + ownerFaction);
			Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getDisplayName() + " permission set griefprevention.ignoreclaims true");
			//since lp performs asynch, this must be delayed
			new BukkitRunnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					player.performCommand("ignoreclaims");
				}
				
			}.runTaskLater(plugin, 1);
			
		}
	}
	
	
}
