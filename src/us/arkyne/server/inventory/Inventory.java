package us.arkyne.server.inventory;

import us.arkyne.server.inventory.item.InventoryItem;

public interface Inventory
{
	public InventoryItem getItem(int index);
	public InventoryItem getArmor(int index);
	
	public InventoryItem[] getItems();
	public InventoryItem[] getArmor();
	
	public String name();
}