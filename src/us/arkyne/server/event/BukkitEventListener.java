package us.arkyne.server.event;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.EntityType;
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
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.sk89q.worldedit.Vector;

import us.arkyne.server.ArkyneMain;
import us.arkyne.server.event.customevents.ArkynePlayerChatEvent;
import us.arkyne.server.event.customevents.InventoryItemClickEvent;
import us.arkyne.server.event.customevents.JoinSignClickEvent;
import us.arkyne.server.event.customevents.JoinSignCreateEvent;
import us.arkyne.server.game.Game;
import us.arkyne.server.game.arena.Arena;
import us.arkyne.server.inventory.item.InventoryItem;
import us.arkyne.server.minigame.Joinable;
import us.arkyne.server.player.ArkynePlayer;
import us.arkyne.server.util.Cuboid;

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
	public void onServerListPing(ServerListPingEvent event)
	{
		event.setMaxPlayers(500);
		event.setMotd(ChatColor.GREEN + "Arkyne Network " + '\u21E8' + ChatColor.YELLOW + " ArkyneMC.com\n" + ChatColor.GOLD + "" + ChatColor.BOLD + "Coming soon!");
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
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event)
	{
		ArkynePlayer player = main.getArkynePlayerHandler().getPlayer(event.getPlayer());
		
		if (player.getOnlinePlayer().isOp() && player.getOnlinePlayer().getGameMode() == GameMode.CREATIVE)
		{
			return;
		}
		
		Joinable joinable = player.getJoinable();
		
		if (joinable != null && joinable.getBounds() != null)
		{
			if (joinable instanceof Game)
			{
				if (!((Game) joinable).allowPlayerMovement())
				{
					event.getPlayer().setFoodLevel(4);
					
					return;
				}
			}
			
			Cuboid cuboid = joinable.getBounds();
			
			if (cuboid != null)
			{
				if (!cuboid.containsWithoutY(player))
				{
					//Bounce them back into the cuboid region
					
					Vector vector = cuboid.getCenter();
					
					player.pushTowards(new org.bukkit.util.Vector(vector.getX(), player.getLocation().getY(), vector.getZ()));
				} else if (player.getLocation().getY() < cuboid.getMinimumY())
				{
					//If player falls out of lobby, tp them back to spawn
					
					if (joinable.getType() != Joinable.Type.GAME && player.getOnlinePlayer().getHealth() > 0)
					{
						player.teleport(joinable.getSpawn(player));
					}
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityDamage(EntityDamageEvent event)
	{
		if (event.getEntityType() == EntityType.PLAYER)
		{
			ArkynePlayer player = main.getArkynePlayerHandler().getPlayer((Player) event.getEntity());
			
			if (player.getJoinable() instanceof Game)
			{
				if (((Game) player.getJoinable()).allowPvP())
				{
					((Game) player.getJoinable()).onPlayerDamage(player, event);
					
					event.setCancelled(false);
					
					return;
				}
			}
			
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onFoodLevelChange(FoodLevelChangeEvent event)
	{
		if (event.getEntityType() == EntityType.PLAYER)
		{
			ArkynePlayer player = main.getArkynePlayerHandler().getPlayer((Player) event.getEntity());
			
			if (player.getJoinable() instanceof Game)
			{
				if (((Game) player.getJoinable()).allowPvP() || !((Game) player.getJoinable()).allowPlayerMovement())
				{
					return;
				}
			}
			
			event.setCancelled(true);
			
			player.getOnlinePlayer().setFoodLevel(20);
			player.getOnlinePlayer().setSaturation(20);
		}
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event)
	{
		ArkynePlayer player = main.getArkynePlayerHandler().getPlayer(event.getEntity());
		
		if (player.getJoinable() != null && player.getJoinable() instanceof Game)
		{
			if (event.getEntity().getKiller() != null)
			{
				ArkynePlayer killer = main.getArkynePlayerHandler().getPlayer(event.getEntity().getKiller());
				
				((Game) player.getJoinable()).onPlayerDeath(player, killer);
				
				event.setDeathMessage(player.getTitleName() + ChatColor.GRAY + " was killed by " + killer.getTitleName());
			} else
			{
				((Game) player.getJoinable()).onPlayerDeath(player, null);
				
				event.setDeathMessage(player.getTitleName() + ChatColor.GRAY + " died");
			}
		}
	}
	
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event)
	{
		ArkynePlayer player = main.getArkynePlayerHandler().getPlayer(event.getPlayer());
		
		if (player.getJoinable() instanceof Game)
		{
			Game game = (Game) player.getJoinable();
			
			event.setRespawnLocation(game.getSpawn(player));
		} else if (player.getJoinable() != null && player.getJoinable().getSpawn(player) != null)
		{
			event.setRespawnLocation(player.getJoinable().getSpawn(player));
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
		
		Arena arena = main.getMinigameHandler().getArena(block.getBlock().getLocation());
		
		if (arena == null || (arena != null && !arena.getGame().allowEnvironmentChanges()))
		{
			if (bukkitPlayer.hasPermission("arkyne.manage"))
			{
				ArkynePlayer player = main.getArkynePlayerHandler().getPlayer(bukkitPlayer);
				
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
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		ArkynePlayer player = main.getArkynePlayerHandler().getPlayer(event.getPlayer());
		
		player.onLogin();
		
		//event.getPlayer().setFoodLevel(20);
		//event.getPlayer().setWalkSpeed(0.2F);
		//event.getPlayer().removePotionEffect(PotionEffectType.JUMP);
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event)
	{
		ArkynePlayer player = main.getArkynePlayerHandler().getPlayer(event.getPlayer());
		
		player.onLeave();
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
	*/
}