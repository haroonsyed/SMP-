package me.Sshawarma.Command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.Sshawarma.SMP.Main;
import net.md_5.bungee.api.ChatColor;

public class FactionWar implements CommandExecutor{
	
	//OMG I AM AN IDIOT
	//STATIC SHOULD NEVER BE USED, THE DATA IS ONLY DELETED ON SERVER END FOR CLASSES
	//KEEP DATA HERE AT CLASS LEVEL AND USE GETTER SETTER. 
	//BIG FACEPALM. I WILL CORRECT THIS GOING FORWARD.
	//I'll keep the old code as a reminder of bad practice. But going forward no more static. Geez, I knew better.
	
	/*TODO:
	 *	
	 * 		-Loot Event
	 * 	
	 */
	
	
	
	//String is faction name, int is number of votes
	HashMap<String, Integer> voteTracker = new HashMap<String, Integer>();
	ArrayList<UUID> voters = new ArrayList<UUID>();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if(sender instanceof Player) {
			
			Player player = (Player) sender;
			Plugin plugin = Main.getPlugin(Main.class);
			FileConfiguration config = plugin.getConfig();
			
			if(cmd.getName().equalsIgnoreCase("war")) {
				
				if(args.length == 1) {
					
					//Entering war mode
					if(args[0].equalsIgnoreCase("start")) {
						
						//Get list of all players in the same faction (UUID)
						String faction = config.getString("PlayerSetting." + player.getUniqueId().toString() + ".faction");
						
						//Cancel if warmode is already on
						if(config.getBoolean("FactionSettings." + faction + ".war") == true) {
							sender.sendMessage(ChatColor.RED + "War in progress already!");
							return false;
						}
						
						if(config.contains("FactionSettings." + faction + ".war.startTime")) {
							if((System.currentTimeMillis()-config.getLong("FactionSettings." + faction + ".war.startTime")) < 259200000) {
								sender.sendMessage(ChatColor.RED + "A war occured too recently, try again after the cooldown!");
								return false;
							}
						}
						
						if(voters.contains(player.getUniqueId())) {
							sender.sendMessage(ChatColor.RED + "You already voted!");
							return false;
						}
						
						ArrayList<String> membersList = new ArrayList<String>();
						for(String p : config.getConfigurationSection("PlayerSettings").getKeys(false)) {
							if(config.getString("PlayerSettings." + p + ".faction").equals(faction)) {
								membersList.add(p);
							}
						}
						
						//Votes needed equals half a faction
						int votesNeeded = (int) Math.ceil(membersList.size()/2.0f);
						int currentVotes = voteTracker.get(faction) + 1;
						voters.add(player.getUniqueId());
						
						//Increment votetracker
						if(voteTracker.containsKey(faction)) {
							voteTracker.put(faction, currentVotes);
						}
						else {
							voteTracker.put(faction, 1);
							//Start timer to timeout the vote
							new BukkitRunnable() {

								@Override
								public void run() {
									// remove the map from voting to start it again
									voteTracker.remove(faction);
									for(String p : membersList) {
										voters.remove(UUID.fromString(p));
										Bukkit.getServer().getPlayer(UUID.fromString(p)).sendMessage(ChatColor.GREEN + "You can now vote again for war.");
									}
								}
								
							}.runTaskLater(plugin, 2400);
						}
						
						//Check if war declaration is met
						if(votesNeeded-currentVotes <= 0) {
							//Put in war state and set time started
							config.set("FactionSettings." + faction + ".war", true);
							config.set("FactionSettings." + faction + ".war.startTime", System.currentTimeMillis());
							plugin.saveConfig();
							
						}
						
						//Notify players 
						for(String p : membersList) {
							
							OfflinePlayer member = Bukkit.getOfflinePlayer(UUID.fromString(p));
							Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "lp user " + member.getName() + " permission set griefprevention.claims false");
							
							if(!member.isOnline()) {
								continue;
							}
							
							Player reciever = Bukkit.getPlayer(UUID.fromString(p));
							
							//For each member, tell them the vote count.
							reciever.playSound(reciever.getLocation(), Sound.BLOCK_ANVIL_LAND, 10, 1);
							reciever.sendMessage(ChatColor.GOLD + "" + ChatColor.ITALIC + player.getDisplayName() + ChatColor.RESET + "" + ChatColor.GREEN + "" + ChatColor.BOLD + " has voted to start a war");
							reciever.sendMessage(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + (votesNeeded-currentVotes) + " votes needed to initiate war!");
							
							//Notify if war has begun
							if(config.getBoolean("FactionSettings." + faction + ".war") == true) {
								reciever.playSound(reciever.getLocation(), Sound.ENTITY_WOLF_HOWL, 10, 1);
								reciever.sendMessage(ChatColor.MAGIC + "" + ChatColor.BOLD + "Warmode entered.");
								reciever.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "Warmode entered.");
								reciever.sendMessage(ChatColor.MAGIC + "" + ChatColor.BOLD + "Warmode entered.");
							}
							
						}
						
					}
					
				}
				
			
				
			}
			
		}
		
		
		return false;
	}

}