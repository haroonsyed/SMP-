package me.Sshawarma.Events;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.Sshawarma.SMP.Main;
import net.md_5.bungee.api.ChatColor;

public class Beds implements Listener{
	Plugin plugin = Main.getPlugin(Main.class);
	
	//Player is player. Boolean indicated if the timerStarted is active for them.
	private HashMap<String, Boolean> tag = new HashMap<String, Boolean>();
	
	
	@EventHandler
	public void sleepNotify(PlayerBedEnterEvent event) {
		Player player = event.getPlayer();
		
		//Error will occur when it is thundering...who cares
		if((Bukkit.getServer().getWorld("world").getTime()<12541)) {
			return;
		}
		else if(tag.containsKey(player.getDisplayName())==false) {
			tag.put(player.getDisplayName(), false);
			new BukkitRunnable() {

				@Override
				public void run() {
					//if player is on spamTimer, then don't broadcast
					if(tag.get(player.getDisplayName()) == true) {
						this.cancel();
						player.sendMessage(ChatColor.DARK_GRAY + "You are still on spamTimer!");
						return;
					}
					//if player is not on spamTimer, then broadcast
					else if(tag.get(player.getDisplayName()).equals(false)){
						Bukkit.getServer().broadcastMessage(ChatColor.GOLD + "" + ChatColor.BOLD + player.getDisplayName() + " is sleeping now!");

					}
					
				}
			}.runTaskTimerAsynchronously(plugin, 0, 380);
		}
		
		
	}
	
	@EventHandler
	public void sleepLeave(PlayerBedLeaveEvent event) {
		Player player = event.getPlayer();
		//if(Bukkit.getServer().getWorld("world").getTime()<=12541 || Bukkit.getServer().getWorld("world").isThundering()==false) {
		//	player.sendMessage("not daytime");
		//	sleeping = false;
		//	return;
		//}
		
		
		//if Player leaves bed, put them on spamTimer
		if(tag.get(player.getDisplayName())!=null) {
			if(tag.get(player.getDisplayName()).equals(false)) {
				
				//Starts timer and enables variables to stop messages
				Bukkit.getServer().broadcastMessage(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + player.getDisplayName() + " is no longer sleeping!");	
				tag.put(player.getDisplayName(), true);
				event.getPlayer().sendMessage(ChatColor.DARK_GRAY + "spamTimer is on");
				
				
				new BukkitRunnable() {
					
					@Override
					public void run() {
						player.sendMessage(ChatColor.DARK_GRAY + "You are no longer on spamTimer");
						tag.remove(player.getDisplayName());
					}
						
					}.runTaskLaterAsynchronously(plugin, 140);
				
			}
			
		}		
		
		
	}
		
		

}
