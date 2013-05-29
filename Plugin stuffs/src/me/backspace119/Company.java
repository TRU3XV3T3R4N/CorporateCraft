package me.backspace119;

import java.io.IOException;

import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;


public class Company{
	
	
	public boolean startNew(String name, CommandSender sender, FileConfiguration config)
	{
		
		int startUpCosts = config.getInt("startcost");

		
		CorporateCraft.econ.createPlayerAccount(sender.getName() + "comp");
		
		CorporateCraft.econ.withdrawPlayer(sender.getName(), startUpCosts);
		
		
		
		return false;
	}
	
	
}
