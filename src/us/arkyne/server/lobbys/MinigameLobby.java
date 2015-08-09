package us.arkyne.server.lobbys;

import java.util.Map;

import us.arkyne.server.lobby.Lobby;

public class MinigameLobby extends Lobby
{
	@Override
	public void onLoad()
	{
		
	}

	@Override
	public void onUnload()
	{
		
	}

	public MinigameLobby(Map<String, Object> map)
	{
		super(map);
	}

	@Override
	public Map<String, Object> serialize()
	{
		return null;
	}
}