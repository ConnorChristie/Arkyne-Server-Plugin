package us.arkyne.server.loader;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.serialization.ConfigurationSerialization;

public abstract class Loader
{
	private List<Loadable> loadables = new ArrayList<Loadable>();
	
	public void addLoadable(Loadable loadable)
	{
		loadables.add(loadable);
		
		ConfigurationSerialization.registerClass(loadable.getClass());
	}
	
	public void loadAll()
	{
		for (Loadable loadable : loadables)
		{
			loadable.load();
		}
	}
	
	public void unloadAll()
	{
		for (Loadable loadable : loadables)
		{
			loadable.unload();
		}
	}
}