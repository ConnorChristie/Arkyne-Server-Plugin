package us.arkyne.server.minigame;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import us.arkyne.server.ArkyneMain;
import us.arkyne.server.config.LobbysConfig;
import us.arkyne.server.event.customevents.MinigameJoinEvent;
import us.arkyne.server.game.Game;
import us.arkyne.server.game.GameHandler;
import us.arkyne.server.game.arena.Arena;
import us.arkyne.server.game.arena.ArenaHandler;
import us.arkyne.server.inventory.Inventory;
import us.arkyne.server.loader.Loadable;
import us.arkyne.server.loader.Loader;
import us.arkyne.server.lobby.Lobby;
import us.arkyne.server.lobby.MinigameLobby;
import us.arkyne.server.message.SignMessage;
import us.arkyne.server.player.ArkynePlayer;
import us.arkyne.server.plugin.MinigamePlugin;
import us.arkyne.server.scoreboard.ArkyneScoreboard;
import us.arkyne.server.util.Cuboid;

public abstract class Minigame extends Loader implements Loadable, Joinable
{
	private ArkyneMain main;
	private MinigamePlugin plugin;
	
	private String name;
	private String id;
	
	private MinigameLobby lobby;
	private LobbysConfig lobbysConfig;
	
	private ArenaHandler arenaHandler;
	private GameHandler gameHandler;
	
	private List<ArkynePlayer> players = new ArrayList<ArkynePlayer>();
	
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
		lobby = (MinigameLobby) lobbysConfig.getLobby();
		
		arenaHandler = new ArenaHandler(this);
		gameHandler = new GameHandler(this);
		
		addLoadable(lobby);
		
		addLoadable(arenaHandler);
		addLoadable(gameHandler);
		
		plugin.getLogger().info("Loaded " + name + "!");
	}
	
	public void onUnload()
	{
		saveAll();
		
		plugin.getLogger().info("Unloaded " + name + "!");
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getId()
	{
		return id;
	}
	
	public String getIdString()
	{
		return id + "-L-1";
	}
	
	public Lobby getLobby()
	{
		return lobby;
	}
	
	public MinigamePlugin getPlugin()
	{
		return plugin;
	}
	
	public ArkyneMain getMain()
	{
		return main;
	}
	
	public ArenaHandler getArenaHandler()
	{
		return arenaHandler;
	}
	
	public GameHandler getGameHandler()
	{
		return gameHandler;
	}
	
	public List<ArkynePlayer> getPlayers()
	{
		return players;
	}
	
	public ArkyneScoreboard getScoreboard(ArkynePlayer player)
	{
		ArkyneScoreboard sb = ArkyneMain.getInstance().getScoreboardHandler().getDefaultScoreboard();
		
		sb.setServer(getIdString());
		
		return sb;
	}
	
	public void join(ArkynePlayer player)
	{
		join(player, null);
	}
	
	public void join(ArkynePlayer player, Callable<Void> afterTeleport)
	{
		//Just in case a minigame ever wants to cancel the join event??? Future proofing!
		
		MinigameJoinEvent event = new MinigameJoinEvent(this, player);
		Bukkit.getServer().getPluginManager().callEvent(event);
		
		if (!event.isCancelled())
		{
			players.add(player);
			
			if (lobby != null)
			{
				//Teleport to minigame lobby
				
				lobby.join(player, afterTeleport);
				player.setJoinable(this);
			}
		}
	}
	
	public void leave(ArkynePlayer player)
	{
		players.remove(player);
		
		if (lobby != null)
		{
			lobby.leave(player);
		}
	}
	
	public boolean isJoinable(ArkynePlayer player)
	{
		return lobby != null;
	}
	
	public Type getType()
	{
		return Joinable.Type.MINIGAME;
	}
	
	public Inventory getInventory(ArkynePlayer player)
	{
		if (lobby != null)
		{
			return lobby.getInventory(player);
		}
		
		return null;
	}
	
	public Cuboid getBounds()
	{
		return lobby.getBounds();
	}
	
	public Location getSpawn(ArkynePlayer player)
	{
		return lobby.getSpawn(player);
	}
	
	public abstract <T extends Game> T createGame(Arena arena);
	
	public abstract <T extends Arena> T createArena(String mapName, String worldName);
	
	public boolean setLobby(Location spawn, Cuboid cuboid, Inventory inventory, SignMessage signMessage)
	{
		if (lobby == null)
		{
			lobby = new MinigameLobby(this, getName(), 1, spawn, cuboid, inventory, signMessage);
			
			addLoadable(lobby);
			
			lobby.setIdPrefix(id);
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