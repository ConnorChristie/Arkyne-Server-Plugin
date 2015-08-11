package us.arkyne.server.util;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URLClassLoader;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPluginLoader;

import us.arkyne.server.ArkyneMain;

public class Util
{
	public static final String PREFIX = ChatColor.GOLD + "[Arkyne] ";
	
	private static Field loadersF;
	private static SimpleCommandMap scm;
	
	private static Map<String, Command> kc;
	
	public static void noticeableConsoleMessage(String message)
	{
		ArkyneMain.getInstance().getLogger().severe("------------------------------------------------------------");
		ArkyneMain.getInstance().getLogger().severe(message);
		ArkyneMain.getInstance().getLogger().severe("------------------------------------------------------------");
	}
	
	//Load plugin jar and unload it with this
	
	@SuppressWarnings("unchecked")
	public static void unloadPlugin(Plugin plugin) throws Exception
	{
		PluginManager bpm = Bukkit.getServer().getPluginManager();
		
		if (!(bpm instanceof SimplePluginManager))
		{
			throw new Exception("Unknown Bukkit plugin system detected: " + bpm.getClass().getName());
		}
		
		SimplePluginManager spm = (SimplePluginManager) bpm;
		
		if (scm == null)
		{
			Field scmF = SimplePluginManager.class.getDeclaredField("commandMap");
			scmF.setAccessible(true);
			
			scm = ((SimpleCommandMap) scmF.get(spm));
			
			if (!(scm instanceof SimpleCommandMap))
			{
				throw new Exception("Unsupported Bukkit command system detected: " + scm.getClass().getName());
			}
		}
		
		if (kc == null)
		{
			Field kcF = scm.getClass().getDeclaredField("knownCommands");
			kcF.setAccessible(true);
			
			kc = ((Map<String, Command>) kcF.get(scm));
		}
		
		plugin.getClass().getClassLoader().getResources("*");
		
		Field lnF = spm.getClass().getDeclaredField("lookupNames");
		
		lnF.setAccessible(true);
		Map<String, Plugin> ln = (Map<String, Plugin>) lnF.get(spm);
		
		Field plF = spm.getClass().getDeclaredField("plugins");
		
		plF.setAccessible(true);
		List<Plugin> pl = (List<Plugin>) plF.get(spm);
		
		synchronized (scm)
		{
			Iterator<Map.Entry<String, Command>> it = kc.entrySet().iterator();
			
			while (it.hasNext())
			{
				Map.Entry<String, Command> entry = (Map.Entry<String, Command>) it.next();
				
				if ((entry.getValue() instanceof PluginCommand))
				{
					PluginCommand c = (PluginCommand) entry.getValue();
					
					if (c.getPlugin().getName().equalsIgnoreCase(plugin.getName()))
					{
						c.unregister(scm);
						
						it.remove();
					}
				}
			}
		}
		
		spm.disablePlugin(plugin);
		
		synchronized (spm)
		{
			ln.remove(plugin.getName());
			pl.remove(plugin);
		}
		
		JavaPluginLoader jpl = (JavaPluginLoader) plugin.getPluginLoader();
		
		if (loadersF == null)
		{
			loadersF = jpl.getClass().getDeclaredField("loaders");
			loadersF.setAccessible(true);
		}
		
		try
		{
			Map<String, ?> loaderMap = (Map<String, ?>) loadersF.get(jpl);
			
			loaderMap.remove(plugin.getDescription().getName());
		} catch (Exception e)
		{
			throw e;
		}
		
		closeClassLoader(plugin);
		
		System.gc();
		System.gc();
	}
	
	private static void closeClassLoader(Plugin plugin)
	{
		try
		{
			((URLClassLoader) plugin.getClass().getClassLoader()).close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}