package us.arkyne.server.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import us.arkyne.server.MinigameMain;
import us.arkyne.server.event.customevents.PlayerChangeLobbyEvent;

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
		
	}
}