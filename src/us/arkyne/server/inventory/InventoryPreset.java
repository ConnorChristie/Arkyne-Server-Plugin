package us.arkyne.server.inventory;

import static org.bukkit.Material.*;

import static org.bukkit.ChatColor.*;

import us.arkyne.server.player.ArkynePlayer;

public enum InventoryPreset implements Inventory
{
	// Arrays have to be exactly 36 deep
	
	MAIN_LOBBY(new Item[] {
			/* Hotbar */ Item.i(NETHER_STAR, AQUA + "Pick a Class"), null, null, null, null, null, null, null, null,
			/* Row 1 */ null, null, null, null, null, null, null, null, null,
			/* Row 2 */ null, null, null, null, null, null, null, null, null,
			/* Row 3 */ null, null, null, null, null, null, null, null, null
	});
							
	private Item[] items;
	
	private InventoryPreset(Item[] items)
	{
		this.items = items;
	}
	
	public void updateInventory(ArkynePlayer player)
	{
		player.getOnlinePlayer().getInventory().setContents(items);
	}
	
	public Item getItem(int index)
	{
		return items[index];
	}

	@Override
	public Item[] getItems()
	{
		return items;
	}
}