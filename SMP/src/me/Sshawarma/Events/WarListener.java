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
		Player player = event.getPlayer();
		Plugin plugin = Main.getPlugin(Main.class);
		FileConfiguration config = plugin.getConfig();
		
		
		String haveAccess = GriefPrevention.instance.dataStore.getClaimAt(player.getLocation(), true, null).allowAccess(player);
		String ownerName = GriefPrevention.instance.dataStore.getClaimAt(player.getLocation(), true, null).getOwnerName();
		UUID owner = Bukkit.getOfflinePlayer(ownerName).getUniqueId();
		boolean isInWarringTerritory = config.getBoolean("FactionSettings." + config.getString("PlayerSettings." + owner.toString() + ".faction") + ".war");
		boolean isWarringFaction = config.getBoolean("FactionSettings." + config.getString(player.getUniqueId().toString()) + ".war");
		
		if(isInWarringTerritory && isWarringFaction && haveAccess != null) {
			player.sendMessage("You are in warring faction's territory" + " OWNER: " + Bukkit.getOfflinePlayer(owner).getName());
			Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getDisplayName() + " permission set griefprevention.ignoreclaims true");
			player.performCommand("ignoreclaims");
		}
		else if(isWarringFaction) {
			//Only runs once because he wont have permission after
			player.performCommand("ignoreclaims");
			Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getDisplayName() + " permission set griefprevention.ignoreclaims false");
		}
	}
	
	
}
