package me.Sshawarma.SMP;

import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import me.Sshawarma.Command.AgniKai;
import me.Sshawarma.Command.ChestTrustCommand;
import me.Sshawarma.Command.FindCommand;
import me.Sshawarma.Command.Messaging;
import me.Sshawarma.Command.SetColor;
import me.Sshawarma.Command.SetFaction;
import me.Sshawarma.Events.Beds;
import me.Sshawarma.Events.ChatColorChanger;
import me.Sshawarma.Events.ChatListener;
import me.Sshawarma.Events.Death;
import me.Sshawarma.Events.FactionAttack;
import me.Sshawarma.Events.JoinListener;
import me.Sshawarma.Events.PKManager;
import me.Sshawarma.Events.ChestAccess;
import me.Sshawarma.Events.StopCreeper;
import net.md_5.bungee.api.ChatColor;

public class Main extends JavaPlugin{
	
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
		getServer().getPluginManager().registerEvents(new FactionAttack(), this);
		getServer().getPluginManager().registerEvents(new me.Sshawarma.Events.AgniKai(), this);
		this.getCommand("find").setExecutor(new FindCommand());
		this.getCommand("chatcolor").setExecutor(new SetColor());
		this.getCommand("chrust").setExecutor(new ChestTrustCommand());
		this.getCommand("dischrust").setExecutor(new ChestTrustCommand());
		this.getCommand("setFaction").setExecutor(new SetFaction());
		this.getCommand("faction").setExecutor(new SetFaction());
		this.getCommand("msg").setExecutor(new Messaging());
		this.getCommand("r").setExecutor(new Messaging());
		this.getCommand("agnikai").setExecutor(new AgniKai());
		this.getCommand("setAgniKaiLoc").setExecutor(new AgniKai());
		new PKManager().runTaskTimer(Main.getPlugin(Main.class), 1, 100);
		
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
		
		//Sends all players out of agnikai
		for(String p : me.Sshawarma.Command.AgniKai.ogLocations.keySet()) {
			Bukkit.getServer().getPlayer(p).teleport(me.Sshawarma.Command.AgniKai.ogLocations.get(p));
		}
		
		getServer().getConsoleSender().sendMessage(ChatColor.RED + "\n\nSMP has been disabled!\n");		
	}
	
}
