package us.arkyne.server.minigame;

import java.util.HashMap;
import java.util.Map;

import us.arkyne.server.ArkyneMain;
import us.arkyne.server.game.Game;
import us.arkyne.server.loader.Loadable;
import us.arkyne.server.loader.Loader;
import us.arkyne.server.plugin.MinigamePlugin;

public abstract class Minigame extends Loader<Game> implements Loadable
{
	//Minigame variables like a timer, field location
	
	private ArkyneMain main;
	private MinigamePlugin plugin;
	
	private String name;
	private String id;
	
	public Minigame(MinigamePlugin plugin, String name, String id)
	{
		main = ArkyneMain.getInstance();
		
		this.plugin = plugin;
		
		this.name = name;
		this.id = id;
	}
	
	public void onLoad()
	{
		plugin.getLogger().info("Loaded the " + name + " minigame!");
	}
	
	public void onUnload()
	{
		plugin.getLogger().info("Unloaded the " + name + " minigame!");
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
	
	public Minigame(Map<String, Object> map)
	{
		//Make a minigames config, load the lobby from it
	}
	
	public Map<String, Object> serialize()
	{
		Map<String, Object> map = new HashMap<String, Object>();
		
		return map;
	}
}