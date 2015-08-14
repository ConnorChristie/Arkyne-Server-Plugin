package us.arkyne.server.minigame;

import java.util.List;

import us.arkyne.server.inventory.Inventory;
import us.arkyne.server.player.ArkynePlayer;

public interface Joinable
{
	public void join(ArkynePlayer player);
	
	public List<ArkynePlayer> getPlayers();
	
	public Type getType();
	
	public Inventory getInventory();
	
	public static enum Type
	{
		MINIGAME, GAME, MAINLOBBY;
	}
}