package us.arkyne.server.inventory;

import us.arkyne.server.inventory.item.InventoryItem;
import us.arkyne.server.inventory.item.InventoryItemPreset;

public enum InventoryPreset implements Inventory
{
	//Arrays have to be exactly 36 deep
	
	MAIN_LOBBY(new InventoryItem[] {
			/* Hotbar */ InventoryItemPreset.DUMMY_ITEM, null, null, null, null, null, null, null, null,
			/* Row 1 */  null, null, null, null, null, null, null, null, null,
			/* Row 2 */  null, null, null, null, null, null, null, null, null,
			/* Row 3 */  null, null, null, null, null, null, null, null, null
	}, new InventoryItem[] {
			/* Armor */  null, null, null, null
	});
	
	private InventoryItem[] items;
	private InventoryItem[] armor;
	
	private InventoryPreset(InventoryItem[] items, InventoryItem[] armor)
	{
		this.items = items;
		this.armor = armor;
	}
	
	public InventoryItem getItem(int index)
	{
		return items[index];
	}

	@Override
	public InventoryItem[] getItems()
	{
		return items;
	}

	@Override
	public InventoryItem getArmor(int index)
	{
		return armor[index];
	}

	@Override
	public InventoryItem[] getArmor()
	{
		return armor;
	}
}