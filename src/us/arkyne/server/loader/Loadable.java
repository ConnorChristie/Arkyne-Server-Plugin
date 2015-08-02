package us.arkyne.server.loader;

import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

public interface Loadable extends ConfigurationSerializable
{
	public void load();
	public void onLoad();
	
	public void unload();
	public void onUnload();
	
	public void deserialize(Map<String, Object> map);
}