package us.arkyne.server.util;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import us.arkyne.server.MinigameMain;

public class Util
{
	private static final String PREFIX = ChatColor.GOLD + "[Arkyne] ";
	
	public static void sendMessage(CommandSender to, String message)
	{
		sendMessage(to, message, ChatColor.AQUA);
	}
	
	public static void sendMessage(CommandSender to, String message, ChatColor msgColor)
	{
		if (to instanceof ConsoleCommandSender)
		{
			MinigameMain.getInstance().getLogger().info(ChatColor.stripColor(message));
		} else if (to instanceof Player)
		{
			to.sendMessage(PREFIX + msgColor + message);
		}
	}
	
	public static void noticeableConsoleMessage(String message)
	{
		MinigameMain.getInstance().getLogger().severe("------------------------------------------------------------");
		MinigameMain.getInstance().getLogger().severe(message);
		MinigameMain.getInstance().getLogger().severe("------------------------------------------------------------");
	}
	
	public static void teleportPlayerRaw(Player player, Location location)
	{
		player.teleport(location, TeleportCause.PLUGIN);
		
		player.setFallDistance(-1F);
		player.setVelocity(new Vector(0, 0, 0));
		
		player.setFireTicks(0);
		player.setHealth(player.getMaxHealth());
	}
	
	public static void teleportPlayer(final Player player, final Location location)
	{
		new BukkitRunnable()
		{
			public void run()
			{
				location.getChunk().load(false);
				
				while (!location.getChunk().isLoaded()) { }
				
				//Loaded chunk, most likely it is already loaded!
				
				teleportPlayerRaw(player, location);
			}
		}.runTaskAsynchronously(MinigameMain.getInstance());
	}
}