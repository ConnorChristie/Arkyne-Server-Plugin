package us.arkyne.server.config;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerialization;

import us.arkyne.server.lobby.Lobby;
import us.arkyne.server.lobby.MainLobby;
import us.arkyne.server.lobby.MinigameLobby;
import us.arkyne.server.lobby.PregameLobby;

public class LobbysConfig extends Config
{
	public LobbysConfig(File dataFolder)
	{
		super(dataFolder, "lobbys");
		
		ConfigurationSerialization.registerClass(MainLobby.class);
		ConfigurationSerialization.registerClass(MinigameLobby.class);
		ConfigurationSerialization.registerClass(PregameLobby.class);
		
		loadConfig();
	}
	
	//Gets all the saved lobbys in the config, and casts them to a Lobby
	public Map<Integer, Lobby> getLobbys()
	{
		return getInstanceMap("lobbys", new HashMap<Integer, Lobby>());
	}
	
	public MainLobby getMainLobby()
	{
		return (MainLobby) get("mainlobby");
	}
	
	public Lobby getLobby()
	{
		return (Lobby) get("lobby");
	}
}