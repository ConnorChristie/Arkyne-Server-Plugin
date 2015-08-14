package us.arkyne.server.minigame;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.UnknownDependencyException;

import us.arkyne.server.game.Game;
import us.arkyne.server.loader.Loader;
import us.arkyne.server.player.ArkynePlayer;
import us.arkyne.server.plugin.MinigamePlugin;
import us.arkyne.server.util.Util;

public class MinigameHandler extends Loader
{
	private Map<String, Minigame> minigames = new HashMap<String, Minigame>();
	
	private Map<String, File> unloadedPlugins = new HashMap<String, File>();
	
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
		minigame.loadAll();
		
		checkAwaitingPlayers(minigame);
	}
	
	public void unRegisterMinigame(Minigame minigame)
	{
		//If player is in this minigame, TP them to main lobby
		
		minigames.remove(minigame.getId());
		
		minigame.unloadAll();
		removeLoadable(minigame);
	}
	
	public boolean unloadPlugin(String pluginName)
	{
		Minigame minigame = getMain().getMinigameHandler().getMinigame(pluginName);
		Plugin plugin = minigame != null ? minigame.getPlugin() : Bukkit.getServer().getPluginManager().getPlugin(pluginName);
		
		if (plugin != null && plugin instanceof MinigamePlugin)
		{
			String name = plugin.getName();
			
			unloadedPlugins.put(name, ((MinigamePlugin) plugin).getFile());
			
			if (minigame != null) unRegisterMinigame(minigame);
			
			try
			{
				Util.unloadPlugin(minigame.getPlugin());
				
				getMain().getLogger().info("Fully unloaded and disabled " + name + "!");
				
				return true;
			} catch (Exception e)
			{
				e.printStackTrace();
				
				Util.noticeableConsoleMessage("Error unloading " + name + "'s minigame plugin");
			}
		}
		
		return false;
	}
	
	public void loadPlugin(String minigameName)
	{
		for (Map.Entry<String, File> minigame : unloadedPlugins.entrySet())
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
	
	public void reloadPlugin(String pluginName)
	{
		unloadPlugin(pluginName);
		loadPlugin(pluginName);
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

	public Joinable getJoinable(Location signLocation)
	{
		for (Minigame minigame : minigames.values())
		{
			if (minigame.getLobby() != null && minigame.getLobby().isSign(signLocation))
			{
				return minigame;
			} else
			{
				for (Game game : minigame.getGameHandler().getGames().values())
				{
					if (game.getPregameLobby().isSign(signLocation))
					{
						return game;
					}
				}
			}
		}
		
		return null;
	}
	
	private void checkAwaitingPlayers(Minigame minigame)
	{
		for (ArkynePlayer player : getMain().getArkynePlayerHandler().getPlayers())
		{
			if (player.isAwaitingMinigameLoad())
			{
				if (player.getAwaitingType() == Joinable.Type.MINIGAME)
				{
					if (minigame.getId().equalsIgnoreCase(player.getAwaitingId()))
					{
						player.setJoinable(minigame);
						
						player.setAwaitingMinigameLoad(false);
					}
				} else if (player.getAwaitingType() == Joinable.Type.GAME)
				{
					String[] ids = player.getAwaitingId().split("-");
					
					if (minigame.getId().equalsIgnoreCase(ids[0]))
					{
						player.setJoinable(minigame.getGameHandler().getGame(Integer.parseInt(ids[1])));
						
						player.setAwaitingMinigameLoad(false);
					}
				}
			}
		}
	}
}