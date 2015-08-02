package us.arkyne.server.minigame;

import java.util.HashMap;
import java.util.Map;

import us.arkyne.server.Main;
import us.arkyne.server.command.cmds.ArkyneCommand;
import us.arkyne.server.loader.Loadable;
import us.arkyne.server.lobby.Lobbys;

public abstract class MiniGame extends Loadable implements ArkyneCommand
{
	//Minigame variables like a timer, field location
	
	private Lobbys lobbys;
	
	public MiniGame(Main main)
	{
		lobbys = new Lobbys(main);
	}
	
	public void load()
	{
		//Load lobbys for each location
		
		lobbys.addLoadable(new MiniGameLobby());
		lobbys.loadAll();
		
		onLoad();
	}
	
	public void unload()
	{
		lobbys.unloadAll();
		
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