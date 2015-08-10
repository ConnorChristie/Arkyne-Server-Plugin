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
import us.arkyne.server.lobby.Lobbys;
import us.arkyne.server.minigame.Minigames;
import us.arkyne.server.player.ArkynePlayers;

public class ArkyneMain extends JavaPlugin
{
	private static ArkyneMain instance;
	
	private List<Loader<?>> loaders = new ArrayList<Loader<?>>();
	
	private Lobbys lobbys;
	private Minigames miniGames;
	
	private ArkynePlayers arkynePlayers;
	
	private CommandHandler commandHandler;
	private EventListener eventListener;
	
	public void onEnable()
	{
		instance = this;
		
		//Sets up the lobbys and minigames loaders
		setupLobbys();
		setupMiniGames();
		
		loadAll();
		
		arkynePlayers = new ArkynePlayers();
		commandHandler = new CommandHandler();
		eventListener = new EventListener();
		
		registerCommands();
		
		getLogger().info("Loaded all lobby's and minigames!");
	}
	
	public void onDisable()
	{
		unloadAll();
		
		getLogger().info("Unloaded all lobby's and minigames!");
	}
	
	private void loadAll()
	{
		for (Loader<?> loader : loaders)
		{
			loader.loadAll();
		}
	}
	
	private void unloadAll()
	{
		for (Loader<?> loader : loaders)
		{
			loader.unloadAll();
		}
	}
	
	private void setupLobbys()
	{
		lobbys = new Lobbys(this);
		loaders.add(lobbys);
	}
	
	private void setupMiniGames()
	{
		miniGames = new Minigames(this);
		loaders.add(miniGames);
	}
	
	private void registerCommands()
	{
		//When adding commands, make sure you register them in the plugin.yml!
		
		commandHandler.registerCommand(ArkyneCommand.class);
		commandHandler.registerCommand(LobbyCommand.class);
	}
	
	public ArkynePlayers getArkynePlayers()
	{
		return arkynePlayers;
	}
	
	public Lobbys getLobbys()
	{
		return lobbys;
	}
	
	public Minigames getMiniGames()
	{
		return miniGames;
	}
	
	public List<Loader<?>> getLoaders()
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