package me.Sshawarma.SMP.Events;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.Plugin;
import me.Sshawarma.SMP.Main.Main;
import net.md_5.bungee.api.ChatColor;

public class AgniKai implements Listener{
	
	Plugin plugin = Main.getPlugin(Main.class);
	FileConfiguration config = plugin.getConfig();
	
	//Checks if player died in agni kai. If so, respawn with same amount of xp and back at the agni kai area.
	@EventHandler
	public void onDeathInAgniKai(PlayerDeathEvent event) {
		if(me.Sshawarma.SMP.Command.AgniKai.ogLocations.containsKey(event.getEntity().getUniqueId().toString())) {
			event.setKeepLevel(true);
			event.setKeepInventory(true);
			event.getDrops().clear();
			event.setDroppedExp(0);
			for(Player p : Bukkit.getServer().getOnlinePlayers()) {
				p.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 10, 0.75f);
			}
		}
	}
	
	@EventHandler
	public void respawn(PlayerRespawnEvent event) {
		if(me.Sshawarma.SMP.Command.AgniKai.ogLocations.containsKey(event.getPlayer().getUniqueId().toString())) {
			
			Location loc = Bukkit.getWorld("world").getSpawnLocation();
			loc.setX(config.getDouble("AgniKai.Location.X"));
			loc.setY(config.getDouble("AgniKai.Location.Y"));
			loc.setZ(config.getDouble("AgniKai.Location.Z"));
			event.getPlayer().sendMessage("Respawning at AgniKai arena!");
			
			event.setRespawnLocation(loc);
		}
	}
	
	@EventHandler
	public void tpOut(PlayerQuitEvent event) {
		//If in agnikai teleport them out
		UUID id = event.getPlayer().getUniqueId();
		if(me.Sshawarma.SMP.Command.AgniKai.ogLocations.containsKey(id.toString())) {
			Bukkit.getOfflinePlayer(id).getPlayer().teleport(me.Sshawarma.SMP.Command.AgniKai.ogLocations.get(id.toString()));
			me.Sshawarma.SMP.Command.AgniKai.ogLocations.remove(id.toString());
		}
		
	}
	
	//Stops players from dropping items
	@EventHandler
	public void stopTrades(PlayerDropItemEvent event) {
		Player p = event.getPlayer();
		if(me.Sshawarma.SMP.Command.AgniKai.ogLocations.containsKey(p.getUniqueId().toString())) {
			event.setCancelled(true);
			p.sendMessage(ChatColor.RED + "No trading items in the arena!");
		}
	}
	
}
