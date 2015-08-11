package us.arkyne.server.util;

import org.bukkit.ChatColor;

import us.arkyne.server.ArkyneMain;

public class Util
{
	public static final String PREFIX = ChatColor.GOLD + "[Arkyne] ";
	
	public static void noticeableConsoleMessage(String message)
	{
		ArkyneMain.getInstance().getLogger().severe("------------------------------------------------------------");
		ArkyneMain.getInstance().getLogger().severe(message);
		ArkyneMain.getInstance().getLogger().severe("------------------------------------------------------------");
	}
}