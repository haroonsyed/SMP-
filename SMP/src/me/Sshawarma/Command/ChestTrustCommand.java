package me.Sshawarma.Command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChestTrustCommand implements CommandExecutor{
	
	private static HashMap<String, List<String>> trustedPlayers = new HashMap<String, List<String>>();
	
	public static HashMap<String, List<String>> getTrustedPlayers() {
		return trustedPlayers;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(cmd.getName().equalsIgnoreCase("chrust")) {
			if(sender instanceof Player) {
				if(args.length == 1) {
					Player player = (Player) sender;
					List<String> trusted = new ArrayList<String>();
					
					for(Entry<String, List<String>> entry : trustedPlayers.entrySet()) {
						//player.sendMessage(entry.getKey() + "going through keys");
						if(entry.getKey().equalsIgnoreCase(player.getDisplayName())) {
							
							trusted = entry.getValue();
							if(!(trusted.contains(args[0].toUpperCase()))) {
								player.sendMessage(ChatColor.GREEN + "Trusted player added!");
								trusted.add(args[0].toUpperCase());
							}
							else {
								player.sendMessage(ChatColor.RED + "Player already is in trusted list!");
							}
							
						}
						
					}
					
					if(!(trustedPlayers.containsKey(player.getDisplayName().toUpperCase()))) {
						trusted.add(args[0].toUpperCase());
						player.sendMessage(ChatColor.GREEN + "Player added to trusted list!");
					}
					
					//Remember ignorecase I suppose
					
					trustedPlayers.put(player.getDisplayName().toUpperCase(), trusted);
					player.sendMessage(ChatColor.ITALIC + "Be aware that trusted players are not saved on next login/server start!");
					player.sendMessage("Trusted are: " + trusted.toString());
					
				}
			}
		}
		
		if(cmd.getName().equalsIgnoreCase("dischrust")) {
			
			if(sender instanceof Player) {
			
				if(args.length == 1) {
					Player player = (Player) sender;
					List<String> trusted = new ArrayList<String>();
					
					for(Entry<String, List<String>> entry : trustedPlayers.entrySet()) {
						
						if(entry.getKey().equalsIgnoreCase(player.getDisplayName())) {
							trusted = entry.getValue();
							if(trusted.contains(args[0].toUpperCase())) {
								player.sendMessage(ChatColor.GREEN + "Player removed from trusted!");
								trusted.remove(args[0].toUpperCase());
							}
							else {
								player.sendMessage(ChatColor.RED + "Player already didn't have permission!");
							}

						}
						
					}
					
					if(!(trustedPlayers.containsKey(player.getDisplayName().toUpperCase()))) {
						player.sendMessage(ChatColor.RED + "Noone was in trusted!");
					}
					
					trustedPlayers.put(player.getDisplayName().toUpperCase(), trusted);
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
