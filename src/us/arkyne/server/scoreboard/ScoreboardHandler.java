package us.arkyne.server.scoreboard;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import us.arkyne.server.ArkyneMain;
import us.arkyne.server.loader.Loader;
import us.arkyne.server.player.ArkynePlayer;

public class ScoreboardHandler extends Loader
{
	private ArkyneMain main;
	
	private String onlineStaff = "";
	private String displayStaff = "";
	
	private ArkyneScoreboard defaultScoreboard;
	
	private int flashCount;
	private boolean flashDirection;
	
	private Runnable sbFlasher;
	private Runnable staffRunnable;
	
	private List<ArkyneScoreboard> scoreboards = new ArrayList<ArkyneScoreboard>();
	
	private int index = 8;
	private int width = 16;
	
	public ScoreboardHandler()
	{
		main = ArkyneMain.getInstance();
	}
	
	@Override
	public void onLoad()
	{
		defaultScoreboard = new ArkyneScoreboard("[Arkyne Network!]", new String[] {
			ChatColor.AQUA + "" + ChatColor.BOLD + "Coins",
			"350",
			" ",
			ChatColor.RED + "" + ChatColor.BOLD + "Crystals",
			"110",
			"  ",
			ChatColor.GREEN + "" + ChatColor.BOLD + "Staff Online",
			"{StaffOnline}",
			"   ",
			ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Server",
			"My Server",
			"    ",
			ChatColor.YELLOW + "" + ChatColor.BOLD + "Website",
			"www.ArkyneMC.com"
		});
		
		updateOnlineStaff();
		
		runScoreboardScroller();
		runScoreboardFlasher();
	}

	@Override
	public void onUnload()
	{
		
	}
	
	public ArkyneScoreboard getDefaultScoreboard()
	{
		return defaultScoreboard;
	}
	
	public void addScoreboard(ArkyneScoreboard scoreboard)
	{
		scoreboards.add(scoreboard);
	}
	
	public void updateScoreboard(ArkyneScoreboard scoreboard)
	{
		scoreboard.flashTitle(flashDirection);
		scoreboard.updateStaff(displayStaff);
	}
	
	public String getOnlineStaff()
	{
		return onlineStaff;
	}
	
	public void updateOnlineStaff()
	{
		onlineStaff = "";
		
		for (ArkynePlayer p : main.getArkynePlayerHandler().getPlayers())
		{
			if (p.isOnline() && p.getOnlinePlayer().hasPermission("arkyne.manage"))
			{
				onlineStaff += p.getBracketedName() + "  ";
			}
		}
		
		onlineStaff = (onlineStaff.equals("") ? "None " : onlineStaff) + StringUtils.repeat(" ", width - 1);
	}
	
	public void runScoreboardScroller()
	{
		staffRunnable = () ->
		{
			displayStaff = "";
			
			String staffOnline = ChatColor.stripColor(onlineStaff);
			
			if (index - width / 2 < 0)
			{
				displayStaff += staffOnline.substring(staffOnline.length() + (index - width / 2), staffOnline.length());
				displayStaff += staffOnline.substring(0, index + width / 2);
			} else if (index + width / 2 > staffOnline.length())
			{
				displayStaff += staffOnline.substring(index - width / 2, staffOnline.length());
				displayStaff += staffOnline.substring(0, (index + width / 2) - staffOnline.length());
			} else
			{
				displayStaff += staffOnline.substring(index - width / 2, index + width / 2);
			}
			
			for (ArkyneScoreboard sb : scoreboards)
			{
				sb.updateStaff(displayStaff);
			}
			
			index++;
			
			if (index == staffOnline.length() - width / 2 + 2)
			{
				updateOnlineStaff();
				
				index = ChatColor.stripColor(onlineStaff).length() - width / 2 + 2;
			} else if (index >= staffOnline.length())
			{
				index = 0;
			}
			
			Bukkit.getScheduler().runTaskLater(ArkyneMain.getInstance(), staffRunnable, 4);
		};
		
		Bukkit.getScheduler().runTask(ArkyneMain.getInstance(), staffRunnable);
	}
	
	private void runScoreboardFlasher()
	{
		sbFlasher = () ->
		{
			if (flashCount < 6)
			{
				for (ArkyneScoreboard sb : scoreboards)
				{
					sb.flashTitle(flashDirection);
				}

				flashDirection = !flashDirection;
				
				Bukkit.getScheduler().runTaskLater(ArkyneMain.getInstance(), sbFlasher, 4);
				
				flashCount++;
			} else
			{
				flashCount = 0;
				
				Bukkit.getScheduler().runTaskLater(ArkyneMain.getInstance(), sbFlasher, 60);
			}
		};
		
		Bukkit.getScheduler().runTaskLater(ArkyneMain.getInstance(), sbFlasher, 5);
	}
}