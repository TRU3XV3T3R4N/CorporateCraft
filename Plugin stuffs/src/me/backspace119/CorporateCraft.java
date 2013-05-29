package me.backspace119;

import java.io.IOException;
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

	@Override
	public void onEnable() {
		if (!setupEconomy()) {
			logger.info(String.format("[%s] - Disabled due to no Vault dependency found!",getDescription().getName()));
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
		setupPermissions();
		setupChat();

		PluginDescriptionFile pdfFile = this.getDescription();

		this.logger.info(pdfFile.getName() + " v. " + pdfFile.getVersion() + " Enabled");

	}

	@Override
	public void onDisable() {

		PluginDescriptionFile pdfFile = this.getDescription();
		this.logger.info(pdfFile.getName() + " DISABLED");

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
						sender.sendMessage(ChatColor.GREEN + "Current company balance: " + String.valueOf(econ.getBalance(player.getName() + "-COMPANY")));
						sender.sendMessage(ChatColor.GOLD + "/ccAccess withdraw <amount>");
						sender.sendMessage(ChatColor.GOLD + "/ccAccess deposit <amount>");
						
						return true;
					} else {
						sender.sendMessage(ChatColor.GOLD + "You do not own a company");
						return false;
					}
				} else {
					if(String.valueOf(args[0]).equalsIgnoreCase("withdraw"))
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

}
