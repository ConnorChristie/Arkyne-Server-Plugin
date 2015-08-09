package us.arkyne.server.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import us.arkyne.server.MinigameMain;

public abstract class Config extends YamlConfiguration
{
	private String fileName;
	private File configFile;
	
	private MinigameMain main;
	
	public Config(String fileName)
	{
		this.fileName = fileName;
		
		main = MinigameMain.getInstance();
	}
	
	@SuppressWarnings("unchecked")
	public <T> Map<String, T> getInstanceMap(String path, Map<String, T> def)
	{
		ConfigurationSection section = getConfigurationSection(path);
		
		if (section != null)
		{
			for (String id : section.getKeys(false))
			{
				def.put(id, (T) section.get(id));
			}
		}
		
		return def;
	}
	
	public void saveConfig()
	{
		if (configFile == null)
			return;
		
		try
		{
			save(configFile);
		} catch (IOException ex)
		{
			ex.printStackTrace();
		}
	}
	
	public void loadConfig()
	{
		try
		{
			if (configFile == null)
				configFile = new File(main.getDataFolder(), fileName + ".yml");
			
			if (!configFile.exists())
			{
				configFile.getParentFile().mkdirs();
				configFile.createNewFile();
			}
			
			load(configFile);
			
			try
			{
				// Look for defaults in the jar
				Reader defConfigStream = new InputStreamReader(main.getResource(fileName + ".yml"), "UTF8");
				
				if (defConfigStream != null)
				{
					YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
					setDefaults(defConfig);
				}
			} catch (Exception e)
			{
				//Could not load defaults, no big deal...
			}
		} catch (IOException | InvalidConfigurationException e)
		{
			e.printStackTrace();
			
			//Could not load saved file, bigger deal...
		}
	}
	
	public void saveDefaultConfig()
	{
		if (!configFile.exists())
		{
			main.saveResource(fileName + ".yml", false);
		}
	}
}