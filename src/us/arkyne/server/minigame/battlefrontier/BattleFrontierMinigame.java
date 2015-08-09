package us.arkyne.server.minigame.battlefrontier;

import java.util.Map;

import us.arkyne.server.MinigameMain;
import us.arkyne.server.command.Command;
import us.arkyne.server.minigame.Minigame;

public class BattleFrontierMinigame extends Minigame
{
	public BattleFrontierMinigame(MinigameMain main)
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
	
	@Override
	public boolean arkyneCommand(Command command)
	{
		return false;
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