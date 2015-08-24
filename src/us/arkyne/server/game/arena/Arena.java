package us.arkyne.server.game.arena;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.World.Environment;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.util.Vector;

import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.bukkit.BukkitWorld;

import us.arkyne.server.ArkyneMain;
import us.arkyne.server.game.team.ArenaTeam;
import us.arkyne.server.game.team.GameTeam;
import us.arkyne.server.loader.Loadable;
import us.arkyne.server.minigame.Minigame;
import us.arkyne.server.player.ArkynePlayer;
import us.arkyne.server.util.Cuboid;
import us.arkyne.server.util.Util;

public abstract class Arena implements Loadable, ConfigurationSerializable
{
	protected int id;
	protected Minigame minigame;
	
	protected String mapName;
	
	protected String worldName;
	protected Cuboid cuboid;
	
	protected List<ArenaTeam> teams = new ArrayList<ArenaTeam>();
	
	public Arena(Minigame minigame, int id, String mapName, String worldName)
	{
		this.id = id;
		this.minigame = minigame;
		
		this.mapName = mapName;
		this.worldName = worldName;
	}
	
	@Override
	public void onLoad()
	{
		//Make a subclass of ArenaTeam (BFArenaTeam) and move the core field to there...
	}

	@Override
	public void onUnload()
	{
		
	}
	
	public abstract void addTeam(String team, Location spawn);
	
	public Location getSpawn(ArkynePlayer player)
	{
		return ((GameTeam) player.getExtra("team")).getTeam().getSpawn();
	}
	
	public ArenaTeam getTeam(String team)
	{
		for (ArenaTeam t : teams)
		{
			if (t.getTeamName().equalsIgnoreCase(team))
			{
				return t;
			}
		}
		
		return null;
	}
	
	public List<ArenaTeam> getTeams()
	{
		return teams;
	}
	
	public void setBounds(Cuboid cuboid)
	{
		this.cuboid = cuboid;
	}

	public Cuboid getBounds()
	{
		return cuboid;
	}
	
	public World getWorld()
	{
		return Bukkit.getWorld(worldName);
	}
	
	public Minigame getMinigame()
	{
		return minigame;
	}
	
	public int getId()
	{
		return id;
	}
	
	public String getMapName()
	{
		return mapName;
	}
	
	public void updateWorld(World world)
	{
		for (ArenaTeam team : teams)
		{
			team.getSpawn().setWorld(world);
		}
		
		cuboid.setWorld(BukkitUtil.getLocalWorld(world));
	}
	
	public void regenArena(int gameId, Callable<Void> finished)
	{
		try
		{
			long start = System.currentTimeMillis();
			
			File arenaWorld = new File(worldName + "_" + gameId);
			boolean deleted = ArkyneMain.getInstance().getMultiverse().getMVWorldManager().deleteWorld(arenaWorld.getName(), true, true);
			
			FileUtils.copyDirectory(new File(worldName), arenaWorld);
			new File(arenaWorld, "uid.dat").delete();
			
			boolean success = ArkyneMain.getInstance().getMultiverse().getMVWorldManager().addWorld(arenaWorld.getName(), Environment.NORMAL, null, WorldType.NORMAL, null, null, false);
			
			System.out.println("Deleted: " + deleted + ", Loaded: " + success + ", " + (System.currentTimeMillis() - start));
			
			if (finished != null)
			{
				try
				{
					finished.call();
				} catch (Exception e) { }
			}
		} catch (IOException e)
		{
			e.printStackTrace();
			
			Util.noticeableConsoleMessage("Could not regenerate arena for game: " + gameId);
		}
	}
	
	public void save()
	{
		minigame.getArenaHandler().save(this);
	}
	
	@SuppressWarnings("unchecked")
	public Arena(Map<String, Object> map)
	{
		minigame = ArkyneMain.getInstance().getMinigameHandler().getMinigame(map.get("minigame").toString());
		id = (Integer) map.get("id");
		
		mapName = map.get("map").toString();
		worldName = map.get("world").toString();
		
		if (map.containsKey("boundry_min") && map.containsKey("boundry_max"))
		{
			World world = Bukkit.getWorld(worldName);
			
			Location min = ((Vector) map.get("boundry_min")).toLocation(world);
			Location max = ((Vector) map.get("boundry_max")).toLocation(world);
			
			cuboid = new Cuboid(getWorld(), BukkitUtil.toVector(min), BukkitUtil.toVector(max));
		}
		
		teams = (List<ArenaTeam>) map.get("teams");
		
		for (ArenaTeam team : teams)
		{
			team.setArena(this);
		}
	}
	
	@Override
	public Map<String, Object> serialize()
	{
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("minigame", minigame.getId());
		map.put("id", id);
		
		map.put("map", mapName);
		map.put("world", worldName);
		
		if (cuboid != null)
		{
			map.put("boundry_min", BukkitUtil.toLocation(((BukkitWorld) cuboid.getWorld()).getWorld(), cuboid.getMinimumPoint()).toVector());
			map.put("boundry_max", BukkitUtil.toLocation(((BukkitWorld) cuboid.getWorld()).getWorld(), cuboid.getMaximumPoint()).toVector());
		}
		
		map.put("teams", teams);
		
		return map;
	}
}