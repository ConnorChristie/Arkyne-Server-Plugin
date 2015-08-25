package us.arkyne.server.scoreboard;

import java.util.concurrent.Callable;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import us.arkyne.server.ArkyneMain;

public class TextScroller
{
	private String text = "";
	private String displayText = "";
	
	private int index;
	private int width;
	
	private Runnable runTask;
	private Callable<Void> onUpdate;
	
	public TextScroller(String text, int width, int interval, Callable<Void> onTextChange, Callable<Void> onUpdate)
	{
		this.text = StringUtils.repeat(" ", width + 2) + text;
		
		this.index = 0;
		this.width = width;
		
		this.onUpdate = onUpdate;
		
		runTask = () ->
		{
			try
			{
				displayText = next();
				
				onTextChange.call();
			} catch (Exception e) { }
			
			Bukkit.getScheduler().runTaskLater(ArkyneMain.getInstance(), runTask, interval);
		};
		
		Bukkit.getScheduler().runTask(ArkyneMain.getInstance(), runTask);
	}
	
	public String getText()
	{
		return displayText;
	}
	
	public void setText(String text)
	{
		this.text = StringUtils.repeat(" ", width + 2) + text;
	}
	
	public String next()
	{
		if ((text.charAt(index) == ChatColor.COLOR_CHAR) || (index - 1 >= 0 && text.charAt(index - 1) == ChatColor.COLOR_CHAR))
		{
			index++;
			
			return next();
		}
		
		String display = "";
		String beforeString = text.substring(0, index);
		
		int visibleCharLength = 0;
		
		for (int i = index; i < text.length(); i++)
		{
			if (visibleCharLength <= width)
			{
				display += text.charAt(i);
				
				if ((text.charAt(i) == ChatColor.COLOR_CHAR) || (i - 1 >= 0 && text.charAt(i - 1) == ChatColor.COLOR_CHAR))
				{
					continue;
				} else
				{
					visibleCharLength++;
				}
			}
		}
		
		if (visibleCharLength <= width)
		{
			for (int i = 0; i < text.length(); i++)
			{
				if (visibleCharLength < width)
				{
					display += text.charAt(i);
					
					if ((text.charAt(i) == ChatColor.COLOR_CHAR) || (i - 1 >= 0 && text.charAt(i - 1) == ChatColor.COLOR_CHAR))
					{
						continue;
					} else
					{
						visibleCharLength++;
					}
				}
			}
		}
		
		display = ChatColor.getLastColors(beforeString) + display;
		
		index++;
		
		if (index == text.length())
		{
			index = 0;
			
			try
			{
				onUpdate.call();
			} catch (Exception e) { }
		}
		
		return display;
	}
}