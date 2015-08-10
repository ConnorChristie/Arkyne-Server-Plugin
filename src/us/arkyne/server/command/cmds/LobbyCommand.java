package us.arkyne.server.command.cmds;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import us.arkyne.server.MinigameMain;
import us.arkyne.server.command.Command;
import us.arkyne.server.command.CommandExecutor;
import us.arkyne.server.event.customevents.PlayerChangeLobbyEvent;
import us.arkyne.server.lobby.Lobby;
import us.arkyne.server.player.ArkynePlayer;

public class LobbyCommand implements CommandExecutor
{
	public static String[] commandNames = new String[] { "lobby" };
	
	private MinigameMain main;
	
	public LobbyCommand()
	{
		main = MinigameMain.getInstance();
	}
	
	public boolean lobbyCommand(Command command)
	{
		if (command.isSubCommandMessageIfError("join", 1, false, "Usage: /{cmd} join <name|id>"))
		{
			Lobby lobby = main.getLobbys().getLobby(command.getArg(0));
			
			if (lobby != null)
			{
				ArkynePlayer player = command.getPlayer();
				
				PlayerChangeLobbyEvent event = new PlayerChangeLobbyEvent(player, player.getLobby(), lobby);
				Bukkit.getServer().getPluginManager().callEvent(event);
				
				if (!event.isCancelled())
				{
					player.changeLobby(lobby);
				}
			} else
			{
				command.sendSenderMessage("Invalid name or id entered", ChatColor.RED);
			}
			
			return true;
		}
		
		return command.wasArgLengthError();
	}
}