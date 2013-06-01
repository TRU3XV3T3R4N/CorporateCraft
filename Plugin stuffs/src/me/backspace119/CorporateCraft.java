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
	public static final ConfigHandler configHandler = new ConfigHandler();
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
		setupPermissions();
		setupChat();
		loadCustomConfig();
		PluginDescriptionFile pdfFile = this.getDescription();

		this.logger.info(pdfFile.getName() + " v. " + pdfFile.getVersion() + " Enabled");

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

	
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

		if (!(sender instanceof Player)) {
			logger.info("only players may issue commands!!!");
			return false;
		}

		Player player = (Player) sender;

		if (commandLabel.equals("heal") || commandLabel.equals("h")) {
			if (perms.has(sender, "corporatecraft.heal")) {
				if (args.length == 0) {
					player.setHealth(20);
					player.sendMessage(ChatColor.AQUA + "YOU HAVE BEEN HEALED");

				} else if (args.length == 1) {

					Player targetPlayer = player.getServer().getPlayer(args[0]);
					targetPlayer.setHealth(20);
					targetPlayer.sendMessage(ChatColor.AQUA + "Player: "
							+ player.getName() + " has healed you");
					

				}
				return true;
			} else {
				sender.sendMessage(ChatColor.RED
						+ "you do not have permission to heal");
			}

		} else if (commandLabel.equals("test-permission")) {

			if (perms.has(player, "example.plugin.awesome")) {
				sender.sendMessage("You are awesome!");
				return true;
			} else {
				sender.sendMessage("You suck!");
				return false;
			}

		} else if (commandLabel.equalsIgnoreCase("ccStart")) {
			if (perms.has(sender, "corporatecraft.ccStart")) {
				if (args.length < 1) {
					sender.sendMessage(ChatColor.RED
							+ "PLEASE INPUT NAME OF NEW COMPANY == /StartCompany <name_of_new_company");
				} else {
					if(company.startNew(args[0], sender, getConfig()))
					{
						logger.severe("ERROR WHILE CREATING COMPANY -- PROBABLY AN ERROR WHILE SAVING CONFIG");
					}else{
					logger.info(sender.getName() + " HAS STARTED COMPANY " + args[0]);
					sender.sendMessage(ChatColor.BLUE+ "Congratulations! you have begun company "+ args[0] + " successfully type /ccAccess to access the account");
					return true;
				}
				}
			} else {
				sender.sendMessage(ChatColor.RED
						+ "you do not have permission to Start a Company");
			}
		} else if (commandLabel.equals("ccDefaults")) {
			saveDefaultConfig();
			return true;

		} else if (commandLabel.equals("ccReload")) {
			reloadConfig();
			return true;
		} else if (commandLabel.equalsIgnoreCase("ccAccess"))
		{
			if(perms.has(sender, "corporatecraft.ccAccess"))
			{
				if(args.length < 1)
				{
					if(econ.hasAccount(player.getName() + "comp"))
					{
						sender.sendMessage(ChatColor.GREEN + "Current company balance: " + String.valueOf(econ.getBalance(player.getName() + "comp")));
						sender.sendMessage(ChatColor.GOLD + "/ccAccess withdraw <amount>");
						sender.sendMessage(ChatColor.GOLD + "/ccAccess deposit <amount>");
						
						return true;
					} else {
						sender.sendMessage(ChatColor.GOLD + "You do not own a company");
						return false;
					}
				} else {
					if(args[0].equalsIgnoreCase("withdraw"))
					{
						if(args.length < 2)
						{
							sender.sendMessage(ChatColor.RED + "Not enough Arguements /ccAccess withdraw <amount>");
						}else{
							econ.depositPlayer(sender.getName(),Double.parseDouble(args[1]));
							econ.withdrawPlayer(sender.getName() + "comp", Double.parseDouble(args[1]));
						}
					}else if (args[0].equalsIgnoreCase("deposit"))
					{
						if(args.length < 2)
						{
							sender.sendMessage(ChatColor.RED + "Not enough Arguements /ccAccess deposit <amount>");
						}else{
							econ.withdrawPlayer(sender.getName(),Double.parseDouble(args[1]));
							econ.depositPlayer(sender.getName() + "comp", Double.parseDouble(args[1]));
						}
					}
				}
			} else {
				
				sender.sendMessage("You do not have permission to access a company account!");
			}
		}
		return false;

	}
	
	
	
	
	//===========================================================================================================
	//PAST HERE IS CONFIG STUFF FROM THE OTHER FILE I NEED TO TRY TO FIND HOW TO MAKE IT WORK THERE AND NOT HERE
	//===========================================================================================================
	
	
	
	
	private FileConfiguration customConfig = null;
	private File customConfigFile = null;

	public void loadCustomConfig() {
		if (customConfigFile == null) {
			customConfigFile = new File(getDataFolder(),"companies.yml");
		}
		customConfig = YamlConfiguration.loadConfiguration(customConfigFile);

		// Look for defaults in the jar
		InputStream defConfigStream = getResource("companies.yml");
		if (defConfigStream != null) {
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
			customConfig.setDefaults(defConfig);
		}

	}

	public FileConfiguration getCustomConfig() {
		if (customConfig == null) {
			loadCustomConfig();
		}
		return customConfig;
	}

	public void saveCustomConfig() {
		if (customConfig == null || customConfigFile == null) {
			return;
		}
		try {
			getCustomConfig().save(customConfigFile);
		} catch (IOException ex) {
			getLogger().log(Level.SEVERE,
					"Could not save config to " + customConfigFile, ex);
		}

	}
	
	
	
	
	
	
	

}
