package us.arkyne.server.inventory.handler;

import org.bukkit.inventory.ItemStack;

import us.arkyne.server.inventory.InventoryClick;
import us.arkyne.server.inventory.item.InventoryItem;
import us.arkyne.server.inventory.item.InventoryItemPreset;
import us.arkyne.server.util.Util;

public class InventoryItemHandler
{
	public static void registerInventoryItemPresets(InventoryItem[] inventoryItems)
	{
		for (InventoryItem preset : inventoryItems)
		{
			Util.addEnum(InventoryItemPreset.class, preset.name(), new Class<?>[] { ItemStack.class, InventoryClick.class }, new Object[] { preset.getItem(), preset.getInventoryClick() });
		}
	}
}