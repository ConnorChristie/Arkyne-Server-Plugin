package us.arkyne.server.loader;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.serialization.ConfigurationSerialization;

import us.arkyne.server.MinigameMain;
import us.arkyne.server.command.CommandExecutor;
import us.arkyne.server.command.CommandHandler;

public abstract class Loader implements CommandExecutor, Loadable
{
	private MinigameMain main;
	
	private List<Loadable> loadables = new ArrayList<Loadable>();
	
	public Loader(MinigameMain main)
	{
		this.main = main;
		
		CommandHandler.registerExecutor(this);
	}
	
	public MinigameMain getMain()
	{
		return main;
	}
	
	public void addLoadable(Loadable loadable)
	{
		loadables.add(loadable);
		
		CommandHandler.registerExecutor(loadable);
		ConfigurationSerialization.registerClass(loadable.getClass());
	}
	
	public void loadAll()
	{
		for (Loadable loadable : loadables)
		{
			loadable.onLoad();
		}
	}
	
	public void unloadAll()
	{
		for (Loadable loadable : loadables)
		{
			loadable.onUnload();
		}
	}
	
	
}