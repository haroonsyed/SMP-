package me.Sshawarma.Events;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;
import net.md_5.bungee.api.ChatColor;

public class PreventChestBreak implements Listener{
	
	@EventHandler
	public void stopChestBreak(BlockDamageEvent event) {
		//Pretty simples. Stops chest being broken if it is in deathChests Hasmap
		Player player = event.getPlayer();
		if(event.getBlock().getType() == Material.CHEST) {
			Block block = event.getBlock();
			if(Death.getdChests().containsKey(block.getLocation())) {
				player.sendMessage(ChatColor.ITALIC + "" + ChatColor.RED + "You cannot break a death chest!");
				event.setCancelled(true);
			}
			if(Death.getdChests2().containsKey(block.getLocation())) {
				player.sendMessage(ChatColor.ITALIC + "" + ChatColor.RED + "You cannot break a death chest!");
				event.setCancelled(true);
			}
		}
		
	}
}
