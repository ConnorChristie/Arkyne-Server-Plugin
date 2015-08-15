package us.arkyne.server.event;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import us.arkyne.server.ArkyneMain;
import us.arkyne.server.event.customevents.ArkynePlayerChatEvent;
import us.arkyne.server.event.customevents.InventoryItemClickEvent;
import us.arkyne.server.event.customevents.JoinSignClickEvent;
import us.arkyne.server.event.customevents.JoinSignCreateEvent;
import us.arkyne.server.inventory.item.InventoryItem;
import us.arkyne.server.minigame.Joinable;
import us.arkyne.server.player.ArkynePlayer;

public class BukkitEventListener implements Listener
{
	private ArkyneMain main;
	
	private Map<Block, Long> adminBlocks = new HashMap<Block, Long>();
	
	public BukkitEventListener()
	{
		main = ArkyneMain.getInstance();
		
		main.getServer().getPluginManager().registerEvents(this, main);
	}
	
	@EventHandler
	public void onSignChange(SignChangeEvent event)
	{
		if (event.getLine(0).equalsIgnoreCase("[minigame]") || event.getLine(0).equalsIgnoreCase("[game]"))
		{
			Joinable.Type signType = event.getLine(0).equalsIgnoreCase("[minigame]") ? Joinable.Type.MINIGAME : Joinable.Type.GAME;
			
			new BukkitRunnable()
			{
				public void run()
				{
					Bukkit.getServer().getPluginManager().callEvent(new JoinSignCreateEvent(signType, event.getLines(), event.getBlock().getLocation()));
				}
			}.runTaskLater(main, 2);
		}
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		ArkynePlayer player = main.getArkynePlayerHandler().getPlayer(event.getPlayer());
		
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK)
		{
			Block block = event.getClickedBlock();
			
			if (block.getState() instanceof Sign)
			{
				Joinable joinable = main.getMinigameHandler().getJoinable(block.getLocation());
				
				if (joinable != null)
				{
					JoinSignClickEvent clickEvent = new JoinSignClickEvent(player, joinable);
					Bukkit.getServer().getPluginManager().callEvent(clickEvent);
					
					if (!clickEvent.isCancelled())
					{
						if (clickEvent.getJoinable() != null)
						{
							if (clickEvent.getJoinable().isJoinable(player))
							{
								clickEvent.getJoinable().join(player);
							} else
							{
								player.sendMessage("Sorry, but you are not able to join this game", ChatColor.RED);
							}
						}
					}
				}
			}
		} else if (event.getAction() == Action.RIGHT_CLICK_AIR)
		{
			if (player.getInventory() != null)
			{
				InventoryItem item = player.getInventory().getItem(event.getPlayer().getInventory().getHeldItemSlot());
				
				if (item != null)
				{
					InventoryItemClickEvent clickEvent = new InventoryItemClickEvent(player, item);
					Bukkit.getServer().getPluginManager().callEvent(clickEvent);
					
					if (!clickEvent.isCancelled())
					{
						if (clickEvent.getItem() != null && clickEvent.getItem().getInventoryClick() != null)
						{
							clickEvent.getItem().getInventoryClick().onClick(player);
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event)
	{
		event.setCancelled(true);
		
		ArkynePlayer player = main.getArkynePlayerHandler().addPlayer(event.getPlayer());
		
		ArkynePlayerChatEvent chatEvent = new ArkynePlayerChatEvent(player, event.getMessage(), main.getArkynePlayerHandler().getPlayers());
		Bukkit.getServer().getPluginManager().callEvent(chatEvent);
		
		if (!chatEvent.isCancelled())
		{
			for (ArkynePlayer recipient : chatEvent.getRecipients())
			{
				recipient.sendMessageRaw(chatEvent.getMessage());
			}
		}
	}
	
	// Minigames can un-cancel the event if the player is allowed to place/break the block
	@EventHandler(priority = EventPriority.LOWEST)
	public void onBlockBreak(BlockBreakEvent event)
	{
		handleAdminBlocks(event.getPlayer(), event, event);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onBlockPlace(BlockPlaceEvent event)
	{
		handleAdminBlocks(event.getPlayer(), event, event);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event)
	{
		handleAdminBlocks(event.getPlayer(), new BlockPlaceEvent(event.getBlockClicked(), null, null, null, null, false), event);
	}
	
	private void handleAdminBlocks(Player bukkitPlayer, BlockEvent block, Cancellable cancel)
	{
		//Ask if they really want to break or place the block
		
		ArkynePlayer player = main.getArkynePlayerHandler().getPlayer(bukkitPlayer);
		
		if (bukkitPlayer.hasPermission("arkyne.manage"))
		{
			if (adminBlocks.containsKey(block.getBlock()))
			{
				if (System.currentTimeMillis() < adminBlocks.get(block.getBlock()))
				{
					adminBlocks.remove(block.getBlock());
				} else
				{
					player.sendMessage("Do that again if you really want to", ChatColor.RED);
					
					adminBlocks.put(block.getBlock(), System.currentTimeMillis() + 8 * 1000);
					
					cancel.setCancelled(true);
				}
			} else
			{
				player.sendMessage("Do that again if you really want to", ChatColor.RED);
				
				adminBlocks.put(block.getBlock(), System.currentTimeMillis() + 8 * 1000);
				
				cancel.setCancelled(true);
			}
		} else
		{
			cancel.setCancelled(true);
		}
	}
	
	/*
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		// If player was in game but left, try to make them go back in that game
		// Otherwise spawn them in the main lobby
		
		ArkynePlayer player = main.getArkynePlayerHandler().addPlayer(event.getPlayer());
		
		//TODO: Check if in game, lobby... But for now, just TP them to main lobby
		
		//TODO: If player is not in game, check these
		{
			if (!player.isInLobby())
			{
				player.changeLobby(main.getLobbyHandler().getMainLobby());
			}
			
			if (player.isInLobby())
			{
				updateSigns(null, main.getArkynePlayerHandler().getPlayer(event.getPlayer()).getLobby());
				
				player.setInventory(player.getLobby().getInventory());
			}
		}
	}
	
	@EventHandler
	public void onPlayerQuit(final PlayerQuitEvent event)
	{
		//TODO: When player leave, check game status and what not
		
		new BukkitRunnable()
		{
			public void run()
			{
				//Have to update later because player hasn't actually left yet
				
				updateSigns(main.getArkynePlayerHandler().getPlayer(event.getPlayer()).getLobby(), null);
			}
		}.runTaskLater(main, 5);
	}
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event)
	{
		event.setCancelled(true);
		
		ArkynePlayer player = main.getArkynePlayerHandler().addPlayer(event.getPlayer());
		
		//TODO: Remove chat colors in messages
		
		String prefix = ChatColor.translateAlternateColorCodes('&', PermissionsEx.getUser(event.getPlayer()).getPrefix());
		String message = ChatColor.translateAlternateColorCodes('&', event.getMessage());
		
		//TODO: Check if the player is in a game, else do this
		{
			if (player.isInLobby())
			{
				for (ArkynePlayer p : player.getLobby().getPlayers())
				{
					p.sendMessageRaw(ChatColor.RED + ".:" + ChatColor.BLUE + "1" + ChatColor.RED + ":. " + ChatColor.GRAY + prefix + event.getPlayer().getName() + ": " + ChatColor.GRAY + message);
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event)
	{
		ArkynePlayer player = main.getArkynePlayerHandler().getPlayer(event.getPlayer());
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
		ArkynePlayer player = main.getArkynePlayerHandler().getPlayer(event.getPlayer());
		
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK)
		{
			Block block = event.getClickedBlock();
			
			if (block.getState() instanceof Sign)
			{
				Lobby lobby = main.getLobbyHandler().getLobby(block.getLocation());
				
				if (lobby != null)
				{
					player.changeLobby(lobby);
					
					return;
				}
			}
		} else if (event.getAction() == Action.RIGHT_CLICK_AIR)
		{
			if (player.getInventory() != null)
			{
				Item item = player.getInventory().getItem(event.getPlayer().getInventory().getHeldItemSlot());
				
				if (item != null)
				{
					item.clickItem(player);
				}
			}
		}
	}
	
	//Minigames can un-cancel the event if the player is allowed to place/break the block
	@EventHandler(priority = EventPriority.LOWEST)
	public void onBlockBreak(BlockBreakEvent event)
	{
		handleAdminBlocks(event.getPlayer(), event, event);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onBlockPlace(BlockPlaceEvent event)
	{
		handleAdminBlocks(event.getPlayer(), event, event);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event)
	{
		handleAdminBlocks(event.getPlayer(), new BlockPlaceEvent(event.getBlockClicked(), null, null, null, null, false), event);
	}
	
	@EventHandler
	public void onPlayerChangeLobby(PlayerChangeLobbyEvent event)
	{
		//Update signs, change player count
		
		//Get old lobby sign, and new lobby sign
		
		Lobby fromLobby = event.getFromLobby();
		Lobby toLobby = event.getToLobby();
		
		updateSigns(fromLobby, toLobby);
		
		if (toLobby != null)
		{
			event.getPlayer().setInventory(toLobby.getInventory());
		} else
		{
			event.getPlayer().getOnlinePlayer().getInventory().clear();
		}
	}
	
	private void updateSigns(Lobby fromLobby, Lobby toLobby)
	{
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
			if (main.getLobbyHandler().containsLobby(event.getLine(1)))
			{
				Lobby lobby = main.getLobbyHandler().getLobby(event.getLine(1));
				
				lobby.setSign(event.getBlock().getLocation());
				lobby.updateSign(event);
				
				main.getLobbyHandler().save(lobby);
			} else
			{
				event.setLine(1, ChatColor.DARK_RED + "Invalid ID");
			}
		}
	}
	
	private void handleAdminBlocks(Player bukkitPlayer, BlockEvent block, Cancellable cancel)
	{
		//Ask if they really want to break or place the block
		
		ArkynePlayer player = main.getArkynePlayerHandler().getPlayer(bukkitPlayer);
		
		if (bukkitPlayer.hasPermission("arkyne.manage"))
		{
			if (adminBlocks.containsKey(block.getBlock()))
			{
				if (System.currentTimeMillis() < adminBlocks.get(block.getBlock()))
				{
					adminBlocks.remove(block.getBlock());
				} else
				{
					player.sendMessage("Do that again if you really want to", ChatColor.RED);
					
					adminBlocks.put(block.getBlock(), System.currentTimeMillis() + 8 * 1000);
					
					cancel.setCancelled(true);
				}
			} else
			{
				player.sendMessage("Do that again if you really want to", ChatColor.RED);
				
				adminBlocks.put(block.getBlock(), System.currentTimeMillis() + 8 * 1000);
				
				cancel.setCancelled(true);
			}
		} else
		{
			cancel.setCancelled(true);
		}
	}
	*/
}