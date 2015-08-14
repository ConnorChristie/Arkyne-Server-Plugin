package us.arkyne.server.event.customevents;

import org.bukkit.Location;

import us.arkyne.server.player.ArkynePlayer;

public class PlayerFlyEvent extends ArkyneEvent
{
	private ArkynePlayer player;
	private Location from;
	
	public PlayerFlyEvent(ArkynePlayer player, Location from)
	{
		this.player = player;
		this.from = from;
	}
	
	public ArkynePlayer getPlayer()
	{
		return player;
	}

	public Location getLocation()
	{
		return from;
	}
}