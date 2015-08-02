package us.arkyne.server.loader;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.serialization.ConfigurationSerialization;

import us.arkyne.server.Main;
import us.arkyne.server.command.CommandHandler;

public abstract class Loader
{
	private Main main;
	
	private List<Loadable> loadables = new ArrayList<Loadable>();
	
	public Loader(Main main)
	{
		this.main = main;
	}
	
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