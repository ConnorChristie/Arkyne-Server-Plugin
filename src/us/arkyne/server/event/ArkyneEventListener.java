package us.arkyne.server.event;

import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
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
import us.arkyne.server.lobby.Lobby;
import us.arkyne.server.minigame.Joinable;
import us.arkyne.server.minigame.Minigame;
import us.arkyne.server.player.ArkynePlayer;

public class ArkyneEventListener implements Listener
{
	private ArkyneMain main;
	
	public ArkyneEventListener()
	{
		main = ArkyneMain.getInstance();
		
		main.getServer().getPluginManager().registerEvents(this, main);
		
		new BukkitRunnable()
		{
			public void run()
			{
				for (ArkynePlayer player : main.getArkynePlayerHandler().getPlayers())
				{
					if (player.isOnline())
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
			Minigame minigame = main.getMinigameHandler().getMinigame(event.getIdName());
			
			if (minigame != null && minigame.getLobby() != null)
			{
				Lobby lobby = minigame.getLobby();
				
				lobby.setSign(event.getSignLocation());
				lobby.updateSign();
			}
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
		
		String prefix = ChatColor.translateAlternateColorCodes('&', PermissionsEx.getUser(event.getSender().getOnlinePlayer()).getPrefix());
		String message = ChatColor.translateAlternateColorCodes('&', event.getMessage());
		
		String formattedMessage = ChatColor.RED + ".:" + ChatColor.BLUE + "1" + ChatColor.RED + ":. " + ChatColor.GRAY + prefix + event.getSender().getOnlinePlayer().getName() + ": " + ChatColor.GRAY + message;
		
		event.setMessage(formattedMessage);
		
		Iterator<ArkynePlayer> recipients = event.getRecipients().iterator();
		
		while (recipients.hasNext())
		{
			ArkynePlayer recipient = recipients.next();
			
			//TODO: Send message to everyone in that players current lobby, pregame lobby or game
			if (!recipient.getOnlinePlayer().getName().equalsIgnoreCase("ChillerCraft"))
			{
				recipients.remove();
			}
		}
	}
}