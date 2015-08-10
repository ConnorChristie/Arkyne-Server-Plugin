package us.arkyne.server.lobby;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;

import us.arkyne.server.loader.Loadable;
import us.arkyne.server.player.ArkynePlayer;

public class Lobby implements Loadable, ConfigurationSerializable
{
	private String name;
	private String id;
	
	private Location spawn;
	private Location min, max;
	
	private Location sign;
	
	private List<ArkynePlayer> currentPlayers = new ArrayList<ArkynePlayer>();
	
	public Lobby(String name, String id, Location spawn, Location min, Location max)
	{
		this.name = name;
		this.id = id;
		
		this.spawn = spawn;
		
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
	
	public void joinLobby(ArkynePlayer player)
	{
		currentPlayers.add(player);
		
		player.teleport(spawn);
	}
	
	public void leaveLobby(ArkynePlayer player)
	{
		currentPlayers.remove(player);
	}
	
	public boolean isInLobby(ArkynePlayer player)
	{
		return currentPlayers.contains(player);
	}
	
	public String getName()
	{
		return name;
	}

	public String getId()
	{
		return id;
	}
	
	public Location getSpawn()
	{
		return spawn;
	}

	public Location getMin()
	{
		return min;
	}

	public Location getMax()
	{
		return max;
	}
	
	public void setSign(Location sign)
	{
		this.sign = sign;
	}
	
	public Location getSign()
	{
		return sign;
	}
	
	public int getPlayerCount()
	{
		return currentPlayers.size();
	}
	
	private List<String> playersToUUID()
	{
		List<String> uuids = new ArrayList<String>();
		
		for (ArkynePlayer player : currentPlayers)
		{
			uuids.add(player.getBukkitPlayer().getUniqueId().toString());
		}
		
		return uuids;
	}
	
	@SuppressWarnings("unchecked")
	public Lobby(Map<String, Object> map)
	{
		name = map.get("name").toString();
		id = map.get("id").toString();
		
		spawn = (Location) map.get("spawn");
		sign = (Location) map.get("sign");
		
		min = (Location) map.get("boundry_min");
		max = (Location) map.get("boundry_max");
		
		//Maybe it was a reload? Have to persist the players in the lobby then
		
		List<String> uuids = (List<String>) map.get("players");
		
		if (uuids != null)
		{
			for (String uuid : uuids)
			{
				//Check if player is online, if online, most likely a reload, so persist the data
				
				Player player = Bukkit.getPlayer(UUID.fromString(uuid));
				
				if (player != null && player.isOnline())
				{
					currentPlayers.add(new ArkynePlayer(player));
				}
			}
		}
	}
	
	public Map<String, Object> serialize()
	{
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("name", name);
		map.put("id", id);
		
		map.put("spawn", spawn);
		map.put("sign", sign);
		
		map.put("boundry_min", min);
		map.put("boundry_max", max);
		
		map.put("players", playersToUUID());
		
		return map;
	}
}