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

public class Main extends JavaPlugin
{
	private List<Loader> loaders = new ArrayList<Loader>();
	
	private Lobbys lobbys;
	private MiniGames miniGames;
	
	private CommandHandler commandHandler;
	
	public void onEnable()
	{
		setupLobbys();
		setupMiniGames();
		
		commandHandler = new CommandHandler(this);
		
		getLogger().info("Loaded all lobby's and minigames!");
	}
	
	public void onDisable()
	{
		lobbys.unloadAll();
		miniGames.unloadAll();
		
		getLogger().info("Unloaded all lobby's and minigames!");
	}
	
	private void setupLobbys()
	{
		lobbys = new Lobbys(this);
		lobbys.loadAll();
		
		loaders.add(lobbys);
	}
	
	private void setupMiniGames()
	{
		miniGames = new MiniGames(this);
		miniGames.loadAll();
		
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
}