package us.arkyne.server.game.arena;

import java.util.HashMap;
import java.util.Map;

import us.arkyne.server.config.ArenasConfig;
import us.arkyne.server.loader.Loader;
import us.arkyne.server.minigame.Minigame;

public class ArenaHandler extends Loader
{
	private Minigame minigame;
	private ArenasConfig arenasConfig;
	
	private Map<Integer, Arena> arenas = new HashMap<Integer, Arena>();
	
	public ArenaHandler(Minigame minigame)
	{
		this.minigame = minigame;
	}
	
	@Override
	public void onLoad()
	{
		arenasConfig = new ArenasConfig(minigame.getPlugin().getDataFolder());
		arenas = arenasConfig.getArenas();
		
		for (Arena arena : arenas.values())
		{
			addLoadable(arena);
		}
	}

	@Override
	public void onUnload()
	{
		
	}
	
	public Map<Integer, Arena> getArenas()
	{
		return arenas;
	}
	
	public int getArenaCount()
	{
		return arenas.size();
	}
	
	public ArenasConfig getArenasConfig()
	{
		return arenasConfig;
	}
	
	public void addArena(Arena arena)
	{
		arenas.put(arena.getId(), arena);
		addLoadable(arena);
		
		arena.onLoad();
	}
	
	public int getNextId()
	{
		//Just make sure we never get a duplicate key
		if (arenas.containsKey(arenas.size() + 1))
		{
			int highest = 0;
			
			for (Integer key : arenas.keySet())
			{
				highest = key > highest ? key : highest;
			}
			
			return highest + 1;
		}
		
		return arenas.size() + 1;
	}
	
	public Arena getArena(int id)
	{
		return arenas.get(id);
	}
	
	public boolean containsArena(int id)
	{
		return arenas.containsKey(id);
	}
	
	public void save(Arena arena)
	{
		arenasConfig.set("arenas." + arena.getId(), arena);
		arenasConfig.saveConfig();
	}
	
	public void saveAll()
	{
		for (Map.Entry<Integer, Arena> arena : arenas.entrySet())
		{
			arenasConfig.set("arenas." + arena.getKey(), arena.getValue());
		}
		
		arenasConfig.saveConfig();
	}
}