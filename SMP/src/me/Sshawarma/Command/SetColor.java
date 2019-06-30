package me.Sshawarma.Command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.Sshawarma.SMP.Main;
import net.md_5.bungee.api.ChatColor;

public class SetColor implements CommandExecutor{
	
	Plugin plugin = Main.getPlugin(Main.class);
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if(sender instanceof Player) {
			if(cmd.getName().equalsIgnoreCase("chatcolor")) {
				if(args.length != 1){
					sender.sendMessage("Please input a color!");
					sender.sendMessage("Choose from : dark_red, red, gold, yellow, dark_green, green, aqua, dark_aqua, dark_blue, blue, light_purple, dark_purple, white, gray, dark_gray, black");
				}
				else if(args.length == 1) {
					//Checks all colors
					switch(args[0].toUpperCase()) {
						
						case "DARK_RED":
							plugin.getConfig().set(sender.getName(), "&4");	
							plugin.saveConfig();
							break;
						case "RED":
							plugin.getConfig().set(sender.getName(), "&c");	
							plugin.saveConfig();
							break;
						case "GOLD":
							plugin.getConfig().set(sender.getName(), "&6");	
							plugin.saveConfig();
							break;
						case "YELLOW":
							plugin.getConfig().set(sender.getName(), "&e");	
							plugin.saveConfig();
							break;
						case "DARK_GREEN":
							plugin.getConfig().set(sender.getName(), "&2");	
							plugin.saveConfig();
							break;
						case "GREEN":
							plugin.getConfig().set(sender.getName(), "&a");	
							plugin.saveConfig();
							break;
						case "AQUA":
							plugin.getConfig().set(sender.getName(), "&b");	
							plugin.saveConfig();
							break;
						case "DARK_AQUA":
							plugin.getConfig().set(sender.getName(), "&3");	
							plugin.saveConfig();
							break;
						case "DARK_BLUE":
							plugin.getConfig().set(sender.getName(), "&1");	
							plugin.saveConfig();
							break;
						case "BLUE":
							plugin.getConfig().set(sender.getName(), "&9");	
							plugin.saveConfig();
							break;
						case "LIGHT_PURPLE":
							plugin.getConfig().set(sender.getName(), "&d");	
							plugin.saveConfig();
							break;
						case "DARK_PURPLE":
							plugin.getConfig().set(sender.getName(), "&5");	
							plugin.saveConfig();
							break;
						case "WHITE":
							plugin.getConfig().set(sender.getName(), "&f");	
							plugin.saveConfig();
							break;
						case "GRAY":
							plugin.getConfig().set(sender.getName(), "&7");	
							plugin.saveConfig();
							break;
						case "DARK_GRAY":
							plugin.getConfig().set(sender.getName(), "&8");	
							plugin.saveConfig();
							break;
						case "BLACK":
							plugin.getConfig().set(sender.getName(), "&0");	
							plugin.saveConfig();
							break;
						default:
							sender.sendMessage(ChatColor.RED + "INVALID COLOR!");
							sender.sendMessage("Possible colors are: dark_red, red, gold, yellow, dark_green, green, aqua, dark_aqua, dark_blue, blue, light_purple, dark_purple, white, gray, dark_gray, black");
					
					}
						
					
					
					/*if(args[0].equalsIgnoreCase("red")) {
						plugin.getConfig().set(sender.getName(), "&c");	
						plugin.saveConfig();
					}*/

				
				}
			}
		}
		
		
		return false;
	}
	
}
