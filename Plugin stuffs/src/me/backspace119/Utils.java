package me.backspace119;


import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
/**
 * 
 * 
 * @author Mtihc @ line 29-71 may be slightly edited for my needs 
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
	public static Company getCompany(String name)
	{
		return companyMap.get(name);
	}
	
	
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
}
