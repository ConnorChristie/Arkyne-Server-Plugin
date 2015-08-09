package us.arkyne.server.command.cmds;

import us.arkyne.server.command.Command;
import us.arkyne.server.command.CommandExecutor;

public interface LobbyCommand extends CommandExecutor
{
	public String[] commandNames = new String[] { "lobby" };
	
	public boolean lobbyCommand(Command cmd);
}