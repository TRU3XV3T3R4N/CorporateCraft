package me.backspace119;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;



public class Company{
	
	ConfigHandler configHandler;
	
	public Company(ConfigHandler configHandler)
	{
		this.configHandler = configHandler;
	}
	
	public boolean startNew(String name, CommandSender sender, FileConfiguration config)
	{
		
		int startUpCosts = config.getInt("startcost");
		if(!CorporateCraft.econ.has(sender.getName(), startUpCosts))
		{
		 sender.sendMessage(ChatColor.YELLOW + "You do not have enough money to start a new company! The cost is $" + startUpCosts);	
		}else{
		List<String> managers = Arrays.asList(sender.getName());
		CorporateCraft.econ.createPlayerAccount(sender.getName() + "comp");
		
		CorporateCraft.econ.withdrawPlayer(sender.getName(), startUpCosts);
		
		CorporateCraft.econ.withdrawPlayer(sender.getName() + "comp", CorporateCraft.econ.getBalance(sender.getName() + "comp"));
		
		CorporateCraft.econ.depositPlayer(sender.getName() + "comp", 100);
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		Date date = new Date();
		
		//set the data in the companies.yml that coincides with defaults for a new company
		//this will later be configurable inside the master config
		configHandler.getConfig().set("companies" + "." + name + ".founder", sender.getName());
		configHandler.getConfig().set("companies" + "." + name + ".owner", sender.getName());
		configHandler.getConfig().set("companies" + "." + name + ".founded", dateFormat.format(date));
		configHandler.getConfig().set("companies" + "." + name + ".managers", managers);
		configHandler.getConfig().set("companies" + "." + name + ".hiring", false);
		
		configHandler.getConfig().set("companies" + "." + name + ".stockValue", -1.00);
		configHandler.getConfig().set("companies" + "." + name + ".stockShares", -1.00);
		if(configHandler.getConfig().contains(sender.getName()))
		{
		configHandler.getConfig().getStringList(sender.getName()).add(name);
		}else{
			List<String> list = Arrays.asList(name);
			configHandler.getConfig().set(sender.getName(), list);
		}
		
		
		 configHandler.saveConfig();
		
		}
		return false;
	}
	
	
	public double getValue(String companyName)
	{
		return CorporateCraft.econ.getBalance(configHandler.getConfig().get(companyName + ".owner") + "comp");
	}
	
	public double getStockValue(String companyName)
	{
		return configHandler.getConfig().getDouble(companyName + ".stockValue");
	}
	
	public void setStockValue(String companyName, double value)
	{
		configHandler.getConfig().set(companyName + ".stockValue", value);
	}
	public void disbandCompany(String companyName)
	{
		//will have to run economy entirely through corporatecraft in order to delete
		//the account correctly
	}
	
	
	
	
}
