package me.Sshawarma.Command;

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

public class Messaging implements CommandExecutor{
	
	/*	TODO:
	 * 		-Cooldown on msg or /r spam
	 * 
	 * 
	 * 
	 */
	
	Plugin plugin = Main.getPlugin(Main.class);
	
	
	//Player, Player last messaged
	static HashMap<String, String> lastMessaged = new HashMap<String, String>();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// Messaging system in the works I suppose.
		if(sender instanceof Player) {
			
			if((cmd.getName().equalsIgnoreCase("msg") || cmd.getName().equalsIgnoreCase("r")) && args.length > 0) {
				
				//Make the message
				boolean sentMessage = false;
				String msg = "";
				for(int i=1; i<args.length; i++) {
					msg += args[i] + " ";
				}
				
				//MSG
				if(cmd.getName().equalsIgnoreCase("msg")  && args.length > 1) {
					//Get player args[0], message them, add them to sender's last messaged
					for(Player p : Bukkit.getServer().getOnlinePlayers()) {
						if(p.getDisplayName().equalsIgnoreCase(args[0])) {
							p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_FLUTE, 10, 1);
							p.sendMessage(ChatColor.ITALIC + "" + ChatColor.LIGHT_PURPLE + "<" + ((Player) sender).getDisplayName() + "> [MSG] " + ChatColor.GRAY + msg);
							sender.sendMessage(ChatColor.ITALIC + "" + ChatColor.LIGHT_PURPLE + "<" + ((Player) sender).getDisplayName() + "> [MSG] " + ChatColor.GRAY + msg);
							lastMessaged.put(((Player) sender).getDisplayName(), p.getDisplayName());
							lastMessaged.put(p.getDisplayName(), ((Player) sender).getDisplayName());
							
							sentMessage = true;
						}
					}
				}
				
				//R 
				//Check if there is a player in the Hashmap, then perform command
				else if(cmd.getName().equalsIgnoreCase("r") && lastMessaged.containsKey(((Player) sender).getDisplayName())){
					
					String senderName = ((Player) sender).getDisplayName();
					msg = args[0] + " " + msg;
					
					//Get last messaged from hashmap, message them, add them to sender's last messaged
					for(Player p : Bukkit.getServer().getOnlinePlayers()) {
						if(p.getDisplayName().equalsIgnoreCase(lastMessaged.get(senderName))) {
							p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_FLUTE, 10, 1);
							p.sendMessage(ChatColor.ITALIC + "" + ChatColor.LIGHT_PURPLE + "<" + ((Player) sender).getDisplayName() + "> [MSG] " + ChatColor.GRAY + msg);
							sender.sendMessage(ChatColor.ITALIC + "" + ChatColor.LIGHT_PURPLE + "<" + ((Player) sender).getDisplayName() + "> [MSG] " + ChatColor.GRAY + msg);
							lastMessaged.put(((Player) sender).getDisplayName(), p.getDisplayName());
							lastMessaged.put(p.getDisplayName(), ((Player) sender).getDisplayName());
							sentMessage = true;
						}
					}
				}
				
				//Invalid arguments
				else {
					sender.sendMessage(ChatColor.RED + "Invalid Message or player is offline!");
				}
				
				//No player online with that name
				//if(sentMessage == false) {
				//	sender.sendMessage(ChatColor.RED + "No player online with that name!");
				//}
				
				
				
			}
		}
		return false;
	}
	
	//Called from ChatListener when player uses @
	public void messageFaction(String StringUUID, String msg) {
		
		FileConfiguration config = plugin.getConfig();
		String senderFaction = config.getString("PlayerSettings." + StringUUID + ".faction");
		String color = ChatColor.translateAlternateColorCodes('&', config.getString("PlayerSettings." + StringUUID + ".chatcolor"));
		String sender = Bukkit.getPlayer(UUID.fromString(StringUUID)).getDisplayName();
		
		//Loop through online players. If player.faction matches, then send message
		for(Player p : Bukkit.getOnlinePlayers()) {
			if(config.getString("PlayerSettings." + p.getUniqueId().toString() + ".faction").equalsIgnoreCase(senderFaction)) {
				p.sendMessage(ChatColor.YELLOW + "<" + sender + ">[FACTION] " + color + msg);
			}
		}
		
	}
	
}
