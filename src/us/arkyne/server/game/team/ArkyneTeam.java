package us.arkyne.server.game.team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import us.arkyne.server.player.ArkynePlayer;

public class ArkyneTeam implements ConfigurationSerializable
{
	private String teamName;
	private Location spawn;
	
	private int score = 0;
	
	private List<ArkynePlayer> players = new ArrayList<ArkynePlayer>();
	
	public ArkyneTeam(String teamName, Location spawn)
	{
		this.teamName = teamName;
		this.spawn = spawn;
	}
	
	public String getTeamName()
	{
		return teamName;
	}

	public Location getSpawn()
	{
		return spawn;
	}

	public int getScore()
	{
		return score;
	}

	public List<ArkynePlayer> getPlayers()
	{
		return players;
	}

	public ArkyneTeam(Map<String, Object> map)
	{
		
	}

	@Override
	public Map<String, Object> serialize()
	{
		Map<String, Object> map = new HashMap<String, Object>();
		
		
		
		return map;
	}
}