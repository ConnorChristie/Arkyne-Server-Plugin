package us.arkyne.server.config;

import java.util.ArrayList;
import java.util.List;

import us.arkyne.server.lobby.Lobby;

public class LobbysConfig extends Config
{
	public LobbysConfig()
	{
		super("lobbys");
	}
	
	//Gets all the saved lobbys in the config, and casts them to a Lobby
	public List<Lobby> getLobbys()
	{
		return getInstanceList("lobbys", new ArrayList<Lobby>());
	}
}