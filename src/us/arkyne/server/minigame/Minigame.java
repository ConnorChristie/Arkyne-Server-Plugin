package us.arkyne.server.minigame;

import java.util.HashMap;
import java.util.Map;

import us.arkyne.server.ArkyneMain;
import us.arkyne.server.loader.Loadable;

public abstract class Minigame implements Loadable
{
	//Minigame variables like a timer, field location
	
	public Minigame(ArkyneMain main)
	{
		
	}
	
	public Minigame(Map<String, Object> map)
	{
		
	}
	
	public Map<String, Object> serialize()
	{
		Map<String, Object> map = new HashMap<String, Object>();
		
		return map;
	}
}