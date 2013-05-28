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
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;


public class CorporateCraft extends JavaPlugin{
	
	public final Logger logger = Logger.getLogger("Minecraft");
	public static CorporateCraft plugin;
	public static Economy econ = null;
    public static Permission perms = null;
    public static Chat chat = null;
    public static Company company = new Company();
    
	@Override
	public void onEnable()
	{
		if (!setupEconomy() ) {
            logger.info(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        setupPermissions();
        setupChat();
        
        
        
		PluginDescriptionFile pdfFile = this.getDescription();
		
		this.logger.info(pdfFile.getName() + " v. " + pdfFile.getVersion() + " Enabled");
		
	}
	
	@Override
	public void onDisable()
	{
		
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
	        RegisteredServiceProvider<Chat> chatProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.chat.Chat.class);
	        if(chatProvider != null)
	        {
	        chat = chatProvider.getProvider();
	        }
	        return chat != null;
	    }
	 private boolean setupPermissions() {
	        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
	        perms = rsp.getProvider();
	        return perms != null;
	    }
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
	{
		
		
		
		
		if(!(sender instanceof Player)) {
            logger.info("only players may issue commands!!!");
            return false;
        }
		
		if(!perms.has(sender, "coporatecraft." + commandLabel))
		{
			sender.sendMessage("you do not have permission to run this command!");
			return false;
		}
		Player player = (Player) sender;
		
		if(commandLabel.equals("heal") || commandLabel.equals("h"))
		{
			if(args.length == 0)
			{
				player.setHealth(20);
				player.sendMessage(ChatColor.AQUA + "YOU HAVE BEEN HEALED");
				
			}else if(args.length == 1){
				
				Player targetPlayer = player.getServer().getPlayer(args[0]);
				targetPlayer.setHealth(20);
				targetPlayer.sendMessage(ChatColor.AQUA + "Player: " + player.getName() + " has healed you");
				
				
			}
			
			
		} else if(cmd.getLabel().equals("test-permission"))
		{
            // Lets test if user has the node "example.plugin.awesome" to determine if they are awesome or just suck
            if(perms.has(player, "example.plugin.awesome")) {
                sender.sendMessage("You are awesome!");
            } else {
                sender.sendMessage("You suck!");
            }
            
        }else if(cmd.getLabel().equalsIgnoreCase("ccStart"))
        {
        	if(args.length < 1)
        	{
        		sender.sendMessage(ChatColor.RED + "PLEASE INPUT NAME OF NEW COMPANY == /StartCompany <name_of_new_company");
        	}else{
        		startNew(args[0], player);
        	}
        }else if(cmd.getLabel().equals("ccDefaults"))
        {
        	saveDefaultConfig();
        	String pass = getConfig().getString("startcost");
        	logger.info(pass);
        }else if(cmd.getLabel().equals("ccReload"))
        {
        	reloadConfig();
        }
		return false;
       
	}
	
	public boolean startNew(String name, Player player)
	{
		int startUpCosts = this.getConfig().getInt("startcost");
		
		
			
		
		econ.createBank(name, player.getName());
		
		String pass = String.valueOf(startUpCosts);
		logger.info(pass);
		logger.info(player.getName());
		
		econ.withdrawPlayer(player.getName(), (double) startUpCosts);
		
		
		return false;
	}
	
	
}
