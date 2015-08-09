package us.arkyne.server.loader;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.serialization.ConfigurationSerialization;

import us.arkyne.server.MinigameMain;
import us.arkyne.server.command.CommandExecutor;
import us.arkyne.server.command.CommandHandler;

public abstract class Loader<T extends Loadable> implements CommandExecutor, Loadable
{
	private MinigameMain main;
	
	private List<T> loadables = new ArrayList<T>();
	
	public Loader(MinigameMain main)
	{
		this.main = main;
		
		CommandHandler.registerExecutor(this);
	}
	
	public void addLoadable(T loadable)
	{
		loadables.add(loadable);
		
		CommandHandler.registerExecutor(loadable);
		ConfigurationSerialization.registerClass(loadable.getClass());
	}
	
	public void loadAll()
	{
		onLoad();
		
		for (T loadable : loadables)
		{
			loadable.onLoad();
		}
	}
	
	public void unloadAll()
	{
		onUnload();
		
		for (T loadable : loadables)
		{
			loadable.onUnload();
		}
	}
	
	protected MinigameMain getMain()
	{
		return main;
	}
}