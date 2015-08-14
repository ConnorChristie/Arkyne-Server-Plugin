package us.arkyne.server.inventory.item;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import us.arkyne.server.inventory.InventoryClick;

public enum InventoryItemPreset implements InventoryItem
{
	DUMMY_ITEM(ChatColor.AQUA + "Click me to do Something!", Material.NETHER_STAR, null);
	
	private ItemStack item;
	private InventoryClick inventoryClick;
	
	private InventoryItemPreset(String displayName, Material material, InventoryClick inventoryClick)
	{
		this.item = new ItemStack(material, 1);
		this.inventoryClick = inventoryClick;
		
		ItemMeta meta = item.getItemMeta();
		
		meta.setDisplayName(displayName);
		item.setItemMeta(meta);
	}
	
	private InventoryItemPreset(ItemStack item, InventoryClick inventoryClick)
	{
		this.item = item;
		this.inventoryClick = inventoryClick;
	}

	public ItemStack getItem()
	{
		return item;
	}

	public InventoryClick getInventoryClick()
	{
		return inventoryClick;
	}
}