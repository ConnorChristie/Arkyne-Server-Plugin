package us.arkyne.server.minigame;

import java.util.HashMap;
import java.util.Map;

import us.arkyne.server.MinigameMain;
import us.arkyne.server.command.cmds.ArkyneCommand;
import us.arkyne.server.loader.Loadable;

public abstract class Minigame implements Loadable, ArkyneCommand
{
	//Minigame variables like a timer, field location
	
	public Minigame(MinigameMain main)
	{
		
	}
	
	public Minigame(Map<String, Object> map)
	{
		
	}
	
	public Map<String, Object> serialize()
	{
		Map<String, Object> map = new HashMap<String, Object>();
		
		return map;
	}
}