package us.arkyne.server.lobby;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;

import us.arkyne.server.MinigameMain;
import us.arkyne.server.config.LobbysConfig;
import us.arkyne.server.loader.Loader;

public class Lobbys extends Loader<Lobby>
{
	private LobbysConfig lobbysConfig;
	
	private Map<String, Lobby> lobbys = new HashMap<String, Lobby>();
	
	public Lobbys(MinigameMain main)
	{
		super(main);
		
		lobbysConfig = new LobbysConfig();
		
		//Load all lobby's from the config file
		lobbys = lobbysConfig.getLobbys();
		
		for (Lobby lobby : lobbys.values())
		{
			addLoadable(lobby);
		}
	}
	
	@Override
	public void onLoad()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUnload()
	{
		// TODO Auto-generated method stub
		
	}
	
	public boolean containsLobby(String id)
	{
		return lobbys.containsKey(id);
	}
	
	public Lobby getLobby(String check)
	{
		for (Lobby lobby : lobbys.values())
		{
			if (lobby.getName().equalsIgnoreCase(check) || lobby.getId().equalsIgnoreCase(check))
			{
				return lobby;
			}
		}
		
		return null;
	}
	
	public boolean createLobby(String name, String id, Location spawn, Location min, Location max)
	{
		if (!containsLobby(id))
		{
			Lobby lobby = new Lobby(name, id, spawn, min, max);
			
			//Other lobby creation stuff
			
			lobbys.put(id, lobby);
			
			saveLobbys();
			
			return true;
		}
		
		return false;
	}
	
	public void saveLobbys()
	{
		for (String id : lobbys.keySet())
		{
			lobbysConfig.set("lobbys." + id, lobbys.get(id));
		}
		
		lobbysConfig.saveConfig();
	}
}