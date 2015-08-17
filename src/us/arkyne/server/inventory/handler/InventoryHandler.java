package us.arkyne.server.inventory.handler;

import us.arkyne.server.inventory.Inventory;
import us.arkyne.server.inventory.InventoryPreset;
import us.arkyne.server.inventory.item.InventoryItem;
import us.arkyne.server.util.Util;

public class InventoryHandler
{
	public static void registerInventoryPresets(Inventory[] inventorys)
	{
		for (Inventory preset : inventorys)
		{
			Util.addEnum(InventoryPreset.class, preset.name(), new Class<?>[] { InventoryItem[].class, InventoryItem[].class }, new Object[] { preset.getItems(), preset.getArmor() });
		}
	}
}