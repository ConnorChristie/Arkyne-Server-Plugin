package us.arkyne.server.config;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerialization;

import us.arkyne.server.game.Game;
import us.arkyne.server.game.team.ArkyneTeam;

public class GamesConfig extends Config
{
	public GamesConfig(File dataFolder)
	{
		super(dataFolder, "games");
		
		ConfigurationSerialization.registerClass(ArkyneTeam.class);
		
		loadConfig();
	}
	
	public Map<Integer, Game> getGames()
	{
		return getInstanceMap("games", new HashMap<Integer, Game>());
	}
}