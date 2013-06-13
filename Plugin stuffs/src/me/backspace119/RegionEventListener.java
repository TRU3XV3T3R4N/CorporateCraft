package me.backspace119;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Logger;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
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
				
				
				 
				
				invConfig.restoreInv(e.getPlayer());
				
			}
			}
		}
	}
	

}
