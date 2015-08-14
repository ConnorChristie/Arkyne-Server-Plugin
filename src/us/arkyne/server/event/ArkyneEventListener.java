package us.arkyne.server.event;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import ru.tehkode.permissions.bukkit.PermissionsEx;
import us.arkyne.server.ArkyneMain;
import us.arkyne.server.event.customevents.ArkynePlayerChatEvent;
import us.arkyne.server.event.customevents.InventoryItemClickEvent;
import us.arkyne.server.event.customevents.JoinSignClickEvent;
import us.arkyne.server.event.customevents.JoinSignCreateEvent;
import us.arkyne.server.event.customevents.PlayerFlyEvent;
import us.arkyne.server.game.Game;
import us.arkyne.server.lobby.Lobby;
import us.arkyne.server.minigame.Joinable;
import us.arkyne.server.minigame.Minigame;
import us.arkyne.server.player.ArkynePlayer;

public class ArkyneEventListener implements Listener
{
	private ArkyneMain main;
	
	private List<String> swearReplacements = new ArrayList<String>();
	
	public ArkyneEventListener()
	{
		main = ArkyneMain.getInstance();
		main.getServer().getPluginManager().registerEvents(this, main);
		
		swearReplacements.add("fuck");
		swearReplacements.add("shit");
		swearReplacements.add("damn");
		swearReplacements.add("god");
		
		new BukkitRunnable()
		{
			public void run()
			{
				for (ArkynePlayer player : main.getArkynePlayerHandler().getPlayers())
				{
					if (player.isOnline() && player.getOnlinePlayer().isFlying())
					{
						Bukkit.getServer().getPluginManager().callEvent(new PlayerFlyEvent(player, player.getLocation()));
					}
				}
			}
		}.runTaskTimerAsynchronously(main, 0, 2);
	}
	
	@EventHandler
	public void onPlayerFly(PlayerFlyEvent event)
	{
		ArkynePlayer player = event.getPlayer();
		
		if (player.getOnlinePlayer().getGameMode() == GameMode.SPECTATOR)
		{
			if (player.getJoinable() != null)
			{
				if (!player.getJoinable().getBounds().contains(player))
				{
					player.pushTowards(player.getJoinable().getSpawn());
				}
			}
		}
	}
	
	@EventHandler
	public void onJoinSignCreate(JoinSignCreateEvent event)
	{
		if (event.getSignType() == Joinable.Type.MINIGAME)
		{
			Minigame minigame = main.getMinigameHandler().getMinigame(event.getLine(1));
			
			if (minigame != null && minigame.getLobby() != null)
			{
				Lobby lobby = minigame.getLobby();
				
				lobby.setSign(event.getSignLocation());
				lobby.updateSign();
				
				return;
			}
		} else if (event.getSignType() == Joinable.Type.GAME)
		{
			Minigame minigame = main.getMinigameHandler().getMinigame(event.getLine(1));
			
			if (minigame != null)
			{
				Game game = minigame.getGameHandler().getGame(Integer.parseInt(event.getLine(2)));
				
				if (game != null)
				{
					game.setSign(event.getSignLocation());
					game.updateSign();
					
					return;
				}
			}
		}
		
		if (event.getSignLocation().getBlock().getState() instanceof Sign)
		{
			Sign sign = (Sign) event.getSignLocation().getBlock().getState();
			
			sign.setLine(1, ChatColor.RED + "Invalid ID");
			sign.setLine(2, ChatColor.RED + "Invalid ID");
			sign.setLine(3, ChatColor.RED + "Invalid ID");
			
			sign.update(true);
		}
	}
	
	@EventHandler
	public void onJoinSignClick(JoinSignClickEvent event)
	{
		//For other minigames to use! Future proofing!
	}
	
	@EventHandler
	public void onInventoryItemClick(InventoryItemClickEvent event)
	{
		//For other minigames to use! Future proofing!
	}
	
	@EventHandler
	public void onPlayerChat(ArkynePlayerChatEvent event)
	{
		//TODO: Remove chat colors in messages
		
		for (String swear : swearReplacements)
		{
			if (event.getMessage().toLowerCase().contains(swear))
			{
				event.setMessage(event.getMessage().replaceAll("(?i)" + swear, StringUtils.repeat('*', swear.length())));
			}
		}
		
		String prefix = ChatColor.translateAlternateColorCodes('&', PermissionsEx.getUser(event.getSender().getOnlinePlayer()).getPrefix());
		String message = ChatColor.translateAlternateColorCodes('&', event.getMessage());
		
		String formattedMessage = ChatColor.RED + ".:" + ChatColor.BLUE + "1" + ChatColor.RED + ":. " + ChatColor.GRAY + prefix + event.getSender().getOnlinePlayer().getName() + ": " + ChatColor.GRAY + message;
		
		event.setMessage(formattedMessage);
		
		Iterator<ArkynePlayer> recipients = event.getRecipients().iterator();
		
		while (recipients.hasNext())
		{
			ArkynePlayer recipient = recipients.next();
			
			//TODO: Send message to everyone in that players current lobby, pregame lobby or game
			if (recipient.isOnline() && !recipient.getOnlinePlayer().getName().equalsIgnoreCase("ChillerCraft"))
			{
				//recipients.remove();
			}
		}
	}
}