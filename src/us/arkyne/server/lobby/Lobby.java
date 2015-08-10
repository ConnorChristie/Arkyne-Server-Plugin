package us.arkyne.server.lobby;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.util.Vector;

import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.bukkit.BukkitWorld;

import us.arkyne.server.loader.Loadable;
import us.arkyne.server.message.SignMessage;
import us.arkyne.server.player.ArkynePlayer;
import us.arkyne.server.util.Cuboid;

public class Lobby implements Loadable, ConfigurationSerializable
{
	private String name;
	private String id;
	
	private Location spawn;
	private Location sign;
	
	private Cuboid cuboid;
	
	private List<ArkynePlayer> currentPlayers = new ArrayList<ArkynePlayer>();
	
	public Lobby(String name, String id, Location spawn, Cuboid cuboid)
	{
		this.name = name;
		this.id = id;
		
		this.spawn = spawn;
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
	
	public void joinLobby(ArkynePlayer player)
	{
		addPlayer(player);
		
		player.teleport(spawn);
		
		updateSign();
	}
	
	public void leaveLobby(ArkynePlayer player)
	{
		currentPlayers.remove(player);
		
		updateSign();
	}
	
	public boolean isInLobby(ArkynePlayer player)
	{
		return currentPlayers.contains(player);
	}
	
	public void addPlayer(ArkynePlayer player)
	{
		currentPlayers.add(player);
	}
	
	public void updateSign()
	{
		if (this.sign != null && this.sign.getBlock().getState() instanceof Sign)
		{
			Sign sign = (Sign) this.sign.getBlock().getState();
			
			for (int i = 0; i < 4; i++)
			{
				sign.setLine(i, SignMessage.LOBBY
						.replace(i, "{lobby}", getName())
						.replace("{lobby-id}", getId())
						.replace("{count}", getPlayerCount() + ""));
			}
			
			sign.update(true);
		}
	}
	
	public void updateSign(SignChangeEvent event)
	{
		for (int i = 0; i < 4; i++)
		{
			event.setLine(i, SignMessage.LOBBY
					.replace(i, "{lobby}", getName())
					.replace("{lobby-id}", getId())
					.replace("{count}", getPlayerCount() + ""));
		}
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

	public Cuboid getCuboid()
	{
		return cuboid;
	}
	
	public void setSign(Location sign)
	{
		this.sign = sign;
	}
	
	public Location getSign()
	{
		return sign;
	}
	
	//Count only the players online
	public int getPlayerCount()
	{
		int count = 0;
		
		for (ArkynePlayer player : currentPlayers)
		{
			count += player.isOnline() ? 1 : 0;
		}
		
		return count;
	}
	
	public Lobby(Map<String, Object> map)
	{
		name = map.get("name").toString();
		id = map.get("id").toString();
		
		UUID world = UUID.fromString(map.get("world").toString());
		
		spawn = ((Vector) map.get("spawn")).toLocation(Bukkit.getWorld(world));
		sign = map.get("sign") != null ? ((Vector) map.get("sign")).toLocation(Bukkit.getWorld(world)) : null;
		
		Location min = ((Vector) map.get("boundry_min")).toLocation(Bukkit.getWorld(world));
		Location max = ((Vector) map.get("boundry_max")).toLocation(Bukkit.getWorld(world));
		
		cuboid = new Cuboid(min.getWorld(), BukkitUtil.toVector(min), BukkitUtil.toVector(max));
	}
	
	public Map<String, Object> serialize()
	{
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("name", name);
		map.put("id", id);
		
		map.put("world", spawn.getWorld().getUID().toString());
		
		map.put("spawn", spawn.toVector());
		map.put("sign", sign != null ? sign.toVector() : null);
		
		map.put("boundry_min", BukkitUtil.toLocation(((BukkitWorld) cuboid.getWorld()).getWorld(), cuboid.getMinimumPoint()).toVector());
		map.put("boundry_max", BukkitUtil.toLocation(((BukkitWorld) cuboid.getWorld()).getWorld(), cuboid.getMaximumPoint()).toVector());
		
		return map;
	}
}