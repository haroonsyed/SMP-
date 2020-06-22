package me.Sshawarma.Command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FactionWar implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if(sender instanceof Player) {
			
			if(cmd.getName().equalsIgnoreCase("faction")) {
				if(args.length == 2) {
					if(args[0].equalsIgnoreCase("war")) {
						
					}
				}
			}
			
			
		}
		
		return false;
	}

}
