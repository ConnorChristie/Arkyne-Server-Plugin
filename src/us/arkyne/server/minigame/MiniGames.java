package us.arkyne.server.minigame;

import us.arkyne.server.loader.Loader;
import us.arkyne.server.minigame.battlefrontier.BattleFrontierMinigame;

public class MiniGames extends Loader
{
	public MiniGames()
	{
		addLoadable(new BattleFrontierMinigame());
	}
}