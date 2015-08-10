package us.arkyne.server.lobby;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;

import us.arkyne.server.ArkyneMain;
import us.arkyne.server.config.LobbysConfig;
import us.arkyne.server.loader.Loader;
import us.arkyne.server.util.Cuboid;

public class Lobbys extends Loader<Lobby>
{
	private LobbysConfig lobbysConfig;
	
	private Map<String, Lobby> lobbys = new HashMap<String, Lobby>();
	
	public Lobbys(ArkyneMain main)
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
	
	public boolean createLobby(String name, String id, Location spawn, Cuboid cuboid)
	{
		if (!containsLobby(id))
		{
			Lobby lobby = new Lobby(name, id, spawn, cuboid);
			
			//Other lobby creation stuff
			
			lobbys.put(id, lobby);
			
			save();
			
			return true;
		}
		
		return false;
	}
	
	public void save()
	{
		for (Map.Entry<String, Lobby> lobby : lobbys.entrySet())
		{
			lobbysConfig.set("lobbys." + lobby.getKey(), lobby.getValue());
		}
		
		lobbysConfig.saveConfig();
	}
}