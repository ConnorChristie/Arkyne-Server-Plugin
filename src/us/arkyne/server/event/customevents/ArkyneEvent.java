package us.arkyne.server.event.customevents;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public abstract class ArkyneEvent extends Event
{
	private static final HandlerList handlers = new HandlerList();
	
	protected boolean isCancelled;
	
	@Override
	public HandlerList getHandlers()
	{
		return handlers;
	}
	
	public static HandlerList getHandlerList()
	{
		return handlers;
	}
}