package us.arkyne.server.lobby;

import java.util.Map;

import org.bukkit.Location;

import us.arkyne.server.ArkyneMain;
import us.arkyne.server.inventory.Inventory;
import us.arkyne.server.message.SignMessage;
import us.arkyne.server.minigame.Joinable;
import us.arkyne.server.minigame.Minigame;
import us.arkyne.server.util.Cuboid;

public class MinigameLobby extends Lobby
{
	private Minigame minigame;
	
	public MinigameLobby(Minigame minigame, String name, Integer id, Location spawn, Cuboid cuboid, Inventory inventory, SignMessage signMessage)
	{
		super(name, id, spawn, cuboid, inventory, signMessage);
		
		this.minigame = minigame;
		
		setIdPrefix(minigame.getId() + "-L");
	}
	
	@Override
	public Type getType()
	{
		return Joinable.Type.MINIGAME;
	}
	
	public MinigameLobby(Map<String, Object> map)
	{
		super(map);
		
		minigame = ArkyneMain.getInstance().getMinigameHandler().getMinigame(map.get("minigame").toString());
		
		setIdPrefix(minigame.getId() + "-L");
	}
	
	public Map<String, Object> serialize()
	{
		Map<String, Object> map = super.serialize();
		
		map.put("minigame", minigame.getId());
		
		return map;
	}
}