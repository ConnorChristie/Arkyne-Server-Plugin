package us.arkyne.server.lobby;

import org.bukkit.Location;

import us.arkyne.server.ArkyneMain;
import us.arkyne.server.config.LobbysConfig;
import us.arkyne.server.loader.Loader;
import us.arkyne.server.util.Cuboid;

public class MainLobbyHandler extends Loader
{
	private LobbysConfig lobbysConfig;
	
	private MainLobby mainLobby;

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
			mainLobby = new MainLobby(spawn, cuboid);
			
			addLoadable(mainLobby);
			saveAll();
			
			mainLobby.onLoad();
			
			return true;
		}
		
		return false;
	}
	
	public MainLobby getMainLobby()
	{
		return mainLobby;
	}
	
	public void saveAll()
	{
		lobbysConfig.set("mainlobby", mainLobby);
		lobbysConfig.saveConfig();
	}
}