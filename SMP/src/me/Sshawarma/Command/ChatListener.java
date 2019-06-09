package me.Sshawarma.Command;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import net.md_5.bungee.api.ChatColor;

public class ChatListener implements Listener {
	
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
}
