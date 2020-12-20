package main.java.me.Sshawarma.SMP.Events;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.Sshawarma.SMP.Main.Main;

public class PKManager extends BukkitRunnable{
	
	PluginManager pm = Bukkit.getServer().getPluginManager();
	Plugin plugin = Main.getPlugin(Main.class);
	String lastChanged = "";
	
	//Need to run this on a task timer
	@Override
	public void run() {
		if(pm.getPlugin("ProjectKorra") != null) {
			if(Bukkit.getServer().getWorld("world").getTime() > 13000 && Bukkit.getServer().getWorld("world").getTime() < 23000 && !lastChanged.equals("night")) {
				pm.getPlugin("ProjectKorra").getConfig().set("Abilities.Water.Bloodbending.Range", 10);
				pm.getPlugin("ProjectKorra").getConfig().set("Abilities.Water.Bloodbending.Duration", 6000);
				pm.getPlugin("ProjectKorra").getConfig().set("Abilities.Water.Bloodbending.Cooldown", 3000);
				pm.getPlugin("ProjectKorra").saveConfig();
				pm.getPlugin("ProjectKorra").reloadConfig();
				//Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "pk reload");
				lastChanged = "night";
			}
			else if((Bukkit.getServer().getWorld("world").getTime() < 13000 || Bukkit.getServer().getWorld("world").getTime() > 23000) && !lastChanged.equals("day")){
				pm.getPlugin("ProjectKorra").getConfig().set("Abilities.Water.Bloodbending.Range", 5);
				pm.getPlugin("ProjectKorra").getConfig().set("Abilities.Water.Bloodbending.Duration", 3000);
				pm.getPlugin("ProjectKorra").getConfig().set("Abilities.Water.Bloodbending.Cooldown", 10000);
				pm.getPlugin("ProjectKorra").saveConfig();
				pm.getPlugin("ProjectKorra").reloadConfig();
				//Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "pk reload");
				lastChanged = "day";
			}
		}
		
	}
}
