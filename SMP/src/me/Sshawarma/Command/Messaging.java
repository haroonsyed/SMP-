package me.Sshawarma.Command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Messaging implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// Messaging system in the works I suppose.
		if(sender instanceof Player) {
			
			// MSG
			if(cmd.getName().equalsIgnoreCase("msg")) {
				
			}
			
		}
		
		return false;
	}

}
