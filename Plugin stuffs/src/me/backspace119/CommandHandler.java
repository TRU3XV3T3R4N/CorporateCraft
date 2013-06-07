package me.backspace119;

import java.util.List;
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
	private static Company company;
	private JavaPlugin plugin;
	public CommandHandler(Permission perms, Logger logger, ConfigHandler configHandler, JavaPlugin plugin, Economy econ)
	{
		this.perms = perms;
		this.logger = logger;
		this.configHandler = configHandler;
		this.plugin = plugin;
		this.econ = econ;
		company = new Company(configHandler);
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
			player.sendMessage(indifferentColor() + "Corporate Craft v. " + plugin.getDescription().getVersion());
		
		}else if (args[0].equals("heal") || args[0].equals("h")) {
			if (perms.has(sender, "corporatecraft.heal")) {
				if (args.length == 0) {
					player.setHealth(20);
					player.sendMessage(noErrorColor() + "YOU HAVE BEEN HEALED");

				} else if (args.length == 1) {

					Player targetPlayer = player.getServer().getPlayer(args[0]);
					targetPlayer.setHealth(20);
					targetPlayer.sendMessage(noErrorColor() + "Player: "
							+ player.getName() + " has healed you");
					

				}
				return true;
			} else {
				sender.sendMessage(severeErrorColor()
						+ "you do not have permission to heal");
			}

		} else if (args[0].equals("test-permission")) {

			if (perms.has(player, "example.plugin.awesome")) {
				sender.sendMessage(noErrorColor() + "You are awesome!");
				return true;
			} else {
				sender.sendMessage(severeErrorColor() + "You suck!");
				return false;
			}

		} else if (args[0].equalsIgnoreCase("Start")) {
			if (perms.has(sender, "corporatecraft.ccStart")) {
				if (args.length < 2) {
					sender.sendMessage(severeErrorColor()
							+ "PLEASE INPUT NAME OF NEW COMPANY == /cc Start <name_of_new_company");
				} else {
					if(company.startNew(args[1], sender, plugin.getConfig()))
					{
						logger.severe(severeErrorColor() + "ERROR WHILE CREATING COMPANY -- PROBABLY AN ERROR WHILE SAVING CONFIG");
					}else{
					logger.info(sender.getName() + " HAS STARTED COMPANY " + args[1]);
					sender.sendMessage(congratulationsColor() + "Congratulations! you have begun company "+ args[1] + " successfully type /cc Access to access the account");
					
					return true;
				}
				}
			} else {
				sender.sendMessage(severeErrorColor() + "you do not have permission to Start a Company");
			}
		} else if (args[0].equals("Defaults")) {
			plugin.saveDefaultConfig();
			return true;

		}else if(args[0].equalsIgnoreCase("Reset"))
		{
			configHandler.saveDefaultConfig();
		}else if (args[0].equals("Reload")) {
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
						sender.sendMessage(noErrorColor() + "Current company balance: " + String.valueOf(econ.getBalance(player.getName() + "comp")));
						sender.sendMessage(indifferentColor() + "/cc Access withdraw <amount>");
						sender.sendMessage(indifferentColor() + "/cc Access deposit <amount>");
						
						return true;
					} else {
						sender.sendMessage(severeErrorColor() + "You do not own a company");
						return false;
					}
				} else {
					if(args[1].equalsIgnoreCase("withdraw"))
					{
						if(args.length < 3)
						{
							sender.sendMessage(syntaxErrorColor() + "Not enough Arguements /cc Access withdraw <amount>");
						}else{
							econ.depositPlayer(sender.getName(),Double.parseDouble(args[2]));
							econ.withdrawPlayer(sender.getName() + "comp", Double.parseDouble(args[2]));
						}
					}else if (args[1].equalsIgnoreCase("deposit"))
					{
						if(args.length < 3)
						{
							sender.sendMessage(syntaxErrorColor() + "Not enough Arguements /cc Access deposit <amount>");
						}else{
							econ.withdrawPlayer(sender.getName(),Double.parseDouble(args[2]));
							econ.depositPlayer(sender.getName() + "comp", Double.parseDouble(args[2]));
						}
					}
				}
			} else {
				
				sender.sendMessage("You do not have permission to access a company account!");
			}
		}else if(args[0].equalsIgnoreCase("SetHiring"))
		{
			if(args.length < 2)
			{
				player.sendMessage(syntaxErrorColor() + "Please put yes or no after SetHiring -- /cc SetHiring <yes|no>");
			}else{
				if(args[1].equalsIgnoreCase("yes"))
				{
					List<String> pass = configHandler.getConfig().getStringList(player.getName());
					if(pass.size() < 1)
					{
						player.sendMessage(severeErrorColor() + "You do not have permission to do this in any company");
						
					}else if(pass.size() < 2)
					{
						configHandler.getConfig().set("companies." + pass.get(0) + ".hiring", true);
						configHandler.getConfig().getStringList("companies.hiring").addAll(pass);
						configHandler.saveConfig();
					}else{
						if(args.length < 3)
						{
						player.sendMessage(syntaxErrorColor() + "you have permission to do this in multiple companies. Please specify a company == /cc SetHiring <yes|no> <name_of_company>");
						}else{
							configHandler.getConfig().set("companies." + args[2] + ".hiring", true);
							configHandler.getConfig().getStringList("hiring").add(args[2]);
							List<String> pass2 = configHandler.getConfig().getStringList("hiring");
							
							configHandler.saveConfig();
						}
						
					}
					
				}else if(args[1].equalsIgnoreCase("no"))
				{
					List<String> pass = configHandler.getConfig().getStringList(player.getName());
					if(pass.size() < 1)
					{
						player.sendMessage(severeErrorColor() + "You do not have permission to do this in any company");
						
					}else if(pass.size() < 2)
					{
						configHandler.getConfig().set("companies." + pass.get(0) + ".hiring", false);
						configHandler.getConfig().getStringList("companies.hiring").removeAll(pass);
						configHandler.saveConfig();
					}else{
						if(args.length < 3)
						{
						player.sendMessage(syntaxErrorColor() + "you have permission to do this in multiple companies. Please specify a company == /cc SetHiring <yes|no> <name_of_company>");
						}else{
							configHandler.getConfig().set("companies." + args[2] + ".hiring", false);
							
							List<String> pass1 = configHandler.getConfig().getStringList("hiring");
							pass1.remove(args[2]);
							configHandler.getConfig().set("hiring", pass1);
							configHandler.saveConfig();
						}
						
					}
				}
			}
		}else if(args[0].equalsIgnoreCase("Apply"))
		{
			if(args.length < 2)
			{
				player.sendMessage(syntaxErrorColor() + "/cc Apply <companyName> <position>");
			}
		}else if(args[0].equalsIgnoreCase("hiring"))
		{
			player.sendMessage(noErrorColor() + "Companies currently hiring:");
			List<String> pass = configHandler.getConfig().getStringList("hiring");
			for (String company : pass) {
			    player.sendMessage(company);
			}
			
		}
			
		}
		
		return false;

	
	}
	
	
	
	public ChatColor syntaxErrorColor()
	{
		return ChatColor.GOLD;
	}
			
	public ChatColor congratulationsColor()
	{
		return ChatColor.BLUE;
	}
	public ChatColor noErrorColor()
	{
		return ChatColor.GREEN;
	}
	public ChatColor indifferentColor()
	{
		return ChatColor.GRAY;
	}
	public ChatColor severeErrorColor()
	{
		return ChatColor.RED;
	}
	
	
}
