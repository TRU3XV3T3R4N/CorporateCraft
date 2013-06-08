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
		company = new Company(configHandler, plugin);
	}

	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

		
		
		
		
		if(cmd.getName().equalsIgnoreCase("cc"))
		{
			if (!(sender instanceof Player)) {
				logger.info("only players may issue commands!!!");
				return false;
			}

			Player player = (Player) sender;
			
			List<String> pass = configHandler.getConfig().getStringList(player.getName());
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
					
					if(pass.size() < 1)
					{
						player.sendMessage(severeErrorColor() + noPerm());
						
					}else if(pass.size() < 2)
					{
						
						configHandler.getConfig().set("companies." + pass.get(0) + ".hiring", true);
						List<String> list = configHandler.getConfig().getStringList("hiring");
						list.add(pass.get(0));
						configHandler.getConfig().set("hiring", list);
						configHandler.saveConfig();
					}else{
						if(args.length < 3)
						{
						player.sendMessage(syntaxErrorColor() + "you have permission to do this in multiple companies. Please specify a company == /cc SetHiring <yes|no> <name_of_company>");
						}else{
							if(pass.contains(args[2]))
							{
							configHandler.getConfig().set("companies." + args[2] + ".hiring", true);
							
							List<String> list = configHandler.getConfig().getStringList("hiring");
							
							list.add(args[2]);
							configHandler.getConfig().set("hiring", list);
							configHandler.saveConfig();
							}else{
								player.sendMessage(severeErrorColor() + "You do not own that company!");
							}
						}
						
					}
					
				}else if(args[1].equalsIgnoreCase("no"))
				{
					
					if(pass.size() < 1)
					{
						player.sendMessage(severeErrorColor() + noPerm());
						
					}else if(pass.size() < 2)
					{
						configHandler.getConfig().set("companies." + pass.get(0) + ".hiring", false);
						List<String> list = configHandler.getConfig().getStringList("companies.hiring");
						list.remove(pass.get(0));
						configHandler.getConfig().set("hiring", list);
						configHandler.saveConfig();
					}else{
						if(args.length < 3)
						{
							
						player.sendMessage(syntaxErrorColor() + "you have permission to do this in multiple companies. Please specify a company == /cc SetHiring <yes|no> <name_of_company>");
						}else{
							if(pass.contains(args[2]))
							{		
						
							configHandler.getConfig().set("companies." + args[2] + ".hiring", false);
							
							List<String> list = configHandler.getConfig().getStringList("hiring");
							list.remove(args[2]);
							configHandler.getConfig().set("hiring", list);
							configHandler.saveConfig();
						
						}else{
							player.sendMessage(severeErrorColor() + "You do not own that company!");
						}
					}
						}
						
					
				}
			}
		}else if(args[0].equalsIgnoreCase("Apply"))
		{
			if(perms.has(sender, "corporatecraft.apply"))
			{
			if(args.length < 3)
			{
				player.sendMessage(syntaxErrorColor() + "/cc Apply <companyName> <position>");
			}else{
				if(company.isHiring(args[1]))
				{
					company.apply(player.getName(), args[1], args[2]);
				}
			}
			}else{
				player.sendMessage(severeErrorColor() + noPerm());
			}
			
		}else if(args[0].equalsIgnoreCase("hiring"))
		{
			player.sendMessage(noErrorColor() + "Companies currently hiring:");
			List<String> hiring = configHandler.getConfig().getStringList("hiring");
			for (String company : hiring) {
			    player.sendMessage(company);
			}
			
		}else if(args[0].equalsIgnoreCase("info"))
		{
			if(args.length < 2)
			{
				player.sendMessage(syntaxErrorColor() + "/cc Info <companyName>");
			}else{
				player.sendMessage(configHandler.getConfig().getString("companies." + args[1] + ".description"));
			}
		}else if(args[0].equalsIgnoreCase("reviewApps"))
		{
			
			if(pass.size() < 1)
			{
				player.sendMessage(severeErrorColor() + noPerm());
			}else if(pass.size() < 2)
			{
				for(String applicant: company.reviewApplications(pass.get(0)))
				{
					player.sendMessage(applicant);
				}
			}else{
				if(args.length < 2)
				{
					player.sendMessage(syntaxErrorColor() + "You have permission to do this in multiple companies please specify a name /cc ReviewApps <companyName>");
					
				}else{
					for(String applicant: company.reviewApplications(args[1]))
					{
						player.sendMessage(applicant);
					}
				}
			}
		}else if(args[0].equalsIgnoreCase("hire"))
		{
			
			if(pass.size() < 1)
			{
				player.sendMessage(severeErrorColor() + noPerm() );
			}else if(pass.size() < 2)
			{
				if(!(args.length < 3 && args.length > 3))
				{
					if(!company.hire(args[1], args[2], args[3], player))
					{
						player.sendMessage(severeErrorColor() + "invalid position");
					}
				}else{
					if(args.length == 4)
					{
						if(!company.hire(args[1], args[2], args[3], player))
						{
							player.sendMessage(severeErrorColor() + "invalid position");
						}
					}else{
					player.sendMessage(syntaxErrorColor() + "not enough args must be: /cc Hire <applicant> <position> or /cc Hire <company> <appicant> <position>");
					}
					
				}
				
			}else{
				if(args.length < 4)
				{
					player.sendMessage(syntaxErrorColor() + "You have permission to do this in multiple companies please specify a company /cc Hire <company> <applicant> <position>" );
					
				}else{
					company.hire(args[1], args[2], args[3], player);
				}
			}
		}else if(args[0].equalsIgnoreCase("setDescription"))
		{
			if(pass.size() < 1)
			{
				player.sendMessage(severeErrorColor() + noPerm());
			}else if(pass.size() < 2)
			{
				if(args.length > 1)
				{
				company.setDescription(pass.get(0), args[1]);
				player.sendMessage(noErrorColor() + "You successfully changed the description of " + args[1]);
				}else{
					player.sendMessage(syntaxErrorColor() + "not enough arguements /cc setDescription <description> or /cc setDescription <company> <description");
					
				}
			}else{
				if(args.length < 2)
				{
					player.sendMessage(syntaxErrorColor() + "You have permission to do this in multiple companies please specify a company /cc setDescription <company> <description>");
				}else{
					company.setDescription(args[1], args[2]);
					player.sendMessage(noErrorColor() + "You successfully changed the description of " + args[1]);
				}
			}
		}else if(args[0].equalsIgnoreCase("addRegion"))
		{
			if(perms.has(sender, "corporatecraft.region.add"))
			{
				if(pass.size() < 1)
				{
					player.sendMessage(severeErrorColor() + noPerm());
				}else if(pass.size() < 2)
				{
					if(args.length < 4)
				if(!company.addRegion(pass.get(0), args[1], player, args[2]))
				{
					player.sendMessage(severeErrorColor() + "you cannot add this region as this type either the type is bad or you do not own the region");
				}else{
					player.sendMessage(noErrorColor() + "you have successfully added the region to your companies list");
				}
				}else{
					if(args.length < 4)
					{
						player.sendMessage(syntaxErrorColor() + "You have permission to do this in multiple companies please speicify a company /cc add Region <company> <regionID> <regionType>");
						
					}else{
						if(!company.addRegion(args[1], args[2], player, args[3]))
						{
							player.sendMessage(severeErrorColor() + "you cannot add this region as this type either the type is bad or you do not own the region");
						}else{
							player.sendMessage(noErrorColor() + "you have successfully added the region to your companies list");
						}
				
					}
				}
			}else{
				player.sendMessage(severeErrorColor() + noPerm());
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
