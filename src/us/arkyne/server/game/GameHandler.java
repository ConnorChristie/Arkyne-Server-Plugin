package us.arkyne.server.game;

import java.util.HashMap;
import java.util.Map;

import us.arkyne.server.config.GamesConfig;
import us.arkyne.server.loader.Loader;
import us.arkyne.server.minigame.Minigame;

public class GameHandler extends Loader
{
	private Minigame minigame;
	private GamesConfig gamesConfig;
	
	private Map<Integer, Game> games = new HashMap<Integer, Game>();
	
	public GameHandler(Minigame minigame)
	{
		this.minigame = minigame;
	}
	
	@Override
	public void onLoad()
	{
		gamesConfig = new GamesConfig(minigame.getPlugin().getDataFolder());
		games = gamesConfig.getGames();
		
		for (Game game : games.values())
		{
			addLoadable(game);
		}
		
		loadLoadables();
	}

	@Override
	public void onUnload()
	{
		unloadLoadables();
	}
	
	public Map<Integer, Game> getGames()
	{
		return games;
	}
	
	public int getGameCount()
	{
		return games.size();
	}
	
	public GamesConfig getGamesConfig()
	{
		return gamesConfig;
	}
	
	public void addGame(Game game)
	{
		games.put(game.getId(), game);
		addLoadable(game);
		
		game.onLoad();
	}
	
	public int getNextId()
	{
		//Just make sure we never get a duplicate key
		if (games.containsKey(games.size() + 1))
		{
			int highest = 0;
			
			for (Integer key : games.keySet())
			{
				highest = key > highest ? key : highest;
			}
			
			return highest + 1;
		}
		
		return games.size() + 1;
	}
	
	public Game getGame(int id)
	{
		return games.get(id);
	}
	
	public boolean containsGame(int id)
	{
		return games.containsKey(id);
	}
	
	public void save(Game game)
	{
		gamesConfig.set("games." + game.getId(), game);
		gamesConfig.saveConfig();
	}
	
	public void saveAll()
	{
		for (Map.Entry<Integer, Game> game : games.entrySet())
		{
			gamesConfig.set("games." + game.getKey(), game.getValue());
		}
		
		gamesConfig.saveConfig();
	}
}