package us.arkyne.server.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Sign;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.util.Vector;

import us.arkyne.nms.screenmessage.ScreenMessageAPI;
import us.arkyne.server.ArkyneMain;
import us.arkyne.server.game.arena.Arena;
import us.arkyne.server.game.status.GameStatus;
import us.arkyne.server.game.status.GameSubStatus;
import us.arkyne.server.game.status.IGameSubStatus;
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
	protected IGameSubStatus igameSubStatus;
	
	protected Arena arena;
	protected Lobby pregameLobby;
	
	protected SignMessage signMessage;
	
	protected int maxPlayers;
	protected int startPlayers;
	
	
	/* Game variables */
	protected int timer = 0;
	protected Runnable countdownRunnable;
	
	
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
		setGameSubStatus(GameSubStatus.PREGAME_STANDBY);
		
		loadLoadables();
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
	
	public void setGameSubStatus(GameSubStatus subStatus)
	{
		this.gameSubStatus = subStatus;
		this.igameSubStatus = getGameSubStatus(subStatus);
		
		switch (subStatus)
		{
			case PREGAME_STANDBY:
			case PREGAME_COUNTDOWN:
			case GAME_REGEN:
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
		
		updateSign();
	}
	
	public void join(ArkynePlayer player)
	{
		//TODO: Join the pregame lobby
		
		players.add(player);
		
		player.setJoinable(this);
		player.teleport(pregameLobby.getSpawn(player));
		
		updateSign();
		
		if (gameSubStatus == GameSubStatus.PREGAME_STANDBY && getPlayerCount() >= startPlayers)
		{
			//Start game countdown
			
			setGameSubStatus(GameSubStatus.PREGAME_COUNTDOWN);
		}
	}
	
	public void leave(ArkynePlayer player)
	{
		//TODO: Update the game sign
		
		players.remove(player);
		
		player.setJoinableNoLeave(minigame);
		player.teleport(minigame.getSpawn(player));
		
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
			player.sendMessageRaw(color + message);
		}
	}
	
	protected void startCountdown()
	{
		int updateRate = 5;
		int secondsConversion = 20 / updateRate;
		
		timer = igameSubStatus.getDuration() * secondsConversion - 1;
		
		countdownRunnable = new Runnable()
		{
			public void run()
			{
				int seconds = timer / secondsConversion + 1;
				
				if (timer <= 0)
				{
					for (ArkynePlayer player : players)
					{
						player.getOnlinePlayer().setExp(0);
						player.getOnlinePlayer().setLevel(0);
						
						player.getOnlinePlayer().playSound(player.getLocation(), Sound.LEVEL_UP, 10, 1);
					}
					
					switch (gameSubStatus)
					{
						case PREGAME_COUNTDOWN:
							sendPlayersMessage("Teleported to game arena!", ChatColor.GREEN);
							setGameSubStatus(GameSubStatus.GAME_COUNTDOWN);
							
							filterOffline();
							onGameStart();
							
							break;
						case GAME_COUNTDOWN:
							sendPlayersMessage("The game has started!", ChatColor.GREEN);
							setGameSubStatus(GameSubStatus.GAME_PLAYING);
							
							break;
						case GAME_PLAYING:
							onGameEnd();
							
							sendPlayersMessage("The game has ended!", ChatColor.GREEN);
							setGameSubStatus(GameSubStatus.GAME_END);
							
							break;
						case GAME_END:
							setGameSubStatus(GameSubStatus.GAME_REGEN);
							
							for (ArkynePlayer player : players)
							{
								player.getOnlinePlayer().setHealth(player.getOnlinePlayer().getMaxHealth());
								player.getOnlinePlayer().setFoodLevel(20);
								player.getOnlinePlayer().setSaturation(20);
								
								player.getOnlinePlayer().setGameMode(GameMode.SURVIVAL);
								player.getOnlinePlayer().setAllowFlight(false);
								player.getOnlinePlayer().setFlying(false);
								
								player.setJoinableNoLeave(minigame);
								player.teleport(minigame.getSpawn(player));
							}
							
							players.clear();
							
							updateSign();
							regenArena();
							
							break;
						default: break;
					}
					
					onStatusChange(gameSubStatus);
				} else
				{
					boolean alertSound = false;
					boolean alertScreen = false;
					boolean alertMessage = false;
					
					if (timer != igameSubStatus.getDuration())
					{
						if ((timer + 1) % secondsConversion == 0) //One second
						{
							if (igameSubStatus.getTimeString() != null && (seconds == 30 || seconds == 60))
							{
								if (seconds % 10 == 0 || seconds <= 5)
								{
									alertMessage = true;
								}
							}
							
							if (seconds <= 5)
							{
								alertSound = true;
								
								if (igameSubStatus.getScreenString() != null && seconds <= 3)
								{
									alertScreen = true;
								}
							}
						}
					}
					
					float percent = ((float) timer) / ((float) igameSubStatus.getDuration() * secondsConversion);
					
					for (ArkynePlayer player : players)
					{
						player.getOnlinePlayer().setExp(percent);
						player.getOnlinePlayer().setLevel(seconds);
						
						if (alertMessage)
						{
							player.sendMessage(igameSubStatus.getTimeString().replace("{time}", seconds + ""), ChatColor.AQUA);
						}
						
						if (alertSound)
						{
							player.getOnlinePlayer().playSound(player.getLocation(), Sound.ORB_PICKUP, 10, 1);
						}
						
						if (alertScreen)
						{
							ScreenMessageAPI.sendTitle(player.getOnlinePlayer(), ChatColor.GREEN + "" + seconds, igameSubStatus.getScreenString(), 0, 20, 5);
						}
					}
					
					timer--;
					
					Bukkit.getScheduler().runTaskLater(getMain(), countdownRunnable, updateRate);
				}
			}
		};
		
		Bukkit.getScheduler().runTask(getMain(), countdownRunnable);
	}
	
	protected abstract void onGameStart();
	protected abstract void onGameEnd();
	
	protected abstract IGameSubStatus getGameSubStatus(GameSubStatus status);
	protected abstract void onStatusChange(GameSubStatus status);
	
	protected abstract boolean canPvP();
	protected abstract Inventory getPlayerInventory(ArkynePlayer player);
	
	public abstract void onPlayerDamage(ArkynePlayer player, EntityDamageEvent event);
	public abstract void onPlayerDeath(ArkynePlayer player, ArkynePlayer killer);
	
	public boolean allowPlayerMovement()
	{
		return gameSubStatus != GameSubStatus.GAME_COUNTDOWN;
	}
	
	public boolean allowEnvironmentChanges()
	{
		return gameSubStatus == GameSubStatus.GAME_PLAYING;
	}
	
	public boolean allowPvP()
	{
		if (gameSubStatus == GameSubStatus.GAME_PLAYING)
		{
			return canPvP();
		}
		
		return false;
	}
	
	public boolean isJoinable(ArkynePlayer player)
	{
		//TODO: Check if player is high enough rank to join past player limit!
		
		return pregameLobby != null
				&& arena != null
				&& gameStatus == GameStatus.PREGAME
				&& (gameSubStatus == GameSubStatus.PREGAME_STANDBY || gameSubStatus == GameSubStatus.PREGAME_COUNTDOWN)
				&& players.size() < maxPlayers;
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
			
			String status = getPlayerCount() + "/" + maxPlayers + " Players";
			
			if (isRegenerating())
			{
				status = ChatColor.RED + "Regenerating";
			} else if (gameSubStatus != GameSubStatus.PREGAME_STANDBY && gameSubStatus != GameSubStatus.PREGAME_COUNTDOWN)
			{
				status = ChatColor.RED + "In Progress";
			} else if (getPlayerCount() >= maxPlayers)
			{
				status = ChatColor.RED + "Game Full";
			}
			
			for (int i = 0; i < 4; i++)
			{
				sign.setLine(i, signMessage
						.replace(i, "{game}", minigame.getName())
						.replace("{game-id}", minigame.getId() + "-G-" + getId())
						.replace("{status}",  status)
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
	public Inventory getInventory(ArkynePlayer player)
	{
		return gameStatus == GameStatus.PREGAME ? pregameLobby.getInventory(player) : getPlayerInventory(player);
	}
	
	public Cuboid getBounds()
	{
		return gameStatus == GameStatus.PREGAME ? pregameLobby.getBounds() : arena.getBounds();
	}
	
	public Location getSpawn()
	{
		return getSpawn(null);
	}
	
	public Location getSpawn(ArkynePlayer player)
	{
		return gameStatus == GameStatus.PREGAME ? pregameLobby.getSpawn(player) : arena.getSpawn(player);
	}
	
	public List<ArkynePlayer> getPlayers()
	{
		return players;
	}
	
	public int getPlayerCount()
	{
		return players.size();
	}
	
	public boolean isRegenerating()
	{
		return gameSubStatus == GameSubStatus.GAME_REGEN;
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
	
	private void regenArena()
	{
		getArena().getArenaReset().resetBlocks();
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