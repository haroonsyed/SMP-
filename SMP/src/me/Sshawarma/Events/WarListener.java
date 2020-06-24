package me.Sshawarma.Events;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;

import me.Sshawarma.SMP.Main;
import me.ryanhamshire.GriefPrevention.GriefPrevention;

public class WarListener implements Listener{
	
		
	@EventHandler
	public void isWarModeAllowed(PlayerMoveEvent event) {
		
		//Check player has moved.
		if(event.getTo().getBlockX() == event.getFrom().getBlockX() && event.getTo().getBlockY() == event.getFrom().getBlockY() && event.getTo().getBlockZ() == event.getFrom().getBlockZ()) {
			return;
		}
		

		
		//Else do all this
		Player player = event.getPlayer();
		Plugin plugin = Main.getPlugin(Main.class);
		FileConfiguration config = plugin.getConfig();
		boolean isWarringFaction = config.getBoolean("FactionSettings." + config.getString("PlayerSettings." + event.getPlayer().getUniqueId().toString() + ".faction") + ".war.isWarring");
		boolean ignoringClaims = GriefPrevention.instance.dataStore.getPlayerData(player.getUniqueId()).ignoreClaims;
		
		//Check if warmode is on
		if(!isWarringFaction) {
			return;
		}
		
		//Check if player is not in a claim
		if(GriefPrevention.instance.dataStore.getClaimAt(player.getLocation(), true, null) == null) {
			
			if(isWarringFaction && ignoringClaims == true) {
				//Only runs once because he wont have permission after
				player.performCommand("ignoreclaims");
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getDisplayName() + " permission set griefprevention.ignoreclaims false");
			}
			
			return;
		}
		
		
		
		String ownerName = GriefPrevention.instance.dataStore.getClaimAt(player.getLocation(), true, null).getOwnerName();
		UUID owner = Bukkit.getOfflinePlayer(ownerName).getUniqueId();
		boolean isInWarringTerritory = config.getBoolean("FactionSettings." + config.getString("PlayerSettings." + owner.toString() + ".faction") + ".war.isWarring");
		boolean haveAccess = false;
		
		//Error here, fix
		if(GriefPrevention.instance.dataStore.getClaimAt(player.getLocation(), true, null).allowAccess(player) == null) {
					haveAccess = true;
		}
		
		//USE FROM AND TO to track when the enter.
		if(isInWarringTerritory && isWarringFaction && haveAccess == false && ignoringClaims == false) {
			player.sendMessage("You are in warring faction's territory" + " OWNER: " + Bukkit.getOfflinePlayer(owner).getName());
			Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getDisplayName() + " permission set griefprevention.ignoreclaims true");
			player.performCommand("ignoreclaims");
		}
	}
	
	
}
