package us.arkyne.server.scoreboard;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Objective;

import us.arkyne.server.ArkyneMain;
import us.arkyne.server.player.ArkynePlayer;

public class ScoreboardHandler
{
	private ArkyneMain main;
	
	private int flashCount;
	private boolean flashDirection;
	
	private Runnable sbFlasher;
	
	public ScoreboardHandler()
	{
		main = ArkyneMain.getInstance();
		
		runScoreboardFlasher();
	}
	
	public void buildObjective(Objective objective, String[] lines)
	{
		for (String score : objective.getScoreboard().getEntries())
		{
			objective.getScoreboard().resetScores(score);
		}
		
		ArrayUtils.reverse(lines);
		
		for (int i = 0; i < lines.length; i++)
		{
			objective.getScore(lines[i]).setScore(i + 1);
		}
	}
	
	private void runScoreboardFlasher()
	{
		sbFlasher = () ->
		{
			if (flashCount < 6)
			{
				for (ArkynePlayer p : main.getArkynePlayerHandler().getPlayers())
				{
					if (p.isOnline())
					{
						p.flashScoreboardTitle(flashDirection);
					}
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