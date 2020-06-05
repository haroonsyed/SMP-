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
	static HashMap<String, String> joinRequests;
	
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
					for(String faction : plugin.getConfig().getConfigurationSection("FactionSettings").getKeys(false)) {
						if(args[1].equalsIgnoreCase(faction)) {
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
									Bukkit.getPlayer(args[1]).playSound(Bukkit.getPlayer(args[0]).getLocation(), Sound.BLOCK_NOTE_BLOCK_GUITAR, 10, 1);
									Bukkit.getPlayer(args[1]).sendMessage(ChatColor.GREEN + "You have joined the faction: " + joinRequests.get(args[0]));
								}
								((Player) sender).sendMessage("Successfully added to faction!");
								joinRequests.remove(args[1]);
							}
						}
					}
					else if(args[0].equalsIgnoreCase("deny")) {
						//Check if player requested to join their clan and deny them.
						if(joinRequests.containsKey(args[1])) {
							if(plugin.getConfig().getString("PlayerSettings." + ((Player)sender).getDisplayName() + ".faction").equalsIgnoreCase(joinRequests.get(args[1]))) {
								if(Bukkit.getPlayer(args[1]) != null) {
									Bukkit.getPlayer(args[1]).playSound(Bukkit.getPlayer(args[0]).getLocation(), Sound.ENTITY_GHAST_SCREAM, 10, 1);
									Bukkit.getPlayer(args[1]).sendMessage(ChatColor.RED + "You have been denied from the faction: " + joinRequests.get(args[0]));
								}
								((Player) sender).sendMessage("Successfully denied to faction!");
								joinRequests.remove(args[1]);
							}
						}
					}
					else if(args[0].equalsIgnoreCase("choose")) {
						//Spam timer
						if(joinRequests.containsKey(((Player)sender).getDisplayName())) {
							if(joinRequests.get(((Player)sender).getDisplayName()).equalsIgnoreCase(args[1])) {
								sender.sendMessage(ChatColor.RED + "Request already sent!");
							}
						}
						//Check that player is not already in said faction
						else if(!args[1].equals(plugin.getConfig().get("PlayerSettings." + ((Player)sender) + ".faction"))){
							//Check if the faction is already there
							boolean factionExists = false;
							for(String faction : plugin.getConfig().getConfigurationSection("FactionSettings").getKeys(false)) {
								if(args[1].equalsIgnoreCase(faction)) {
									factionExists = true;
								}
							}
							//If it exists, contact it's members and ask to join. (Spam timer may be needed?)
							//Don't think I should make admins in factions, so any member can accept the new member
							if(factionExists) {
								//Goes thorugh online players in faction and asks to join new player
								for(Player p : Bukkit.getServer().getOnlinePlayers()) {
									if(plugin.getConfig().getString("PlayerSettings." + p.getDisplayName() + ".faction").equalsIgnoreCase(args[1])) {
										p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, 10, 1);
										p.sendMessage(ChatColor.AQUA + "Player " + ((Player) sender).getDisplayName() + " would like to join the faction!");
										p.sendMessage("Perform the command /faction accept playername to accept");
									}
									joinRequests.put(((Player) sender).getDisplayName(), args[1]);
								}
								new BukkitRunnable() {

									@Override
									public void run() {
										// remove player from the list
										joinRequests.remove(((Player)sender).getDisplayName());
										sender.sendMessage(ChatColor.RED + "The faction did not accept you in time!");
									}
									
								}.runTaskLater(plugin, 600);
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
				}
			}
		}
		
		return true;
	}
	
}
