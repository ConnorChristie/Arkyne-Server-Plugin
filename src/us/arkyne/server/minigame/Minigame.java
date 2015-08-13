package us.arkyne.server.minigame;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import us.arkyne.server.ArkyneMain;
import us.arkyne.server.config.LobbysConfig;
import us.arkyne.server.event.customevents.MinigameJoinEvent;
import us.arkyne.server.game.GameHandler;
import us.arkyne.server.inventory.Inventory;
import us.arkyne.server.loader.Loadable;
import us.arkyne.server.loader.Loader;
import us.arkyne.server.lobby.Lobby;
import us.arkyne.server.message.SignMessage;
import us.arkyne.server.player.ArkynePlayer;
import us.arkyne.server.plugin.MinigamePlugin;
import us.arkyne.server.util.Cuboid;

public abstract class Minigame extends Loader implements Loadable
{
	private ArkyneMain main;
	private MinigamePlugin plugin;
	
	private String name;
	private String id;
	
	private LobbysConfig lobbysConfig;
	
	private Lobby lobby;
	private GameHandler gameHandler;
	
	private List<ArkynePlayer> players = new ArrayList<ArkynePlayer>();
	
	//TODO: Add signs! Click sign to goto minigame lobby, sign to goto pregame lobby
	
	public Minigame(MinigamePlugin plugin, String name, String id)
	{
		main = ArkyneMain.getInstance();
		
		this.plugin = plugin;
		
		this.name = name;
		this.id = id;
	}
	
	public void onLoad()
	{
		lobbysConfig = new LobbysConfig(plugin.getDataFolder());
		lobby = lobbysConfig.getLobby();
		
		gameHandler = new GameHandler(this);
		
		addLoadable(lobby);
		addLoadable(gameHandler);
		
		plugin.getLogger().info("Loaded " + name + " and " + gameHandler.getGameCount() + " games!");
	}
	
	public void onUnload()
	{
		saveAll();
		
		plugin.getLogger().info("Unloaded " + name + " and " + gameHandler.getGameCount() + " games!");
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getId()
	{
		return id;
	}
	
	public MinigamePlugin getPlugin()
	{
		return plugin;
	}
	
	public ArkyneMain getMain()
	{
		return main;
	}
	
	public GameHandler getGameHandler()
	{
		return gameHandler;
	}
	
	public List<ArkynePlayer> getPlayers()
	{
		return players;
	}
	
	public void addPlayer(ArkynePlayer player)
	{
		player.setMinigame(this);
		players.add(player);
	}
	
	public boolean joinMinigame(ArkynePlayer player)
	{
		//Just in case a minigame ever wants to cancel the join event??? Future proofing!
		MinigameJoinEvent event = new MinigameJoinEvent(this, player);
		Bukkit.getServer().getPluginManager().callEvent(event);
		
		if (!event.isCancelled())
		{
			addPlayer(player);
			
			if (lobby != null)
			{
				//Teleport to minigame lobby
				
				lobby.joinLobby(player);
				
				return true;
			}
		}
		
		return false;
	}
	
	public abstract int createGame();
	
	public boolean setLobby(Location spawn, Cuboid cuboid, Inventory inventory, SignMessage signMessage)
	{
		if (lobby == null)
		{
			lobby = new Lobby(getName(), 1, spawn, cuboid, inventory, signMessage);
			
			addLoadable(lobby);
			lobby.onLoad();
			
			saveLobby();
			
			return true;
		}
		
		return false;
	}
	
	public void saveLobby()
	{
		lobbysConfig.set("lobby", lobby);
		lobbysConfig.saveConfig();
	}
	
	public void saveAll()
	{
		lobbysConfig.set("lobby", lobby);
		lobbysConfig.saveConfig();
		
		gameHandler.saveAll();
	}
}