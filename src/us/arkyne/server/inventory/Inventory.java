package us.arkyne.server.inventory;

import us.arkyne.server.player.ArkynePlayer;

public interface Inventory
{
	public void updateInventory(ArkynePlayer player);
	
	public Item getItem(int index);
	
	public String name();
}