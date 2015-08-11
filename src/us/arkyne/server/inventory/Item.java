package us.arkyne.server.inventory;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import us.arkyne.server.player.ArkynePlayer;

public class Item extends ItemStack
{
	private InventoryClick inventoryClick;
	
	public Item(Material material, String displayName, InventoryClick inventoryClick)
	{
		super(material, 1);
		
		this.inventoryClick = inventoryClick;
		
		ItemMeta meta = getItemMeta();
		
		meta.setDisplayName(displayName);
		
		setItemMeta(meta);
	}
	
	public static Item i(Material material, String displayName)
	{
		return new Item(material, displayName, null);
	}
	
	public static Item i(Material material, String displayName, InventoryClick inventoryClick)
	{
		return new Item(material, displayName, inventoryClick);
	}
	
	public void clickItem(ArkynePlayer player)
	{
		if (inventoryClick != null)
		{
			inventoryClick.onClick(player);
		}
	}
	
	//InventoryClickEvent 
}