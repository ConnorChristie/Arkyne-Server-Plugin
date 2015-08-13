package us.arkyne.server.game;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import us.arkyne.server.inventory.Inventory;
import us.arkyne.server.loader.Loadable;
import us.arkyne.server.loader.Loader;
import us.arkyne.server.lobby.Lobby;
import us.arkyne.server.message.SignMessage;
import us.arkyne.server.minigame.Minigame;
import us.arkyne.server.util.Cuboid;

public abstract class Game extends Loader implements Loadable, ConfigurationSerializable
{
	protected int id;
	
	protected Minigame minigame;
	
	protected Arena arena;
	protected Lobby pregameLobby;
	
	//TODO: Add player limit and signs should display: #/total Players
	
	public Game(int id)
	{
		this.id = id;
	}
	
	public void onLoad()
	{
		if (arena != null) addLoadable(arena);
		if (pregameLobby != null) addLoadable(pregameLobby);
	}
	
	public void onUnload()
	{
		
	}
	
	public int getId()
	{
		return id;
	}
	
	public Arena getArena()
	{
		return arena;
	}
	
	public Lobby getPregameLobby()
	{
		return pregameLobby;
	}
	
	public boolean createPregameLobby(Location spawn, Cuboid cuboid, Inventory inventory, SignMessage signMessage)
	{
		if (pregameLobby == null)
		{
			pregameLobby = new Lobby(minigame.getName(), minigame.getId() + "-" + id, spawn, cuboid, inventory, signMessage);
			
			addLoadable(pregameLobby);
			pregameLobby.onLoad();
			
			save();
			
			return true;
		}
		
		return false;
	}
	
	public void save()
	{
		minigame.save(this);
	}
	
	public Game(Map<String, Object> map)
	{
		id = (Integer) map.get("id");
		
		arena = (Arena) map.get("arena");
		pregameLobby = (Lobby) map.get("pregame_lobby");
	}
	
	public Map<String, Object> serialize()
	{
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("id", id);
		
		map.put("arena", arena);
		map.put("pregame_lobby", pregameLobby);
		
		return map;
	}
}