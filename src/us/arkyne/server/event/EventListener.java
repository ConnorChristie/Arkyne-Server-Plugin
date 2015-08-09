package us.arkyne.server.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import us.arkyne.server.MinigameMain;

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
		
	}
}