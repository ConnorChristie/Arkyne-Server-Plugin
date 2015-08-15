package us.arkyne.server.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import us.arkyne.server.ArkyneMain;
import us.arkyne.server.inventory.Inventory;
import us.arkyne.server.loader.Loadable;
import us.arkyne.server.loader.Loader;
import us.arkyne.server.lobby.Lobby;
import us.arkyne.server.lobby.PregameLobby;
import us.arkyne.server.message.SignMessage;
import us.arkyne.server.message.SignMessagePreset;
import us.arkyne.server.minigame.Joinable;
import us.arkyne.server.minigame.Minigame;
import us.arkyne.server.player.ArkynePlayer;
import us.arkyne.server.util.Cuboid;

public abstract class Game extends Loader implements Loadable, Joinable, ConfigurationSerializable
{
	protected int id;
	
	protected Minigame minigame;
	protected Location sign;
	
	protected String mapName;
	
	protected GameStatus gameStatus;
	protected GameSubStatus gameSubStatus;
	
	protected Arena arena;
	protected Lobby pregameLobby;
	
	protected SignMessage signMessage;
	
	protected int maxPlayers;
	protected int startPlayers;
	
	
	/* Game variables */
	protected int timer = 0;
	protected BukkitRunnable countdownTask;
	
	
	protected List<ArkynePlayer> players = new ArrayList<ArkynePlayer>();
	
	//TODO: Add player limit and signs should display: #/total Players
	
	public Game(Minigame minigame, int id, int maxPlayers, int startPlayers, String mapName, SignMessage signMessage)
	{
		this.minigame = minigame;
		this.id = id;
		
		this.maxPlayers = maxPlayers;
		this.startPlayers = startPlayers;
		
		this.mapName = mapName;
		this.signMessage = signMessage;
	}
	
	public void onLoad()
	{
		if (arena != null)
		{
			arena.setGame(this);
			
			addLoadable(arena);
		}
		
		if (pregameLobby != null) addLoadable(pregameLobby);
		
		gameStatus = GameStatus.PREGAME;
		gameSubStatus = GameSubStatus.PREGAME_STANDBY;
	}
	
	public void onUnload()
	{
		
	}
	
	public int getId()
	{
		return id;
	}
	
	public String getIdString()
	{
		return minigame.getId() + "-" + id;
	}
	
	public Arena getArena()
	{
		return arena;
	}
	
	public Lobby getPregameLobby()
	{
		return pregameLobby;
	}
	
	public void setSign(Location sign)
	{
		this.sign = sign;
	}
	
	public Location getSign()
	{
		return sign;
	}
	
	public String getMapName()
	{
		return mapName;
	}
	
	protected void setGameSubStatus(GameSubStatus subStatus)
	{
		this.gameSubStatus = subStatus;
		
		switch (subStatus)
		{
			case PREGAME_STANDBY:
			case PREGAME_COUNTDOWN:
				gameStatus = GameStatus.PREGAME;
				
				break;
			case GAME_COUNTDOWN:
			case GAME_PLAYING:
			case GAME_END:
				gameStatus = GameStatus.GAME;
				
				break;
		}
		
		switch (subStatus)
		{
			case PREGAME_COUNTDOWN:
			case GAME_COUNTDOWN:
			case GAME_PLAYING:
			case GAME_END:
				startCountdown();
				
				break;
			default: break;
		}
	}
	
	public void join(ArkynePlayer player)
	{
		//TODO: Join the pregame lobby
		
		players.add(player);
		
		player.setJoinable(this);
		player.teleport(pregameLobby.getSpawn());
		
		updateSign();
		
		if (gameSubStatus == GameSubStatus.PREGAME_STANDBY && getPlayerCount() >= startPlayers)
		{
			//Start game countdown
			
			filterOffline();
			setGameSubStatus(GameSubStatus.PREGAME_COUNTDOWN);
		}
	}
	
	public void leave(ArkynePlayer player)
	{
		//TODO: Update the game sign
		
		players.remove(player);
		
		player.setJoinableNoLeave(minigame);
		player.teleport(minigame.getSpawn());
		
		updateSign();
	}
	
	private void filterOffline()
	{
		Iterator<ArkynePlayer> playerIterator = players.iterator();
		
		while (playerIterator.hasNext())
		{
			ArkynePlayer player = playerIterator.next();
			
			if (!player.isOnline())
			{
				player.setJoinable(null);
				
				playerIterator.remove();
			}
		}
	}
	
	public void sendPlayersMessage(String message, ChatColor color)
	{
		for (ArkynePlayer player : players)
		{
			player.sendMessage(message, color);
		}
	}
	
	protected void startCountdown()
	{
		timer = gameSubStatus.getDuration();
		
		countdownTask = new BukkitRunnable()
		{
			public void run()
			{
				if (timer <= 0)
				{
					//Add methods for all these!
					
					switch (gameSubStatus)
					{
						case PREGAME_COUNTDOWN:
							spawnPlayers();
							setGameSubStatus(GameSubStatus.GAME_COUNTDOWN);
							
							break;
						case GAME_COUNTDOWN:
							sendPlayersMessage("The game has started!", ChatColor.GREEN);
							setGameSubStatus(GameSubStatus.GAME_PLAYING);
							
							break;
						case GAME_PLAYING:
							sendPlayersMessage("The game has ended!", ChatColor.GREEN);
							setGameSubStatus(GameSubStatus.GAME_END);
							
							break;
						case GAME_END:
							
							
							break;
					}
					
					countdownTask.cancel();
				} else
				{
					timer--;
				}
			}
		};
		
		countdownTask.runTaskTimer(getMain(), 0, 20);
	}
	
	protected abstract void spawnPlayers();
	
	public boolean isJoinable(ArkynePlayer player)
	{
		//TODO: Check if player is high enough rank to join past player limit!
		
		return (pregameLobby != null && arena != null) && (gameStatus == GameStatus.PREGAME && players.size() < maxPlayers);
	}
	
	public boolean isSign(Location signLocation)
	{
		return sign != null && sign.getWorld().equals(signLocation.getWorld()) && sign.distance(signLocation) < 1;
	}
	
	public void updateSign()
	{
		if (this.sign != null && this.sign.getBlock().getState() instanceof Sign)
		{
			Sign sign = (Sign) this.sign.getBlock().getState();
			
			for (int i = 0; i < 4; i++)
			{
				sign.setLine(i, signMessage
						.replace(i, "{game}", minigame.getName())
						.replace("{game-id}", minigame.getId() + "-G-" + getId())
						.replace("{count}",   getPlayerCount() + "")
						.replace("{max}",     maxPlayers + "")
						.replace("{map}",     (arena != null ? mapName : "")));
			}
			
			sign.update(true);
		}
	}
	
	public Type getType()
	{
		return Joinable.Type.GAME;
	}
	
	
	
	//Return different values based on whether players are in arena or lobby
	
	//TODO: Check if in game or not, then return either lobby inv or game inv
	public Inventory getInventory()
	{
		return gameStatus == GameStatus.PREGAME ? pregameLobby.getInventory() : arena.getInventory();
	}
	
	public Cuboid getBounds()
	{
		return gameStatus == GameStatus.PREGAME ? pregameLobby.getBounds() : arena.getBounds();
	}
	
	public Location getSpawn()
	{
		return getSpawn(null);
	}
	
	public Location getSpawn(String team)
	{
		return gameStatus == GameStatus.PREGAME ? pregameLobby.getSpawn() : arena.getSpawn(team);
	}
	
	
	
	
	
	public List<ArkynePlayer> getPlayers()
	{
		return players;
	}
	
	public int getPlayerCount()
	{
		return players.size();
	}
	
	public boolean createPregameLobby(Location spawn, Cuboid cuboid, Inventory inventory, SignMessage signMessage)
	{
		if (pregameLobby == null)
		{
			pregameLobby = new PregameLobby(minigame, minigame.getName() + "-G", id, spawn, cuboid, inventory, signMessage);
			
			addLoadable(pregameLobby);
			pregameLobby.onLoad();
			
			save();
			
			return true;
		}
		
		return false;
	}
	
	public boolean createArena(Arena arena)
	{
		if (this.arena == null)
		{
			this.arena = arena;
			
			addLoadable(this.arena);
			this.arena.onLoad();
			
			save();
			
			return true;
		}
		
		return false;
	}
	
	public void save()
	{
		minigame.getGameHandler().save(this);
	}
	
	public Game(Map<String, Object> map)
	{
		minigame = ArkyneMain.getInstance().getMinigameHandler().getMinigame(map.get("minigame").toString());
		id = (Integer) map.get("id");
		
		arena = (Arena) map.get("arena");
		pregameLobby = (Lobby) map.get("pregame_lobby");
		
		mapName = map.get("map_name").toString();
		
		maxPlayers = Integer.parseInt(map.get("max_players").toString());
		startPlayers = Integer.parseInt(map.get("start_players").toString());
		
		UUID signWorld = map.get("sign_world") != null ? UUID.fromString(map.get("sign_world").toString()) : null;
		
		sign = map.get("sign") != null ? ((Vector) map.get("sign")).toLocation(Bukkit.getWorld(signWorld)) : null;
		signMessage = SignMessagePreset.valueOf(map.get("sign_message").toString());
	}
	
	public Map<String, Object> serialize()
	{
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("minigame", minigame.getId());
		map.put("id", id);
		
		map.put("max_players", maxPlayers);
		map.put("start_players", startPlayers);
		
		map.put("arena", arena);
		map.put("pregame_lobby", pregameLobby);
		
		map.put("map_name", mapName);
		map.put("sign_world", sign != null ? sign.getWorld().getUID().toString() : null);
		
		map.put("sign", sign != null ? sign.toVector() : null);
		map.put("sign_message", signMessage.name());
		
		return map;
	}
}