package us.arkyne.server.lobby;

import java.util.Map;

import org.bukkit.Location;

import us.arkyne.server.ArkyneMain;
import us.arkyne.server.inventory.Inventory;
import us.arkyne.server.message.SignMessage;
import us.arkyne.server.minigame.Joinable;
import us.arkyne.server.minigame.Minigame;
import us.arkyne.server.util.Cuboid;

public class PregameLobby extends Lobby
{
	private Minigame minigame;
	
	public PregameLobby(Minigame minigame, String name, Integer id, Location spawn, Cuboid cuboid, Inventory inventory, SignMessage signMessage)
	{
		super(name, id, spawn, cuboid, inventory, signMessage);
		
		this.minigame = minigame;
	}
	
	@Override
	public Type getType()
	{
		return Joinable.Type.GAME;
	}
	
	public PregameLobby(Map<String, Object> map)
	{
		super(map);
		
		minigame = ArkyneMain.getInstance().getMinigameHandler().getMinigame(map.get("minigame").toString());
	}
	
	public Map<String, Object> serialize()
	{
		Map<String, Object> map = super.serialize();
		
		map.put("minigame", minigame.getId());
		
		return map;
	}
}