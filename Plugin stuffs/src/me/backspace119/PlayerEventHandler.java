package me.backspace119;

import net.milkbowl.vault.permission.Permission;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.plugin.java.JavaPlugin;





public class PlayerEventHandler implements Listener{

	JavaPlugin plugin;
	Permission perms;
	public PlayerEventHandler(JavaPlugin plugin, Permission perms)
	{
		
		this.plugin = plugin;
		this.perms = perms;
		
	}
	
	
	@EventHandler
	public void onPlayerDrop(PlayerDropItemEvent e)
	{
		
		//checks if the player is in a CorporateCraft before canceling the drop event
		if(Utils.playerInCompanyPlot.contains(e.getPlayer().getName()))
		{
			if(!perms.has(e.getPlayer(), "corporatecraft.override.regions")){
			e.setCancelled(true);
			
			e.getPlayer().sendMessage(ChatColor.RED + "you are not permitted to drop items in this companys plot");
			}
			
		}
		
	}
}
