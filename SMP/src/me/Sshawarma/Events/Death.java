package me.Sshawarma.Events;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Chest;
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
	private HashMap<Location, Material> dChests = new HashMap<Location, Material>();
	
	@EventHandler
	public void placeChest(PlayerDeathEvent event) {
		if(event.getEntity() instanceof Player) {
			
			//Variables
			Player player = event.getEntity();
			Location chestLoc = event.getEntity().getLocation();
			
			dChests.put(chestLoc, chestLoc.getBlock().getType());
			player.getInventory().clear();
			
			//Moves standard death message to before coordinate message
			for (Player onlinePlayers : Bukkit.getOnlinePlayers()){
				onlinePlayers.playSound(onlinePlayers.getLocation(), Sound.ENTITY_VILLAGER_DEATH, 10, 1);
			}
			String deathMessage = ChatColor.DARK_RED + "" + ChatColor.BOLD +event.getDeathMessage().toString();
			event.setDeathMessage(null);
			Bukkit.getServer().broadcastMessage(deathMessage);
			
			//Tells Player Where their death occurred
			Bukkit.getServer().broadcastMessage(ChatColor.BOLD + "" + ChatColor.WHITE + player.getDisplayName() + "'s death coordinates are: " + ChatColor.LIGHT_PURPLE + event.getEntity().getLocation().getBlockX() + 
				ChatColor.GREEN + " " + event.getEntity().getLocation().getBlockY() +ChatColor.BLUE + " " + event.getEntity().getLocation().getBlockZ() + ChatColor.WHITE + " in " + event.getEntity().getWorld().getName());

			//WIP Places player inventory into an invincible chest. Change bcs beacon is client side.
			//Player inventory of type Inventory must be converted to itemstack array to add to chests
			ArrayList<ItemStack> inven = new ArrayList<ItemStack>();
			//iterate through player inventory and add to itemstacks
			//may have to get player armor
			for(ItemStack i : player.getInventory().getContents()) {
				if(i!= null) {
					inven.add(i);
				}
			}
			
			//Makes Chest
			chestLoc.getBlock().setType(Material.CHEST);
			Chest chest = (Chest) chestLoc;
			
			//May have to make a new chest, sets chest contents to this
			if(inven.size()<28) {
				chest.getInventory().setContents(inven.toArray(new ItemStack[inven.size()]));
			}
			//Make an adjacent chest... Which means I may have to track another :(
			else {
				
			}
			
			//runnable to remove chest
			new BukkitRunnable() {

				@Override
				public void run() {
					chestLoc.getBlock().setType(dChests.get(chestLoc));
					player.playSound(chestLoc, Sound.BLOCK_ANVIL_FALL, 10, 1);
					player.sendMessage(ChatColor.DARK_RED + "Your death chest has despawned :(");
					dChests.remove(chestLoc);
					
				}
			//THIS WILL RUN FOR 3 MINUTES, CHANGE TO 5 LATER	
			}.runTaskLater(plugin, 3600);
			
		}
		
	}
}
