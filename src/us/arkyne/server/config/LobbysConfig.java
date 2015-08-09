package us.arkyne.server.config;

import java.util.HashMap;
import java.util.Map;

import us.arkyne.server.lobby.Lobby;

public class LobbysConfig extends Config
{
	public LobbysConfig()
	{
		super("lobbys");
	}
	
	//Gets all the saved lobbys in the config, and casts them to a Lobby
	public Map<String, Lobby> getLobbys()
	{
		return getInstanceMap("lobbys", new HashMap<String, Lobby>());
	}
}