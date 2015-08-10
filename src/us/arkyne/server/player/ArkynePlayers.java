package us.arkyne.server.player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;

import us.arkyne.server.config.PlayersConfig;

public class ArkynePlayers
{
	private Map<UUID, ArkynePlayer> players = new HashMap<UUID, ArkynePlayer>();
	
	private PlayersConfig playersConfig;
	
	public ArkynePlayers()
	{
		playersConfig = new PlayersConfig();
		
		//Load players from config
		players = playersConfig.getPlayers();
	}
	
	public void addPlayer(Player player)
	{
		players.put(player.getUniqueId(), new ArkynePlayer(player.getUniqueId()));
	}
	
	public void removePlayer(Player player)
	{
		players.remove(player.getUniqueId());
	}
	
	public ArkynePlayer getPlayer(Player player)
	{
		return getPlayer(player.getUniqueId());
	}
	
	public ArkynePlayer getPlayer(UUID uuid)
	{
		//Never return a null arkyne player
		
		if (!players.containsKey(uuid))
			players.put(uuid, new ArkynePlayer(uuid));
		
		return players.get(uuid);
	}
	
	public void save()
	{
		for (Map.Entry<UUID, ArkynePlayer> player : players.entrySet())
		{
			playersConfig.set("players." + player.getKey().toString(), player.getValue());
		}
		
		playersConfig.saveConfig();
	}
}