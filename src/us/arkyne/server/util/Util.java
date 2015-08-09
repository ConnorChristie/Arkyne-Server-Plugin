package us.arkyne.server.util;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

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
}