package us.arkyne.server.lobby;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;

import us.arkyne.server.ArkyneMain;
import us.arkyne.server.config.LobbysConfig;
import us.arkyne.server.inventory.Inventory;
import us.arkyne.server.loader.Loader;
import us.arkyne.server.util.Cuboid;

public class LobbyHandler extends Loader<Lobby>
{
	private Lobby mainLobby;
	private LobbysConfig lobbysConfig;
	
	private Map<String, Lobby> lobbys = new HashMap<String, Lobby>();
	
	public LobbyHandler(ArkyneMain main)
	{
		super(main);
	}
	
	@Override
	public void onLoad()
	{
		lobbysConfig = new LobbysConfig();
		
		// Load all lobby's from the config file
		lobbys = lobbysConfig.getLobbys();
		mainLobby = lobbysConfig.getMainLobby();
		
		if (mainLobby != null)
		{
			lobbys.put(mainLobby.getId(), mainLobby);
		}
		
		addLoadable(mainLobby);
		
		for (Lobby lobby : lobbys.values())
		{
			addLoadable(lobby);
		}
	}
	
	@Override
	public void onUnload()
	{
		saveAll();
	}
	
	public Lobby getMainLobby()
	{
		return mainLobby;
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
	
	public Lobby getLobby(Location signLocation)
	{
		for (Lobby lobby : lobbys.values())
		{
			if (lobby.getSign() != null && lobby.getSign().equals(signLocation))
			{
				return lobby;
			}
		}
		
		return null;
	}
	
	public boolean createMainLobby(Location spawn, Cuboid cuboid)
	{
		if (mainLobby == null)
		{
			mainLobby = new Lobby("MainLobby", "ML-1", spawn, cuboid, Inventory.MAIN_LOBBY);
			
			save(mainLobby);
			
			return true;
		}
		
		return false;
	}
	
	public boolean createLobby(String name, String id, Location spawn, Cuboid cuboid)
	{
		if (!containsLobby(id))
		{
			Lobby lobby = new Lobby(name, id, spawn, cuboid, Inventory.LOBBY);
			
			lobbys.put(id, lobby);
			
			save(lobby);
			
			return true;
		}
		
		return false;
	}
	
	public void save(Lobby lobby)
	{
		if (lobby == mainLobby)
		{
			lobbysConfig.set("mainlobby", lobby);
		} else
		{
			lobbysConfig.set("lobbys." + lobby.getId(), lobby);
		}
		
		lobbysConfig.saveConfig();
	}
	
	public void saveAll()
	{
		lobbysConfig.set("mainlobby", mainLobby);
		
		for (Map.Entry<String, Lobby> lobby : lobbys.entrySet())
		{
			lobbysConfig.set("lobbys." + lobby.getKey(), lobby.getValue());
		}
		
		lobbysConfig.saveConfig();
	}
}