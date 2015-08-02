package us.arkyne.server.minigame.battlefrontier;

import java.util.Map;

import us.arkyne.server.Main;
import us.arkyne.server.command.Command;
import us.arkyne.server.minigame.MiniGame;

public class BattleFrontierMinigame extends MiniGame
{
	public BattleFrontierMinigame(Main main)
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
	public void arkyneCommand(Command command)
	{
		
	}
	
	@Override
	public void deserialize(Map<String, Object> map)
	{
		super.deserialize(map);
	}
	
	@Override
	public Map<String, Object> serialize()
	{
		Map<String, Object> map = super.serialize();
		
		return map;
	}
}