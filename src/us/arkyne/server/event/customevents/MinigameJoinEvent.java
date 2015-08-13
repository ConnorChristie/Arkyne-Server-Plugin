package us.arkyne.server.event.customevents;

import org.bukkit.event.Cancellable;

import us.arkyne.server.minigame.Minigame;
import us.arkyne.server.player.ArkynePlayer;

public class MinigameJoinEvent extends MinigameEvent implements Cancellable
{
	private Minigame minigame;
	private ArkynePlayer player;
	
	public MinigameJoinEvent(Minigame minigame, ArkynePlayer player)
	{
		this.minigame = minigame;
		this.player = player;
	}
	
	public Minigame getMinigame()
	{
		return minigame;
	}
	
	public ArkynePlayer getPlayer()
	{
		return player;
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