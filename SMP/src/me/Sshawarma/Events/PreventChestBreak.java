package me.Sshawarma.Events;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;

import net.md_5.bungee.api.ChatColor;

public class PreventChestBreak implements Listener{
	
	
	@EventHandler
	public void stopChestBreak(BlockDamageEvent event) {
		//THE ISSUE WITH loc IS THAT IT IS LOCATION OF PLAYERDEATH, which is adding decimal stuff, NOT BLOCK, which is perfect integer. 
		
		
		//Pretty simples. Stops chest being broken if it is in deathChests Hashmap
		Player player = event.getPlayer();
		if(event.getBlock().getType() == Material.CHEST) {
			Location chestLoc = event.getBlock().getLocation();
			if(Death.getdChests().containsKey(chestLoc)) {
				player.sendMessage(ChatColor.ITALIC + "" + ChatColor.RED + "You cannot break a death chest!");
				event.setCancelled(true);
			}
			if(Death.getdChests2().containsKey(chestLoc)) {
				player.sendMessage(ChatColor.ITALIC + "" + ChatColor.RED + "You cannot break a death chest!");
				event.setCancelled(true);
			}
		}
		
	}
}
