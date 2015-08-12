package us.arkyne.server.config;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import us.arkyne.server.game.Game;

public class GamesConfig extends Config
{
	public GamesConfig(File dataFolder)
	{
		super(dataFolder, "games");
		
		loadConfig();
	}
	
	public Map<Integer, Game> getGames()
	{
		return getInstanceMapInt("games", new HashMap<Integer, Game>());
	}
}