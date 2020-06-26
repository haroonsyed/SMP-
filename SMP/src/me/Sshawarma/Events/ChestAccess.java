package me.Sshawarma.Events;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import me.Sshawarma.Command.ChestTrustCommand;
import net.md_5.bungee.api.ChatColor;

public class ChestAccess implements Listener{
	
	@EventHandler
	public void stopChestExplosion(BlockExplodeEvent event) {
		
		Location chestLoc = Bukkit.getServer().getWorld("world").getSpawnLocation();
		
		//Prevent concurrent modification with an iterator
		Iterator<Block> it = event.blockList().iterator();
		
		while(it.hasNext()) {
			
			Block b = it.next();
			
			chestLoc = b.getLocation();
			
			if(Death.getdChests().containsKey(chestLoc) || Death.getdChests2().containsKey(chestLoc)) {
				it.remove();;
			}
			
		}
		
	}
	
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
				UUID strictOwner = Death.getdChestPlayers().get(chest.getBlock().getLocation());
				UUID strictOwner2 = Death.getdChestPlayers2().get(chest.getBlock().getLocation());
				
				
				if(Death.getdChests().containsKey(chest.getBlock().getLocation())){
					
					ChestTrustCommand ctc = new ChestTrustCommand();
					ArrayList<UUID> trustedps = ctc.getTrustedPlayers(strictOwner);
					
					try {
						if(strictOwner.equals(player.getUniqueId())) {
							return;
						
						}
						
						else if(trustedps.contains(player.getUniqueId())){
							player.sendMessage(ChatColor.AQUA + "Chest Owner: " + Bukkit.getOfflinePlayer(strictOwner).getName());
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
					
					ChestTrustCommand ctc = new ChestTrustCommand();
					ArrayList<UUID> trustedps = ctc.getTrustedPlayers(strictOwner2);
					
					try {
						if(strictOwner2.equals(player.getUniqueId())) {
							return;
						
						}
						
						else if(trustedps.contains(player.getUniqueId())) {
							player.sendMessage(ChatColor.AQUA + "Chest Owner: " + Bukkit.getOfflinePlayer(strictOwner2).getName());
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
