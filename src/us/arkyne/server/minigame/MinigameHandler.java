package us.arkyne.server.minigame;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.UnknownDependencyException;

import us.arkyne.server.loader.Loader;
import us.arkyne.server.util.Util;

public class MinigameHandler extends Loader<Minigame>
{
	private Map<String, Minigame> minigames = new HashMap<String, Minigame>();
	
	private Map<String, File> unloadedMinigames = new HashMap<String, File>();
	
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
	
	public void registerMinigame(Minigame minigame)
	{
		minigames.put(minigame.getId(), minigame);
		
		addLoadable(minigame);
		minigame.onLoad();
	}
	
	public void unRegisterMinigame(Minigame minigame)
	{
		minigames.remove(minigame.getId());
		
		minigame.onUnload();
		removeLoadable(minigame);
	}
	
	public void unloadMinigamePlugin(Minigame minigame)
	{
		if (minigame != null)
		{
			unloadedMinigames.put(minigame.getName(), minigame.getPlugin().getFile());
			
			unRegisterMinigame(minigame);
			
			try
			{
				Util.unloadPlugin(minigame.getPlugin());
			} catch (Exception e)
			{
				e.printStackTrace();
				
				Util.noticeableConsoleMessage("Error unloading " + minigame.getName() + "'s minigame plugin");
			}
		}
	}
	
	public void loadMinigamePlugin(String minigameName)
	{
		for (Map.Entry<String, File> minigame : unloadedMinigames.entrySet())
		{
			if (minigame.getKey().toLowerCase().contains(minigameName.toLowerCase()))
			{
				try
				{
					Plugin plugin = Bukkit.getServer().getPluginManager().loadPlugin(minigame.getValue());
					
					Bukkit.getServer().getPluginManager().enablePlugin(plugin);
				} catch (UnknownDependencyException | InvalidPluginException | InvalidDescriptionException e)
				{
					e.printStackTrace();
					
					Util.noticeableConsoleMessage("Error loading " + minigame.getKey() + "'s minigame plugin");
				}
				
				return;
			}
		}
	}
	
	public void reloadMinigamePlugin(Minigame minigame)
	{
		if (minigame != null)
		{
			String minigameName = minigame.getName();
			
			unloadMinigamePlugin(minigame);
			loadMinigamePlugin(minigameName);
		}
	}
	
	public Minigame getMinigame(String check)
	{
		for (Minigame minigame : minigames.values())
		{
			if (minigame.getName().equalsIgnoreCase(check) || minigame.getId().equalsIgnoreCase(check))
			{
				return minigame;
			}
		}
		
		return null;
	}
}