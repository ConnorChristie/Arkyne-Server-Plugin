package us.arkyne.server.minigame;

import java.util.HashMap;
import java.util.Map;

import us.arkyne.server.ArkyneMain;
import us.arkyne.server.config.GamesConfig;
import us.arkyne.server.game.Game;
import us.arkyne.server.loader.Loadable;
import us.arkyne.server.loader.Loader;
import us.arkyne.server.plugin.MinigamePlugin;

//Add generics, just to make our life easier!
public abstract class Minigame<T extends Game> extends Loader implements Loadable
{
	// Minigame variables like a timer, field location
	
	private ArkyneMain main;
	private MinigamePlugin plugin;
	
	private String name;
	private String id;
	
	private GamesConfig<T> gamesConfig;
	
	private Map<Integer, T> games = new HashMap<Integer, T>();
	
	public Minigame(MinigamePlugin plugin, String name, String id)
	{
		main = ArkyneMain.getInstance();
		
		this.plugin = plugin;
		
		this.name = name;
		this.id = id;
	}
	
	public void onLoad()
	{
		gamesConfig = new GamesConfig<T>(plugin.getDataFolder());
		games = gamesConfig.getGames();
		
		for (T game : games.values())
		{
			addLoadable(game);
		}
		
		plugin.getLogger().info("Loaded " + name + " and " + games.size() + " games!");
	}
	
	public void onUnload()
	{
		saveAll();
		
		plugin.getLogger().info("Unloaded " + name + " and " + games.size() + " games!");
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getId()
	{
		return id;
	}
	
	public MinigamePlugin getPlugin()
	{
		return plugin;
	}
	
	protected ArkyneMain getMain()
	{
		return main;
	}
	
	protected GamesConfig<T> getGamesConfig()
	{
		return gamesConfig;
	}
	
	public abstract int createGame();
	
	protected void addGame(T game)
	{
		games.put(game.getId(), game);
		addLoadable(game);
		
		game.onLoad();
	}
	
	protected int getNextId()
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
	
	public T getGame(int id)
	{
		return games.get(id);
	}
	
	public boolean containsGame(int id)
	{
		return games.containsKey(id);
	}
	
	public void save(T game)
	{
		gamesConfig.set("games." + game.getId(), game);
		gamesConfig.saveConfig();
	}
	
	public void saveAll()
	{
		for (Map.Entry<Integer, T> game : games.entrySet())
		{
			gamesConfig.set("games." + game.getKey(), game.getValue());
		}
		
		gamesConfig.saveConfig();
	}
}