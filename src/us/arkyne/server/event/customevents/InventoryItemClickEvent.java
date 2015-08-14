package us.arkyne.server.event.customevents;

import org.bukkit.event.Cancellable;

import us.arkyne.server.inventory.item.InventoryItem;
import us.arkyne.server.player.ArkynePlayer;

public class InventoryItemClickEvent extends ArkyneEvent implements Cancellable
{
	private ArkynePlayer player;
	private InventoryItem item;
	
	public InventoryItemClickEvent(ArkynePlayer player, InventoryItem item)
	{
		this.player = player;
		this.item = item;
	}
	
	public ArkynePlayer getPlayer()
	{
		return player;
	}

	public InventoryItem getItem()
	{
		return item;
	}

	@Override
	public boolean isCancelled()
	{
		return isCancelled;
	}

	@Override
	public void setCancelled(boolean isCancelled)
	{
		this.isCancelled = isCancelled;
	}
}