package us.arkyne.server.loader;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;

import us.arkyne.server.ArkyneMain;

public abstract class Loader implements Loadable
{
	private ArkyneMain main;
	
	private List<Loadable> loadables = new ArrayList<Loadable>();
	
	public Loader()
	{
		this.main = ArkyneMain.getInstance();
	}
	
	protected void addLoadable(Loadable loadable)
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
	
	protected void removeLoadable(Loadable loadable)
	{
		if (loadable != null)
		{
			loadables.remove(loadable);
		}
	}
	
	public void loadAll()
	{
		onLoad();
		
		loadLoadables();
	}
	
	public void unloadAll()
	{
		onUnload();
		
		for (Loadable loadable : loadables)
		{
			loadable.onUnload();
		}
	}
	
	protected void loadLoadables()
	{
		for (Loadable loadable : loadables)
		{
			loadable.onLoad();
		}
	}
	
	protected void unloadLoadables()
	{
		for (Loadable loadable : loadables)
		{
			loadable.onUnload();
		}
	}
	
	protected ArkyneMain getMain()
	{
		return main;
	}
}