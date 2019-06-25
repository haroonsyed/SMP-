package me.Sshawarma.Events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import me.Sshawarma.Command.FindCommand;
import net.md_5.bungee.api.ChatColor;

public class ChatListener implements Listener {
	

	
	
	@EventHandler
	public void onResponse(AsyncPlayerChatEvent event) {
		//if accept
		if(FindCommand.getSendersSize()!= 0 && event.getMessage().equalsIgnoreCase("accept")) {
			if(event.getPlayer().getDisplayName() == FindCommand.getReciever(FindCommand.getArrayLoc())) {
				event.setCancelled(true);
				event.getPlayer().sendMessage(ChatColor.GREEN + "Response Sent!");
				FindCommand.setAccept(true);
			}
		}
		
		//if deny
		if(FindCommand.getSendersSize()!=0 && event.getMessage().equalsIgnoreCase("deny")) {
			if(event.getPlayer().getDisplayName() == FindCommand.getReciever(FindCommand.getArrayLoc())) {
				event.setCancelled(true);
				FindCommand.setDeny(true);
			}
		}
	}
	
	
}
