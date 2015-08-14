package us.arkyne.server.inventory;

import org.bukkit.inventory.ItemStack;

import us.arkyne.server.inventory.item.InventoryItem;
import us.arkyne.server.inventory.item.InventoryItemPreset;
import us.arkyne.server.player.ArkynePlayer;

public enum InventoryPreset implements Inventory
{
	//Arrays have to be exactly 36 deep
	
	MAIN_LOBBY(new InventoryItem[] {
			/* Hotbar */ InventoryItemPreset.DUMMY_ITEM, null, null, null, null, null, null, null, null,
			/* Row 1 */ null, null, null, null, null, null, null, null, null,
			/* Row 2 */ null, null, null, null, null, null, null, null, null,
			/* Row 3 */ null, null, null, null, null, null, null, null, null
	});
	
	private InventoryItem[] items;
	
	private InventoryPreset(InventoryItem[] items)
	{
		this.items = items;
	}
	
	public void updateInventory(ArkynePlayer player)
	{
		if (player.isOnline())
		{
			ItemStack[] itemStacks = new ItemStack[items.length];
			
			for (int i = 0; i < items.length; i++)
			{
				itemStacks[i] = items[i] != null ? items[i].getItem() : null;
			}
			
			player.getOnlinePlayer().getInventory().setContents(itemStacks);
		}
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
}