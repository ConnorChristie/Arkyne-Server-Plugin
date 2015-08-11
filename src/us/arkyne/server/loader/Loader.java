package us.arkyne.server.loader;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;

import us.arkyne.server.ArkyneMain;

public abstract class Loader<T extends Loadable> implements Loadable
{
	private ArkyneMain main;
	
	private List<T> loadables = new ArrayList<T>();
	
	public Loader()
	{
		this.main = ArkyneMain.getInstance();
	}
	
	public void addLoadable(T loadable)
	{
		if (loadable != null)
		{
			loadables.add(loadable);
			
			if (loadable instanceof ConfigurationSerializable)
			{
				ConfigurationSerialization.registerClass(((ConfigurationSerializable) loadable).getClass());
			}
		}
	}
	
	public void removeLoadable(T loadable)
	{
		if (loadable != null)
		{
			loadables.remove(loadable);
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
	
	protected ArkyneMain getMain()
	{
		return main;
	}
}