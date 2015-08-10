package us.arkyne.server.minigame.battlefrontier;

import java.util.Map;

import us.arkyne.server.ArkyneMain;
import us.arkyne.server.minigame.Minigame;

public class BattleFrontierMinigame extends Minigame
{
	public BattleFrontierMinigame(ArkyneMain main)
	{
		super(main);
	}

	@Override
	public void onLoad()
	{
		
	}

	@Override
	public void onUnload()
	{
		
	}
	
	public BattleFrontierMinigame(Map<String, Object> map)
	{
		super(map);
	}
	
	@Override
	public Map<String, Object> serialize()
	{
		Map<String, Object> map = super.serialize();
		
		return map;
	}
}