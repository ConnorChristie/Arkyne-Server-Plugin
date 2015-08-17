package us.arkyne.server.minigame;

import java.util.List;

import org.bukkit.Location;

import us.arkyne.server.inventory.Inventory;
import us.arkyne.server.player.ArkynePlayer;
import us.arkyne.server.util.Cuboid;

public interface Joinable
{
	public String getIdString();
	
	public void join(ArkynePlayer player);
	public void leave(ArkynePlayer player);
	
	public boolean isJoinable(ArkynePlayer player);
	public List<ArkynePlayer> getPlayers();
	
	public Type getType();
	public Inventory getInventory(ArkynePlayer player);
	
	public Cuboid getBounds();
	public Location getSpawn(ArkynePlayer player);
	
	public static enum Type
	{
		MAINLOBBY, MINIGAME, GAME;
	}
}