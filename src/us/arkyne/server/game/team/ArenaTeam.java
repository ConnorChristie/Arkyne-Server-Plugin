package us.arkyne.server.game.team;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import us.arkyne.server.game.Game;
import us.arkyne.server.game.arena.Arena;

public class ArenaTeam implements ConfigurationSerializable
{
	private Arena arena;
	
	private String teamName;
	private Location spawn;
	
	public ArenaTeam(Arena arena, String teamName, Location spawn)
	{
		this.arena = arena;
		
		this.teamName = teamName;
		this.spawn = spawn;
	}
	
	public String getTeamName()
	{
		return teamName;
	}
	
	public String getColor()
	{
		return ChatColor.valueOf(teamName.toUpperCase()) + "";
	}

	public Location getSpawn(Game game)
	{
		Location gameSpawn = spawn.clone();
		gameSpawn.setWorld(arena.getWorld(game));
		
		return gameSpawn;
	}
	
	public void setArena(Arena arena)
	{
		this.arena = arena;
	}

	public ArenaTeam(Map<String, Object> map)
	{
		teamName = map.get("name").toString();
		spawn = (Location) map.get("spawn");
	}

	@Override
	public Map<String, Object> serialize()
	{
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("name", teamName);
		map.put("spawn", spawn);
		
		return map;
	}
}