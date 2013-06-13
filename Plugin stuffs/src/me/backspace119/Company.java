package me.backspace119;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.databases.RegionDBUtil;


/**
 * 
 * @author backspace119
 *
 *This is the corporate craft plugin for bukkit type servers
 *Copyright (C) 2013  backspace119 
 *
 *This program is free software; you can redistribute it and/or
 *modify it under the terms of the GNU General Public License
 *as published by the Free Software Foundation; either version 2
 *of the License, or (at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *
 */
public class Company{
	
	ConfigHandler configHandler;
	JavaPlugin plugin;
	private final String name;
	private final Player owner;
	private int level;
	private int xp;
	private List<Integer> keep;
	public Company(ConfigHandler configHandler, JavaPlugin plugin, FileConfiguration config, Player player, String name)
	{
		this.configHandler = configHandler;
		this.plugin = plugin;
		this.owner = player;
		this.name = name;
	
		level = 1;
		xp = 0;
		
		int startUpCosts = config.getInt("startcost");
		if(!CorporateCraft.econ.has(player.getName(), startUpCosts))
		{
		 player.sendMessage(ChatColor.YELLOW + "You do not have enough money to start a new company! The cost is $" + startUpCosts);	
		}else{
		List<String> managers = Arrays.asList(player.getName());
		List<String> employees = Arrays.asList("");
		List<String> regionsFiller = Arrays.asList("");
		CorporateCraft.econ.createPlayerAccount(name + "CORPORATECRAFT");
		
		CorporateCraft.econ.withdrawPlayer(player.getName(), startUpCosts);
		CorporateCraft.econ.withdrawPlayer(name + "CORPORATECRAFT", CorporateCraft.econ.getBalance(name + "CORPORATECRAFT"));
		
		CorporateCraft.econ.depositPlayer(name + "CORPORATECRAFT", 100);
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		Date date = new Date();
		
		//set the data in the companies.yml that coincides with defaults for a new company
		//this will later be configurable inside the master config
		configHandler.getConfig().set("companies." + name + ".founder", player.getName());
		configHandler.getConfig().set("companies." + name + ".owner", player.getName());
		configHandler.getConfig().set("companies." + name + ".founded", dateFormat.format(date));
		configHandler.getConfig().set("companies." + name + ".managers", managers);
		configHandler.getConfig().set("companies." + name + ".hiring", false);
		configHandler.getConfig().set("companies." + name + ".employees", employees);
		configHandler.getConfig().set("companies." + name + ".regions.work", regionsFiller);
		configHandler.getConfig().set("companies." + name + ".regions.store", regionsFiller);
		configHandler.getConfig().set("companies." + name + ".regions.hq", regionsFiller);
		configHandler.getConfig().set("companies." + name + ".stockValue", -1.00);
		configHandler.getConfig().set("companies." + name + ".stockShares", -1.00);
		configHandler.getConfig().set("companies." + name + ".description", "default description");
		configHandler.getConfig().set("companies." + name + ".level", level);
		configHandler.getConfig().set("companies." + name + ".xp", xp);
		List<String> applicants = Arrays.asList("");
		configHandler.getConfig().set("companies." + name + ".applicants", applicants);

		configHandler.getConfig().set(player.getName(), name);

		configHandler.saveConfig();
		
		Utils.companyMap.put(name, this);
		
		}
		
	}
	
	
	public double getValue()
	{
		return CorporateCraft.econ.getBalance(configHandler.getConfig().get(name + ".owner") + "comp");
	}
	
	public double getStockValue()
	{
		return configHandler.getConfig().getDouble(name + ".stockValue");
	}
	
	public void setStockValue(String companyName, double value)
	{
		configHandler.getConfig().set(companyName + ".stockValue", value);
	}
	public void disbandCompany()
	{
		//will have to run economy entirely through corporatecraft in order to delete
		//the account correctly
	}
	public boolean apply(String playerName, String position)
	{
		//redundant eh? only seems to work like this though. Calling .add() on getStringList() wont update the config for some reason
		List<String> applicants = configHandler.getConfig().getStringList("companies." + name + ".applicants");
		applicants.add(playerName + " - applied for " + ChatColor.GREEN + position);
		configHandler.getConfig().set("copmanies." + name + ".applicants", applicants);
		
		return true;
	}

	
	public boolean clearApplications()
	{
		//make empty list to clear the applications (maybe just use .clear() in the future *.*
		 List<String> list = Arrays.asList("");
		configHandler.getConfig().set("companies." + name + ".applicants", list);
		
		return true;
	}
	
	public List<String> reviewApplications()
	{
		//return the list of applicants so it can be iterated through and sent back
		List<String> applicants = configHandler.getConfig().getStringList("companies." + name + ".applicants");
		
		return applicants;
	}
	
	public boolean hire(String applicant, String position, Player p)
	{
		
		
		
		if(position.equalsIgnoreCase("worker"))
		{
			List<String> employees = configHandler.getConfig().getStringList("companies." + name + ".employees");
			employees.add(applicant);
			configHandler.getConfig().set("companies." + name + ".employees", employees);
			configHandler.saveConfig();
			WorldGuardPlugin wgplugin = (WorldGuardPlugin) plugin.getServer().getPluginManager().getPlugin("WorldGuard");
			World world = p.getWorld();
			RegionManager manager = wgplugin.getRegionManager(world);
			String[] toAdd = new String[]{applicant};
			for(String regionId : configHandler.getConfig().getStringList("companies." + name + ".regions.work"))
			{
				
				RegionDBUtil.addToDomain(manager.getRegion(regionId).getMembers(),toAdd, 0);
			}
			return true;
		}else if(position.equalsIgnoreCase("manager"))
		{
			List<String> managers = configHandler.getConfig().getStringList("companies." + name + ".managers");
			managers.add(applicant);
			List<String> playerCompanies = configHandler.getConfig().getStringList(p.getName());
			playerCompanies.add(name);
			configHandler.getConfig().set(p.getName(), playerCompanies);
			configHandler.getConfig().set("companies." + name + ".managers", managers);
			configHandler.saveConfig();
			return true;
		}
		
		return false;
	}
	public void setDescription( String description)
	{
		configHandler.getConfig().set("companies." + name + ".description", description);
		configHandler.saveConfig();
	}
	
	public boolean isHiring()
	{
		return configHandler.getConfig().getBoolean("companies." + name + ".hiring");
	}
	
	public boolean addRegion( String regionID, Player p, String type)
	{
		
		WorldGuardPlugin wgplugin = (WorldGuardPlugin) plugin.getServer().getPluginManager().getPlugin("WorldGuard");
		World world = p.getWorld();
		RegionManager manager = wgplugin.getRegionManager(world);
		if(manager.getRegion(regionID).getOwners().contains(p.getName()))
		{
			if(type.equals("hq") || type.equals("work") || type.equals("store"))
			{
				List<String> companyLand = configHandler.getConfig().getStringList("companyLand");
				List<String> thisCompaniesLand = configHandler.getConfig().getStringList("companies." + name + ".regions." + type);
				companyLand.add(regionID);
				thisCompaniesLand.add(regionID);
				configHandler.getConfig().set("companyLand", companyLand);
				configHandler.getConfig().set("companies." + name + "regions." + type, thisCompaniesLand);
				configHandler.saveConfig();
				return true;
			}else{
			return false;
			}
		}
		
		return false;
	}
	
	public void saveCompany()
	{
		configHandler.saveConfig();
	}
	
	public List<Integer> keepsOnLeave()
	{
		keep = configHandler.getConfig().getIntegerList("companies." + name + ".keeps");
		return keep;
	}
	public boolean addKeep(int materialID)
	{
		if(keep.size() < amountAllowedToKeep())
		{
			keep.add(materialID);
			return true;
		}else{
			return false;
		}
		
	}
	public boolean removeKeep(int materialID)
	{
		return keep.remove(materialID) != null;
	}
	private int amountAllowedToKeep()
	{
		if(level < 5)
		{
			return 5;
		}else if(level < 10)
		{
			return 10;
		}else if(level < 15)
		{
			return 15;
		}else if(level < 20)
		{
			return 20;
		}
		return 0;
	}
}
