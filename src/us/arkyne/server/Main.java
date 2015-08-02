package us.arkyne.server;

import org.bukkit.plugin.java.JavaPlugin;

import us.arkyne.server.lobby.Lobbys;
import us.arkyne.server.minigame.MiniGames;

public class Main extends JavaPlugin
{
	private Lobbys lobbys;
	private MiniGames miniGames;
	
	public void onEnable()
	{
		setupLobbys();
		setupMiniGames();
	}
	
	public void onDisable()
	{
		lobbys.unloadAll();
		miniGames.unloadAll();
	}
	
	private void setupLobbys()
	{
		lobbys = new Lobbys();
		lobbys.loadAll();
	}
	
	private void setupMiniGames()
	{
		miniGames = new MiniGames();
		miniGames.loadAll();
	}
	
	public Lobbys getLobbys()
	{
		return lobbys;
	}
	
	public MiniGames getMiniGames()
	{
		return miniGames;
	}
}