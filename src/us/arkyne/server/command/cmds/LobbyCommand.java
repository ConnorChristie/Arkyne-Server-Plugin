package us.arkyne.server.command.cmds;

import org.bukkit.ChatColor;

import us.arkyne.server.ArkyneMain;
import us.arkyne.server.command.Command;
import us.arkyne.server.command.CommandExecutor;
import us.arkyne.server.lobby.Lobby;

public class LobbyCommand implements CommandExecutor
{
	public static String[] commandNames = new String[] { "lobby" };
	
	private ArkyneMain main;
	
	public LobbyCommand()
	{
		main = ArkyneMain.getInstance();
	}
	
	public boolean lobbyCommand(Command command)
	{
		if (command.isSubCommandMessageIfError("join", 1, false, "Usage: /{cmd} join <name|id>"))
		{
			Lobby lobby = main.getLobbyHandler().getLobby(command.getArg(0));
			
			if (lobby != null)
			{
				command.getPlayer().changeLobby(lobby);
			} else
			{
				command.sendSenderMessage("Invalid name or id entered", ChatColor.RED);
			}
			
			return true;
		} else if (command.isSubCommandMessageIfError("leave", 0, false, "Usage: /{cmd} leave"))
		{
			command.getPlayer().changeLobby(null);
			
			return true;
		}
		
		return command.wasArgLengthError();
	}
}