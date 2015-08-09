package us.arkyne.server.config;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerialization;

import us.arkyne.server.lobby.Lobby;

public class LobbysConfig extends Config
{
	public LobbysConfig()
	{
		super("lobbys");
		
		ConfigurationSerialization.registerClass(Lobby.class);
		
		loadConfig();
	}
	
	//Gets all the saved lobbys in the config, and casts them to a Lobby
	public Map<String, Lobby> getLobbys()
	{
		return getInstanceMap("lobbys", new HashMap<String, Lobby>());
	}
}