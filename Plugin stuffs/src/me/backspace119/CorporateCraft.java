package me.backspace119;


import java.util.logging.Logger;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

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
public class CorporateCraft extends JavaPlugin {

	public final Logger logger = Logger.getLogger("Minecraft");
	public static CorporateCraft plugin;
	public static Economy econ = null;
	public static Permission perms = null;
	public static Chat chat = null;
	public static ConfigHandler configHandler;
	
	@Override
	public void onEnable() {
		if (!setupEconomy()) {
			logger.info(String.format("[%s] - Disabled due to no Vault dependency found!",getDescription().getName()));
			getServer().getPluginManager().disablePlugin(this);
			return;
		}else if(!setupWorldGuard())
		{
			logger.info(String.format("[%s] - Disabled due to no WorldGuard dependency found!",getDescription().getName()));
			getServer().getPluginManager().disablePlugin(this);
		}
		plugin = this;
		setupPermissions();
		setupChat();
		
		
		
		PluginDescriptionFile pdfFile = this.getDescription();

		this.logger.info(pdfFile.getName() + " v. " + pdfFile.getVersion() + " Enabled");

		//register config
		 configHandler = new ConfigHandler(plugin, "companies.yml");
		 configHandler.reloadConfig();
		 if(!configHandler.getConfig().getString("goodFile").equalsIgnoreCase("true"))
		 {
			 System.out.println(configHandler.getConfig().getString("goodFile"));
			 configHandler.saveDefaultConfig();
			 configHandler.reloadConfig();
		 }
		 
		 CommandHandler executor = new CommandHandler(perms, logger, configHandler, plugin, econ);
		//regsiter command listeners	
		getCommand("cc").setExecutor(executor);
		 
		 //register event listeners
		 getServer().getPluginManager().registerEvents(new RegionEventListener(configHandler, this, logger, perms), this);
		 getServer().getPluginManager().registerEvents(new PlayerEventHandler(this, perms), this);
		
		 
	}

	@Override
	public void onDisable() {

		
		PluginDescriptionFile pdfFile = this.getDescription();
		this.logger.info(pdfFile.getName() + " DISABLED");

	}

	private boolean setupWorldGuard()
	{
		if (getServer().getPluginManager().getPlugin("WorldGuard") == null) {
			logger.severe("WORLDGUARD NOT FOUND!");
			return false;
		}else{
			return true;
		}
	}
	private boolean setupEconomy() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			logger.severe("VAULT NOT FOUND!");
			return false;
		}
		
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
		if (rsp == null) {
			logger.severe("REGISTERED SERVICE PROVIDER DID NOT FIND VAULT!");
			return false;
		}
		econ = rsp.getProvider();
		return econ != null;
	}

	private boolean setupChat() {
		RegisteredServiceProvider<Chat> chatProvider = getServer()
				.getServicesManager().getRegistration(
						net.milkbowl.vault.chat.Chat.class);
		if (chatProvider != null) {
			chat = chatProvider.getProvider();
		}
		return chat != null;
	}

	private boolean setupPermissions() {
		RegisteredServiceProvider<Permission> rsp = getServer()
				.getServicesManager().getRegistration(
						net.milkbowl.vault.permission.Permission.class);
		perms = rsp.getProvider();
		
		return perms != null;
		
	}
	
	

	}
	
