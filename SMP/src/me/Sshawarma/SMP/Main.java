package me.Sshawarma.SMP;

import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import me.Sshawarma.Command.ChestTrustCommand;
import me.Sshawarma.Command.FindCommand;
import me.Sshawarma.Command.SetColor;
import me.Sshawarma.Events.Beds;
import me.Sshawarma.Events.ChatColorChanger;
import me.Sshawarma.Events.ChatListener;
import me.Sshawarma.Events.Death;
import me.Sshawarma.Events.JoinListener;
import me.Sshawarma.Events.ChestAccess;
import me.Sshawarma.Events.StopCreeper;
import net.md_5.bungee.api.ChatColor;

public class Main extends JavaPlugin{
	
	//desyncing with github
	
	//First real plugin woooo!
	@Override
	public void onEnable(){
		getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "\n\nSMP has been enabled!\n");
		getServer().getPluginManager().registerEvents(new Beds(), this);
		getServer().getPluginManager().registerEvents(new ChatListener(), this);
		getServer().getPluginManager().registerEvents(new Death(), this);
		getServer().getPluginManager().registerEvents(new ChestAccess(), this);
		getServer().getPluginManager().registerEvents(new StopCreeper(), this);
		getServer().getPluginManager().registerEvents(new JoinListener(), this);
		getServer().getPluginManager().registerEvents(new ChatColorChanger(), this);
		this.getCommand("find").setExecutor(new FindCommand());
		this.getCommand("chatcolor").setExecutor(new SetColor());
		this.getCommand("chrust").setExecutor(new ChestTrustCommand());
		this.getCommand("dischrust").setExecutor(new ChestTrustCommand());
		
		
		getConfig().options().copyDefaults(true);
		saveConfig();
		
	}
	
	@Override
	public void onDisable() {
		
		//Removes all chests (I hope) on server disable
		for(Location loc : Death.getdChests().keySet()) {
			loc.getBlock().setType(Death.getdChests().get(loc));
		}
		for(Location loc : Death.getdChests2().keySet()) {
			loc.getBlock().setType(Death.getdChests2().get(loc));
		}
		getServer().getConsoleSender().sendMessage(ChatColor.RED + "\n\nSMP has been disabled!\n");		
	}
	
}
