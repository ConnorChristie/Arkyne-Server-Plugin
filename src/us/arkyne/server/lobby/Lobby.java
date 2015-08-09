package us.arkyne.server.lobby;

import java.util.HashMap;
import java.util.Map;

import us.arkyne.server.loader.Loadable;

public abstract class Lobby implements Loadable
{
	public void deserialize(Map<String, Object> map)
	{
		
	}
	
	public Map<String, Object> serialize()
	{
		Map<String, Object> map = new HashMap<String, Object>();
		
		return map;
	}
}