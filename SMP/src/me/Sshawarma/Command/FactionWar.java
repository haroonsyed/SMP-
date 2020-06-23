package me.Sshawarma.Command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import me.Sshawarma.SMP.Main;
import net.md_5.bungee.api.ChatColor;

public class FactionWar implements CommandExecutor{
	
	//OMG I AM AN IDIOT
	//STATIC SHOULD NEVER BE USED, THE DATA IS ONLY DELETED ON SERVER END FOR CLASSES
	//KEEP DATA HERE AT CLASS LEVEL AND USE GETTER SETTER. 
	//BIG FACEPALM. I WILL CORRECT THIS GOING FORWARD.
	//I'll keep the old code as a reminder of bad practice. But going forward no more static. Geez, I knew better.
	
	/*TODO:
	 *		-Save trusted list instead of only allowing factions
	 * 		-Loot Event
	 */
	
	
	
	//String is faction name, int is number of votes
	HashMap<String, Integer> voteTracker = new HashMap<String, Integer>();

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
						
						ArrayList<String> membersList = new ArrayList<String>();
						for(String p : config.getConfigurationSection("PlayerSettings").getKeys(false)) {
							if(config.getString("PlayerSettings." + p + ".faction").equals(faction)) {
								membersList.add(p);
							}
						}
						
						//Votes needed equals half a faction
						int votesNeeded = (int) Math.ceil(membersList.size()/2.0f);
						int currentVotes = voteTracker.get(faction) + 1;
						
						//Increment votetracker
						if(voteTracker.containsKey(faction)) {
							voteTracker.put(faction, currentVotes);
						}
						else {
							voteTracker.put(faction, 1);
						}
						
						//Check if war declaration is met
						if(votesNeeded-currentVotes <= 0) {
							//Put in war state and set time started
							config.set("FactionSettings." + faction + ".war", true);
							config.set("FactionSettings." + faction + ".war.startTime", System.currentTimeMillis());
							plugin.saveConfig();
							
							//Trust all factions already in war mode. Need exact reversion, for now distrust those not in faction.
							
							//Get all players in a war
							ArrayList<UUID> warPlayers = new ArrayList<UUID>();
							for(String p : config.getConfigurationSection("PlayerSettings").getKeys(false)) {
								String pFaction = config.getString("PlayerSettings." + p + ".faction");
								if(config.getBoolean("FactionSettings." + pFaction + ".war") == true) {
									warPlayers.add(UUID.fromString(p));
								}
							}
							
							//Trust each other for land destruction
							for(UUID p : warPlayers) {
								//Assumes luckperms is installed
								Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "lp user " + Bukkit.getPlayer(p).getDisplayName() + " permission set griefprevention.claims true");
								for(UUID x : warPlayers) {
									
									//Add check if player is online!
									Bukkit.getPlayer(p).performCommand("permissiontrust " + Bukkit.getPlayer(x).getDisplayName());
									
								}
								//Assumes luckperms is installed
								Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "lp user " + Bukkit.getPlayer(p).getDisplayName() + " permission set griefprevention.claims false");
							}
							
						}
						
						//Notify players and check if war mode should be initiated
						for(String p : membersList) {
							
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
