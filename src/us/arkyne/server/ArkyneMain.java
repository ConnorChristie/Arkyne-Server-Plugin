package us.arkyne.server;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;

import us.arkyne.server.command.CommandHandler;
import us.arkyne.server.command.cmds.ArkyneCommand;
import us.arkyne.server.command.cmds.LobbyCommand;
import us.arkyne.server.event.EventListener;
import us.arkyne.server.loader.Loader;
import us.arkyne.server.lobby.LobbyHandler;
import us.arkyne.server.minigame.MinigameHandler;
import us.arkyne.server.player.ArkynePlayerHandler;

public class ArkyneMain extends JavaPlugin
{
	private static ArkyneMain instance;
	
	private List<Loader> loaders = new ArrayList<Loader>();
	
	private LobbyHandler lobbyHandler;
	private MinigameHandler minigameHandler;
	
	private ArkynePlayerHandler arkynePlayerHandler;
	
	private CommandHandler commandHandler;
	private EventListener eventListener;
	
	public void onEnable()
	{
		instance = this;
		
		//Sets up the lobbys and minigames loaders
		setupLobbyHandler();
		setupMiniGameHandler();
		
		loadAll();
		
		arkynePlayerHandler = new ArkynePlayerHandler();
		commandHandler = new CommandHandler();
		eventListener = new EventListener();
		
		registerCommands();
		
		getLogger().info("Loaded all lobby's and minigames!");
	}
	
	public void onDisable()
	{
		unloadAll();
		
		arkynePlayerHandler.saveAll();
		
		getLogger().info("Unloaded all lobby's and minigames!");
	}
	
	private void loadAll()
	{
		for (Loader loader : loaders)
		{
			loader.loadAll();
		}
	}
	
	private void unloadAll()
	{
		for (Loader loader : loaders)
		{
			loader.unloadAll();
		}
	}
	
	private void setupLobbyHandler()
	{
		lobbyHandler = new LobbyHandler();
		loaders.add(lobbyHandler);
	}
	
	private void setupMiniGameHandler()
	{
		minigameHandler = new MinigameHandler();
		loaders.add(minigameHandler);
	}
	
	private void registerCommands()
	{
		//When adding commands, make sure you register them in the plugin.yml!
		
		commandHandler.registerCommand(ArkyneCommand.class);
		commandHandler.registerCommand(LobbyCommand.class);
	}
	
	public ArkynePlayerHandler getArkynePlayerHandler()
	{
		return arkynePlayerHandler;
	}
	
	public LobbyHandler getLobbyHandler()
	{
		return lobbyHandler;
	}
	
	public MinigameHandler getMinigameHandler()
	{
		return minigameHandler;
	}
	
	public CommandHandler getCommandHandler()
	{
		return commandHandler;
	}
	
	public List<Loader> getLoaders()
	{
		return loaders;
	}
	
	public WorldEditPlugin getWorldEdit()
	{
		Plugin plugin = getServer().getPluginManager().getPlugin("WorldEdit");
		
		if (plugin == null || !(plugin instanceof WorldEditPlugin)) return null;
		
		return (WorldEditPlugin) plugin;
	}
	
	public static ArkyneMain getInstance()
	{
		return instance;
	}
}