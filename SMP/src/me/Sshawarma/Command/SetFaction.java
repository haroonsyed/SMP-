package me.Sshawarma.Command;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.Sshawarma.SMP.Main;
import net.md_5.bungee.api.ChatColor;

public class SetFaction implements CommandExecutor{
	
	Plugin plugin = Main.getPlugin(Main.class);
	//Requests to join a faction are stored here
	static HashMap<String, String> joinRequests = new HashMap<String, String>();
	
	//POTENTIAL ISSUE WHERE FACTIONS CAN EXIST AFTER BECOMING EMPTY, BECOMING IMPOSSIBLE TO JOIN

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if(sender instanceof Player && sender.isOp()) {
			
			if(cmd.getName().equalsIgnoreCase("setFaction")) {
				
				if(args.length != 2) {
					sender.sendMessage(ChatColor.DARK_RED + "Please input a faction and Player!");
					return false;
				}
				
				else {
					plugin.getConfig().set("PlayerSettings." + args[1]+ ".faction", args[0]);
					//If the faction is being created, give it default settings
					boolean factionExists = false;
					for(String playerSetting : plugin.getConfig().getConfigurationSection("PlayerSettings").getKeys(false)) {
						if(args[1].equalsIgnoreCase(plugin.getConfig().getString(playerSetting + ".faction"))) {
							factionExists = true;
						}
					}
					if(factionExists == false) {
						plugin.getConfig().set("FactionSettings." + args[0] + ".peaceful", "false");
						plugin.getConfig().set("FactionSettings." + args[0] + ".friendlyFire", "true");
					}
					plugin.saveConfig();
				}
				
			}
			
		}
		if(sender instanceof Player) {
			//Set faction by player
			//Accept/Deny into faction by member
			if(cmd.getName().equalsIgnoreCase("faction")) {
				if(args.length == 2) {
					if(args[0].equalsIgnoreCase("accept")) {
						//Check if player requested to join their clan and add them.
						if(joinRequests.containsKey(args[1])) {
							if(plugin.getConfig().getString("PlayerSettings." + ((Player)sender).getDisplayName() + ".faction").equalsIgnoreCase(joinRequests.get(args[1]))) {
								plugin.getConfig().set("PlayerSettings." + args[1] + ".faction", joinRequests.get(args[1]));
								plugin.saveConfig();
								if(Bukkit.getPlayer(args[1]) != null) {
									Bukkit.getPlayer(args[1]).playSound(Bukkit.getPlayer(args[1]).getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 10, 1);
									Bukkit.getPlayer(args[1]).sendMessage(ChatColor.GREEN + "You have joined the faction: " + joinRequests.get(args[1]));
								}
								((Player) sender).sendMessage(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "Successfully added to faction!");
								joinRequests.remove(args[1]);
							}
						}
					}
					else if(args[0].equalsIgnoreCase("deny")) {
						//Check if player requested to join their clan and deny them.
						if(joinRequests.containsKey(args[1])) {
							if(plugin.getConfig().getString("PlayerSettings." + ((Player)sender).getDisplayName() + ".faction").equalsIgnoreCase(joinRequests.get(args[1]))) {
								if(Bukkit.getPlayer(args[1]) != null) {
									Bukkit.getPlayer(args[1]).playSound(Bukkit.getPlayer(args[1]).getLocation(), Sound.ENTITY_GHAST_SCREAM, 10, 1);
									Bukkit.getPlayer(args[1]).sendMessage(ChatColor.RED + "You have been denied from the faction: " + joinRequests.get(args[1]));
								}
								((Player) sender).sendMessage("Successfully denied to faction!");
								joinRequests.remove(args[1]);
							}
						}
					}
					else if(args[0].equalsIgnoreCase("leave")) {
						//Return to default
						if(args[1].equalsIgnoreCase("default")) {
							plugin.getConfig().set("PlayerSettings." + ((Player) sender).getDisplayName() + ".faction", "default");
							sender.sendMessage(ChatColor.GREEN + "Back to default faction!");
							plugin.saveConfig();
						}
					}
					else if(args[0].equalsIgnoreCase("choose") || args[0].equalsIgnoreCase("join")) {
						//Return to default
						if(args[1].equalsIgnoreCase("default")) {
							plugin.getConfig().set("PlayerSettings." + ((Player) sender).getDisplayName() + ".faction", "default");
							sender.sendMessage(ChatColor.GREEN + "Back to default faction!");
							plugin.saveConfig();
						}
						//Spam timer
						else if(joinRequests.containsKey(((Player)sender).getDisplayName())) {
							if(joinRequests.get(((Player)sender).getDisplayName()).equalsIgnoreCase(args[1])) {
								sender.sendMessage(ChatColor.RED + "Request already sent!");
							}
						}
						//Check that player is not already in said faction
						else if(!args[1].equalsIgnoreCase(plugin.getConfig().getString("PlayerSettings." + ((Player)sender).getDisplayName() + ".faction"))){
							//Check if the faction is already there
							boolean factionExists = false;
							for(String playerSetting : plugin.getConfig().getConfigurationSection("PlayerSettings").getKeys(false)) {
								if(args[1].equalsIgnoreCase(plugin.getConfig().getString("PlayerSettings." + playerSetting + ".faction"))) {
									factionExists = true;
								}
							}
							//If it exists, contact it's members and ask to join. (Spam timer may be needed?)
							//Don't think I should make admins in factions, so any member can accept the new member
							if(factionExists) {
								//Goes thorugh online players in faction and asks to join new player
								for(Player p : Bukkit.getServer().getOnlinePlayers()) {
									if(plugin.getConfig().getString("PlayerSettings." + p.getDisplayName() + ".faction").equalsIgnoreCase(args[1])) {
										p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 10, 1);
										p.sendMessage(ChatColor.AQUA + "Player " + ((Player) sender).getDisplayName() + " would like to join the faction!");
										p.sendMessage(ChatColor.GOLD + ""+  ChatColor.BOLD + "Perform the command /faction accept/deny playername to accept/deny");
									}
									joinRequests.put(((Player) sender).getDisplayName(), args[1]);
								}
								new BukkitRunnable() {

									@Override
									public void run() {
										// remove player from the list
										if(joinRequests.containsKey(((Player) sender).getDisplayName())) {
											joinRequests.remove(((Player)sender).getDisplayName());
											sender.sendMessage(ChatColor.RED + "The faction did not accept you in time!");
										}
									}
									
								}.runTaskLater(plugin, 600);
								sender.sendMessage(ChatColor.GREEN + "Request to join faction sent!");
							}
							//Else create the faction and make this person a part of it
							else {
								sender.sendMessage(ChatColor.GREEN + "Success! You are now in " + args[1] + " faction!");
								plugin.getConfig().set("PlayerSettings." + ((Player) sender).getDisplayName()+ ".faction", args[1]);
								plugin.getConfig().set("FactionSettings." + args[1] + ".peaceful", "false");
								plugin.getConfig().set("FactionSettings." + args[1] + ".friendlyFire", "true");
								plugin.saveConfig();
							}
						}
						else {
							sender.sendMessage(ChatColor.RED + "Already in that faction!");
						}
					}
					
					else if(args[0].equalsIgnoreCase("color")) {
						SetColor translator = new SetColor();
						String colorCode = translator.translateColorToCode(args[1]);
						String faction = plugin.getConfig().getString("PlayerSettings." + ((Player)sender).getDisplayName() + ".faction");
						if(colorCode != "") {
							plugin.getConfig().set("FactionSettings." + faction + ".color", colorCode);
							plugin.saveConfig();
							sender.sendMessage(ChatColor.GREEN + "Color set to " + ChatColor.translateAlternateColorCodes('&', colorCode) + args[1]);
						}
						else {
							sender.sendMessage(ChatColor.RED + "INVALID COLOR!");
							sender.sendMessage("Possible colors are: dark_red, red, gold, yellow, dark_green, green, aqua, dark_aqua, dark_blue, blue, light_purple, dark_purple, white, gray, dark_gray, black");
						}
					}
					
				}
			}
		}
		
		return true;
	}
	
}
