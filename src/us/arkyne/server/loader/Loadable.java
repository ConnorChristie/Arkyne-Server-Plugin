package us.arkyne.server.loader;

import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

import us.arkyne.server.command.CommandExecutor;
import us.arkyne.server.command.CommandHandler;

public abstract class Loadable implements ConfigurationSerializable, CommandExecutor
{
	public Loadable()
	{
		CommandHandler.registerExecutor(this);
	}
	
	public abstract void load();
	public abstract void onLoad();
	
	public abstract void unload();
	public abstract void onUnload();
	
	public abstract void deserialize(Map<String, Object> map);
}