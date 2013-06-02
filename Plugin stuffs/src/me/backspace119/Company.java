package me.backspace119;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;


public class Company{
	
	
	public boolean startNew(String name, CommandSender sender, FileConfiguration config, ConfigHandler configHandler)
	{
		
		int startUpCosts = config.getInt("startcost");
		
		List<String> managers = Arrays.asList("");
		
		CorporateCraft.econ.createPlayerAccount(sender.getName() + "comp");
		
		CorporateCraft.econ.withdrawPlayer(sender.getName(), startUpCosts);
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		Date date = new Date();
		configHandler.getCustomConfig().set(name + ".founder", sender.getName());
		configHandler.getCustomConfig().set(name + ".owner", sender.getName());
		configHandler.getCustomConfig().set(name + ".founded", dateFormat.format(date));
		configHandler.getCustomConfig().set(name + "managers", managers);
		configHandler.getCustomConfig().getStringList(name + ".managers").add(sender.getName());
		configHandler.getCustomConfig().getStringList("companies").add(name);
		
		
		return configHandler.saveCustomConfig();
	}
	
	
}
