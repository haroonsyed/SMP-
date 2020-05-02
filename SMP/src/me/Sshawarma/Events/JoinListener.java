package me.Sshawarma.Events;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import me.Sshawarma.SMP.Main;
import net.md_5.bungee.api.ChatColor;

public class JoinListener implements Listener{
	@EventHandler
	public void giveDefaults(PlayerJoinEvent event) {
		
		Plugin plugin = Main.getPlugin(Main.class);
		Player player = event.getPlayer();
		
		//If they arent already registered in config, give them the defaults
		if(!(plugin.getConfig().contains(player.getDisplayName()))) {
			plugin.getConfig().set(player.getDisplayName(), "&7");
			plugin.saveConfig();
			
			player.sendMessage(ChatColor.DARK_GRAY + "---------" + ChatColor.GREEN + "Your chat color by default is: "  + ChatColor.GRAY + "GRAY" + ChatColor.DARK_GRAY + "---------");
			player.sendMessage(ChatColor.DARK_GREEN + "If you would like to change your chatcolor, please use /chatcolor (desired color here w/o parenthesis)");
			
		}
	}
	
	@EventHandler
	public void whiteListNotification(PlayerJoinEvent event) {
		//If the whitelist does not contain the player...
		if(!Bukkit.getServer().getWhitelistedPlayers().contains(Bukkit.getServer().getOfflinePlayer(event.getPlayer().getUniqueId()))) {
			//Find op players and play a sound alerting them who the player is
			for(Player p : Bukkit.getServer().getOnlinePlayers()) {
				if(p.isOp()) {
					p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 10, 1);
					p.sendMessage(ChatColor.RED + "Non-Whitelisted player " + event.getPlayer().getName() + " is trying to join!");
				}
			}
		}
	}
}
