package us.arkyne.server.event.customevents;

import org.bukkit.Location;

import us.arkyne.server.minigame.Joinable;

public class JoinSignCreateEvent extends ArkyneEvent
{
	private Joinable.Type signType;
	
	private String idName;
	private Location signLocation;
	
	public JoinSignCreateEvent(Joinable.Type signType, String idName, Location signLocation)
	{
		this.signType = signType;
		
		this.idName = idName;
		this.signLocation = signLocation;
	}

	public Joinable.Type getSignType()
	{
		return signType;
	}

	public String getIdName()
	{
		return idName;
	}

	public Location getSignLocation()
	{
		return signLocation;
	}
}