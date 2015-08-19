package us.arkyne.server.game.team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import us.arkyne.server.player.ArkynePlayer;

public class ArkyneTeam implements ConfigurationSerializable
{
	private String teamName;
	private Location spawn;
	
	private int score = 0;
	private int spawnRadius = 2;
	
	private Random random = new Random();
	
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
	
	public void addScore(int points)
	{
		score += points;
	}
	
	public List<ArkynePlayer> getPlayers()
	{
		return players;
	}
	
	public void joinTeam(ArkynePlayer player)
	{
		players.add(player);
		
		Location loc = getSpawn().clone();
		loc.add(random.nextInt(spawnRadius * 2) - spawnRadius, 0, random.nextInt(spawnRadius * 2) - spawnRadius);
		
		player.teleport(loc);
		player.setExtra("team", this);
	}

	public ArkyneTeam(Map<String, Object> map)
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