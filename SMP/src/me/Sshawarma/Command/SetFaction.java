package me.Sshawarma.Command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.Sshawarma.SMP.Main;
import net.md_5.bungee.api.ChatColor;

public class SetFaction implements CommandExecutor{
	
	Plugin plugin = Main.getPlugin(Main.class);

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if(sender instanceof Player && sender.isOp()) {
			
			if(cmd.getName().equalsIgnoreCase("setFaction")) {
				
				if(args.length != 2) {
					sender.sendMessage(ChatColor.DARK_RED + "Please input a faction and Player!");
				}
				
				else {
					plugin.getConfig().set(args[1]+ ".faction", args[0]);
				}
				
			}
			
		}
		
		return false;
	}
	
}
