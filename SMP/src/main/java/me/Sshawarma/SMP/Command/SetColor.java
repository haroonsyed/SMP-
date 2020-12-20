package me.Sshawarma.SMP.Command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.Sshawarma.SMP.Main.Main;
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
					
					String colorCode = translateColorToCode(args[0]);
					if(!colorCode.equals("")) {
						sender.sendMessage(ChatColor.GREEN + "Color set to " + ChatColor.translateAlternateColorCodes('&', colorCode) + args[0]);
						plugin.getConfig().set("PlayerSettings." + ((Player)sender).getUniqueId().toString() + ".chatcolor", colorCode);	
						plugin.saveConfig();
					}
					
					else {
						sender.sendMessage(ChatColor.RED + "INVALID COLOR!");
						sender.sendMessage("Possible colors are: dark_red, red, gold, yellow, dark_green, green, aqua, dark_aqua, dark_blue, blue, light_purple, dark_purple, white, gray, dark_gray, black");
					}
					
				}
			}
		}
		
		
		return false;
	}
	
	//Abstracted functionf or use elsewhere
	public String translateColorToCode(String color) {
		
		String colorCode = "";
		
		switch(color.toUpperCase()) {
		
			case "DARK_RED":
				colorCode = "&4";
				break;
			case "RED":
				colorCode = "&c";
				break;
			case "GOLD":
				colorCode = "&6";
				break;
			case "YELLOW":
				colorCode = "&e";
				break;
			case "DARK_GREEN":
				colorCode = "&2";
				break;
			case "GREEN":
				colorCode = "&a";
				break;
			case "AQUA":
				colorCode = "&b";
				break;
			case "DARK_AQUA":
				colorCode = "&3";
				break;
			case "DARK_BLUE":
				colorCode = "&1";
				break;
			case "BLUE":
				colorCode = "&9";
				break;
			case "LIGHT_PURPLE":
				colorCode = "&d";
				break;
			case "DARK_PURPLE":
				colorCode = "&5";
				break;
			case "WHITE":
				colorCode = "&f";
				break;
			case "GRAY":
				colorCode = "&7";
				break;
			case "DARK_GRAY":
				colorCode = "&8";
				break;
			case "BLACK":
				colorCode = "&0";
				break;
			default:
				
		}
		
		return colorCode;
		
	}
	
}
