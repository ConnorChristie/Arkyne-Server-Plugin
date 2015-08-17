package us.arkyne.server.game.arena;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.util.Vector;

import com.sk89q.worldedit.LocalWorld;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.bukkit.BukkitWorld;

import us.arkyne.server.game.Game;
import us.arkyne.server.inventory.Inventory;
import us.arkyne.server.inventory.InventoryPreset;
import us.arkyne.server.loader.Loadable;
import us.arkyne.server.player.ArkynePlayer;
import us.arkyne.server.util.Cuboid;

@SuppressWarnings("deprecation")
public abstract class Arena implements Loadable, ConfigurationSerializable
{
	protected Game game;
	
	protected Cuboid cuboid;
	protected Inventory inventory;
	
	protected ArenaReset arenaReset;
	
	protected Map<String, Location> spawns = new HashMap<String, Location>();
	
	public Arena(Game game, Cuboid cuboid, Inventory inventory)
	{
		this.game = game;
		
		this.inventory = inventory;
		this.cuboid = cuboid;
	}
	
	@Override
	public void onLoad()
	{
		arenaReset = new ArenaReset(game);
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
	
	public abstract Location getSpawn(ArkynePlayer player);
	
	public List<String> getTeams()
	{
		return new ArrayList<String>(spawns.keySet());
	}

	public Cuboid getBounds()
	{
		return cuboid;
	}
	
	public ArenaReset getArenaReset()
	{
		return arenaReset;
	}
	
	@SuppressWarnings("unchecked")
	public Arena(Map<String, Object> map)
	{
		UUID world = UUID.fromString(map.get("world").toString());
		
		Location min = ((Vector) map.get("boundry_min")).toLocation(Bukkit.getWorld(world));
		Location max = ((Vector) map.get("boundry_max")).toLocation(Bukkit.getWorld(world));
		
		cuboid = new Cuboid(min.getWorld(), BukkitUtil.toVector(min), BukkitUtil.toVector(max));
		inventory = InventoryPreset.valueOf(map.get("inventory").toString());
		
		spawns = (Map<String, Location>) map.get("spawns");
	}
	
	@Override
	public Map<String, Object> serialize()
	{
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("world", BukkitUtil.toWorld((LocalWorld) cuboid.getWorld()).getUID().toString());
		
		map.put("boundry_min", BukkitUtil.toLocation(((BukkitWorld) cuboid.getWorld()).getWorld(), cuboid.getMinimumPoint()).toVector());
		map.put("boundry_max", BukkitUtil.toLocation(((BukkitWorld) cuboid.getWorld()).getWorld(), cuboid.getMaximumPoint()).toVector());
		
		map.put("inventory", inventory.name());
		map.put("spawns", spawns);
		
		return map;
	}
}