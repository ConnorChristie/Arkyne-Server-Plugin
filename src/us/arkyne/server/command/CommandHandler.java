package us.arkyne.server.command;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import us.arkyne.server.ArkyneMain;
import us.arkyne.server.util.Util;

public class CommandHandler implements org.bukkit.command.CommandExecutor
{
	private static Map<String[], Class<? extends CommandExecutor>> mappedCommandClasses = new HashMap<String[], Class<? extends CommandExecutor>>();
	private static List<CommandExecutor> registeredExecutors = new ArrayList<CommandExecutor>();
	
	public void registerCommand(Class<? extends CommandExecutor> commandClass)
	{
		registerCommand(ArkyneMain.getInstance(), commandClass);
	}
	
	public void registerCommand(JavaPlugin plugin, Class<? extends CommandExecutor> commandClass)
	{
		try
		{
			String[] commandNames = (String[]) commandClass.getField("commandNames").get(null);
			PluginCommand command = plugin.getCommand(commandNames[0]);
			
			if (command != null)
			{
				command.setExecutor(this);
				
				mappedCommandClasses.put(commandNames, commandClass);
				registeredExecutors.add(commandClass.newInstance());
			} else
			{
				Util.noticeableConsoleMessage("The command: " + commandNames[0] + ", is not registered in the plugin.yml");
			}
			
			//Get command class from the string array
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException | InstantiationException e)
		{
			//Could not instantiate command class
		}
	}
	
	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args)
	{
		Command command = new Command(label, sender, args);
		
		for (String[] commandNames : mappedCommandClasses.keySet())
		{
			if (ArrayUtils.contains(commandNames, label.toLowerCase()))
			{
				Class<? extends CommandExecutor> commandClass = mappedCommandClasses.get(commandNames);
				
				for (CommandExecutor executor : getExecutors(commandClass))
				{
					//Find all command executors for that command, and execute
					
					try
					{
						boolean executed = ((boolean) commandClass.getMethod(commandNames[0] + "Command", Command.class).invoke(executor, command));
						
						if (executed) return true;
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e)
					{
						e.printStackTrace();
						
						Util.noticeableConsoleMessage("Could not invoke the method: " + commandNames[0] + "Command, in the command class");
					}
				}
				
				//Send user message because the command was not handled by anyone
				
				command.sendSenderMessage("Usage: /{cmd} <subcommand>", ChatColor.RED);
				
				return true;
			}
		}
		
		return true;
	}
	
	private List<CommandExecutor> getExecutors(Class<? extends CommandExecutor> instance)
	{
		List<CommandExecutor> list = new ArrayList<CommandExecutor>();
		
		for (CommandExecutor executor : registeredExecutors)
		{
			if (instance.isInstance(executor))
			{
				list.add(executor);
			}
		}
		
		return list;
	}
}