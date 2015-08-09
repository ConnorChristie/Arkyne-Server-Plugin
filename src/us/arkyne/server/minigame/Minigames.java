package us.arkyne.server.minigame;

import us.arkyne.server.MinigameMain;
import us.arkyne.server.loader.Loader;
import us.arkyne.server.minigame.battlefrontier.BattleFrontierMinigame;

public class Minigames extends Loader<Minigame>
{
	public Minigames(MinigameMain main)
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
}