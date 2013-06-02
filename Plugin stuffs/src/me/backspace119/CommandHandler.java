package me.backspace119;

import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import org.bukkit.plugin.java.JavaPlugin;

public class CommandHandler implements CommandExecutor{
	private Permission perms;
	private Logger logger;
	private Economy econ;
	private ConfigHandler configHandler;
	private static Company company = new Company();
	private JavaPlugin plugin;
	public CommandHandler(Permission perms, Logger logger, ConfigHandler configHandler, JavaPlugin plugin, Economy econ)
	{
		this.perms = perms;
		this.logger = logger;
		this.configHandler = configHandler;
		this.plugin = plugin;
		this.econ = econ;
	}

	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

		
		
		
		
		if(cmd.getName().equalsIgnoreCase("cc"))
		{
			if (!(sender instanceof Player)) {
				logger.info("only players may issue commands!!!");
				return false;
			}

			Player player = (Player) sender;
			
			
		if (args.length < 1)
		{
			player.sendMessage(ChatColor.GOLD + "Corporate Craft v. " + plugin.getDescription().getVersion());
		
		}else if (args[0].equals("heal") || args[0].equals("h")) {
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

		} else if (args[0].equals("test-permission")) {

			if (perms.has(player, "example.plugin.awesome")) {
				sender.sendMessage("You are awesome!");
				return true;
			} else {
				sender.sendMessage("You suck!");
				return false;
			}

		} else if (args[0].equalsIgnoreCase("Start")) {
			if (perms.has(sender, "corporatecraft.ccStart")) {
				if (args.length < 1) {
					sender.sendMessage(ChatColor.RED
							+ "PLEASE INPUT NAME OF NEW COMPANY == /StartCompany <name_of_new_company");
				} else {
					if(company.startNew(args[0], sender, plugin.getConfig(), configHandler))
					{
						logger.severe("ERROR WHILE CREATING COMPANY -- PROBABLY AN ERROR WHILE SAVING CONFIG");
					}else{
					logger.info(sender.getName() + " HAS STARTED COMPANY " + args[1]);
					sender.sendMessage(ChatColor.BLUE+ "Congratulations! you have begun company "+ args[1] + " successfully type /cc Access to access the account");
					
					return true;
				}
				}
			} else {
				sender.sendMessage(ChatColor.RED
						+ "you do not have permission to Start a Company");
			}
		} else if (args[0].equals("Defaults")) {
			plugin.saveDefaultConfig();
			return true;

		} else if (args[0].equals("Reload")) {
			plugin.reloadConfig();
			return true;
		} else if (args[0].equalsIgnoreCase("Access"))
		{
			if(perms.has(sender, "corporatecraft.ccAccess"))
			{
				if(args.length < 2)
				{
					if(econ.hasAccount(player.getName() + "comp"))
					{
						sender.sendMessage(ChatColor.GREEN + "Current company balance: " + String.valueOf(econ.getBalance(player.getName() + "comp")));
						sender.sendMessage(ChatColor.GOLD + "/cc Access withdraw <amount>");
						sender.sendMessage(ChatColor.GOLD + "/cc Access deposit <amount>");
						
						return true;
					} else {
						sender.sendMessage(ChatColor.GOLD + "You do not own a company");
						return false;
					}
				} else {
					if(args[1].equalsIgnoreCase("withdraw"))
					{
						if(args.length < 3)
						{
							sender.sendMessage(ChatColor.RED + "Not enough Arguements /cc Access withdraw <amount>");
						}else{
							econ.depositPlayer(sender.getName(),Double.parseDouble(args[2]));
							econ.withdrawPlayer(sender.getName() + "comp", Double.parseDouble(args[2]));
						}
					}else if (args[1].equalsIgnoreCase("deposit"))
					{
						if(args.length < 3)
						{
							sender.sendMessage(ChatColor.RED + "Not enough Arguements /cc Access deposit <amount>");
						}else{
							econ.withdrawPlayer(sender.getName(),Double.parseDouble(args[2]));
							econ.depositPlayer(sender.getName() + "comp", Double.parseDouble(args[2]));
						}
					}
				}
			} else {
				
				sender.sendMessage("You do not have permission to access a company account!");
			}
		}
		}
		return false;

	
	}
			
		
	
}
