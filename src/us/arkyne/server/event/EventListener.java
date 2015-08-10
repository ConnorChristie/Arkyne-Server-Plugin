package us.arkyne.server.event;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import us.arkyne.server.MinigameMain;
import us.arkyne.server.event.customevents.PlayerChangeLobbyEvent;
import us.arkyne.server.lobby.Lobby;
import us.arkyne.server.message.SignMessage;

public class EventListener implements Listener
{
	private MinigameMain main;
	
	public EventListener()
	{
		main = MinigameMain.getInstance();
		
		main.getServer().getPluginManager().registerEvents(this, main);
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		// If player was in game but left, try to make them go back in that game
		// Otherwise spawn them in the main lobby
		
		
	}
	
	@EventHandler
	public void onPlayerChangeLobby(PlayerChangeLobbyEvent event)
	{
		//Update signs, change player count
		
		
	}
	
	@EventHandler
	public void onSignChange(SignChangeEvent event)
	{
		if (event.getLine(0).equalsIgnoreCase("[lobby]"))
		{
			if (main.getLobbys().containsLobby(event.getLine(1)))
			{
				Lobby lobby = main.getLobbys().getLobby(event.getLine(1));
				
				for (int i = 0; i < 4; i++)
				{
					event.setLine(i, SignMessage.LOBBY
							.replace(i, "{lobby}", lobby.getName())
							.replace("{lobby-id}", lobby.getId())
							.replace("{count}", lobby.getPlayerCount() + ""));
				}
				
				lobby.setSign(event.getBlock().getLocation());
				
				main.getLobbys().saveLobbys();
			} else
			{
				event.setLine(1, ChatColor.DARK_RED + "Invalid ID");
			}
		}
	}
}