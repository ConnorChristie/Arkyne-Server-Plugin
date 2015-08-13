package us.arkyne.server.config;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerialization;

import us.arkyne.server.lobby.Lobby;

public class LobbysConfig extends Config
{
	public LobbysConfig(File dataFolder)
	{
		super(dataFolder, "lobbys");
		
		ConfigurationSerialization.registerClass(Lobby.class);
		
		loadConfig();
	}
	
	//Gets all the saved lobbys in the config, and casts them to a Lobby
	public Map<Integer, Lobby> getLobbys()
	{
		return getInstanceMap("lobbys", new HashMap<Integer, Lobby>());
	}
	
	public Lobby getMainLobby()
	{
		return (Lobby) get("mainlobby");
	}
	
	public Lobby getLobby()
	{
		return (Lobby) get("lobby");
	}
}