package me.backspace119;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BufferedInventoryHandler {

	private static final String FILE_EXT = ".yml";
	private static final String ITEMS_KEY = "items";
	private static final String ARMOR_KEY = "armor";
	 
	/**
	 * Store the player's inventory in a .yml file with the player's name
	 */
	public static void storeBufferedInv(String companyName, ItemStack[] items) throws IOException {
	    
	   
	    // Create the file and the config
	    File file = new File(companyName + FILE_EXT);
	    YamlConfiguration config = new YamlConfiguration();
	 
	    // Populate and save the config
	    config.set(ITEMS_KEY, items);
	    
	    config.save(file);
	}
	 
	/**
	 * Restore the inventory from a .yml file with the player's name
	 */
	public static void givePlayerBufferedInv(Player p,String companyName) throws FileNotFoundException, IOException, InvalidConfigurationException {
	    // Grab the file and load the config
	    File file = new File(companyName + FILE_EXT);
	    YamlConfiguration config = new YamlConfiguration();
	    config.load(file);
	   
	    // Get the items and armor lists
	    List<?> itemsList = config.getList(ITEMS_KEY);
	    
	   
	    // Turn the lists into arrays
	    ItemStack[] items = itemsList.toArray(new ItemStack[itemsList.size()]);
	    int i = 0;
	    boolean flag = false;
	   while(p.getInventory().firstEmpty() != -1 || items.length < i);
	   {
		   if(items.length == i - 1)
		   {
			   flag = true;
		   }
	    // Set the player inventory contents
	    p.getInventory().addItem(items[i]);
	    
	    items[i] = null;
	    
	    i++;
	   }
	  
	   storeBufferedInv(companyName, items);
	    // Delete files
	   if(flag)
	   {
	    file.delete();
	   }
	}
	public static ItemStack[] getBufferedInv(String companyName) throws FileNotFoundException, IOException, InvalidConfigurationException
	{
		// Grab the file and load the config
	    File file = new File(companyName + FILE_EXT);
	    YamlConfiguration config = new YamlConfiguration();
	    config.load(file);
	   
	    // Get the items and armor lists
	    List<?> itemsList = config.getList(ITEMS_KEY);
	    
	   
	    // Turn the lists into arrays
	    ItemStack[] items = itemsList.toArray(new ItemStack[itemsList.size()]);
	    return items;
	}
	public static YamlConfiguration getFile(String companyName) throws FileNotFoundException, IOException, InvalidConfigurationException
	{
		File file = new File(companyName + FILE_EXT);
	    YamlConfiguration config = new YamlConfiguration();
	    config.load(file);
	    return config;
	    
	}
	
}
