package us.arkyne.server.command.cmds;

import org.bukkit.command.CommandSender;

import us.arkyne.server.command.CommandExecutor;

public interface ArkyneCommand extends CommandExecutor
{
	public void arkyneCommand(CommandSender sender, String[] args);
}