package me.Sshawarma.SMP;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;
import me.Sshawarma.Command.ChatListener;
import me.Sshawarma.Command.Commands;
import me.Sshawarma.Events.Beds;
import me.Sshawarma.Events.Death;
import me.Sshawarma.Events.PreventChestBreak;
import net.md_5.bungee.api.ChatColor;

public class Main extends JavaPlugin{
	
	//First real plugin woooo! ALso a test to see how GitHub updates projects.
	@Override
	public void onEnable(){
		getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "\n\nSMP has been enabled!\n");
		getServer().getPluginManager().registerEvents(new Beds(), this);
		getServer().getPluginManager().registerEvents(new ChatListener(), this);
		getServer().getPluginManager().registerEvents(new Death(), this);
		getServer().getPluginManager().registerEvents(new PreventChestBreak(), this);
		this.getCommand("find").setExecutor(new Commands());
		
	}
	
	@Override
	public void onDisable() {
		
		//Removes all chests (I hope) on server disable
		for(Location loc : Death.getdChests().keySet()) {
			loc.getBlock().setType(Material.AIR);
		}
		for(Location loc : Death.getdChests2().keySet()) {
			loc.getBlock().setType(Material.AIR);
		}
		getServer().getConsoleSender().sendMessage(ChatColor.RED + "\n\nSMP has been disabled!\n");		
	}
	
}
