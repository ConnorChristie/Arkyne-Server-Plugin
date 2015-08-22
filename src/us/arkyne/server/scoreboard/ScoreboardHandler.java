package us.arkyne.server.scoreboard;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import us.arkyne.server.ArkyneMain;
import us.arkyne.server.loader.Loader;
import us.arkyne.server.player.ArkynePlayer;

public class ScoreboardHandler extends Loader
{
	private ArkyneMain main;
	
	private String displayStaff = "";
	private String displayNews = "";
	
	private TextScroller staffScroller;
	private TextScroller newsScroller;
	
	private int flashCount;
	private boolean flashDirection;
	
	private Runnable sbFlasher;
	
	private List<ArkyneScoreboard> scoreboards = new ArrayList<ArkyneScoreboard>();
	
	public ScoreboardHandler()
	{
		main = ArkyneMain.getInstance();
	}
	
	@Override
	public void onLoad()
	{
		runScoreboardScroller();
		runScoreboardFlasher();
	}

	@Override
	public void onUnload()
	{
		
	}
	
	public ArkyneScoreboard getDefaultScoreboard()
	{
		return new ArkyneScoreboard("  [Arkyne Network!]   ", new String[] {
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
	}
	
	public void addScoreboard(ArkyneScoreboard scoreboard)
	{
		scoreboards.add(scoreboard);
	}
	
	public void updateScoreboard(ArkyneScoreboard scoreboard)
	{
		scoreboard.flashTitle(flashDirection);
		
		scoreboard.updateStaff(displayStaff);
		scoreboard.updateNews(displayNews);
	}
	
	public String getOnlineStaff()
	{
		String onlineStaff = "";
		
		List<ArkynePlayer> players = main.getArkynePlayerHandler().getAdminPlayers();
		
		for (int i = 0; i < players.size(); i++)
		{
			onlineStaff += players.get(i).getTitleName() + (i == players.size() - 1 ? " " : ChatColor.WHITE + ", ");
		}
		
		return onlineStaff.equals("") ? "None " : onlineStaff;
	}
	
	public String getNews()
	{
		String news = "This is just some news scrolling across the screen!";
		
		return news;
	}
	
	public void runScoreboardScroller()
	{
		staffScroller = new TextScroller(getOnlineStaff(), 18, 4, () ->
		{
			displayStaff = staffScroller.getText();
			
			for (ArkyneScoreboard sb : scoreboards) sb.updateStaff(displayStaff);
			
			return null;
		}, () ->
		{
			staffScroller.setText(getOnlineStaff());
			
			return null;
		});
		
		/*
		newsScroller = new TextScroller(getNews(), 18, 4, () ->
		{
			displayNews = newsScroller.getText();
			
			for (ArkyneScoreboard sb : scoreboards) sb.updateNews(displayNews);
			
			return null;
		}, () ->
		{
			newsScroller.setText(getNews());
			
			return null;
		});
		*/
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