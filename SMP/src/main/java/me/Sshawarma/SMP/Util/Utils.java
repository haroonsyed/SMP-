package me.Sshawarma.SMP.Util;

import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import me.Sshawarma.SMP.Main.Main;

public class Utils {
	
	Plugin plugin = Main.getPlugin(Main.class);
	FileConfiguration config = plugin.getConfig();
	
	public void migrateConfigToUUID(String name) {
		//Get ID
		@SuppressWarnings("deprecation")
		OfflinePlayer p = Bukkit.getOfflinePlayer(name);
		UUID id = p.getUniqueId();
		//Create new section with ID, delete old one (Backup config before!)
		Map<String, Object> data = config.getConfigurationSection("PlayerSettings." + name).getValues(true);
		config.set("PlayerSettings." + name, null);
		config.createSection("PlayerSettings." + id, data);
		//Save
		plugin.saveConfig();
		
	}
	
	
}
