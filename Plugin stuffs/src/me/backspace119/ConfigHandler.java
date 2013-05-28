package me.backspace119;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;


public class ConfigHandler {
	
	private FileConfiguration customConfig = null;
	private File customConfigFile = null;
	public void loadConfig()
	{
		if (customConfigFile == null) {
		    customConfigFile = new File(CorporateCraft.plugin.getDataFolder(), "customConfig.yml");
		    }
		    customConfig = YamlConfiguration.loadConfiguration(customConfigFile);
		 
		    // Look for defaults in the jar
		    InputStream defConfigStream = CorporateCraft.plugin.getResource("customConfig.yml");
		    if (defConfigStream != null) {
		        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
		        customConfig.setDefaults(defConfig);
		    }
		
		
	}
	
	public FileConfiguration getCustomConfig() {
	    if (customConfig == null) {
	        loadConfig();
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
	        CorporateCraft.plugin.getLogger().log(Level.SEVERE, "Could not save config to " + customConfigFile, ex);
	    }
   
	}
	
	

}
