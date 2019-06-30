package me.Sshawarma.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import me.Sshawarma.SMP.Main;
import net.md_5.bungee.api.ChatColor;

public class JoinListener implements Listener{
	@EventHandler
	public void giveDefaults(PlayerJoinEvent event) {
		
		Plugin plugin = Main.getPlugin(Main.class);
		Player player = event.getPlayer();
		
		//If they arent already registered in config, give them the defaults
		if(!(plugin.getConfig().contains(player.getDisplayName()))) {
			plugin.getConfig().set(player.getDisplayName(), "&7");
			plugin.saveConfig();
			
			player.sendMessage(ChatColor.DARK_GRAY + "---------" + ChatColor.GREEN + "Your chat color by default is: "  + ChatColor.GRAY + "GRAY" + ChatColor.DARK_GRAY + "---------");
			player.sendMessage(ChatColor.DARK_GREEN + "If you would like to change your chatcolor, please use /chatcolor (desired color here w/o parenthesis)");
			
		}
		
		/*
		ArrayList<String> playersList = new ArrayList<String>();
		playersList.add("null");
		playersList.clear();
		
		try {
			playersList = ChestTrustCommand.getTrustedPlayers(player.getDisplayName().toUpperCase());
			player.sendMessage(ChatColor.DARK_GREEN + "Trusted players are: " + playersList.toString());
		}
		catch(Exception e){
			player.sendMessage(ChatColor.DARK_GREEN + "trustedPlayers list created!. Add/remove to it with /chrust or /dischrust");
		}
		

		ChestTrustCommand.setTrustedPlayers(player.getDisplayName().toUpperCase(), playersList);
		*/
	}
}
