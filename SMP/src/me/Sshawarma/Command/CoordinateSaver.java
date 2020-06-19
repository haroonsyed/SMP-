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

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(sender instanceof Player) {
			
			Plugin plugin = Main.getPlugin(Main.class);
			FileConfiguration config = plugin.getConfig();
			Player player = (Player) sender;
			
			
			if(cmd.getName().equalsIgnoreCase("spot")) {
				
				//Prevents null
				if(!config.contains("PlayerSettings." + player.getDisplayName() + ".coords")) {
					config.set("PlayerSettings." + player.getDisplayName() + ".coords", "");
				}
				
				if(args.length == 2) {
					
					//Delete coordinate
					if(args[0].equalsIgnoreCase("delete")) {
						
						boolean deleted = false;
						
						for(String spot : config.getConfigurationSection("PlayerSettings." + player.getDisplayName() + ".coords").getKeys(false)) {
							if(spot.equalsIgnoreCase(args[1])) {
								config.set("PlayerSettings." + player.getDisplayName() + ".coords." + args[1], null);
								player.sendMessage(ChatColor.GREEN + "Spot deleted!");
								deleted = true;
							}
						}
						
						if(!deleted) {
							player.sendMessage(ChatColor.RED + "Could not find that spot!");
						}
						
					}
					
					//Save coordinate with name and color
					else if(args[0].equalsIgnoreCase("save")){
						Location loc = player.getLocation();
						SetColor translator = new SetColor();
						String colorCode = translator.translateColorToCode(args[1]);
						if(args[1].equalsIgnoreCase("")) {
							return false;
						}
						
						config.set("PlayerSettings." + player.getDisplayName() + ".coords." + args[1] + ".X", loc.getBlockX());
						config.set("PlayerSettings." + player.getDisplayName() + ".coords." + args[1] + ".Y", loc.getBlockY());
						config.set("PlayerSettings." + player.getDisplayName() + ".coords." + args[1] + ".Z", loc.getBlockZ());
						
						config.set("PlayerSettings." + player.getDisplayName() + ".coords." + args[1] + ".Color", colorCode);
						
						player.sendMessage(ChatColor.GREEN + "Spot Saved!");
						
					}
					
					else {
						return false;
					}
					
				}
				
				//List spots with name, loc and color
				else if(args[0].equalsIgnoreCase("list")) {
					
					player.sendMessage(ChatColor.BOLD + "====================");
					player.sendMessage(ChatColor.GOLD + "SAVED SPOTS:");
					player.sendMessage(ChatColor.BOLD + "====================");
					player.sendMessage("");
					
					for(String spot : config.getConfigurationSection("PlayerSettings." + player.getDisplayName() + ".coords").getKeys(false)) {
						
						String color = config.getString("PlayerSettings." + player.getDisplayName() + ".coords." + spot + ".Color");
						int X = config.getInt("PlayerSettings." + player.getDisplayName() + ".coords." + spot + ".X");
						int Y = config.getInt("PlayerSettings." + player.getDisplayName() + ".coords." + spot + ".Y");
						int Z = config.getInt("PlayerSettings." + player.getDisplayName() + ".coords." + spot + ".Z");
						
						
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', color) + spot + ":");
						player.sendMessage("	-X " + X);
						player.sendMessage("	-Y " + Y);
						player.sendMessage("	-Z " + Z);
						player.sendMessage("");
					}
					
				}
				
			}		
			
		}
		
		return true;
	}

}
