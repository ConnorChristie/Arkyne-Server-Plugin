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
import org.bukkit.util.Vector;

import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.bukkit.BukkitWorld;

import us.arkyne.server.inventory.Inventory;
import us.arkyne.server.inventory.InventoryPreset;
import us.arkyne.server.loader.Loadable;
import us.arkyne.server.message.SignMessage;
import us.arkyne.server.message.SignMessagePreset;
import us.arkyne.server.minigame.Joinable;
import us.arkyne.server.player.ArkynePlayer;
import us.arkyne.server.util.Cuboid;

public abstract class Lobby implements Loadable, Joinable, ConfigurationSerializable
{
	private String name;
	private Integer id;
	
	private String idPrefix;
	
	private Location spawn;
	private Location sign;
	
	private Cuboid cuboid;
	
	private Inventory inventory;
	private SignMessage signMessage;
	
	private List<ArkynePlayer> currentPlayers = new ArrayList<ArkynePlayer>();
	
	public Lobby(String name, Integer id, Location spawn, Cuboid cuboid, Inventory inventory, SignMessage signMessage)
	{
		this.name = name;
		this.id = id;
		
		this.spawn = spawn;
		this.cuboid = cuboid;
		
		this.inventory = inventory;
		this.signMessage = signMessage;
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
	
	public void setIdPrefix(String idPrefix)
	{
		this.idPrefix = idPrefix;
	}
	
	public void join(ArkynePlayer player)
	{
		addPlayer(player);
		
		player.teleport(spawn);
		
		updateSign();
	}
	
	public void leave(ArkynePlayer player)
	{
		currentPlayers.remove(player);
		
		updateSign();
	}
	
	public boolean isJoinable(ArkynePlayer player)
	{
		return true;
	}
	
	public boolean isInLobby(ArkynePlayer player)
	{
		return currentPlayers.contains(player);
	}
	
	public void addPlayer(ArkynePlayer player)
	{
		currentPlayers.add(player);
	}
	
	public boolean isSign(Location signLocation)
	{
		return sign != null && sign.getWorld().equals(signLocation.getWorld()) && sign.distance(signLocation) < 1;
	}
	
	public void updateSign()
	{
		if (this.sign != null && this.sign.getBlock().getState() instanceof Sign)
		{
			Sign sign = (Sign) this.sign.getBlock().getState();
			
			for (int i = 0; i < 4; i++)
			{
				sign.setLine(i, signMessage
						.replace(i, "{lobby}", getName())
						.replace("{lobby-id}", idPrefix + "-" + getId())
						.replace("{count}", getPlayerCount() + ""));
			}
			
			sign.update(true);
		}
	}
	
	public String getName()
	{
		return name;
	}

	public Integer getId()
	{
		return id;
	}
	
	public String getIdString()
	{
		return id + "";
	}
	
	public Location getSpawn()
	{
		return spawn;
	}

	public Cuboid getBounds()
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
	
	public List<ArkynePlayer> getPlayers()
	{
		return currentPlayers;
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
	
	public Inventory getInventory()
	{
		return inventory;
	}
	
	public Lobby(Map<String, Object> map)
	{
		name = map.get("name").toString();
		id = Integer.parseInt(map.get("id").toString());
		
		UUID world = UUID.fromString(map.get("world").toString());
		UUID signWorld = map.get("sign_world") != null ? UUID.fromString(map.get("sign_world").toString()) : null;
		
		spawn = ((Vector) map.get("spawn")).toLocation(Bukkit.getWorld(world));
		sign = map.get("sign") != null ? ((Vector) map.get("sign")).toLocation(Bukkit.getWorld(signWorld)) : null;
		
		Location min = ((Vector) map.get("boundry_min")).toLocation(Bukkit.getWorld(world));
		Location max = ((Vector) map.get("boundry_max")).toLocation(Bukkit.getWorld(world));
		
		cuboid = new Cuboid(min.getWorld(), BukkitUtil.toVector(min), BukkitUtil.toVector(max));
		
		inventory = InventoryPreset.valueOf(map.get("inventory").toString());
		signMessage = SignMessagePreset.valueOf(map.get("sign_message").toString());
	}
	
	public Map<String, Object> serialize()
	{
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("name", name);
		map.put("id", id);
		
		map.put("world", spawn.getWorld().getUID().toString());
		map.put("sign_world", sign != null ? sign.getWorld().getUID().toString() : null);
		
		map.put("spawn", spawn.toVector());
		map.put("sign", sign != null ? sign.toVector() : null);
		
		map.put("boundry_min", BukkitUtil.toLocation(((BukkitWorld) cuboid.getWorld()).getWorld(), cuboid.getMinimumPoint()).toVector());
		map.put("boundry_max", BukkitUtil.toLocation(((BukkitWorld) cuboid.getWorld()).getWorld(), cuboid.getMaximumPoint()).toVector());
		
		map.put("inventory", inventory.name());
		map.put("sign_message", signMessage.name());
		
		return map;
	}
}