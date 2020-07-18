package me.Sshawarma.Command;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.Sshawarma.SMP.Main;
import net.md_5.bungee.api.ChatColor;

public class CoordinateSaver implements CommandExecutor{

	Plugin plugin = Main.getPlugin(Main.class);
	FileConfiguration config = plugin.getConfig();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(sender instanceof Player) {
			
			
			Player player = (Player) sender;
			
			
			if(cmd.getName().equalsIgnoreCase("spot")) {
				
				//Prevents null
				if(!config.contains("PlayerSettings." + player.getUniqueId().toString() + ".coords")) {
					config.set("PlayerSettings." + player.getUniqueId().toString() + ".coords", "");
					plugin.saveConfig();
				}
				
				if(args.length == 2) {
					
					//Delete coordinate
					if(args[0].equalsIgnoreCase("delete")) {
						
						boolean deleted = false;
						
						for(String spot : config.getConfigurationSection("PlayerSettings." + player.getUniqueId().toString() + ".coords").getKeys(false)) {
							if(spot.equals(args[1])) {
								config.set("PlayerSettings." + player.getUniqueId().toString() + ".coords." + args[1], null);
								player.sendMessage(ChatColor.GREEN + "Spot deleted!");
								deleted = true;
								plugin.saveConfig();
							}
						}
						
						if(!deleted) {
							player.sendMessage(ChatColor.RED + "Could not find that spot! Perhaps case doesn't match?");
						}
						
					}
					
					else {
						return false;
					}
					
				}
				
				else if(args.length == 3) {
					//Save coordinate with name and color
					if(args[0].equalsIgnoreCase("save")){
						Location loc = player.getLocation();
						SetColor translator = new SetColor();
						String colorCode = translator.translateColorToCode(args[2]);
						if(colorCode.equalsIgnoreCase("")) {
							sender.sendMessage(ChatColor.RED + "INVALID COLOR!");
							sender.sendMessage("Possible colors are: dark_red, red, gold, yellow, dark_green, green, aqua, dark_aqua, dark_blue, blue, light_purple, dark_purple, white, gray, dark_gray, black");
							return false;
						}
						
						config.set("PlayerSettings." + player.getUniqueId().toString() + ".coords." + args[1] + ".X", loc.getBlockX());
						config.set("PlayerSettings." + player.getUniqueId().toString() + ".coords." + args[1] + ".Y", loc.getBlockY());
						config.set("PlayerSettings." + player.getUniqueId().toString() + ".coords." + args[1] + ".Z", loc.getBlockZ());
						
						config.set("PlayerSettings." + player.getUniqueId().toString() + ".coords." + args[1] + ".Color", colorCode);
						
						plugin.saveConfig();
						
						player.sendMessage(ChatColor.GREEN + "Spot Saved!");
						
					}
					
					else {
						return false;
					}
				}
				
				else if(args.length == 1) {
					
					//List spots with name, loc and color
					if(args[0].equalsIgnoreCase("list")) {
						
						player.sendMessage(ChatColor.BOLD + "====================");
						player.sendMessage(ChatColor.GOLD + "SAVED SPOTS:");
						player.sendMessage(ChatColor.BOLD + "====================");
						player.sendMessage("");
						
						//Null fix
						if(config.getConfigurationSection("PlayerSetting." + player.getUniqueId().toString() + ".coords").getKeys(false).size() == 0) {
							sender.sendMessage("No spots saved!");
							return false;
						}
						//if(!config.contains("PlayerSettings." + player.getUniqueId().toString() + ".coords")) {
							//return false;
						//}
						
						for(String spot : config.getConfigurationSection("PlayerSettings." + player.getUniqueId().toString() + ".coords").getKeys(false)) {
							
							String color = config.getString("PlayerSettings." + player.getUniqueId().toString() + ".coords." + spot + ".Color");
							int X = config.getInt("PlayerSettings." + player.getUniqueId().toString() + ".coords." + spot + ".X");
							int Y = config.getInt("PlayerSettings." + player.getUniqueId().toString() + ".coords." + spot + ".Y");
							int Z = config.getInt("PlayerSettings." + player.getUniqueId().toString() + ".coords." + spot + ".Z");
							
							
							player.sendMessage(ChatColor.translateAlternateColorCodes('&', color) + spot + ":");
							player.sendMessage(ChatColor.BOLD + "    -X: " + X);
							player.sendMessage(ChatColor.BOLD + "    -Y: " + Y);
							player.sendMessage(ChatColor.BOLD + "    -Z: " + Z);
							player.sendMessage("");
						}
						
					}
					else {
						return false;
					}
					
				}
				
				else {
					return false;
				}
				
			}		
			
		}
		
		return true;
	}

}
