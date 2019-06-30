package me.Sshawarma.Events;

import java.util.ArrayList;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
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
		
		if(event.getAction()==Action.RIGHT_CLICK_BLOCK) {
			
			if(event.getClickedBlock().getType() == Material.CHEST) {
				Player player = event.getPlayer();
				Chest chest = (Chest) event.getClickedBlock().getState();
				String owner = Death.getdChestPlayers().get(chest.getBlock().getLocation());
				String owner2 = Death.getdChestPlayers2().get(chest.getBlock().getLocation());			
				

				

				
				if(Death.getdChests().containsKey(chest.getBlock().getLocation())){
					
					ArrayList<String> trustedps = ChestTrustCommand.getTrustedPlayers(owner.toUpperCase());
					try {
						if(owner.equalsIgnoreCase(player.getDisplayName())) {
							return;
						
						}
						
						else if(trustedps.contains(player.getDisplayName())){
							player.sendMessage(owner);
							return;
							
						}
						
						else {
							player.sendMessage(ChatColor.RED + "You don't have permission to access this chest!");
							event.setCancelled(true);
						}
					}
					catch(Exception e) {
						event.setCancelled(true);
						player.sendMessage(ChatColor.RED + "You don't have permisison to access this chest!");
					}
					
				}
				
				else if(Death.getdChests2().containsKey(chest.getBlock().getLocation())) {
					
					ArrayList<String> trustedps2 = ChestTrustCommand.getTrustedPlayers(owner2.toUpperCase());
					
					try {
						if(owner2.equalsIgnoreCase(player.getDisplayName())) {
							return;
						
						}
						
						else if(trustedps2.contains(player.getDisplayName().toUpperCase())) {
							return;
							
						}
						else {
							player.sendMessage(ChatColor.RED + "You don't have permission to access this chest!");
							event.setCancelled(true);
						}
					}
					catch(Exception e) {
						event.setCancelled(true);
						player.sendMessage(ChatColor.RED + "You don't have permission to access this chest!");
					}
					
					
				}
			}
			
		}
		
	}
	
}
