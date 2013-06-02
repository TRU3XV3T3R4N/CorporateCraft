package me.backspace119;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigHandler {

	private final String fileName;
	private FileConfiguration customConfig = null;
	private File configFile = null;
	private final JavaPlugin plugin;
	
	 public ConfigHandler(JavaPlugin plugin, String fileName) {
	        if (plugin == null)
	            throw new IllegalArgumentException("plugin cannot be null");
	        if (!plugin.isInitialized())
	            throw new IllegalArgumentException("plugin must be initiaized");
	        this.plugin = plugin;
	        this.fileName = fileName;
	        File dataFolder = plugin.getDataFolder();
	        if (dataFolder == null)
	            throw new IllegalStateException();
	        this.configFile = new File(plugin.getDataFolder(), fileName);
	    }
	
	
	public void loadConfig() {
		if (configFile == null) {
			configFile = new File(CorporateCraft.plugin.getDataFolder(),fileName);
		}
		customConfig = YamlConfiguration.loadConfiguration(configFile);

		// Look for defaults in the jar
		InputStream defConfigStream = CorporateCraft.plugin
				.getResource(fileName);
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

	public boolean saveCustomConfig() {
		if (customConfig == null ||configFile == null) {
			return true;
		}
		try {
			getCustomConfig().save(configFile);
		} catch (IOException ex) {
			plugin.getLogger().log(Level.SEVERE,
					"Could not save config to " + configFile, ex);
			return true;
		}
		return false;

	}

}
