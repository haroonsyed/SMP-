package me.Sshawarma.Command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.Sshawarma.SMP.Main;

public class FactionWar implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if(sender instanceof Player) {
			
			Player player = (Player) sender;
			Plugin plugin = Main.getPlugin(Main.class);
			FileConfiguration config = plugin.getConfig();
			
			if(cmd.getName().equalsIgnoreCase("faction")) {
				
				//War declarations between factions.
				if(args.length == 2) {
					if(args[0].equalsIgnoreCase("war")) {
						
						sender.sendMessage("UUID must be completed first!");
						
					}
				}
			
				
			}
			
		}
		
		
		return false;
	}

}
