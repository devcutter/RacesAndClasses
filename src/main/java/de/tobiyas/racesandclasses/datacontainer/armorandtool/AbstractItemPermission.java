package de.tobiyas.racesandclasses.datacontainer.armorandtool;

import org.bukkit.inventory.ItemStack;

import de.tobiyas.racesandclasses.util.items.ItemUtils.ItemQuality;

public interface AbstractItemPermission {

	public boolean hasPermission(ItemStack item);
	
	public boolean isAlreadyRegistered(ItemQuality quality);
}
