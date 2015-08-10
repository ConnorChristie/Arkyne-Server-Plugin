package us.arkyne.server.player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import us.arkyne.server.ArkyneMain;
import us.arkyne.server.lobby.Lobby;

public class ArkynePlayer implements ConfigurationSerializable
{
	private OfflinePlayer player;
	
	private Lobby lobby;
	
	public ArkynePlayer(UUID uuid)
	{
		this.player = Bukkit.getOfflinePlayer(uuid);
	}
	
	public boolean isOnline()
	{
		return player.isOnline();
	}
	
	public Player getOnlinePlayer()
	{
		return player.getPlayer();
	}
	
	public Lobby getLobby()
	{
		return lobby;
	}
	
	public void changeLobby(Lobby newLobby)
	{
		if (isOnline())
		{
			if (lobby != null)
				lobby.leaveLobby(this);
			
			newLobby.joinLobby(this);
			
			this.lobby = newLobby;
		}
	}
	
	public Location getLocation()
	{
		if (isOnline())
		{
			return getOnlinePlayer().getLocation();
		}
		
		return null;
	}
	
	public void teleportRaw(Location loc)
	{
		if (isOnline())
		{
			getOnlinePlayer().teleport(loc, TeleportCause.PLUGIN);
			
			getOnlinePlayer().setFallDistance(-1F);
			getOnlinePlayer().setVelocity(new Vector(0, 0, 0));
			
			getOnlinePlayer().setFireTicks(0);
			getOnlinePlayer().setHealth(getOnlinePlayer().getMaxHealth());
		}
	}
	
	public void teleport(final Location loc)
	{
		if (isOnline())
		{
			loc.getChunk().load(false);
			
			new BukkitRunnable()
			{
				public void run()
				{
					while (!loc.getChunk().isLoaded()) { }
					
					//Loaded chunk, most likely it is already loaded!
					
					teleportRaw(loc);
				}
			}.runTaskAsynchronously(ArkyneMain.getInstance());
		}
	}
	
	@SuppressWarnings("deprecation")
	public void pushTowards(Location loc)
	{
		if (isOnline())
		{
			Vector direction = loc.toVector().subtract(getLocation().toVector()).normalize();
			getOnlinePlayer().setVelocity(direction);
			
			if (getOnlinePlayer().isInsideVehicle())
			{
				getOnlinePlayer().getVehicle().setVelocity(direction.multiply(2D));
			}
			
			getOnlinePlayer().playEffect(getLocation().clone().add(0.5, 1, 0.5), Effect.POTION_BREAK, 5);
		}
	}
	
	public ArkynePlayer(Map<String, Object> map)
	{
		lobby = ArkyneMain.getInstance().getLobbys().getLobby(map.get("lobby").toString());
	}

	@Override
	public Map<String, Object> serialize()
	{
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("lobby", lobby.getId());
		
		return map;
	}
}