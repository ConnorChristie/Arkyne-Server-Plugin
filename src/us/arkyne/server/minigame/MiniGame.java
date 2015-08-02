package us.arkyne.server.minigame;

import java.util.HashMap;
import java.util.Map;

import us.arkyne.server.loader.Loadable;
import us.arkyne.server.lobby.Lobbys;

public abstract class MiniGame implements Loadable
{
	//Minigame variables like a timer, field location
	
	private Lobbys lobbys;
	
	public MiniGame()
	{
		lobbys = new Lobbys();
	}
	
	public void load()
	{
		//Load lobbys for each location
		
		lobbys.addLoadable(new MiniGameLobby());
		
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