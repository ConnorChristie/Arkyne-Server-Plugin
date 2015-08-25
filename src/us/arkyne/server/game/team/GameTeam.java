package us.arkyne.server.game.team;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;

import us.arkyne.server.game.Game;
import us.arkyne.server.player.ArkynePlayer;

public class GameTeam
{
	private Game game;
	private ArenaTeam team;
	
	private int score = 0;
	private int spawnRadius = 2;
	
	private Random random = new Random();
	
	private List<ArkynePlayer> players = new ArrayList<ArkynePlayer>();
	
	public GameTeam(Game game, ArenaTeam team)
	{
		this.game = game;
		this.team = team;
	}

	public ArenaTeam getTeam()
	{
		return team;
	}

	public int getScore()
	{
		return score;
	}
	
	public void addScore(int points)
	{
		score += points;
	}
	
	public void updateScore()
	{
		//TODO: Update the score for everyones scoreboard!
	}
	
	public List<ArkynePlayer> getPlayers()
	{
		return players;
	}
	
	public void joinTeam(ArkynePlayer player)
	{
		players.add(player);
		
		Location loc = team.getSpawn(game).clone();
		loc.add(random.nextInt(spawnRadius * 2) - spawnRadius, 0, random.nextInt(spawnRadius * 2) - spawnRadius);
		
		player.teleport(loc);
		player.setExtra("team", this);
	}
}