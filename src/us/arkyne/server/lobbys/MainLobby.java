package us.arkyne.server.lobbys;

import java.util.Map;

import org.bukkit.Location;

import us.arkyne.server.lobby.Lobby;

public class MainLobby extends Lobby
{
	public MainLobby(String name, String id, Location min, Location max)
	{
		super(name, id, min, max);
	}
	
	@Override
	public void onLoad()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUnload()
	{
		// TODO Auto-generated method stub
		
	}
	
	public MainLobby(Map<String, Object> map)
	{
		super(map);
	}
}