package me.Sshawarma.SMP.Command;

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

import me.Sshawarma.SMP.Main.Main;
import net.md_5.bungee.api.ChatColor;

public class FactionWar implements CommandExecutor{
	
	//OMG I AM AN IDIOT
	//STATIC SHOULD NEVER BE USED, USE A MANAGER CLASS
	
	/*TODO:
	 *	
	 * 		-Loot Event
	 * 	
	 */
	
	
	
	//String is faction name, int is number of votes
	HashMap<String, Integer> voteTracker = new HashMap<String, Integer>();
	ArrayList<UUID> voters = new ArrayList<UUID>();
	Plugin plugin = Main.getPlugin(Main.class);
	FileConfiguration config = plugin.getConfig();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if(sender instanceof Player) {
			
			Player player = (Player) sender;
			
			if(cmd.getName().equalsIgnoreCase("war")) {
				
				if(args.length == 1) {
					
					//Entering war mode
					if(args[0].equalsIgnoreCase("start")) {
						
						//Get list of all players in the same faction (UUID)
						String faction = config.getString("PlayerSettings." + player.getUniqueId().toString() + ".faction");
						
						if(faction.equals("default")) {
							sender.sendMessage(ChatColor.RED + "You are in default faction! They cannot be involved in wars.");
							return true;
						}
						
						//Cancel if warmode is already on
						if(config.getBoolean("FactionSettings." + faction + ".war.isWarring") == true) {
							sender.sendMessage(ChatColor.RED + "War in progress already!");
							return true;
						}
						
						if(config.contains("FactionSettings." + faction + ".war.startTime")) {
							if((System.currentTimeMillis()-config.getLong("FactionSettings." + faction + ".war.startTime")) < 259200000) {
								sender.sendMessage(ChatColor.RED + "A war occured too recently, try again after the cooldown!");
								return true;
							}
						}
						
						if(voters.contains(player.getUniqueId())) {
							sender.sendMessage(ChatColor.RED + "You already voted!");
							return true;
						}
						
						ArrayList<String> membersList = new ArrayList<String>();
						for(String p : config.getConfigurationSection("PlayerSettings").getKeys(false)) {
							if(config.getString("PlayerSettings." + p + ".faction").equals(faction)) {
								membersList.add(p);
							}
						}
						
						//Votes needed equals half a faction
						int votesNeeded = (int) Math.ceil(membersList.size()*0.66f);
						int currentVotes = 0;
						voters.add(player.getUniqueId());

						//Initialize votetracker
						if(!voteTracker.containsKey(faction)) {
							
							//Update votes
							voteTracker.put(faction, 1);
							currentVotes++;
							
							//Start timer to timeout the vote
							new BukkitRunnable() {

								@Override
								public void run() {
									// remove the map from voting to start it again
									voteTracker.remove(faction);
									boolean isWarringFaction = config.getBoolean("FactionSettings." + faction + ".war.isWarring");
									for(String p : membersList) {
										voters.remove(UUID.fromString(p));
										OfflinePlayer member = Bukkit.getServer().getOfflinePlayer(UUID.fromString(p));
										if(member.isOnline() && !isWarringFaction) {
											member.getPlayer().sendMessage(ChatColor.BOLD + "Not enough votes in the past 2 minutes to start a war.");
											member.getPlayer().sendMessage(ChatColor.GREEN + "You can now vote again for war.");
										}
										
									}
								}
								
							}.runTaskLater(plugin, 2400);
						}
						
						else {
							currentVotes = voteTracker.get(faction) + 1;
							voteTracker.put(faction, currentVotes);
						}
						
						//Check if war declaration is met
						if(votesNeeded-currentVotes <= 0) {
							//Put in war state and set time started
							config.set("FactionSettings." + faction + ".war.isWarring", true);
							config.set("FactionSettings." + faction + ".war.startTime", System.currentTimeMillis());
							plugin.saveConfig();
							
						}
						
						//Notify server if war started
						for(Player p : Bukkit.getServer().getOnlinePlayers()) {
							if(!membersList.contains(p.getUniqueId().toString())) {
								p.sendMessage(ChatColor.RED + "[WAR] A faction has entered war mode! Be ready for anything.");
								p.playSound(p.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 10, 0.5f);
							}
						}
						
						//Notify players 
						for(String p : membersList) {
							
							OfflinePlayer member = Bukkit.getOfflinePlayer(UUID.fromString(p));
							
							
							if(!member.isOnline()) {
								continue;
							}
							
							Player reciever = Bukkit.getPlayer(UUID.fromString(p));
							
							//For each member, tell them the vote count.
							reciever.playSound(reciever.getLocation(), Sound.BLOCK_ANVIL_LAND, 10, 1);
							reciever.sendMessage(ChatColor.GOLD + "" + ChatColor.ITALIC + player.getDisplayName() + ChatColor.RESET + "" + ChatColor.GREEN + "" + ChatColor.BOLD + " has voted to start a war");
							reciever.sendMessage(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + (votesNeeded-currentVotes) + " votes needed to initiate war!");
							
							//Notify if war has begun
							if(config.getBoolean("FactionSettings." + faction + ".war.isWarring") == true) {
								reciever.playSound(reciever.getLocation(), Sound.ENTITY_WOLF_HOWL, 10, 1);
								reciever.sendMessage(ChatColor.MAGIC + "" + ChatColor.BOLD + "Warmode entered.");
								reciever.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "Warmode entered.");
								reciever.sendMessage(ChatColor.MAGIC + "" + ChatColor.BOLD + "Warmode entered.");
								Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "lp user " + member.getName() + " permission set griefprevention.claims false");
							}
							
						}
						
					}
					
				}
				
			
				
			}
			
		}
		
		
		return true;
	}

}