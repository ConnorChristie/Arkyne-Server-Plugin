package us.arkyne.server.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

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
		
		loadConfig();
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
				
			load(configFile);
			
			// Look for defaults in the jar
			Reader defConfigStream = new InputStreamReader(main.getResource(fileName + ".yml"), "UTF8");
			
			if (defConfigStream != null)
			{
				YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
				setDefaults(defConfig);
			}
		} catch (IOException | InvalidConfigurationException e)
		{
			e.printStackTrace();
		}
	}
}