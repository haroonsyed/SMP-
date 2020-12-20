package me.Sshawarma.SMP.Command;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import me.Sshawarma.SMP.Main.Main;
import net.md_5.bungee.api.ChatColor;

public class AgniKai implements CommandExecutor{
	
	
	Plugin plugin = Main.getPlugin(Main.class);
	FileConfiguration config = plugin.getConfig();

	//Original Player locations before entering agni kai
	public static HashMap<String, Location> ogLocations = new HashMap<String, Location>();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		//Create agnikai location. Placeholder value because null wasn't good enough.
		Location kaiLocation = Bukkit.getWorld("world").getSpawnLocation();
		boolean kaiLocationSet = config.contains("AgniKai.Location");
		if(kaiLocationSet) {
			kaiLocation.setX(Double.parseDouble(config.getString("AgniKai.Location.X")));
			kaiLocation.setY(Double.parseDouble(config.getString("AgniKai.Location.Y")));
			kaiLocation.setZ(Double.parseDouble(config.getString("AgniKai.Location.Z")));
			kaiLocation.setWorld(Bukkit.getWorld("world"));
		}
		
		
		if(sender instanceof Player && cmd.getName().equalsIgnoreCase("agnikai")) {
			Player player = (Player) sender;
			
			//Disable all commands during agni kai
			if(!cmd.getName().equalsIgnoreCase("agnikai") && ogLocations.containsKey(player.getUniqueId().toString())) {
				player.sendMessage(ChatColor.RED + "You are currently in an Agni Kai! You may not perform commands.");
				player.sendMessage(ChatColor.RED + "To leave agni kai perform /agnikai");
			}
			
			//If player attempts to join agni kai arena
			if(cmd.getName().equalsIgnoreCase("agnikai")) {
				
				
				if(!kaiLocationSet) {
					sender.sendMessage(ChatColor.RED + "No arena location set yet!");
				}
				
				else if(ogLocations.containsKey(player.getUniqueId().toString())) {
					player.teleport(ogLocations.get(player.getUniqueId().toString()));
					ogLocations.remove(player.getUniqueId().toString());
					player.sendMessage("You have left the AgniKai Arena");
				}
				
				//Worldedit dependency
				else if(Bukkit.getPluginManager().getPlugin("WorldEdit") == null) {
					player.sendMessage(ChatColor.DARK_RED + "AgniKai Disabled until WorldEdit is installed.");
				}
				
				else {
					ogLocations.put(player.getUniqueId().toString(), player.getLocation());
					player.teleport(kaiLocation);
					player.sendMessage(ChatColor.GREEN + "You are currently in an Agni Kai! You may not perform commands.");
					player.sendMessage(ChatColor.GREEN + "To leave agni kai perform /agnikai");
					//Play music. Remove armor, whatever. Save xp amount.
					//Runnable for agnikai music
					new BukkitRunnable() {
						
						int loopTime = 41;
						
						@Override
						public void run() {
							
							if(loopTime > 40) {
								loopTime = 0;
								player.playSound(player.getLocation(), Sound.MUSIC_DISC_13, 10, 1);//(player.getLocation(), Effect.RECORD_PLAY, Material.MUSIC_DISC_13);
							}
							else {
								loopTime++;
							}
							if(!ogLocations.containsKey(player.getUniqueId().toString())) {
								player.stopSound(Sound.MUSIC_DISC_13);
								this.cancel();
							}
						}
						
					}.runTaskTimer(plugin, 0, 40);
				}
				
			}
			
		}
		
		//If OP wants to set arena location.
		//Assumes the arena is enclosed and inescapable.
		if(sender instanceof Player) {
			if(sender.isOp() && cmd.getName().equalsIgnoreCase("setAgniKaiLoc")) {
				Location agniKaiLoc = ((Player) sender).getLocation();
				config.set("AgniKai.Location.X", agniKaiLoc.getBlockX());
				config.set("AgniKai.Location.Y", agniKaiLoc.getBlockY());
				config.set("AgniKai.Location.Z", agniKaiLoc.getBlockZ());
				plugin.saveConfig();
			}
		}
		
		return true;
	}

}
