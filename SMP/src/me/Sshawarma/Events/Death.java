package me.Sshawarma.Events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Chest;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.Sshawarma.SMP.Main;
import net.md_5.bungee.api.ChatColor;

public class Death implements Listener{
	Plugin plugin = Main.getPlugin(Main.class);	
	//Keep track of active chests with location and whether to delete
	private static HashMap<Location, Material> dChests = new HashMap<Location, Material>();
	private static HashMap<Location, Material> dChests2 = new HashMap<Location, Material>();
	private static HashMap<Location, UUID> dChestPlayers = new HashMap<Location, UUID>();
	private static HashMap<Location, UUID> dChestPlayers2 = new HashMap<Location, UUID>();
	
	public static HashMap<Location, Material> getdChests() {
		return dChests;
	}
	
	public static void setdChests(Location loc, Material mat) {
		dChests.put(loc.getBlock().getLocation(), mat);
	}
	
	public static HashMap<Location, Material> getdChests2(){
		return dChests2;
	}
	
	public static void setdChests2(Location loc, Material mat) {
		dChests2.put(loc.getBlock().getLocation(), mat);
	}
	
	public static HashMap<Location, UUID> getdChestPlayers(){
		return dChestPlayers;
	}
	
	public static HashMap<Location, UUID> getdChestPlayers2(){
		return dChestPlayers2;
	}
	
	@EventHandler
	public void placeChest(PlayerDeathEvent event) {
		if(event.getEntity() instanceof Player) {
			
			//Exp
			int xp = event.getEntity().getLevel();
			
			Plugin plugin = Main.getPlugin(Main.class);
			FileConfiguration config = plugin.getConfig();
			String faction = config.getString("PlayerSettings." + event.getEntity().getUniqueId().toString() + ".faction");
			
			//AgniKai Check and War Check
			if(me.Sshawarma.Command.AgniKai.ogLocations.containsKey(event.getEntity().getUniqueId().toString())) {
				return;
			}
			else if(config.getBoolean("FactionSettings." + faction + ".war") == true) {
				return;
			}
			
			//Variables
			Player player = event.getEntity();
			Location chestLoc = new Location(player.getWorld(), player.getLocation().getBlockX(), player.getLocation().getBlockY(),player.getLocation().getBlockZ());
			Location chest2Loc = new Location(chestLoc.getWorld(),player.getLocation().getBlockX(), player.getLocation().getBlockY(),player.getLocation().getBlockZ());
			chest2Loc.add(0, 1, 0);
			
			Material oldChestBlock = chestLoc.getBlock().getType();
			Material oldChestBlockTwo = chest2Loc.getBlock().getType();
			
			dChests.put(chestLoc.getBlock().getLocation(), chestLoc.getBlock().getType());
			dChestPlayers.put(chestLoc.getBlock().getLocation(), player.getUniqueId());
			
			//Moves standard death message to before coordinate message
			for (Player onlinePlayers : Bukkit.getOnlinePlayers()){
				onlinePlayers.playSound(onlinePlayers.getLocation(), Sound.ENTITY_VILLAGER_DEATH, 10, 1);
			}
			String deathMessage = ChatColor.DARK_RED + "" + ChatColor.BOLD +event.getDeathMessage().toString();
			event.setDeathMessage(null);
			Bukkit.getServer().broadcastMessage(deathMessage);
			
			//Tells Player Where their death occurred
			player.sendMessage(ChatColor.BOLD + "" + ChatColor.WHITE + player.getDisplayName() + "'s death coordinates are: " + ChatColor.LIGHT_PURPLE + event.getEntity().getLocation().getBlockX() + 
				ChatColor.GREEN + " " + event.getEntity().getLocation().getBlockY() +ChatColor.BLUE + " " + event.getEntity().getLocation().getBlockZ() + ChatColor.WHITE + " in " + event.getEntity().getWorld().getName());

			//WIP Places player inventory into an invincible chest. Change bcs beacon is client side.
			//Player inventory of type Inventory must be converted to itemstack array to add to chests
			ArrayList<ItemStack> inven = new ArrayList<ItemStack>();
			ArrayList<ItemStack> inven2 = new ArrayList<ItemStack>();
			//iterate through player inventory and add to itemstacks
			//may have to get player armor
			for(ItemStack i : player.getInventory().getContents()) {
				//Optimize cpu usage here!
				if(i== null) {
				}
				else if(inven.size()+1<=27) {
					inven.add(i);
				}
				else {
					inven2.add(i);
				}
			}
			//Makes Chest
			chestLoc.getBlock().setType(Material.AIR);
			chestLoc.getBlock().setType(Material.CHEST);
			Chest chest = (Chest) chestLoc.getBlock().getState();
			
			//May have to make a new chest, sets chest contents to this
			if(inven2.size()==0) {
				chest.getInventory().setContents(inven.toArray(new ItemStack[inven.size()]));
				event.getDrops().clear();
			}
			//Make an adjacent chest... Which means I may have to track another :(
			else{
				//IF player inven is too big
				//Makes second chest
				chest2Loc.getBlock().setType(Material.AIR);
				chest2Loc.getBlock().setType(Material.CHEST);
				Chest chest2 = (Chest) chest2Loc.getBlock().getState();
				dChests2.put(chest2Loc.getBlock().getLocation(), oldChestBlock);
				
				//set second chest to second half of player inven
				chest2.getInventory().setContents(inven.toArray(new ItemStack[27]));
				chest.getInventory().setContents(inven2.toArray(new ItemStack[inven2.size()]));
				event.getDrops().clear();
				event.setDroppedExp(player.getLevel()/2);
				dChestPlayers2.put(chest2Loc.getBlock().getLocation(), player.getUniqueId());
			}
			
			event.getEntity().setLevel(xp/2);
			event.setKeepLevel(true);
			
			//runnable to remove chest
			new BukkitRunnable() {

				@Override
				public void run() {
					//Chest chest = (Chest) chestLoc.getBlock().getState();
					//chest.getInventory().clear();
					chestLoc.getBlock().setType(Material.AIR);
					chestLoc.getBlock().setType(oldChestBlock);
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, 10, 1);
					player.sendMessage(ChatColor.DARK_RED + "Your death chest has despawned :(");
					dChests.remove(chestLoc.getBlock().getLocation());
					dChestPlayers.remove(chestLoc.getBlock().getLocation());
					if(dChests2.containsKey(chest2Loc)) {
						//Chest chest2 = (Chest) chest2Loc.getBlock().getState();
						//chest2.getInventory().clear();
						chest2Loc.getBlock().setType(Material.AIR);
						chest2Loc.getBlock().setType(oldChestBlockTwo);
						dChests2.remove(chest2Loc.getBlock().getLocation());
						dChestPlayers2.remove(chest2Loc.getBlock().getLocation());
					}
					
				}	
			}.runTaskLater(plugin, 12000);
			
		}
		
	}
}
