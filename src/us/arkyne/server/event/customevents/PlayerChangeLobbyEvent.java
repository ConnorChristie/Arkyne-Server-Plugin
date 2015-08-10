package us.arkyne.server.event.customevents;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import us.arkyne.server.lobby.Lobby;

public final class PlayerChangeLobbyEvent extends Event
{
	private static final HandlerList handlers = new HandlerList();
	
	private Player player;
	
	private Lobby fromLobby;
	private Lobby toLobby;
	
	public PlayerChangeLobbyEvent(Player player, Lobby from, Lobby to)
	{
		this.player = player;
		
		this.fromLobby = from;
		this.toLobby = to;
	}
	
	public Player getPlayer()
	{
		return player;
	}

	public Lobby getFromLobby()
	{
		return fromLobby;
	}

	public Lobby getToLobby()
	{
		return toLobby;
	}

	@Override
	public HandlerList getHandlers()
	{
		return handlers;
	}
}