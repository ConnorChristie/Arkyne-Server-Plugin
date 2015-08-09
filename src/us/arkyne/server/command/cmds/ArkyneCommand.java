package us.arkyne.server.command.cmds;

import us.arkyne.server.command.Command;
import us.arkyne.server.command.CommandExecutor;

public interface ArkyneCommand extends CommandExecutor
{
	public String[] commandNames = new String[] { "arkyne", "ark" };
	
	public boolean arkyneCommand(Command cmd);
}