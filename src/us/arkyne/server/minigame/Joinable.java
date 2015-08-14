package us.arkyne.server.minigame;

import java.util.List;

import org.bukkit.Location;

import us.arkyne.server.inventory.Inventory;
import us.arkyne.server.player.ArkynePlayer;
import us.arkyne.server.util.Cuboid;

public interface Joinable
{
	public void join(ArkynePlayer player);
	
	public List<ArkynePlayer> getPlayers();
	
	public Type getType();
	public Inventory getInventory();
	
	public Cuboid getBounds();
	public Location getSpawn();
	
	public static enum Type
	{
		MINIGAME, GAME, MAINLOBBY;
	}
}