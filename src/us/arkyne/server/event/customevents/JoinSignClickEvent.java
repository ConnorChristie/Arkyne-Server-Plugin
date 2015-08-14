package us.arkyne.server.event.customevents;

import org.bukkit.event.Cancellable;

import us.arkyne.server.minigame.Joinable;
import us.arkyne.server.player.ArkynePlayer;

public class JoinSignClickEvent extends ArkyneEvent implements Cancellable
{
	private ArkynePlayer player;
	private Joinable joinable;
	
	public JoinSignClickEvent(ArkynePlayer player, Joinable joinable)
	{
		this.player = player;
		this.joinable = joinable;
	}

	public ArkynePlayer getPlayer()
	{
		return player;
	}

	public void setJoinable(Joinable joinable)
	{
		this.joinable = joinable;
	}
	
	public Joinable getJoinable()
	{
		return joinable;
	}

	@Override
	public boolean isCancelled()
	{
		return isCancelled;
	}

	@Override
	public void setCancelled(boolean isCancelled)
	{
		this.isCancelled = isCancelled;
	}
}