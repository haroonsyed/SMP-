package me.Sshawarma.SMP.Events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.Sshawarma.SMP.Command.FindCommand;
import me.Sshawarma.SMP.Command.Messaging;
import me.Sshawarma.SMP.Main.Main;
import net.md_5.bungee.api.ChatColor;

public class ChatListener implements Listener {
	
	Main main;
	
	public ChatListener(Main main) {
		this.main = main;
	}
	
	
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
			String id = event.getPlayer().getUniqueId().toString();
			
			//Instantiate the messaging class and send this command to it
			Messaging messaging = new Messaging();
			messaging.messageFaction(id, msg);
			
			event.setCancelled(true);
		}
		else if(event.getMessage().contains("afk") || event.getMessage().contains("AFK")) {
			//Learn dependency Injection to get the main class's object of AFK.
			
			new BukkitRunnable() {
				
				public void run() {
					event.getPlayer().sendMessage(main.getAFKListener().getAFKMessage());
				}
				
			}.runTaskLater(Main.getPlugin(Main.class), 1);
			
		}
		
	}
	
	
}
