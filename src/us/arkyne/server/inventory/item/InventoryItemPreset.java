package us.arkyne.server.inventory.item;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import us.arkyne.server.inventory.InventoryClick;

public enum InventoryItemPreset implements InventoryItem
{
	DUMMY_ITEM(ChatColor.AQUA + "Click me to do Something!", Material.NETHER_STAR, 0, 0, null);
	
	private ItemStack item;
	private InventoryClick inventoryClick;
	
	private double attack;
	private double defense;
	
	private InventoryItemPreset(String displayName, Material material, double attack, double defense, InventoryClick inventoryClick)
	{
		this.item = new ItemStack(material, 1);
		this.inventoryClick = inventoryClick;
		
		ItemMeta meta = item.getItemMeta();
		
		meta.setDisplayName(displayName);
		item.setItemMeta(meta);
		
		this.attack = attack;
		this.defense = defense;
	}
	
	private InventoryItemPreset(ItemStack item, double attack, double defense, InventoryClick inventoryClick)
	{
		this.item = item;
		this.inventoryClick = inventoryClick;
		
		this.attack = attack;
		this.defense = defense;
	}

	public ItemStack getItem()
	{
		return item;
	}

	public InventoryClick getInventoryClick()
	{
		return inventoryClick;
	}

	@Override
	public double getAttack()
	{
		return attack;
	}

	@Override
	public double getDefense()
	{
		return defense;
	}
}