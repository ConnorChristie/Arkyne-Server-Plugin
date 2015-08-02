package us.arkyne.server.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import us.arkyne.server.Main;
import us.arkyne.server.command.cmds.ArkyneCommand;

public class CommandHandler implements org.bukkit.command.CommandExecutor
{
	private static List<CommandExecutor> registeredExecutors = new ArrayList<CommandExecutor>();
	
	public CommandHandler(Main main)
	{
		main.getCommand("arkyne").setExecutor(this);
	}
	
	public static void registerExecutor(CommandExecutor executor)
	{
		registeredExecutors.add(executor);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		for (CommandExecutor executor : registeredExecutors)
		{
			if (label.equalsIgnoreCase("arkyne"))
			{
				if (executor instanceof ArkyneCommand)
				{
					((ArkyneCommand) executor).arkyneCommand(sender, args);
				}
			}
		}
		
		return true;
	}
}