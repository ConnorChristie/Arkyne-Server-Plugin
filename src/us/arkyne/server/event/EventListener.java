package us.arkyne.server.event;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import us.arkyne.server.ArkyneMain;
import us.arkyne.server.event.customevents.PlayerChangeLobbyEvent;
import us.arkyne.server.lobby.Lobby;
import us.arkyne.server.player.ArkynePlayer;

public class EventListener implements Listener
{
	private ArkyneMain main;
	
	private Map<Block, Long> adminBlocks = new HashMap<Block, Long>();
	
	public EventListener()
	{
		main = ArkyneMain.getInstance();
		
		main.getServer().getPluginManager().registerEvents(this, main);
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		// If player was in game but left, try to make them go back in that game
		// Otherwise spawn them in the main lobby
		
		main.getArkynePlayers().addPlayer(event.getPlayer());
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event)
	{
		ArkynePlayer player = main.getArkynePlayers().getPlayer(event.getPlayer());
		Lobby lobby = player.getLobby();
		
		if (lobby != null)
		{
			if (!lobby.getCuboid().containsWithoutY(player))
			{
				//Bounce them back into the cuboid region
				
				player.pushTowards(lobby.getSpawn());
			} else if (player.getLocation().getY() < lobby.getCuboid().getMinimumY())
			{
				//If player falls out of lobby, tp them back to spawn
				
				player.teleport(lobby.getSpawn());
			}
		}
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK)
		{
			Block block = event.getClickedBlock();
			
			if (block.getState() instanceof Sign)
			{
				Lobby lobby = main.getLobbys().getLobby(block.getLocation());
				
				if (lobby != null)
				{
					main.getArkynePlayers().getPlayer(event.getPlayer()).changeLobby(lobby);
				}
			}
		}
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event)
	{
		ArkynePlayer player = main.getArkynePlayers().getPlayer(event.getPlayer());
		
		if (event.getPlayer().hasPermission("arkyne.manage"))
		{
			handleAdminBlocks(player, event.getBlock(), event);
		} else
		{
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event)
	{
		ArkynePlayer player = main.getArkynePlayers().getPlayer(event.getPlayer());
		
		if (event.getPlayer().hasPermission("arkyne.manage"))
		{
			handleAdminBlocks(player, event.getBlock(), event);
		} else
		{
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlayerChangeLobby(PlayerChangeLobbyEvent event)
	{
		//Update signs, change player count
		
		//Get old lobby sign, and new lobby sign
		
		Lobby fromLobby = event.getFromLobby();
		Lobby toLobby = event.getToLobby();
		
		if (fromLobby != null)
		{
			fromLobby.updateSign();
		}
		
		if (toLobby != null)
		{
			toLobby.updateSign();
		}
	}
	
	@EventHandler
	public void onSignChange(SignChangeEvent event)
	{
		if (event.getLine(0).equalsIgnoreCase("[lobby]"))
		{
			if (main.getLobbys().containsLobby(event.getLine(1)))
			{
				Lobby lobby = main.getLobbys().getLobby(event.getLine(1));
				
				lobby.setSign(event.getBlock().getLocation());
				lobby.updateSign(event);
				
				main.getLobbys().save(lobby);
			} else
			{
				event.setLine(1, ChatColor.DARK_RED + "Invalid ID");
			}
		}
	}
	
	private void handleAdminBlocks(ArkynePlayer player, Block block, Cancellable cancel)
	{
		//Ask if they really want to break or place the block
		
		if (adminBlocks.containsKey(block))
		{
			if (System.currentTimeMillis() < adminBlocks.get(block))
			{
				adminBlocks.remove(block);
			} else
			{
				player.sendMessage("Do that again if you really want to", ChatColor.RED);
				
				adminBlocks.put(block, System.currentTimeMillis() + 8 * 1000);
				
				cancel.setCancelled(true);
			}
		} else
		{
			player.sendMessage("Do that again if you really want to", ChatColor.RED);
			
			adminBlocks.put(block, System.currentTimeMillis() + 8 * 1000);
			
			cancel.setCancelled(true);
		}
	}
}