package me.Sshawarma.Events;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;

import me.Sshawarma.Command.Commands;
import me.Sshawarma.SMP.Main;
import net.md_5.bungee.api.ChatColor;

public class ChatListener implements Listener {
	
	Plugin plugin = Main.getPlugin(Main.class);
	
	@EventHandler
	public void onResponse(AsyncPlayerChatEvent event) {
		
		//if accept
		if(Commands.getSendersSize()!= 0 && event.getMessage().equalsIgnoreCase("accept")) {
			if(event.getPlayer().getDisplayName() == Commands.getReciever(Commands.getArrayLoc())) {
				event.setCancelled(true);
				event.getPlayer().sendMessage(ChatColor.GREEN + "Response Sent!");
				Commands.setAccept(true);
			}
		}
		
		//if deny
		if(Commands.getSendersSize()!=0 && event.getMessage().equalsIgnoreCase("deny")) {
			if(event.getPlayer().getDisplayName() == Commands.getReciever(Commands.getArrayLoc())) {
				event.setCancelled(true);
				Commands.setDeny(true);
			}
		}
	}
	
	//Changes player message to that specified in chatcolor.
	@EventHandler
	public void changeColor(AsyncPlayerChatEvent event) {
		
		
		Player player = event.getPlayer();
		List<String> players = plugin.getConfig().getStringList("Players");
		List<String> colors = plugin.getConfig().getStringList("Colors");
		
		
		//Color Dark_GRAY
		/*
		if(colors.get(players.indexOf(player.getDisplayName())) == "DARK_GRAY") {
			String msg = event.getMessage();
			event.setCancelled(true);
			player.setDisplayName(ChatColor.DARK_GRAY + "[Player] " + player.getDisplayName());
			player.sendMessage(ChatColor.DARK_GRAY + msg);
		}
		else if() {
			
		}
		*/
		
		String msg = event.getMessage();
		event.setCancelled(true);
		player.setDisplayName(colors.get(players.indexOf(player.getDisplayName())) + "[Player] " +  player.getDisplayName());
		player.sendMessage(colors.get(players.indexOf(player.getDisplayName())) + msg);
		
	}
	
	
}
