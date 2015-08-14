package us.arkyne.server.lobby;

import java.util.Map;

import org.bukkit.Location;

import us.arkyne.server.inventory.InventoryPreset;
import us.arkyne.server.message.SignMessagePreset;
import us.arkyne.server.minigame.Joinable;
import us.arkyne.server.player.ArkynePlayer;
import us.arkyne.server.util.Cuboid;

public class MainLobby extends Lobby
{
	public MainLobby(Location spawn, Cuboid cuboid)
	{
		super("MainLobby", 1, spawn, cuboid, InventoryPreset.MAIN_LOBBY, SignMessagePreset.MAIN_LOBBY);
	}
	
	public void join(ArkynePlayer player)
	{
		super.join(player);
		
		player.setJoinable(this);
	}
	
	public Type getType()
	{
		return Joinable.Type.MAINLOBBY;
	}
	
	public MainLobby(Map<String, Object> map)
	{
		super(map);
	}
	
	public Map<String, Object> serialize()
	{
		Map<String, Object> map = super.serialize();
		
		return map;
	}
}