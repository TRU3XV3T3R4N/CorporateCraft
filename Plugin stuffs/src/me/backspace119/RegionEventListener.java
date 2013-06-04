package me.backspace119;

import java.util.logging.Logger;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.mewin.WGRegionEvents.events.RegionEnterEvent;

public class RegionEventListener implements Listener{

	ConfigHandler configHandler;
	JavaPlugin plugin;
	Logger logger;
	public RegionEventListener(ConfigHandler configHandler, JavaPlugin plugin, Logger logger)
	{
		this.configHandler = configHandler;
		this.plugin = plugin;
		this.logger = logger;
	}
	
	@EventHandler
	public void onRegionEnter(RegionEnterEvent e)
	{
		
System.out.println("made it into enter region event");
		
		if(configHandler.getConfig().getList("companyLand").contains(e.getRegion().getId()));
		{
		if(e.getRegion().isMember(e.getPlayer().getName()))
		{
			//later we will use the below to add members from the companies.yml to regions as members
			//this is just to remind me (backspace119) about the existance of this method and how to use it
			//RegionDBUtil.addToDomain(e.getRegion().getMembers(),(String[]) configHandler.getConfig().getList("").toArray(), 0);
			if(!e.getRegion().isOwner(e.getPlayer().getName()))
			{
				ItemStack[] stack = new ItemStack[e.getPlayer().getInventory().getSize()];
				stack = e.getPlayer().getInventory().getContents();
				configHandler.getConfig().set(e.getPlayer().getName() + "Inventory", stack);
				e.getPlayer().getInventory().clear();
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
