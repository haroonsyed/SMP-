package me.Sshawarma.SMP;

import org.bukkit.plugin.java.JavaPlugin;

import me.Sshawarma.Command.ChatListener;
import me.Sshawarma.Command.Commands;
import me.Sshawarma.Events.Beds;
import net.md_5.bungee.api.ChatColor;

public class Main extends JavaPlugin{
	
	@Override
	public void onEnable(){
		getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "\nSMP has been enabled!\n");
		getServer().getPluginManager().registerEvents(new Beds(), this);
		getServer().getPluginManager().registerEvents(new ChatListener(), this);
		this.getCommand("find").setExecutor(new Commands());
		
	}
	
	@Override
	public void onDisable() {
		getServer().getConsoleSender().sendMessage(ChatColor.RED + "\nSMP has been disabled!\n");
		
	}
	
}
