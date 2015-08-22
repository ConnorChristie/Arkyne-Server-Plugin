package us.arkyne.server.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import us.arkyne.server.ArkyneMain;
import us.arkyne.server.config.PlayersConfig;
import us.arkyne.server.loader.Loader;

public class ArkynePlayerHandler extends Loader
{
	private Map<UUID, ArkynePlayer> players = new HashMap<UUID, ArkynePlayer>();
	
	private PlayersConfig playersConfig;
	
	@Override
	public void onLoad()
	{
		playersConfig = new PlayersConfig();
		
		//Load players from config
		players = playersConfig.getPlayers();
	}

	@Override
	public void onUnload()
	{
		
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
	
	public List<ArkynePlayer> getOnlinePlayers()
	{
		List<ArkynePlayer> players = new ArrayList<ArkynePlayer>();
		
		for (ArkynePlayer p : this.players.values())
		{
			if (p.isOnline())
			{
				players.add(p);
			}
		}
		
		return players;
	}
	
	public List<ArkynePlayer> getAdminPlayers()
	{
		List<ArkynePlayer> players = new ArrayList<ArkynePlayer>();
		
		for (ArkynePlayer p : this.players.values())
		{
			if (p.isOnline() && p.getOnlinePlayer().hasPermission("arkyne.manage"))
			{
				players.add(p);
			}
		}
		
		return players;
	}
	
	public void hideShowPlayers(ArkynePlayer player, List<ArkynePlayer> canSee)
	{
		for (Player p : Bukkit.getOnlinePlayers())
		{
			ArkynePlayer other = ArkyneMain.getInstance().getArkynePlayerHandler().getPlayer(p);
			
			if (canSee.contains(other))
			{
				p.showPlayer(player.getOnlinePlayer());
				player.getOnlinePlayer().showPlayer(p);
			} else
			{
				p.hidePlayer(player.getOnlinePlayer());
				player.getOnlinePlayer().hidePlayer(p);
			}
		}
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