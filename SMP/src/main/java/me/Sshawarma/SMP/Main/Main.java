package me.Sshawarma.SMP.Main;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import me.Sshawarma.SMP.Command.AgniKai;
import me.Sshawarma.SMP.Command.ChestTrustCommand;
import me.Sshawarma.SMP.Command.CoordinateSaver;
import me.Sshawarma.SMP.Command.FactionWar;
import me.Sshawarma.SMP.Command.FindCommand;
import me.Sshawarma.SMP.Command.Messaging;
import me.Sshawarma.SMP.Command.SetColor;
import me.Sshawarma.SMP.Command.SetFaction;
import me.Sshawarma.SMP.Events.AFKListener;
import me.Sshawarma.SMP.Events.Beds;
import me.Sshawarma.SMP.Events.ChatColorChanger;
import me.Sshawarma.SMP.Events.ChatListener;
import me.Sshawarma.SMP.Events.Death;
import me.Sshawarma.SMP.Events.FactionAttack;
import me.Sshawarma.SMP.Events.JoinListener;
import me.Sshawarma.SMP.Events.PKManager;
import me.Sshawarma.SMP.Events.ChestAccess;
import me.Sshawarma.SMP.Events.StopCreeper;
import me.Sshawarma.SMP.Events.WarListener;
import me.Sshawarma.SMP.Events.WarManager;
import net.md_5.bungee.api.ChatColor;

public class Main extends JavaPlugin{
	
	//Package these into a data interface with a manager class, as well as any other data
	//This will clean up main and ensure OOP principles are enforced.
	private AFKListener afkListener;
	private JoinListener joinListener;
	
	//First real plugin woooo!
	@Override
	public void onEnable(){
		getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "\n\nSMP has been enabled!\n");
		getServer().getPluginManager().registerEvents(new Beds(), this);
		getServer().getPluginManager().registerEvents(new ChatListener(this), this);
		getServer().getPluginManager().registerEvents(new Death(), this);
		getServer().getPluginManager().registerEvents(new ChestAccess(), this);
		getServer().getPluginManager().registerEvents(new StopCreeper(), this);
		joinListener = new JoinListener(this);
		getServer().getPluginManager().registerEvents(joinListener, this);
		getServer().getPluginManager().registerEvents(new ChatColorChanger(), this);
		getServer().getPluginManager().registerEvents(new FactionAttack(), this);
		getServer().getPluginManager().registerEvents(new me.Sshawarma.SMP.Events.AgniKai(), this);
		getServer().getPluginManager().registerEvents(new WarListener(), this);
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
		this.getCommand("spot").setExecutor(new CoordinateSaver());
		this.getCommand("war").setExecutor(new FactionWar());
		new PKManager().runTaskTimer(Main.getPlugin(Main.class), 1, 100);
		new WarManager().runTaskTimer(Main.getPlugin(Main.class), 1, 1200);
		//Both in one line to keep intialization localized 
		afkListener = new AFKListener(); 
		afkListener.runTaskTimer(Main.getPlugin(Main.class), 1, 1200 * 3); //1200 is a minute
		
		
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
		for(String p : me.Sshawarma.SMP.Command.AgniKai.ogLocations.keySet()) {
			//If in agnikai teleport them out
			UUID id = UUID.fromString(p);
			Bukkit.getOfflinePlayer(id).getPlayer().teleport(me.Sshawarma.SMP.Command.AgniKai.ogLocations.get(id.toString()));
		}
		
		getServer().getConsoleSender().sendMessage(ChatColor.RED + "\n\nSMP has been disabled!\n");		
	}
	
	public AFKListener getAFKListener() {
		return afkListener;
	}
	
}
