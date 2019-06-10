package me.Sshawarma.Command;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.Sshawarma.SMP.Main;
import net.md_5.bungee.api.ChatColor;



public class Commands implements CommandExecutor{
	Plugin plugin = Main.getPlugin(Main.class);
	private static int arrayLoc=0;
	private static ArrayList<String> senders = new ArrayList<String>();
	private static ArrayList<String> recievers = new ArrayList<String>();
	private static boolean accept = false;
	private static boolean deny = false;
	private static boolean pendingRequest = false;
	
	public static boolean getAccept() {
		return accept;
	}
	public static void setAccept(boolean status) {
		accept = status;
	}
	public static boolean getDeny() {
		return deny;
	}
	public static void setDeny(boolean status) {
		deny = status;
	}
	public static int getSendersSize() {
		return senders.size();
	}
	public static String getReciever(int index) {
		return recievers.get(index);
	}
	public static int getArrayLoc() {
		return arrayLoc;
	}

	
	
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		/*if(!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "\nYou must be a player to use commands!\n");
			return true;
		}
		if(cmd.getName().equalsIgnoreCase("accept") && recievers.size()!= 0) {
			if(sender.getName() == recievers.get(arrayLoc)) {
				accept = true;
			}
		}
		if(cmd.getName().equalsIgnoreCase("deny") && recievers.size()!= 0) {
			if(sender.getName() == recievers.get(arrayLoc)) {
				deny= true;
			}
		}*/
		if(cmd.getName().equalsIgnoreCase("find")){
			Player player = (Player) sender;
			if(args.length == 0) {
				sender.sendMessage(ChatColor.RED + "\nYou must give a target player!\n");
				return true;
			}
			
			else if(Bukkit.getServer().getPlayer(args[0])==null) {
				sender.sendMessage(ChatColor.RED + "\nPlayer is either offline or does not exist.");
				return true;
			}
			else if(pendingRequest == true) {
				return true;
			}
			else if(Bukkit.getServer().getPlayer(args[0]).getName() != sender.getName()){
				
				Player target = Bukkit.getServer().getPlayer(args[0]);
				target.playSound(target.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 10, 1);
				long start = System.currentTimeMillis();
				senders.add(player.getName());
				recievers.add(target.getName());
				pendingRequest = true;
				
				target.sendMessage(ChatColor.AQUA + "" +  ChatColor.BOLD + "\n " + ChatColor.GOLD + player.getName() + ChatColor.AQUA + "" +  ChatColor.BOLD + " would like your location!\n");
				target.sendMessage("You have 20 seconds to respond with accept or deny in chat.");
				sender.sendMessage(ChatColor.GREEN + "Request sent!");
				
				new BukkitRunnable() {

					@Override
					public void run() {
						if((accept == true) && (System.currentTimeMillis()<=start+20000)) {
							
							player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 10, 1);
							player.sendMessage("\n" + ChatColor.DARK_GREEN + target.getName() + "'s location is: " + ChatColor.LIGHT_PURPLE + target.getLocation().getBlockX() + 
								ChatColor.GREEN + " " + target.getLocation().getBlockY() +ChatColor.BLUE + " " + target.getLocation().getBlockZ() +  ChatColor.WHITE + " in " + target.getWorld().getName() +"\n");
							
							accept = false;
							this.cancel();
							senders.clear();
							recievers.clear();
							pendingRequest = false;
							return;
						}
						else if((deny == true) && (System.currentTimeMillis()<=start+20000)){
							
							target.playSound(target.getLocation(), Sound.ENTITY_VILLAGER_DEATH, 10, 1);
							player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_DEATH, 10, 1);
							target.sendMessage(ChatColor.DARK_RED + "\nRequest Denied\n");
							player.sendMessage(ChatColor.DARK_RED + "\nRequest Denied\n");
							deny = false;
							this.cancel();
							senders.clear();
							recievers.clear();
							pendingRequest = false;
							return;
						}
						else if(System.currentTimeMillis()>= start+20000) {
							target.playSound(target.getLocation(), Sound.ENTITY_VILLAGER_DEATH, 10, 1);
							player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_DEATH, 10, 1);
							target.sendMessage(ChatColor.RED + "\nYou have waited too long!\n");
							player.sendMessage(ChatColor.RED + "\nTarget never responded!\n");
							senders.clear();
							recievers.clear();
							pendingRequest = false;
							this.cancel();
							return;
						}
					}
					
					}.runTaskTimerAsynchronously(plugin, 0, 2);
				}
			else {
				sender.sendMessage("There is not point in finding yourself...or an error occured.");
			}
		}
			
		return false;
	}
}
