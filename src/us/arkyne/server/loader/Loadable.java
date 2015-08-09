package us.arkyne.server.loader;

import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

import us.arkyne.server.command.CommandExecutor;

public interface Loadable extends ConfigurationSerializable, CommandExecutor
{
	public void onLoad();
	public void onUnload();
	
	public void deserialize(Map<String, Object> map);
}