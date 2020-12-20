package main.java.me.Sshawarma.SMP.Events;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;

import main.java.me.Sshawarma.SMP.Main.Main;

public class ChatColorChanger implements Listener{
	
	Plugin plugin = Main.getPlugin(Main.class);
	FileConfiguration config = plugin.getConfig();
	
	@EventHandler
	public void changeColor(AsyncPlayerChatEvent event) {
		String msg = event.getMessage();
		Player player = event.getPlayer();
		String faction = config.getString("PlayerSettings." + player.getUniqueId().toString()+ ".faction");
		String colorCodeFac = config.getString("FactionSettings." + faction + ".color");
		String colorCodePl  = config.getString("PlayerSettings." + player.getUniqueId().toString() + ".chatcolor");
		
		//Factioncolor
		if(!config.contains("FactionSettings." + faction + ".color")) {
			config.set("FactionSettings." + faction + ".color", "&f");
			plugin.saveConfig();
		}		
		
		//Pretty Obvious it  grabs config and changes chat message with colors based on OP level
		//Concatenating like this is a nightmare though.
		if(!(player.isOp())) {
			event.setFormat(ChatColor.DARK_GRAY + "<" +ChatColor.translateAlternateColorCodes('&', colorCodePl) + "" +ChatColor.BOLD + player.getDisplayName() + ChatColor.DARK_GRAY + ">"
				+ ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', colorCodeFac) + "[" + faction  + "] "+ "" +  ChatColor.RESET + ""
				+ ChatColor.translateAlternateColorCodes('&', colorCodePl) + msg);
		}
		
		else {
			event.setFormat(ChatColor.GOLD + "<" + ChatColor.translateAlternateColorCodes('&', colorCodePl) + "" + ChatColor.BOLD +  player.getDisplayName() + ChatColor.GOLD + ">"
					+ChatColor.RESET + "" + ChatColor.RED  + "[ADMIN]" + ChatColor.RESET + "" +
					ChatColor.translateAlternateColorCodes('&', colorCodeFac) + "[" + faction  + "] " + "" + ChatColor.RESET + "" +
					ChatColor.translateAlternateColorCodes('&', colorCodePl) + msg);
		}
		
	}
}
