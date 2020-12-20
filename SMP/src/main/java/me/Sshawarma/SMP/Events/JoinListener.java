package main.java.me.Sshawarma.SMP.Events;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.Plugin;

import main.java.me.Sshawarma.SMP.Main.Main;
import main.java.me.Sshawarma.SMP.Util.Utils;
import net.md_5.bungee.api.ChatColor;

public class JoinListener implements Listener{
	
	Main main;
	Plugin plugin = Main.getPlugin(Main.class);
	
	public JoinListener(Main main) {
		this.main = main;
	}
	
	
	@EventHandler
	public void giveDefaults(PlayerJoinEvent event) {
		
		Plugin plugin = Main.getPlugin(Main.class);
		FileConfiguration config = plugin.getConfig();
		Player player = event.getPlayer();
		
		//UUID Migrator
		if(!config.contains("PlayerSettings." + player.getUniqueId().toString()) && config.contains("PlayerSettings." + player.getDisplayName())) {
			Utils util = new Utils();
			util.migrateConfigToUUID(player.getDisplayName());
		}
		
		//If they arent already registered in config, give them the defaults
		if(!(plugin.getConfig().contains("PlayerSettings." + player.getUniqueId().toString()))) {
			plugin.getConfig().set("PlayerSettings." + player.getUniqueId().toString() + ".chatcolor", "&7");
			plugin.getConfig().set("PlayerSettings." + event.getPlayer().getUniqueId().toString() + ".faction", "default");
			
			player.sendMessage(ChatColor.DARK_GRAY + "---------" + ChatColor.GREEN + "Your chat color by default is: "  + ChatColor.GRAY + "GRAY" + ChatColor.DARK_GRAY + "---------");
			player.sendMessage(ChatColor.DARK_GREEN + "If you would like to change your chatcolor, please use /chatcolor (desired color here w/o parenthesis)");
		}
		//Creates the default faction section
		if(!plugin.getConfig().contains("FactionSettings")) {
			plugin.getConfig().set("FactionSettings.default.peaceful", "false");
			plugin.getConfig().set("FactionSettings.default.friendlyFire", "true");
		}
		//Creates the chrusted players list
		if(!plugin.getConfig().contains("PlayerSettings." + player.getUniqueId().toString() + ".chrusted")) {
			ArrayList<String> chrusted = new ArrayList<String>();
			plugin.getConfig().set("PlayerSettings." + player.getUniqueId().toString() + ".chrusted", chrusted);
		}
		
		//Create default for war if it doesn't exist
		String faction = config.getString("PlayerSettings." + player.getUniqueId().toString() + ".faction");
		if(!config.contains("FactionSettings." + faction + ".war.isWarring")) {
			config.set("FactionSettings." + faction + ".war.isWarring", false);
		}
		
		//Disable annoying not. in war mode
		if(player.isOp()) {
			player.performCommand("lp log notify off");
		}
		
		//Help Message
		player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 10, 1);
		player.sendMessage(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "=========================================");
		player.sendMessage(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "|                                                            |");
		player.sendMessage(ChatColor.GREEN + "  For a list of features and commands, type /help");
		player.sendMessage(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "|                                                            |");
		player.sendMessage(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "=========================================");
		
		//AFK Message
		player.sendMessage(main.getAFKListener().getAFKMessage());
		
		
		plugin.saveConfig();
	}
	
	@EventHandler
	public void whiteListNotification(PlayerLoginEvent event) {
		//If the whitelist does not contain the player...
		if(!(Bukkit.getServer().getWhitelistedPlayers().contains(Bukkit.getOfflinePlayer(event.getPlayer().getUniqueId())))) {
			//Find op players and play a sound alerting them who the player is
			for(Player p : Bukkit.getServer().getOnlinePlayers()) {
				if(p.isOp()) {
					p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 10, 1);
					p.sendMessage(ChatColor.RED + "Non-Whitelisted player " + event.getPlayer().getName() + " is trying to join!");
				}
			}
		}
	}
	
	@EventHandler
	public void setFactionSpawn(PlayerRespawnEvent event) {
		if(!event.isBedSpawn() && !plugin.getConfig().getString("PlayerSettings." + event.getPlayer().getUniqueId().toString() + ".faction").equals("Cousins")) {
			Location spawn = event.getRespawnLocation().add(2406, 0, -697);
			spawn.setY(event.getPlayer().getWorld().getHighestBlockYAt(spawn));
			event.getPlayer().teleport(spawn);
		}
	}
	@EventHandler
	public void setFactionSpawn(PlayerJoinEvent event) {
		if(!event.getPlayer().hasPlayedBefore() && !plugin.getConfig().contains("PlayerSettings." + event.getPlayer().getUniqueId().toString())) {
			Location spawn = Bukkit.getServer().getWorld("world").getSpawnLocation();
			spawn.add(2406, 0, -697);
			spawn.setY(event.getPlayer().getWorld().getHighestBlockYAt(spawn));
			event.getPlayer().teleport(spawn);
		}
	}
}
