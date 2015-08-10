package us.arkyne.server.util;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import us.arkyne.server.ArkyneMain;

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
			ArkyneMain.getInstance().getLogger().info(ChatColor.stripColor(message));
		} else if (to instanceof Player)
		{
			to.sendMessage(PREFIX + msgColor + message);
		}
	}
	
	public static void noticeableConsoleMessage(String message)
	{
		ArkyneMain.getInstance().getLogger().severe("------------------------------------------------------------");
		ArkyneMain.getInstance().getLogger().severe(message);
		ArkyneMain.getInstance().getLogger().severe("------------------------------------------------------------");
	}
}