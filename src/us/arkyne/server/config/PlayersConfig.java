package us.arkyne.server.config;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.configuration.serialization.ConfigurationSerialization;

import us.arkyne.server.player.ArkynePlayer;

public class PlayersConfig extends Config
{
	public PlayersConfig()
	{
		super("players");
		
		ConfigurationSerialization.registerClass(ArkynePlayer.class);
		
		loadConfig();
	}
	
	// Gets all the saved players in the config, and casts them to an ArkynePlayer
	public Map<UUID, ArkynePlayer> getPlayers()
	{
		Map<UUID, ArkynePlayer> players = new HashMap<UUID, ArkynePlayer>();
		
		for (Map.Entry<String, ArkynePlayer> player : getInstanceMap("players", new HashMap<String, ArkynePlayer>()).entrySet())
		{
			ArkynePlayer arkPlayer = player.getValue();
			arkPlayer.setUUID(UUID.fromString(player.getKey()));
			
			players.put(arkPlayer.getUUID(), arkPlayer);
		}
		
		return players;
	}
}