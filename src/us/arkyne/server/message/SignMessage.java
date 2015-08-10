package us.arkyne.server.message;

import org.bukkit.ChatColor;

public enum SignMessage
{
	LOBBY(
			ChatColor.DARK_BLUE +   "{lobby}",
			ChatColor.DARK_PURPLE + "{lobby-id}",
			ChatColor.DARK_GREEN +  "{count} Players",
			ChatColor.BLUE +   "Minigame Lobby"
			);
	
	private String[] lines;
	
	private SignMessage(String... lines)
	{
		this.lines = lines;
	}
	
	public String getLine(int line)
	{
		return lines[line];
	}
	
	public String replace(int line, String search, String replace)
	{
		return getLine(line).replace(search, replace);
	}
}