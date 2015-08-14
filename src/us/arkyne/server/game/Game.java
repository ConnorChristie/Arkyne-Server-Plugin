package us.arkyne.server.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
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
	
	protected Arena arena;
	protected Lobby pregameLobby;
	
	protected int maxPlayers;
	protected SignMessage signMessage;
	
	protected List<ArkynePlayer> players = new ArrayList<ArkynePlayer>();
	
	//TODO: Add player limit and signs should display: #/total Players
	
	public Game(Minigame minigame, int id, int maxPlayers, SignMessage signMessage)
	{
		this.minigame = minigame;
		this.id = id;
		
		this.maxPlayers = maxPlayers;
		this.signMessage = signMessage;
	}
	
	public void onLoad()
	{
		if (arena != null) addLoadable(arena);
		if (pregameLobby != null) addLoadable(pregameLobby);
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
	
	public void join(ArkynePlayer player)
	{
		//TODO: Join the pregame lobby
	}
	
	public void leave(ArkynePlayer player)
	{
		//TODO: Update the game sign
	}
	
	public boolean isSign(Location signLocation)
	{
		return sign.getWorld().equals(signLocation.getWorld()) && sign.distance(signLocation) < 1;
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
						.replace("{map}",     (arena != null ? arena.getMapName() : "")));
			}
			
			sign.update(true);
		}
	}
	
	
	
	//Return different values based on whether players are in arena or lobby
	
	public Type getType()
	{
		return Joinable.Type.GAME;
	}
	
	//TODO: Check if in game or not, then return either lobby inv or game inv
	public Inventory getInventory()
	{
		return pregameLobby.getInventory();
	}
	
	public Cuboid getBounds()
	{
		return pregameLobby.getBounds();
	}
	
	public Location getSpawn()
	{
		return pregameLobby.getSpawn();
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
		
		maxPlayers = Integer.parseInt(map.get("max_players").toString());
		
		UUID signWorld = map.get("sign_world") != null ? UUID.fromString(map.get("sign_world").toString()) : null;
		
		sign = map.get("sign") != null ? ((Vector) map.get("sign")).toLocation(Bukkit.getWorld(signWorld)) : null;
		signMessage = SignMessagePreset.valueOf(map.get("sign_message").toString());
	}
	
	public Map<String, Object> serialize()
	{
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("minigame", minigame.getId());
		map.put("id", id);
		
		map.put("arena", arena);
		map.put("pregame_lobby", pregameLobby);
		
		map.put("max_players", maxPlayers);
		map.put("sign_world", sign != null ? sign.getWorld().getUID().toString() : null);
		
		map.put("sign", sign != null ? sign.toVector() : null);
		map.put("sign_message", signMessage.name());
		
		return map;
	}
}