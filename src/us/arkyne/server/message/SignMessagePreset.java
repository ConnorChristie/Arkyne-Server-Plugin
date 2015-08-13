package us.arkyne.server.message;

import org.bukkit.ChatColor;

public enum SignMessagePreset implements SignMessage
{
	MAIN_LOBBY(
		ChatColor.DARK_BLUE +   "{lobby}",
		ChatColor.DARK_PURPLE + "{lobby-id}",
		ChatColor.DARK_GREEN +  "{count} Players",
		ChatColor.BLUE +   "Main Lobby"
	);
	
	private String[] lines;
	
	private SignMessagePreset(String... lines)
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

	@Override
	public String[] getLines()
	{
		return lines;
	}
}