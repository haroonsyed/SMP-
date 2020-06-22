package me.Sshawarma.Command;

import java.util.ArrayList;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.Sshawarma.SMP.Main;

public class ChestTrustCommand implements CommandExecutor{
	
	Plugin plugin = Main.getPlugin(Main.class);
	FileConfiguration config = plugin.getConfig();
	
	
	public ArrayList<String> getTrustedPlayers(String owner) {
		return (ArrayList<String>) config.getStringList("PlayerSettings." + owner + ".chrusted");
	}
	
	public void setTrustedPlayers(String owner, ArrayList<String> players) {
		config.set("PlayerSettings." + owner + ".chrusted", players);
		plugin.saveConfig();
	}
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(cmd.getName().equalsIgnoreCase("chrust")) {
			if(sender instanceof Player) {
				if(args.length == 1) {
					
					//Capitalizes player name. Checks if the chrusted player is there before adding it.
					args[0] = args[0].toUpperCase();
					Player player = (Player) sender;
					ArrayList<String> trusted = (ArrayList<String>) config.getStringList("PlayerSettings." + player.getDisplayName() + ".chrusted");
					
					if(trusted.contains(args[0])) {
						player.sendMessage(ChatColor.RED + "Player already is in trusted list!");
					}
					
					else {
						trusted.add(args[0]);
						config.set("PlayerSettings." + player.getDisplayName() + ".chrusted", trusted);
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
					ArrayList<String> trusted = (ArrayList<String>) config.getStringList("PlayerSettings." + player.getDisplayName() + ".chrusted");
					
					if(trusted.contains(args[0])) {
						trusted.remove(args[0]);
						player.sendMessage(ChatColor.GREEN + "Player removed from trusted!");
						config.set("PlayerSettings." + player.getDisplayName() + ".chrusted", trusted);
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
