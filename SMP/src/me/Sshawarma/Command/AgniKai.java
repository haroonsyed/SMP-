package me.Sshawarma.Command;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.Sshawarma.SMP.Main;
import net.md_5.bungee.api.ChatColor;

public class AgniKai implements CommandExecutor{
	
	//TODO: Save Inventory?!?! Levels?. Make a container for this?
	

	//Original Player locations before entering agni kai
	static HashMap<String, Location> ogLocations = new HashMap<String, Location>();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		Plugin plugin = Main.getPlugin(Main.class);
		FileConfiguration config = plugin.getConfig();
		
		//Create agnikai location. Placeholder value because null wasn't good enough.
		Location kaiLocation = Bukkit.getWorld("world").getSpawnLocation();
		boolean kaiLocationSet = config.contains("AgniKai.Location");
		if(kaiLocationSet) {
			kaiLocation.setX(Double.parseDouble(config.getString("AgniKai.Location.X")));
			kaiLocation.setY(Double.parseDouble(config.getString("AgniKai.Location.Y")));
			kaiLocation.setZ(Double.parseDouble(config.getString("AgniKai.Location.Z")));
			kaiLocation.setWorld(Bukkit.getWorld("world"));
		}
		else {
			sender.sendMessage(ChatColor.RED + "No arena location set yet!");
			return false;
		}
		
		if(sender instanceof Player && cmd.getName().equalsIgnoreCase("agnikai")) {
			
			Player player = (Player) sender;
			
			if(ogLocations.containsKey(player.getDisplayName().toUpperCase())) {
				player.teleport(ogLocations.get(player.getDisplayName().toUpperCase()));
				ogLocations.remove(player.getDisplayName().toUpperCase());
				//Give back xp, whatever idk.
			}
			
			else {
				ogLocations.put(player.getDisplayName().toUpperCase(), player.getLocation());
				player.teleport(kaiLocation);
				//Play music. Remove armor, whatever. Save xp amount.
			}
			
		}
		
		return false;
	}

}
