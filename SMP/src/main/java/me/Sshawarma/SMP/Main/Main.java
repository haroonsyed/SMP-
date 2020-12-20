package main.java.me.Sshawarma.SMP.Main;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import main.java.me.Sshawarma.SMP.Command.AgniKai;
import main.java.me.Sshawarma.SMP.Command.ChestTrustCommand;
import main.java.me.Sshawarma.SMP.Command.CoordinateSaver;
import main.java.me.Sshawarma.SMP.Command.FactionWar;
import main.java.me.Sshawarma.SMP.Command.FindCommand;
import main.java.me.Sshawarma.SMP.Command.Messaging;
import main.java.me.Sshawarma.SMP.Command.SetColor;
import main.java.me.Sshawarma.SMP.Command.SetFaction;
import main.java.me.Sshawarma.SMP.Events.AFKListener;
import main.java.me.Sshawarma.SMP.Events.Beds;
import main.java.me.Sshawarma.SMP.Events.ChatColorChanger;
import main.java.me.Sshawarma.SMP.Events.ChatListener;
import main.java.me.Sshawarma.SMP.Events.Death;
import main.java.me.Sshawarma.SMP.Events.FactionAttack;
import main.java.me.Sshawarma.SMP.Events.JoinListener;
import main.java.me.Sshawarma.SMP.Events.PKManager;
import main.java.me.Sshawarma.SMP.Events.ChestAccess;
import main.java.me.Sshawarma.SMP.Events.StopCreeper;
import main.java.me.Sshawarma.SMP.Events.WarListener;
import main.java.me.Sshawarma.SMP.Events.WarManager;
import net.md_5.bungee.api.ChatColor;

public class Main extends JavaPlugin{
	
	//Class holders
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
		getServer().getPluginManager().registerEvents(new main.java.me.Sshawarma.SMP.Events.AgniKai(), this);
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
		for(String p : main.java.me.Sshawarma.SMP.Command.AgniKai.ogLocations.keySet()) {
			//If in agnikai teleport them out
			UUID id = UUID.fromString(p);
			Bukkit.getOfflinePlayer(id).getPlayer().teleport(main.java.me.Sshawarma.SMP.Command.AgniKai.ogLocations.get(id.toString()));
		}
		
		getServer().getConsoleSender().sendMessage(ChatColor.RED + "\n\nSMP has been disabled!\n");		
	}
	
	public AFKListener getAFKListener() {
		return afkListener;
	}
	
}
