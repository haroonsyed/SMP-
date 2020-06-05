package me.Sshawarma.Events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import me.Sshawarma.Command.FindCommand;
import me.Sshawarma.Command.Messaging;
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
		else if(FindCommand.getSendersSize()!=0 && event.getMessage().equalsIgnoreCase("deny")) {
			if(event.getPlayer().getDisplayName() == FindCommand.getReciever(FindCommand.getArrayLoc())) {
				event.setCancelled(true);
				FindCommand.setDeny(true);
			}
		}
		//If faction chat
		else if(event.getMessage().charAt(0) == '@') {
			String msg = event.getMessage().substring(1);
			String name = event.getPlayer().getDisplayName();
			
			//Instantiate the messaging class and send this command to it
			Messaging messaging = new Messaging();
			messaging.messageFaction(name, msg);
			
			event.setCancelled(true);
		}
		
	}
	
	
}
