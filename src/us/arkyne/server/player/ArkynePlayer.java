package us.arkyne.server.player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import us.arkyne.server.ArkyneMain;
import us.arkyne.server.inventory.Inventory;
import us.arkyne.server.minigame.Minigame;
import us.arkyne.server.util.Util;

public class ArkynePlayer implements ConfigurationSerializable
{
	private UUID uuid;
	private OfflinePlayer player;
	
	private Minigame minigame;
	private Inventory inventory;
	
	public ArkynePlayer(UUID uuid)
	{
		this.uuid = uuid;
		this.player = Bukkit.getOfflinePlayer(uuid);
	}
	
	//TODO: On player join and server reload, update inventory and gamemode
	
	public boolean isOnline()
	{
		return player.isOnline();
	}
	
	public Player getOnlinePlayer()
	{
		return Bukkit.getPlayer(uuid);
	}
	
	public boolean isMinigame()
	{
		return minigame != null;
	}
	
	public void setUUID(UUID uuid)
	{
		this.uuid = uuid;
		this.player = Bukkit.getOfflinePlayer(uuid);
	}
	
	public UUID getUUID()
	{
		return uuid;
	}
	
	public void setMinigame(Minigame minigame)
	{
		this.minigame = minigame;
	}
	
	public Minigame getMinigame()
	{
		return minigame;
	}
	
	public void setInventory(Inventory inventory)
	{
		this.inventory = inventory;
		
		inventory.updateInventory(this);
	}
	
	public Inventory getInventory()
	{
		return inventory;
	}
	
	/*
	public void join(Joinable joinable)
	{
		if (isOnline())
		{
			//TODO: Leave the previous area
			
			joinable.join(this);
		}
	}
	*/
	
	/*
	public void changeLobby(Lobby newLobby)
	{
		if (isOnline())
		{
			if (lobby != null)
				lobby.leaveLobby(this);
			
			if (newLobby != null)
				newLobby.joinLobby(this);
			
			PlayerChangeLobbyEvent event = new PlayerChangeLobbyEvent(this, lobby, newLobby);
			Bukkit.getServer().getPluginManager().callEvent(event);
			
			this.lobby = newLobby;
			
			save();
		}
	}
	*/
	
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
					
					new BukkitRunnable()
					{
						public void run()
						{
							teleportRaw(loc);
						}
					}.runTask(ArkyneMain.getInstance());
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
	
	public void sendMessage(String message)
	{
		sendMessage(message, ChatColor.AQUA);
	}
	
	public void sendMessage(String message, ChatColor msgColor)
	{
		sendMessageRaw(Util.PREFIX + msgColor + message);
	}
	
	public void sendMessageRaw(String message)
	{
		if (isOnline())
		{
			getOnlinePlayer().sendMessage(message);
		}
	}
	
	public void save()
	{
		ArkyneMain.getInstance().getArkynePlayerHandler().save(this);
	}
	
	public ArkynePlayer(Map<String, Object> map)
	{
		if (map.containsKey("lobby"))
		{
			minigame = ArkyneMain.getInstance().getMinigameHandler().getMinigame(map.get("minigame").toString());
			
			minigame.addPlayer(this);
		}
	}

	@Override
	public Map<String, Object> serialize()
	{
		Map<String, Object> map = new HashMap<String, Object>();
		
		if (minigame != null)
		{
			map.put("minigame", minigame.getId());
		}
		
		return map;
	}
}