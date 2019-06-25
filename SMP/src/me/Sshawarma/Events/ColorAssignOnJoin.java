package me.Sshawarma.Events;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import me.Sshawarma.SMP.Main;
import net.md_5.bungee.api.ChatColor;

public class ColorAssignOnJoin implements Listener{
	
	Plugin plugin = Main.getPlugin(Main.class);
	
	@EventHandler
	public void playerToConfig(PlayerJoinEvent event) {
		
		Player player = event.getPlayer();
		List<String> players = plugin.getConfig().getStringList("Players");
		List<String> colors = plugin.getConfig().getStringList("Colors");
		
		Boolean PlayerinConfig = false;
		
		//For Each player in the players list
		//If name is found, set PlayerinConfig to true and stop
		for(String p : players) {
			if(p == player.getDisplayName()) {
				
				PlayerinConfig = true;
				return;
				
			}
			
		}
		
		
		//If player has never joined before while plugin installed, then a chatcolor is assigned
		//and they are added to the config
		if(PlayerinConfig==false) {
			
			player.sendMessage(ChatColor.BOLD + "" + ChatColor.MAGIC + "Choose a ChatColor with /chatcolor! Default: " + ChatColor.DARK_GRAY + "DARK_GREY");
			players.add(player.getDisplayName());
			colors.add(players.indexOf(player.getDisplayName()), ChatColor.DARK_GRAY.toString());
			
		}
		
		
	}
	
}
