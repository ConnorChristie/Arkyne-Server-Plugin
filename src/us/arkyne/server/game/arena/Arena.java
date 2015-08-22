package us.arkyne.server.game.arena;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.util.Vector;

import com.sk89q.worldedit.LocalWorld;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.bukkit.BukkitWorld;

import us.arkyne.server.game.Game;
import us.arkyne.server.game.team.ArkyneTeam;
import us.arkyne.server.loader.Loadable;
import us.arkyne.server.player.ArkynePlayer;
import us.arkyne.server.util.Cuboid;

@SuppressWarnings("deprecation")
public abstract class Arena implements Loadable, ConfigurationSerializable
{
	protected Game game;
	protected Cuboid cuboid;
	
	protected List<ArkyneTeam> teams = new ArrayList<ArkyneTeam>();
	
	public Arena(Game game, Cuboid cuboid)
	{
		this.game = game;
		this.cuboid = cuboid;
	}
	
	@Override
	public void onLoad()
	{
		
	}

	@Override
	public void onUnload()
	{
		
	}
	
	public void setGame(Game game)
	{
		this.game = game;
	}
	
	public Game getGame()
	{
		return game;
	}
	
	public void addTeam(String team, Location spawn)
	{
		teams.add(new ArkyneTeam(this, team, spawn));
	}
	
	public ArkyneTeam getTeam(String team)
	{
		for (ArkyneTeam t : teams)
		{
			if (t.getTeamName().equalsIgnoreCase(team))
			{
				return t;
			}
		}
		
		return null;
	}
	
	public abstract Location getSpawn(ArkynePlayer player);
	
	public List<ArkyneTeam> getTeams()
	{
		return teams;
	}

	public Cuboid getBounds()
	{
		return cuboid;
	}
	
	public World getWorld()
	{
		return BukkitUtil.toWorld((LocalWorld) cuboid.getWorld());
	}
	
	public void updateWorld(World world)
	{
		for (ArkyneTeam team : teams)
		{
			team.getSpawn().setWorld(world);
		}
		
		cuboid.setWorld(BukkitUtil.getLocalWorld(world));
	}
	
	@SuppressWarnings("unchecked")
	public Arena(Map<String, Object> map)
	{
		World world = Bukkit.getWorld(map.get("world").toString());
		
		Location min = ((Vector) map.get("boundry_min")).toLocation(world);
		Location max = ((Vector) map.get("boundry_max")).toLocation(world);
		
		cuboid = new Cuboid(min.getWorld(), BukkitUtil.toVector(min), BukkitUtil.toVector(max));
		teams = (List<ArkyneTeam>) map.get("teams");
		
		for (ArkyneTeam team : teams)
		{
			team.setArena(this);
		}
	}
	
	@Override
	public Map<String, Object> serialize()
	{
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("world", getWorld().getName());
		
		map.put("boundry_min", BukkitUtil.toLocation(((BukkitWorld) cuboid.getWorld()).getWorld(), cuboid.getMinimumPoint()).toVector());
		map.put("boundry_max", BukkitUtil.toLocation(((BukkitWorld) cuboid.getWorld()).getWorld(), cuboid.getMaximumPoint()).toVector());
		
		map.put("teams", teams);
		
		return map;
	}
}