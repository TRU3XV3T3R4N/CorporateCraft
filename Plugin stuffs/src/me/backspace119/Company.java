package me.backspace119;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;


public class Company{
	
	
	public boolean startNew(String name, Player player, FileConfiguration config)
	{
		
		int startUpCosts = config.getInt("startcost");

		CorporateCraft.econ.createPlayerAccount(name, player.getName() + "-COMPANY");
		
		CorporateCraft.econ.withdrawPlayer(player.getName(), startUpCosts);
		
		
		return false;
	}
	
	
}
