package us.arkyne.server.game;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import us.arkyne.server.inventory.Inventory;
import us.arkyne.server.loader.Loadable;
import us.arkyne.server.util.Cuboid;

public abstract class Arena implements Loadable, ConfigurationSerializable
{
	protected Game game;
	
	private Cuboid cuboid;
	private Inventory inventory;
	
	private Map<String, Location> spawns = new HashMap<String, Location>();
	
	public Arena(Game game, Cuboid cuboid, Inventory inventory)
	{
		this.game = game;
		
		this.inventory = inventory;
		this.cuboid = cuboid;
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
	
	public void setGame(Game game)
	{
		this.game = game;
	}
	
	public Game getGame()
	{
		return game;
	}
	
	public void addSpawn(String team, Location spawn)
	{
		spawns.put(team, spawn);
	}
	
	public Location getSpawn(String team)
	{
		return spawns.get(team);
	}

	public Cuboid getBounds()
	{
		return cuboid;
	}
	
	public Inventory getInventory()
	{
		return inventory;
	}
	
	@SuppressWarnings("unchecked")
	public Arena(Map<String, Object> map)
	{
		spawns = (Map<String, Location>) map.get("spawns");
	}

	@Override
	public Map<String, Object> serialize()
	{
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("spawns", spawns);
		
		return map;
	}
}