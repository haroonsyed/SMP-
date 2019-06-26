package me.Sshawarma.Events;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import me.Sshawarma.Command.ChestTrustCommand;
import net.md_5.bungee.api.ChatColor;

public class ChestAccess implements Listener{
	
	
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
	
	@EventHandler
	public void checkPermission(PlayerInteractEvent event) {
		
		//Plugin may be used if I switch to using config
		//Plugin plugin = Main.getPlugin(Main.class);
		
		if(event.getClickedBlock().getType() == Material.CHEST) {
			

			Player player = event.getPlayer();
			Chest chest = (Chest) event.getClickedBlock().getState();
			String owner = Death.getdChestPlayers().get(chest.getBlock().getLocation());
			
			if(Death.getdChests().containsKey(chest.getBlock().getLocation())){
				if(owner == player.getDisplayName().toUpperCase()) {
					
					return;
				
				}
				
				else if(ChestTrustCommand.getTrustedPlayers().get(owner).contains(player.getDisplayName().toUpperCase())){
				
					return;
					
				}
				
				else {
					player.sendMessage(ChatColor.RED + "You don't have permission to access this chest!");
				}
			}
			
			else if(Death.getdChests2().containsKey(chest.getBlock().getLocation())) {
				if(owner == player.getDisplayName().toUpperCase()) {
					
					return;
				
				}
				
				else if(ChestTrustCommand.getTrustedPlayers().get(owner).contains(player.getDisplayName().toUpperCase())) {
					
					return;
					
				}
				else {
					player.sendMessage(ChatColor.RED + "You don't have permission to access this chest!");
				}
			}
			
			
			
			
			
		}
		
	}
	
}
