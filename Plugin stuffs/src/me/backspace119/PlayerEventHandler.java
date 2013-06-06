package me.backspace119;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.plugin.java.JavaPlugin;





public class PlayerEventHandler implements Listener{

	JavaPlugin plugin;
	public PlayerEventHandler(JavaPlugin plugin)
	{
		
		this.plugin = plugin;
		
		
	}
	
	
	@EventHandler
	public void onPlayerDrop(PlayerDropItemEvent e)
	{
		
		//checks if the player is in a CorporateCraft before canceling the drop event
		if(Utils.playerInCompanyPlot.contains(e.getPlayer().getName()))
		{
			
			e.setCancelled(true);
			
			e.getPlayer().sendMessage(ChatColor.RED + "you are not permitted to drop items in this companys plot");
		}
		
	}
}
