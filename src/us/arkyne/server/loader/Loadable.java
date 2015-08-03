package us.arkyne.server.loader;

import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

import us.arkyne.server.Main;
import us.arkyne.server.command.CommandExecutor;

public abstract class Loadable implements ConfigurationSerializable, CommandExecutor
{
	private Main main;
	
	public Loadable()
	{
		main = Main.getInstance();
	}
	
	public Main getMain()
	{
		return main;
	}
	
	public abstract void load();
	public abstract void onLoad();
	
	public abstract void unload();
	public abstract void onUnload();
	
	public abstract void deserialize(Map<String, Object> map);
}