package us.arkyne.server.player;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import us.arkyne.server.MinigameMain;
import us.arkyne.server.lobby.Lobby;

public class ArkynePlayer
{
	private Player player;
	
	private Lobby lobby;
	
	public ArkynePlayer(Player bukkitPlayer)
	{
		this.player = bukkitPlayer;
	}
	
	public Player getBukkitPlayer()
	{
		return player;
	}
	
	public Lobby getLobby()
	{
		return lobby;
	}
	
	public void changeLobby(Lobby newLobby)
	{
		if (lobby != null)
			lobby.leaveLobby(this);
		
		newLobby.joinLobby(this);
		
		this.lobby = newLobby;
	}
	
	public Location getLocation()
	{
		return player.getLocation();
	}
	
	public void teleportRaw(Location loc)
	{
		player.teleport(loc, TeleportCause.PLUGIN);
		
		player.setFallDistance(-1F);
		player.setVelocity(new Vector(0, 0, 0));
		
		player.setFireTicks(0);
		player.setHealth(player.getMaxHealth());
	}
	
	public void teleport(final Location loc)
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
		}.runTaskAsynchronously(MinigameMain.getInstance());
	}
}