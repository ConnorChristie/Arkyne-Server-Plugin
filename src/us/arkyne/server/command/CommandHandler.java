package us.arkyne.server.command;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import us.arkyne.server.MinigameMain;
import us.arkyne.server.command.cmds.ArkyneCommand;

public class CommandHandler implements org.bukkit.command.CommandExecutor
{
	private static List<CommandExecutor> registeredExecutors = new ArrayList<CommandExecutor>();
	
	public CommandHandler(MinigameMain main)
	{
		main.getCommand(ArkyneCommand.commandNames[0]).setExecutor(this);
	}
	
	public static void registerExecutor(CommandExecutor executor)
	{
		registeredExecutors.add(executor);
	}

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args)
	{
		Command command = new Command(label, sender, args);
		
		if (ArrayUtils.contains(ArkyneCommand.commandNames, label.toLowerCase()))
		{
			boolean executed = false;
			
			for (CommandExecutor executor : getExecutors(ArkyneCommand.class))
			{
				//Find all command executors for that command, and execute
				
				executed = ((ArkyneCommand) executor).arkyneCommand(command) ? true : executed;
			}
			
			if (!executed)
			{
				//Send user message because the command was not executed
				
				command.sendSenderMessage("Usage: /{cmd} <subcommand>", ChatColor.RED);
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