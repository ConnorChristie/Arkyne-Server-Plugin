package us.arkyne.server.scoreboard;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import us.arkyne.server.ArkyneMain;

public class ArkyneScoreboard
{
	private Scoreboard scoreboard;
	private Objective objective;
	
	private String title;
	private String[] lines;
	
	private String colorOne = ChatColor.YELLOW + "" + ChatColor.BOLD;
	private String colorTwo = ChatColor.GOLD + "" + ChatColor.BOLD;
	
	private String previousStaff = "{StaffOnline}";
	private String previousNews = "{ServerNews}";
	
	public ArkyneScoreboard(String title, String[] lines)
	{
		this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		this.objective = scoreboard.registerNewObjective(RandomStringUtils.random(13), "dummy");
		
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		setTitle(title);
		setLines(lines, true);
		
		ArkyneMain.getInstance().getScoreboardHandler().addScoreboard(this);
	}
	
	public void flashTitle(boolean on)
	{
		if (title.contains("[") && title.contains("]"))
		{
			String toBeFlashed = title.substring(title.indexOf("["), title.indexOf("]") + 1);
			String flashedTitle = title.replace(toBeFlashed, (on ? colorOne : colorTwo) + toBeFlashed.replace("[", "").replace("]", "") + ChatColor.RESET);
			
			objective.setDisplayName(flashedTitle);
		}
	}
	
	public void updateStaff(String staff)
	{
		int score = objective.getScore(previousStaff).getScore();
		
		if (score != 0)
		{
			scoreboard.resetScores(previousStaff);
			objective.getScore(staff).setScore(score);
			
			previousStaff = staff;
		}
	}
	
	public void updateNews(String news)
	{
		int score = objective.getScore(previousNews).getScore();
		
		if (score != 0)
		{
			scoreboard.resetScores(previousNews);
			objective.getScore(news).setScore(score);
			
			previousNews = news;
		}
	}
	
	public void updateLine(int i, String text)
	{
		lines[lines.length - i - 1] = text;
		
		setLines(lines, false);
	}
	
	public void setServer(String serverId)
	{
		updateLine(10, serverId);
	}
	
	public void setTitle(String title)
	{
		this.title = title;
		
		objective.setDisplayName(title);
		
		ArkyneMain.getInstance().getScoreboardHandler().updateScoreboard(this);
	}
	
	public void setLines(String[] lines, boolean reverse)
	{
		this.lines = lines;
		
		for (String score : objective.getScoreboard().getEntries())
		{
			objective.getScoreboard().resetScores(score);
		}
		
		if (reverse) ArrayUtils.reverse(lines);
		
		for (int i = 0; i < lines.length; i++)
		{
			objective.getScore(lines[i]).setScore(i + 1);
		}
		
		previousStaff = "{StaffOnline}";
		
		ArkyneMain.getInstance().getScoreboardHandler().updateScoreboard(this);
	}
	
	public Scoreboard getScoreboard()
	{
		return scoreboard;
	}
}