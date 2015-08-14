package us.arkyne.server.event.customevents;

import org.bukkit.Location;

import us.arkyne.server.minigame.Joinable;

public class JoinSignCreateEvent extends ArkyneEvent
{
	private Joinable.Type signType;
	
	private String[] lines;
	private Location signLocation;
	
	public JoinSignCreateEvent(Joinable.Type signType, String[] lines, Location signLocation)
	{
		this.signType = signType;
		
		this.lines = lines;
		this.signLocation = signLocation;
	}

	public Joinable.Type getSignType()
	{
		return signType;
	}

	public String[] getLines()
	{
		return lines;
	}
	
	public String getLine(int index)
	{
		return lines[index];
	}

	public Location getSignLocation()
	{
		return signLocation;
	}
}