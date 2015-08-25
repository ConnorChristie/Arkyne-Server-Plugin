package us.arkyne.server.game;

import java.util.ArrayList;
import java.util.List;

import us.arkyne.server.game.arena.Arena;
import us.arkyne.server.game.team.ArenaTeam;
import us.arkyne.server.game.team.GameTeam;

public abstract class GameArena
{
	protected Game game;
	protected Arena arena;
	
	protected List<GameTeam> teams = new ArrayList<GameTeam>();
	
	public GameArena(Game game, Arena arena)
	{
		this.game = game;
		this.arena = arena;
		
		for (ArenaTeam team : arena.getTeams())
		{
			teams.add(getTeam(team));
		}
	}
	
	protected abstract GameTeam getTeam(ArenaTeam team);
	
	public Arena getArena()
	{
		return arena;
	}
	
	public List<GameTeam> getTeams()
	{
		return teams;
	}
}