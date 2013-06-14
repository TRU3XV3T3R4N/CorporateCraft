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
package me.backspace119;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.mewin.WGRegionEvents.events.RegionEnterEvent;
import com.mewin.WGRegionEvents.events.RegionLeaveEvent;

public class RegionEventListener implements Listener{

	ConfigHandler configHandler;
	JavaPlugin plugin;
	Logger logger;
	ConfigHandler tmpHandler;
	Permission perms;
	PlayerInventoryManagement invConfig = new PlayerInventoryManagement();
	
	
	public RegionEventListener(ConfigHandler configHandler, JavaPlugin plugin, Logger logger, Permission perms)
	{
		this.configHandler = configHandler;
		this.plugin = plugin;
		this.logger = logger;
		this.perms = perms;
		tmpHandler = new ConfigHandler(plugin, "player.tmp");
		Utils.playerInCompanyPlot = tmpHandler.getConfig().getStringList("playersInPlots");
	}
	
	@EventHandler
	public void onRegionEnter(RegionEnterEvent e) throws IOException
	{
		
System.out.println("made it into enter region event");
		
		//checks if region is associated with CorporateCraft
		if(configHandler.getConfig().getList("companyLand").contains(e.getRegion().getId()))
		{
			if(!perms.has(e.getPlayer(), "corporatecraft.override.regions"))
			{
			//checks if they're a member of the region (meaning a member of the company)
		if(e.getRegion().isMember(e.getPlayer().getName()))
		{
			
			
			//later we will use the below to add members from the companies.yml to regions as members
			//this is just to remind me (backspace119) about the existance of this method and how to use it
			//RegionDBUtil.addToDomain(e.getRegion().getMembers(),(String[]) configHandler.getConfig().getList("").toArray(), 0);
			
			//if they're the owner they dont need their inventory cleared
			if(!e.getRegion().isOwner(e.getPlayer().getName()))
			{
				Utils.playerInCompanyPlot.add(e.getPlayer().getName());
				
				
				System.out.println(tmpHandler.getConfig().getBoolean(e.getPlayer().getName()));
				if(tmpHandler.getConfig().getBoolean(e.getPlayer().getName()))
				{
				
				
				tmpHandler.getConfig().getStringList("playersInPlots").add(e.getPlayer().getName());
				tmpHandler.getConfig().set(e.getPlayer().getName(), false);
				invConfig.storeInv(e.getPlayer());
				tmpHandler.saveConfig();
				e.getPlayer().getInventory().clear();
				e.getPlayer().sendMessage("You have entered a region of the company you work for. Your inventory has been confiscated and will be returned after you leave");
				
				
				}
					
				
			}else{
				e.getPlayer().sendMessage("Welcome to your Company's region");
			}
			
		}else{
			e.setCancelled(true);
			e.getPlayer().sendMessage("YOU ARE NOT PERMITTED TO ENTER THIS COMPANY'S LAND");
			logger.info(e.getPlayer().getName() + " tried to enter company owned region" + e.getRegion().getId());
			
		}
			}
		}
	}
	
	@EventHandler
	public void onRegionLeave(RegionLeaveEvent e) throws FileNotFoundException, IOException, InvalidConfigurationException
	{
		//checks to make sure region is associated with CorporateCraft
		if(configHandler.getConfig().getList("companyLand").contains(e.getRegion().getId()))
		{
			
			Utils.playerInCompanyPlot.remove(e.getPlayer().getName());
			if(!perms.has(e.getPlayer(), "corporatecraft.override.regions"))
			{
			if(!e.getRegion().isOwner(e.getPlayer().getName()))
			{
				tmpHandler.getConfig().set(e.getPlayer().getName(), true);
				
				tmpHandler.saveConfig();
				
				if(e.getRegion().getId().equalsIgnoreCase(Utils.companyLandMap.get(configHandler.getConfig().getString(e.getPlayer().getName()))))
					{
					Company comp = Utils.getCompany(configHandler.getConfig().getString(e.getPlayer().getName()));
					List<Integer> keep = comp.keepsOnLeave();
					List<ItemStack> stack;
					if(BufferedInventoryHandler.getFile(configHandler.getConfig().getString(e.getPlayer().getName())) == null)
							{
								ItemStack[] pass = new ItemStack[0];
								stack = Arrays.asList(pass);
							}else{
								stack = Arrays.asList(BufferedInventoryHandler.getBufferedInv(configHandler.getConfig().getString(e.getPlayer().getName())));
							}
					for(int materialID : keep)
					{
						for(int i = 0; i < e.getPlayer().getInventory().getSize(); i++)
						{
						if(e.getPlayer().getInventory().getItem(i).getType().getId() == materialID)
						{
							stack.add(e.getPlayer().getInventory().getItem(i));
							CorporateCraft.econ.depositPlayer(e.getPlayer().getName(), configHandler.getConfig().getDouble("companies." + configHandler.getConfig().getString(e.getPlayer().getName()) + "." + materialID) * e.getPlayer().getInventory().getItem(i).getAmount());
						}
						}
						BufferedInventoryHandler.storeBufferedInv(configHandler.getConfig().getString(e.getPlayer().getName()), stack.toArray(new ItemStack[stack.size()]));
					}
					invConfig.restoreInv(e.getPlayer());
					}
				 
				
				
				
			}
			}
		}
	}
	

}
