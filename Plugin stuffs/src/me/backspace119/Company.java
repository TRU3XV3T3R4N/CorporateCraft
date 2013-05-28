package me.backspace119;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;


public class Company{
	
	public boolean startNew(String name, Player player, JavaPlugin plugin)
	{
		double startUpCosts = Double.parseDouble(plugin.getConfig().getString("startupcost"));
		
		
			
		
		CorporateCraft.econ.createBank(name, player.getName());
		
		CorporateCraft.econ.withdrawPlayer(player.getName(), startUpCosts);
		
		
		return false;
	}
	
	
}
