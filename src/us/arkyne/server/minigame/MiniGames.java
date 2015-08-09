package us.arkyne.server.minigame;

import java.util.Map;

import us.arkyne.server.MinigameMain;
import us.arkyne.server.loader.Loader;
import us.arkyne.server.minigame.battlefrontier.BattleFrontierMinigame;

public class MiniGames extends Loader
{
	public MiniGames(MinigameMain main)
	{
		super(main);
		
		addLoadable(new BattleFrontierMinigame(main));
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

	@Override
	public void deserialize(Map<String, Object> map)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public Map<String, Object> serialize()
	{
		// TODO Auto-generated method stub
		return null;
	}
}