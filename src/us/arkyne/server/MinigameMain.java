package us.arkyne.server;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;

import us.arkyne.server.command.CommandHandler;
import us.arkyne.server.loader.Loader;
import us.arkyne.server.lobby.Lobbys;
import us.arkyne.server.minigame.MiniGames;

public class MinigameMain extends JavaPlugin
{
	private static MinigameMain instance;
	
	private List<Loader<?>> loaders = new ArrayList<Loader<?>>();
	
	private Lobbys lobbys;
	private MiniGames miniGames;
	
	private CommandHandler commandHandler;
	
	public void onEnable()
	{
		instance = this;
		
		//Sets up the lobbys and minigames loaders
		setupLobbys();
		setupMiniGames();
		
		loadAll();
		
		commandHandler = new CommandHandler(this);
		
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
		miniGames = new MiniGames(this);
		loaders.add(miniGames);
	}
	
	public Lobbys getLobbys()
	{
		return lobbys;
	}
	
	public MiniGames getMiniGames()
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
	
	public static MinigameMain getInstance()
	{
		return instance;
	}
}