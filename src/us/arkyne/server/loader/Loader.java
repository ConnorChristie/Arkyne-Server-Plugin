package us.arkyne.server.loader;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;

import us.arkyne.server.MinigameMain;

public abstract class Loader<T extends Loadable> implements Loadable
{
	private MinigameMain main;
	
	private List<T> loadables = new ArrayList<T>();
	
	public Loader(MinigameMain main)
	{
		this.main = main;
	}
	
	public void addLoadable(T loadable)
	{
		loadables.add(loadable);
		
		if (loadable instanceof ConfigurationSerializable)
		{
			ConfigurationSerialization.registerClass(((ConfigurationSerializable) loadable).getClass());
		}
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