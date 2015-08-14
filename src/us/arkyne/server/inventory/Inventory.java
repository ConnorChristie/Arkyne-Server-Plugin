package us.arkyne.server.inventory;

import us.arkyne.server.inventory.item.InventoryItem;
import us.arkyne.server.player.ArkynePlayer;

public interface Inventory
{
	public void updateInventory(ArkynePlayer player);
	
	public InventoryItem getItem(int index);
	
	public InventoryItem[] getItems();
	
	public String name();
}