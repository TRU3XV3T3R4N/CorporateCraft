package me.backspace119;


import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.java.JavaPlugin;
/**
 * 
 * 
 * @author Mtihc @ line 52-93 may be slightly edited for my needs 
 * or may be exactly same
 * only used because it is code that is of bukkit and not of Mtihc specifically 
 * although here are his credits for providing his code so i could 
 * have an example: https://github.com/Mtihc/TreasureChest  i do not claim
 * credit for the mentioned code all credit goes to him
 *
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
public class Utils {

	static Map<String, Company> companyMap = new HashMap<String, Company>();
	static List<String> playerInCompanyPlot;
	static Map<String, String> companyLandMap = new HashMap<String, String>();
	static Map<String, String> companyHqMap = new HashMap<String, String>();
	static Map<String, String> companyShopMap = new HashMap<String, String>();
	
	public static Company getCompany(String name)
	{
		return companyMap.get(name);
	}
	
//begin code adapted from MTIHC
	private static HashSet<Byte> invisibleBlocks;

	private static HashSet<Byte> getInvisibleBlocks() {
		if(invisibleBlocks == null) {
			invisibleBlocks  = new HashSet<Byte>();
			invisibleBlocks.add((byte) Material.AIR.getId());

			invisibleBlocks.add((byte) Material.LAVA.getId());
			invisibleBlocks.add((byte) Material.WATER.getId());
			invisibleBlocks.add((byte) Material.STATIONARY_LAVA.getId());
			invisibleBlocks.add((byte) Material.STATIONARY_WATER.getId());
			invisibleBlocks.add((byte) Material.BROWN_MUSHROOM.getId());
			invisibleBlocks.add((byte) Material.RED_MUSHROOM.getId());
			invisibleBlocks.add((byte) Material.RED_ROSE.getId());
			invisibleBlocks.add((byte) Material.YELLOW_FLOWER.getId());
			invisibleBlocks.add((byte) Material.CROPS.getId());
			invisibleBlocks.add((byte) Material.LADDER.getId());
			invisibleBlocks.add((byte) Material.LEVER.getId());
			invisibleBlocks.add((byte) Material.STONE_BUTTON.getId());
			invisibleBlocks.add((byte) Material.PAINTING.getId());
			invisibleBlocks.add((byte) Material.PORTAL.getId());
			invisibleBlocks.add((byte) Material.REDSTONE_TORCH_OFF.getId());
			invisibleBlocks.add((byte) Material.REDSTONE_TORCH_ON.getId());
			invisibleBlocks.add((byte) Material.REDSTONE_WIRE.getId());
			invisibleBlocks.add((byte) Material.SNOW.getId());
			invisibleBlocks.add((byte) Material.SIGN_POST.getId());
			invisibleBlocks.add((byte) Material.TORCH.getId());
			invisibleBlocks.add((byte) Material.VINE.getId());
			invisibleBlocks.add((byte) Material.WALL_SIGN.getId());
			invisibleBlocks.add((byte) Material.HOPPER.getId());
			invisibleBlocks.add((byte) Material.FENCE.getId());
			invisibleBlocks.add((byte) Material.FENCE_GATE.getId());
		}

		return invisibleBlocks;
	}

	public static Block getTargetedContainerBlock(Player player) {
		Block block = player.getTargetBlock(getInvisibleBlocks(), 8);
		if(block == null || !(block.getState() instanceof InventoryHolder)) {
			return null;
		}
		else {
			return block;
		}
	}
	private static String locationToString(Location loc) {
		return loc.getBlockX() + "_" + loc.getBlockY() + "_" + loc.getBlockZ();
	}

	private static Location stringToLocation(String worldName, String key) {
		World world = Bukkit.getWorld(worldName);
		String[] split = key.split("_");
		int x = Integer.parseInt(split[0]);
		int y = Integer.parseInt(split[1]);
		int z = Integer.parseInt(split[2]);
		return new Location(world, x, y, z);
	}
	public static Block getChest(String name, String type, World world)
	{
		
		Block chest = world.getBlockAt(stringToLocation(world.getName(), "companies." + name + "." + type));
		if(!(chest instanceof InventoryHolder))
		{
			return null;
		}else{
		return chest;
		}
	}
//end of code adapted from MTIHC
	/*
	 * error on true -- store location of chest to companies config
	 */
	public static boolean saveTakeChestToCompany(String name, Block chest, ConfigHandler companyConfig)
	{
	
		if(chest == null || !(chest instanceof InventoryHolder))
		{
			return true;
		}else{
			List<String> chestList = companyConfig.getConfig().getStringList("companies." + name + ".takeChests");
			chestList.add(locationToString(chest.getLocation()));
			companyConfig.getConfig().set("companies." + name + ".takeChests", chestList);	
		
		return false;
		}
	}
	
	public static Map<String, String> getCompanyLandMap()
	{
		return companyLandMap;
	}
	
	public static void restoreCompanies(ConfigHandler configHandler, JavaPlugin plugin, FileConfiguration config, Server server)
	{
		List<String> companyNames =
		  configHandler.getConfig().getStringList("companyNames");
		
		for(String companyName: companyNames)
		{
			new Company(configHandler, plugin, companyName);
			plugin.getLogger().info("company " + companyName + " reinitialized");
			for(String regionId: configHandler.getConfig().getStringList("companies." + companyName + "regions.work"))
			{
				companyLandMap.put(companyName, regionId);
			}
			for(String regionId: configHandler.getConfig().getStringList("companies." + companyName + "regions.hq"))
			{
				companyHqMap.put(companyName, regionId);
			}
			for(String regionId: configHandler.getConfig().getStringList("companies." + companyName + "regions.store"))
			{
				companyShopMap.put(companyName, regionId);
			}
		}
		
	}
}
