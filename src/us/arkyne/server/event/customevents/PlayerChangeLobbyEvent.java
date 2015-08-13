package us.arkyne.server.event.customevents;

import us.arkyne.server.lobby.Lobby;
import us.arkyne.server.player.ArkynePlayer;

public final class PlayerChangeLobbyEvent extends MinigameEvent
{
	private ArkynePlayer player;
	
	private Lobby fromLobby;
	private Lobby toLobby;
	
	public PlayerChangeLobbyEvent(ArkynePlayer player, Lobby from, Lobby to)
	{
		this.player = player;
		
		this.fromLobby = from;
		this.toLobby = to;
	}
	
	public ArkynePlayer getPlayer()
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
}