package me.backspace119;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.milkbowl.vault.Vault;
import net.milkbowl.vault.VaultEco.VaultBankAccount;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class CorporateCraft extends JavaPlugin {

	public final Logger logger = Logger.getLogger("Minecraft");
	public static CorporateCraft plugin;
	public static Economy econ = null;
	public static Permission perms = null;
	public static Chat chat = null;
	public static Company company = new Company();
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


		 configHandler = new ConfigHandler(plugin, "companies.yml");
		 configHandler.reloadConfig();
		 CommandHandler executor = new CommandHandler(perms, logger, configHandler, plugin, econ);
			
		getCommand("cc").setExecutor(executor);
		 
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
	
	
	
	
	
	
	


