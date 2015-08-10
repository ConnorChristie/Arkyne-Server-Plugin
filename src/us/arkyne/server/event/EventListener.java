package us.arkyne.server.event;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import us.arkyne.server.ArkyneMain;
import us.arkyne.server.event.customevents.PlayerChangeLobbyEvent;
import us.arkyne.server.lobby.Lobby;
import us.arkyne.server.player.ArkynePlayer;

public class EventListener implements Listener
{
	private ArkyneMain main;
	
	public EventListener()
	{
		main = ArkyneMain.getInstance();
		
		main.getServer().getPluginManager().registerEvents(this, main);
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		// If player was in game but left, try to make them go back in that game
		// Otherwise spawn them in the main lobby
		
		main.getArkynePlayers().addPlayer(event.getPlayer());
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event)
	{
		ArkynePlayer player = main.getArkynePlayers().getPlayer(event.getPlayer());
		
		Lobby lobby = player.getLobby();
		
		if (lobby != null)
		{
			if (!lobby.getCuboid().containsWithoutY(player))
			{
				//Bounce them back into the cuboid region
				
				player.pushTowards(lobby.getSpawn());
			}
		}
	}
	
	@EventHandler
	public void onPlayerChangeLobby(PlayerChangeLobbyEvent event)
	{
		//Update signs, change player count
		
		//Get old lobby sign, and new lobby sign
		
		Lobby fromLobby = event.getFromLobby();
		Lobby toLobby = event.getToLobby();
		
		if (fromLobby != null)
		{
			fromLobby.updateSign();
		}
		
		if (toLobby != null)
		{
			toLobby.updateSign();
		}
	}
	
	@EventHandler
	public void onSignChange(SignChangeEvent event)
	{
		if (event.getLine(0).equalsIgnoreCase("[lobby]"))
		{
			if (main.getLobbys().containsLobby(event.getLine(1)))
			{
				Lobby lobby = main.getLobbys().getLobby(event.getLine(1));
				
				lobby.setSign(event.getBlock().getLocation());
				lobby.updateSign(event);
				
				main.getLobbys().save(lobby);
			} else
			{
				event.setLine(1, ChatColor.DARK_RED + "Invalid ID");
			}
		}
	}
}