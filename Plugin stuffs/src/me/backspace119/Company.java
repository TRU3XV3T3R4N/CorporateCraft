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
		
		CorporateCraft.econ.withdrawPlayer(sender.getName() + "comp", CorporateCraft.econ.getBalance(sender.getName() + "comp"));
		
		CorporateCraft.econ.depositPlayer(sender.getName() + "comp", 100);
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		Date date = new Date();
		
		configHandler.getConfig().set(name + ".founder", sender.getName());
		configHandler.getConfig().set(name + ".owner", sender.getName());
		configHandler.getConfig().set(name + ".founded", dateFormat.format(date));
		configHandler.getConfig().set(name + ".managers", managers);
		configHandler.getConfig().getStringList(name + ".managers").add(sender.getName());
		configHandler.getConfig().getStringList("companies").add(name);
		configHandler.getConfig().set(name + ".stockValue", -1.00);
		configHandler.getConfig().set(name + ".stockShares", -1.00);
		configHandler.getConfig().set(sender.getName(), name);
		
		 configHandler.saveConfig();
		return false;
	}
	
	
	public double getValue(String companyName, ConfigHandler configHandler)
	{
		return CorporateCraft.econ.getBalance(configHandler.getConfig().get(companyName + ".owner") + "comp");
	}
	
	public double getStockValue(String companyName, ConfigHandler configHandler)
	{
		return configHandler.getConfig().getDouble(companyName + ".stockValue");
	}
	
	public void setStockValue(String companyName, ConfigHandler configHandler, double value)
	{
		configHandler.getConfig().set(companyName + ".stockValue", value);
	}
	
	
	
	
}
