package us.arkyne.server.inventory;

import us.arkyne.server.util.Util;

public class InventoryHandler
{
	public static void registerInventoryPresets(Inventory[] inventorys)
	{
		for (Inventory preset : inventorys)
		{
			Util.addInventoryPreset(preset.name(), new Class<?>[] { Item[].class }, new Object[] { preset.getItems() });
		}
	}
}