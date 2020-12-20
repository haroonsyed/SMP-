package main.java.me.Sshawarma.SMP.Command;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import main.java.me.Sshawarma.SMP.Main.Main;

public class ChestTrustCommand implements CommandExecutor{
	
	Plugin plugin = Main.getPlugin(Main.class);
	FileConfiguration config = plugin.getConfig();
	
	
	public ArrayList<UUID> getTrustedPlayers(UUID owner) {
		
		ArrayList<UUID> chrusted = new ArrayList<UUID>();
		ArrayList<String> chrustedString = (ArrayList<String>) config.getStringList("PlayerSettings." + owner.toString() + ".chrusted");
		
		for(String str : chrustedString) {
			chrusted.add(UUID.fromString(str));
		}
		
		return chrusted; 
	}
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(cmd.getName().equalsIgnoreCase("chrust")) {
			if(sender instanceof Player) {
				if(args.length == 1) {
					
					//Capitalizes player name. Checks if the chrusted player is there before adding it.
					args[0] = args[0].toUpperCase();
					Player player = (Player) sender;
					ArrayList<String> trustedID = (ArrayList<String>) config.getStringList("PlayerSettings." + player.getUniqueId().toString() + ".chrusted");
					ArrayList<String> trusted = new ArrayList<String>();
					for(String str : trustedID) {
						trusted.add(Bukkit.getOfflinePlayer(UUID.fromString(str)).getName().toUpperCase());
					}
					
					
					if(trusted.contains(args[0])) {
						player.sendMessage(ChatColor.RED + "Player already is in trusted list!");
					}
					
					else {
						//Get ID of player
						UUID id = Bukkit.getOfflinePlayer(args[0]).getUniqueId();
						trustedID.add(id.toString());
						trusted.add(args[0]);
						config.set("PlayerSettings." + player.getUniqueId().toString() + ".chrusted", trustedID);
						plugin.saveConfig();
						player.sendMessage(ChatColor.GREEN + "Trusted player added!");
					}
					
					player.sendMessage("Trusted are: " + trusted.toString());
					
				}
				else {
					sender.sendMessage(ChatColor.RED + "Please input a player name!");
				}
			}
		}
		
		if(cmd.getName().equalsIgnoreCase("dischrust")) {
			
			if(sender instanceof Player) {
			
				if(args.length == 1) {
					
					//Checks if player exists before removing it.
					args[0] = args[0].toUpperCase();
					Player player = (Player) sender;
					ArrayList<String> trustedID = (ArrayList<String>) config.getStringList("PlayerSettings." + player.getUniqueId().toString() + ".chrusted");
					ArrayList<String> trusted = new ArrayList<String>();
					for(String str : trustedID) {
						trusted.add(Bukkit.getOfflinePlayer(UUID.fromString(str)).getName().toUpperCase());
					}
					
					if(trusted.contains(args[0])) {
						//Get ID of player
						UUID id = Bukkit.getOfflinePlayer(args[0]).getUniqueId();
						trustedID.remove(id.toString());
						trusted.remove(args[0]);
						player.sendMessage(ChatColor.GREEN + "Player removed from trusted!");
						config.set("PlayerSettings." + player.getDisplayName() + ".chrusted", trustedID);
						plugin.saveConfig();
					}
					
					else {
						player.sendMessage(ChatColor.RED + "Player already didn't have permission!");
					}
					
					player.sendMessage("Trusted are: " + trusted.toString());
					
				}
				
				else {
					sender.sendMessage("Only one name at a time!");
				}
				
			}
		}
		
		return false;
	}

}
