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

import net.milkbowl.vault.permission.Permission;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;





public class PlayerEventHandler implements Listener{

	JavaPlugin plugin;
	Permission perms;
	ConfigHandler configHandler;
	public PlayerEventHandler(JavaPlugin plugin, Permission perms, ConfigHandler configHandler)
	{
		
		this.plugin = plugin;
		this.perms = perms;
		this.configHandler = configHandler;
	}
	
	
	@EventHandler
	public void onPlayerDrop(PlayerDropItemEvent e)
	{
		
		//checks if the player is in a CorporateCraft before canceling the drop event
		if(Utils.playerInCompanyPlot.contains(e.getPlayer().getName()))
		{
			if(!perms.has(e.getPlayer(), "corporatecraft.override.regions")){
			e.setCancelled(true);
			
			e.getPlayer().sendMessage(ChatColor.RED + "you are not permitted to drop items in this companys plot");
			}
			
		}
		
	}
	
	public void onPlayerJoin(PlayerJoinEvent e)
	{
		if(configHandler.getConfig().getString(e.getPlayer().getName()) == null)
		{
			configHandler.getConfig().set(e.getPlayer().getName(), "");
		}
	}
}
