package us.arkyne.server.config;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import us.arkyne.server.game.arena.Arena;

public class ArenasConfig extends Config
{
	public ArenasConfig(File dataFolder)
	{
		super(dataFolder, "arenas");
		
		loadConfig();
	}
	
	public Map<Integer, Arena> getArenas()
	{
		return getInstanceMap("arenas", new HashMap<Integer, Arena>());
	}
}