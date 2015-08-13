package us.arkyne.server.lobby;

import org.bukkit.Location;

import us.arkyne.server.ArkyneMain;
import us.arkyne.server.config.LobbysConfig;
import us.arkyne.server.inventory.InventoryPreset;
import us.arkyne.server.loader.Loader;
import us.arkyne.server.message.SignMessagePreset;
import us.arkyne.server.util.Cuboid;

public class MainLobby extends Loader
{
	private LobbysConfig lobbysConfig;
	
	private Lobby mainLobby;

	@Override
	public void onLoad()
	{
		lobbysConfig = new LobbysConfig(ArkyneMain.getInstance().getDataFolder());
		
		mainLobby = lobbysConfig.getMainLobby();
		
		addLoadable(mainLobby);
	}

	@Override
	public void onUnload()
	{
		saveAll();
	}
	
	public boolean createMainLobby(Location spawn, Cuboid cuboid)
	{
		if (mainLobby == null)
		{
			mainLobby = new Lobby("MainLobby", 1, spawn, cuboid, InventoryPreset.MAIN_LOBBY, SignMessagePreset.MAIN_LOBBY);
			
			addLoadable(mainLobby);
			saveAll();
			
			mainLobby.onLoad();
			
			return true;
		}
		
		return false;
	}
	
	public Lobby getMainLobby()
	{
		return mainLobby;
	}
	
	public void saveAll()
	{
		lobbysConfig.set("mainlobby", mainLobby);
		lobbysConfig.saveConfig();
	}
}