package me.backspace119;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
/**
 * 
 * @author garbagemule
 *
 *CREDITS AND SHOUTOUTS TO GARBAGEMULE HE HELPED TREMENDOUSLY WITH THIS CODE (HE WROTE IT ALL XD) ALL OF EVERYTHING GOES TO HIM
 *DIAMONDS TO YOU MY GOOD SIR! DIAMONDS!!!
 *
 */


public class PlayerInventoryManagement {

	private static final String FILE_EXT = ".yml";
	private static final String ITEMS_KEY = "items";
	private static final String ARMOR_KEY = "armor";
	 
	/**
	 * Store the player's inventory in a .yml file with the player's name
	 */
	public void storeInv(Player p) throws IOException {
	    // Grab the player's inventory
	    ItemStack[] items = p.getInventory().getContents();
	    ItemStack[] armor = p.getInventory().getArmorContents();
	   
	    // Create the file and the config
	    File file = new File(p.getName() + FILE_EXT);
	    YamlConfiguration config = new YamlConfiguration();
	 
	    // Populate and save the config
	    config.set(ITEMS_KEY, items);
	    config.set(ARMOR_KEY, armor);
	    config.save(file);
	}
	 
	/**
	 * Restore the inventory from a .yml file with the player's name
	 */
	public void restoreInv(Player p) throws FileNotFoundException, IOException, InvalidConfigurationException {
	    // Grab the file and load the config
	    File file = new File(p.getName() + FILE_EXT);
	    YamlConfiguration config = new YamlConfiguration();
	    config.load(file);
	   
	    // Get the items and armor lists
	    List<?> itemsList = config.getList(ITEMS_KEY);
	    List<?> armorList = config.getList(ARMOR_KEY);
	   
	    // Turn the lists into arrays
	    ItemStack[] items = itemsList.toArray(new ItemStack[itemsList.size()]);
	    ItemStack[] armor = armorList.toArray(new ItemStack[armorList.size()]);
	   
	    // Set the player inventory contents
	    p.getInventory().setContents(items);
	    p.getInventory().setArmorContents(armor);
	   
	    // Delete files
	    file.delete();
	}
}
