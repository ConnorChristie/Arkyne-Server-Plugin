package us.arkyne.server.inventory.item;

import org.bukkit.inventory.ItemStack;

import us.arkyne.server.inventory.InventoryClick;

public interface InventoryItem
{
	public ItemStack getItem();

	public InventoryClick getInventoryClick();
	
	public double getAttack();
	public double getDefense();
	
	public String name();
}