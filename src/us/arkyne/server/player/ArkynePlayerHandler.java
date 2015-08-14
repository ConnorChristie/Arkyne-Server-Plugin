package us.arkyne.server.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;

import us.arkyne.server.config.PlayersConfig;

public class ArkynePlayerHandler
{
	private Map<UUID, ArkynePlayer> players = new HashMap<UUID, ArkynePlayer>();
	
	private PlayersConfig playersConfig;
	
	public ArkynePlayerHandler()
	{
		playersConfig = new PlayersConfig();
		
		//Load players from config
		players = playersConfig.getPlayers();
	}
	
	public ArkynePlayer addPlayer(Player player)
	{
		if (!players.containsKey(player.getUniqueId()))
		{
			ArkynePlayer arkPlayer = new ArkynePlayer(player.getUniqueId());
			
			players.put(player.getUniqueId(), arkPlayer);
			
			save(player.getUniqueId());
			
			return arkPlayer;
		}
		
		return players.get(player.getUniqueId());
	}
	
	public void removePlayer(Player player)
	{
		players.remove(player.getUniqueId());
		
		save(player.getUniqueId());
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
	
	public List<ArkynePlayer> getPlayers()
	{
		return new ArrayList<ArkynePlayer>(players.values());
	}
	
	public void save(ArkynePlayer player)
	{
		playersConfig.set("players." + player.getUUID().toString(), player);
		playersConfig.saveConfig();
	}
	
	public void save(UUID uuid)
	{
		save(getPlayer(uuid));
	}
	
	public void saveAll()
	{
		for (Map.Entry<UUID, ArkynePlayer> player : players.entrySet())
		{
			playersConfig.set("players." + player.getKey().toString(), player.getValue());
		}
		
		playersConfig.saveConfig();
	}
}