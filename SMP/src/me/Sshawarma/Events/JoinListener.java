package me.Sshawarma.Events;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.Plugin;

import me.Sshawarma.SMP.Main;
import net.md_5.bungee.api.ChatColor;

public class JoinListener implements Listener{
	
	Plugin plugin = Main.getPlugin(Main.class);
	
	@EventHandler
	public void giveDefaults(PlayerJoinEvent event) {
		
		Plugin plugin = Main.getPlugin(Main.class);
		Player player = event.getPlayer();
		
		//If they arent already registered in config, give them the defaults
		if(!(plugin.getConfig().contains("PlayerSettings." + player.getDisplayName()))) {
			plugin.getConfig().set("PlayerSettings." + player.getDisplayName() + ".chatcolor", "&7");
			plugin.getConfig().set("PlayerSettings." + event.getPlayer().getDisplayName() + ".faction", "default");
			plugin.saveConfig();
			
			player.sendMessage(ChatColor.DARK_GRAY + "---------" + ChatColor.GREEN + "Your chat color by default is: "  + ChatColor.GRAY + "GRAY" + ChatColor.DARK_GRAY + "---------");
			player.sendMessage(ChatColor.DARK_GREEN + "If you would like to change your chatcolor, please use /chatcolor (desired color here w/o parenthesis)");
		}
		//Creates the default faction section
		if(!plugin.getConfig().contains("FactionSettings")) {
			plugin.getConfig().set("FactionSettings.default.peaceful", "false");
			plugin.getConfig().set("FactionSettings.default.friendlyFire", "true");
			plugin.saveConfig();
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void whiteListNotification(PlayerLoginEvent event) {
		//If the whitelist does not contain the player...
		if(!(Bukkit.getServer().getWhitelistedPlayers().contains(Bukkit.getServer().getOfflinePlayer(event.getPlayer().getName())))) {
			//Find op players and play a sound alerting them who the player is
			for(Player p : Bukkit.getServer().getOnlinePlayers()) {
				if(p.isOp()) {
					p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 10, 1);
					p.sendMessage(ChatColor.RED + "Non-Whitelisted player " + event.getPlayer().getName() + " is trying to join!");
				}
			}
		}
	}
	
	@EventHandler
	public void setFactionSpawn(PlayerRespawnEvent event) {
		if(!event.isBedSpawn() && !plugin.getConfig().getString(event.getPlayer().getName() + ".faction").equals("cousins")) {
			Location spawn = event.getRespawnLocation().add(2406, 0, -697);
			spawn.setY(event.getPlayer().getWorld().getHighestBlockYAt(spawn));
			event.getPlayer().teleport(spawn);
		}
	}
	@EventHandler
	public void setFactionSpawn(PlayerJoinEvent event) {
		if(!event.getPlayer().hasPlayedBefore() && !plugin.getConfig().contains(event.getPlayer().getDisplayName())) {
			Location spawn = Bukkit.getServer().getWorld("world").getSpawnLocation();
			spawn.add(2406, 0, -697);
			spawn.setY(event.getPlayer().getWorld().getHighestBlockYAt(spawn));
			event.getPlayer().teleport(spawn);
		}
	}
}
