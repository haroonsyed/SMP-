package me.Sshawarma.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.Plugin;

import me.Sshawarma.SMP.Main;
import net.md_5.bungee.api.ChatColor;

public class FactionAttack implements Listener{
	
	Plugin plugin = Main.getPlugin(Main.class);
	
	@EventHandler
	public void onAttack(EntityDamageByEntityEvent event) {
		if(event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
			if(plugin.getConfig().getString(event.getDamager().getName() + ".faction") != plugin.getConfig().getString(event.getEntity().getName() + ".faction")) {
				event.setCancelled(true);
				event.getDamager().sendMessage(ChatColor.DARK_RED + "Player is not in the same faction! DamageEvent Cancelled!");
			}
		}
	}
	
}
