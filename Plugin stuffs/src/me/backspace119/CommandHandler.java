package me.backspace119;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;

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
public class CommandHandler implements CommandExecutor{
	private Permission perms;
	private Logger logger;
	private Economy econ;
	private ConfigHandler configHandler;
	
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
			
			//List<String> pass = configHandler.getConfig().getStringList(player.getName());
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
							+ "PLEASE INPUT NAME OF NEW Utils.getCompany(configHandler.getConfig().getString(player.getName())) == /cc Start <name_of_new_Utils.getCompany(configHandler.getConfig().getString(player.getName()))");
				} else {
					Company comp = new Company(configHandler, plugin, plugin.getConfig(), player, args[1]);
					comp.saveCompany();
						
					
					logger.info(sender.getName() + " HAS STARTED COMPANY " + args[1]);
					sender.sendMessage(congratulationsColor() + "Congratulations! you have begun Company "+ args[1] + " successfully. Type /cc Access to access the account");
					
					return true;
				
				}
			} else {
				sender.sendMessage(severeErrorColor() + noPerm());
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
						sender.sendMessage(noErrorColor() + "Current Utils.getCompany(configHandler.getConfig().getString(player.getName())) balance: " + String.valueOf(econ.getBalance(player.getName() + "comp")));
						sender.sendMessage(indifferentColor() + "/cc Access withdraw <amount>");
						sender.sendMessage(indifferentColor() + "/cc Access deposit <amount>");
						
						return true;
					} else {
						sender.sendMessage(severeErrorColor() + "You do not own a Utils.getCompany(configHandler.getConfig().getString(player.getName()))");
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
				
				sender.sendMessage(severeErrorColor() + noPerm());
			}
		}else if(args[0].equalsIgnoreCase("SetHiring"))
		{
			
			if(args.length < 2)
			{
				player.sendMessage(syntaxErrorColor() + "Please put yes or no after SetHiring -- /cc SetHiring <yes|no>");
			}else{
				if(args[1].equalsIgnoreCase("yes"))
				{
					
					if(configHandler.getConfig().getString(player.getName()).equalsIgnoreCase(""))
					{
						player.sendMessage(severeErrorColor() + noPerm());
						
					}else{
						
						configHandler.getConfig().set("companies." + configHandler.getConfig().getString(player.getName()) + ".hiring", true);
						List<String> list = configHandler.getConfig().getStringList("hiring");
						list.add(configHandler.getConfig().getString(player.getName()));
						configHandler.getConfig().set("hiring", list);
						configHandler.saveConfig();
					
						
					}
					
				}else if(args[1].equalsIgnoreCase("no"))
				{
					
					if(configHandler.getConfig().getString(player.getName()).equalsIgnoreCase(""))
					{
						player.sendMessage(severeErrorColor() + noPerm());
						
					}else{
						configHandler.getConfig().set("companies." + configHandler.getConfig().getString(player.getName()) + ".hiring", false);
						List<String> list = configHandler.getConfig().getStringList("companies.hiring");
						list.remove(configHandler.getConfig().getString(player.getName()));
						configHandler.getConfig().set("hiring", list);
						configHandler.saveConfig();
					}
						
					
				}
			}
		}else if(args[0].equalsIgnoreCase("Apply"))
		{
			if(perms.has(sender, "corporatecraft.apply"))
			{
			if(args.length < 3)
			{
				player.sendMessage(syntaxErrorColor() + "/cc Apply <Utils.getCompany(configHandler.getConfig().getString(player.getName()))Name> <position>");
			}else{
				if(Utils.getCompany(configHandler.getConfig().getString(args[1])).isHiring())
				{
					Utils.getCompany(configHandler.getConfig().getString(args[1])).apply(player.getName(), args[2]);
				}
			}
			}else{
				player.sendMessage(severeErrorColor() + noPerm());
			}
			
		}else if(args[0].equalsIgnoreCase("hiring"))
		{
			player.sendMessage(noErrorColor() + "Companies currently hiring:");
			List<String> hiring = configHandler.getConfig().getStringList("hiring");
			for (String comp : hiring) {
			    player.sendMessage(comp);
			}
			
		}else if(args[0].equalsIgnoreCase("info"))
		{
			if(args.length < 2)
			{
				player.sendMessage(syntaxErrorColor() + "/cc Info <Utils.getCompany(configHandler.getConfig().getString(player.getName()))Name>");
			}else{
				player.sendMessage(configHandler.getConfig().getString("companies." + args[1] + ".description"));
			}
		}else if(args[0].equalsIgnoreCase("reviewApps"))
		{
			
			if(configHandler.getConfig().getString(player.getName()).equalsIgnoreCase(""))
			{
				player.sendMessage(severeErrorColor() + noPerm());
			}else{
				for(String applicant: Utils.getCompany(configHandler.getConfig().getString(player.getName())).reviewApplications())
				{
					player.sendMessage(applicant);
				}
			}
		}else if(args[0].equalsIgnoreCase("hire"))
		{
			
			if(configHandler.getConfig().getString(player.getName()).equalsIgnoreCase(""))
			{
				player.sendMessage(severeErrorColor() + noPerm() );
			}else{
				if(!(args.length < 3 && args.length > 3))
				{
					if(!Utils.getCompany(configHandler.getConfig().getString(player.getName())).hire(args[1], args[2], player))
					{
						player.sendMessage(severeErrorColor() + "invalid position");
					}
				}
				
			}	
		}else if(args[0].equalsIgnoreCase("setDescription"))
		{
			if(configHandler.getConfig().getString(player.getName()).equalsIgnoreCase(""))
			{
				player.sendMessage(severeErrorColor() + noPerm());
			}else{
				if(args.length > 1)
				{
				Utils.getCompany(configHandler.getConfig().getString(player.getName())).setDescription(args[1]);
				player.sendMessage(noErrorColor() + "You successfully changed the description of " + configHandler.getConfig().getString(player.getName()));
				}else{
					player.sendMessage(syntaxErrorColor() + "not enough arguements /cc setDescription <description> or /cc setDescription <Utils.getCompany(configHandler.getConfig().getString(player.getName()))> <description");
					
				}
			}
		}else if(args[0].equalsIgnoreCase("addRegion"))
		{
			if(perms.has(sender, "corporatecraft.region.add"))
			{
				if(configHandler.getConfig().getString(player.getName()).equalsIgnoreCase(""))
				{
					player.sendMessage(severeErrorColor() + noPerm());
				}else{
					if(args.length < 4)
				if(!Utils.getCompany(configHandler.getConfig().getString(player.getName())).addRegion( args[1], player, args[2]))
				{
					player.sendMessage(severeErrorColor() + "you cannot add this region as this type either the type is bad or you do not own the region");
				}else{
					player.sendMessage(noErrorColor() + "you have successfully added the region to your companies list");
				}
				}
			}else{
				player.sendMessage(severeErrorColor() + noPerm());
			}
		}else if(args[0].equalsIgnoreCase("SetTakeChest"))
		{
			if(configHandler.getConfig().getString(player.getName()).equalsIgnoreCase(""))
			{
				player.sendMessage(severeErrorColor() + noPerm());
			}else{
			Block chest = Utils.getTargetedContainerBlock(player);
			if(Utils.saveTakeChestToCompany(configHandler.getConfig().getString(player.getName()), chest, configHandler))
			{
				player.sendMessage(noErrorColor() + "You have successfully set this chest to a company take chest");
			}else{
				player.sendMessage(severeErrorColor() + "A CRITICAL ERROR HAS OCURRED PLEASE ENSURE YOU ARE LOOKING AT A CHEST. IF THE PROBLEM PERSISTS ASK A SYSTEM ADMINISTRATOR TO CHECK THE LOGS");
			}
			}
		}else if(args[0].equalsIgnoreCase("ChangePlayerCompany"))
		{
			if(!perms.has(player, "corporatecraft.admin.changeCompany") || !perms.has(player, "corporatecraft.override.changCompany"))
			{
				player.sendMessage(severeErrorColor() + noPerm());
			}else{
				if(args.length < 3)
				{
					configHandler.getConfig().set(player.getName(), args[2]);
					player.sendMessage(noErrorColor() + "You successfully changed your own company assignment to " + args[2]);
					
				}else{
					configHandler.getConfig().set(plugin.getServer().getPlayer(args[2]).getName(), args[3]);
				}
			}
		}else if(args[0].equalsIgnoreCase("getProduct"))
		{
			if(configHandler.getConfig().getString(player.getName()).equalsIgnoreCase(""))
			{
				player.sendMessage(severeErrorColor() + noPerm());
			}else{
				try {
					BufferedInventoryHandler.givePlayerBufferedInv(player, configHandler.getConfig().getString(player.getName()));
				} catch (IOException
						| InvalidConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					player.sendMessage(severeErrorColor() + "DID NOT LOAD BUFFERED INVENTORY CORRECTLY THE FILE MIGHT NOT EXIST");
					return false;
				}
				player.sendMessage(noErrorColor() + "You have successfully withdrawn items from your companies buffered inventory");
				
			}
			
		}else if(args[0].equalsIgnoreCase("addKeep"))
		{
			if(configHandler.getConfig().getString(player.getName()).equalsIgnoreCase(""))
			{
				player.sendMessage(severeErrorColor() + noPerm());
			}else{
				if(args.length < 3)
				{
					player.sendMessage(syntaxErrorColor() + "Not enough arguements /cc addKeep <item id number> <amount_to_pay_for_1_item>");
				}else{
				if(Utils.getCompany(configHandler.getConfig().getString(player.getName())).addKeep(Integer.parseInt(args[2]), Double.parseDouble(args[3])))
				{
					player.sendMessage(severeErrorColor() + "you have reached the maximum number of item types your company can keep from employees at the moment please use /cc removeKeep to give you more space");
					
				}else{
					player.sendMessage(noErrorColor() + "you successfully added an item for your company to keep from employees upon leaving property");
				}
				}
			}
		}else if(args[0].equalsIgnoreCase("removeKeep"))
		{
			if(configHandler.getConfig().getString(player.getName()).equalsIgnoreCase(""))
			{
				player.sendMessage(severeErrorColor() + noPerm());
			}else{
				if(args.length < 2)
				{
					player.sendMessage(syntaxErrorColor() + "Not enough arguements /cc removeKeep <item id number>");
				}else{
				if(Utils.getCompany(configHandler.getConfig().getString(player.getName())).removeKeep(Integer.parseInt(args[2])))
				{
					player.sendMessage(severeErrorColor() + "your company does not keep this item");
					
				}else{
					player.sendMessage(noErrorColor() + "you successfully added an item for your company to keep from employees upon leaving property");
				}
				}
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
	
	public String noPerm()
	{
		return "You do not have permission to do that";
	}
}
