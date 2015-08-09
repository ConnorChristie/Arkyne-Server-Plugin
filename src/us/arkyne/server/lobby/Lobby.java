package us.arkyne.server.lobby;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import us.arkyne.server.command.Command;
import us.arkyne.server.command.cmds.LobbyCommand;
import us.arkyne.server.loader.Loadable;

public class Lobby implements Loadable, ConfigurationSerializable, LobbyCommand
{
	private String name;
	private String id;
	
	private Location min, max;
	
	public Lobby(String name, String id, Location min, Location max)
	{
		this.name = name;
		this.id = id;
		
		this.min = min;
		this.max = max;
	}
	
	@Override
	public void onLoad()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUnload()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean lobbyCommand(Command cmd)
	{
		
		
		return false;
	}
	
	public String getName()
	{
		return name;
	}

	public String getId()
	{
		return id;
	}

	public Location getMin()
	{
		return min;
	}

	public Location getMax()
	{
		return max;
	}
	
	public Lobby(Map<String, Object> map)
	{
		
	}
	
	public Map<String, Object> serialize()
	{
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("name", name);
		map.put("id", id);
		
		map.put("boundry_min", min);
		map.put("boundry_max", max);
		
		return map;
	}
}