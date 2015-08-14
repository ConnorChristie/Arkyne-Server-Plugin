package us.arkyne.server.lobby;

import org.bukkit.Location;

import us.arkyne.server.inventory.InventoryPreset;
import us.arkyne.server.message.SignMessagePreset;
import us.arkyne.server.minigame.Joinable;
import us.arkyne.server.util.Cuboid;

public class MainLobby extends Lobby
{
	public MainLobby(Location spawn, Cuboid cuboid)
	{
		super("MainLobby", 1, spawn, cuboid, InventoryPreset.MAIN_LOBBY, SignMessagePreset.MAIN_LOBBY);
	}
	
	@Override
	public Type getType()
	{
		return Joinable.Type.MAINLOBBY;
	}
}