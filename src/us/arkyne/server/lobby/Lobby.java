package us.arkyne.server.lobby;

import java.util.HashMap;
import java.util.Map;

import us.arkyne.server.loader.Loadable;

public abstract class Lobby extends Loadable
{
	public void load()
	{
		onLoad();
	}
	
	public void unload()
	{
		onUnload();
	}
	
	public void deserialize(Map<String, Object> map)
	{
		
	}
	
	public Map<String, Object> serialize()
	{
		Map<String, Object> map = new HashMap<String, Object>();
		
		return map;
	}
}