package me.Sshawarma.Events;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;

import me.Sshawarma.SMP.Main;

public class ChatColorChanger implements Listener{
	
	Plugin plugin = Main.getPlugin(Main.class);
	
	@EventHandler
	public void changeColor(AsyncPlayerChatEvent event) {
		String msg = event.getMessage();
		Player player = event.getPlayer();
		//player.setDisplayName(plugin.getConfig().getString(player.getDisplayName()) + player.getDisplayName() + ChatColor.GOLD);
		//event.setMessage(plugin.getConfig().getString(player.getDisplayName()) + msg);
		
		//Pretty Obvious it  grabs config and changes chat message with colors based on OP level
		if(!(player.isOp())) {
			event.setFormat(ChatColor.DARK_GRAY + "<" +ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("PlayerSettings." + player.getDisplayName() + ".chatcolor")) + "" +ChatColor.BOLD + player.getDisplayName() + ChatColor.DARK_GRAY + ">"
				+ ChatColor.RESET + ChatColor.DARK_GRAY + ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("PlayerSettings." + player.getDisplayName() + ".chatcolor")) + msg);
		}
		
		else {
			event.setFormat(ChatColor.GOLD + "<" + ChatColor.RED + "" + ChatColor.BOLD +  player.getDisplayName() + ChatColor.GOLD + ">"
					+ChatColor.RESET + "" + ChatColor.RED  + "[ADMIN] " + ChatColor.RESET + "" + ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("PlayerSettings." + player.getDisplayName() + ".chatcolor")) + msg);
		}
		
	}
}
