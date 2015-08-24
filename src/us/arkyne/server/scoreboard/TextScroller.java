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
		this.text = StringUtils.repeat(" ", width - 1) + text;
		
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
		this.text = StringUtils.repeat(" ", width - 1) + text;
	}
	
	public String next()
	{
		String retText = "";
		
		int origIndex = index;
		
		retText = getDisplay(text, index, width);
		
		/*
		if (retText.contains(ChatColor.COLOR_CHAR + ""))
		{
			index = origIndex;
			
			String adjText = getDisplay(text, origIndex, width + StringUtils.countMatches(retText, ChatColor.COLOR_CHAR + "") * 2 - 2);
			
			if (adjText.replace(retText, "").contains(ChatColor.COLOR_CHAR + ""))
			{
				index = origIndex;
				
				adjText = getDisplay(text, origIndex, width + StringUtils.countMatches(adjText.replace(retText, ""), ChatColor.COLOR_CHAR + "") * 2);
				
				if (adjText.replace(retText, "").contains(ChatColor.COLOR_CHAR + ""))
				{
					index = origIndex;
					
					adjText = getDisplay(text, origIndex, width + StringUtils.countMatches(adjText.replace(retText, ""), ChatColor.COLOR_CHAR + "") * 2);
					
					if (adjText.replace(retText, "").contains(ChatColor.COLOR_CHAR + ""))
					{
						index = origIndex;
						
						adjText = getDisplay(text, origIndex, width + StringUtils.countMatches(adjText.replace(retText, ""), ChatColor.COLOR_CHAR + "") * 2);
						
						if (adjText.replace(retText, "").contains(ChatColor.COLOR_CHAR + ""))
						{
							System.out.println("Should have more...");
						} else
						{
							retText = adjText;
						}
					} else
					{
						retText = adjText;
					}
				} else
				{
					retText = adjText;
				}
			} else
			{
				retText = adjText;
			}
		}
		*/
		
		if (retText.contains(ChatColor.COLOR_CHAR + ""))
		{
			index = origIndex;
			
			retText = getDisplay(text, origIndex, width + StringUtils.countMatches(retText, ChatColor.COLOR_CHAR + "") * 2 - 2);
			
			if (retText.contains(ChatColor.COLOR_CHAR + ""))
			{
				index = origIndex;
				
				retText = getDisplay(text, origIndex, width + StringUtils.countMatches(retText, ChatColor.COLOR_CHAR + "") * 2 - 2);
				
				if (retText.contains(ChatColor.COLOR_CHAR + ""))
				{
					index = origIndex;
					
					retText = getDisplay(text, origIndex, width + StringUtils.countMatches(retText, ChatColor.COLOR_CHAR + "") * 2 - 2);
					
					if (retText.contains(ChatColor.COLOR_CHAR + ""))
					{
						index = origIndex;
						
						retText = getDisplay(text, origIndex, width + StringUtils.countMatches(retText, ChatColor.COLOR_CHAR + "") * 2 - 2);
						
						if (retText.contains(ChatColor.COLOR_CHAR + ""))
						{
							index = origIndex;
							
							retText = getDisplay(text, origIndex, width + StringUtils.countMatches(retText, ChatColor.COLOR_CHAR + "") * 2 - 2);
							
							if (retText.contains(ChatColor.COLOR_CHAR + ""))
							{
								index = origIndex;
								
								retText = getDisplay(text, origIndex, width + StringUtils.countMatches(retText, ChatColor.COLOR_CHAR + "") * 2 - 2);
								
								if (retText.contains(ChatColor.COLOR_CHAR + ""))
								{
									index = origIndex;
									
									retText = getDisplay(text, origIndex, width + StringUtils.countMatches(retText, ChatColor.COLOR_CHAR + "") * 2 - 2);
								}
							}
						}
					}
				}
			}
		}
		
		int beginIndex = index;
		
		if (!retText.startsWith(ChatColor.COLOR_CHAR + ""))
		{
			String colors = "";
			
			for (int i = beginIndex; i >= 0; i--)
			{
				if (text.charAt(i) == ChatColor.COLOR_CHAR && isColor(text.charAt(i + 1)))
				{
					retText = text.substring(i, i + 2) + colors + retText;
					
					break;
				} else if (text.charAt(i) == ChatColor.COLOR_CHAR)
				{
					colors = text.charAt(i) + "" + text.charAt(i + 1) + colors;
				}
			}
		}
		
		if (retText.endsWith(ChatColor.COLOR_CHAR + ""))
		{
			retText = retText.substring(0, retText.length() - 1);
		}
		
		index++;
		
		if (index == 2)
		{
			try
			{
				onUpdate.call();
			} catch (Exception e) { }
		} else if (index >= text.length())
		{
			index = 0;
		}
		
		return retText;
	}
	
	private String getDisplay(String whole, int index, int width)
	{
		int beginIndex = index;
		int endIndex = index + width;
		
		String ret = getWindow(whole, beginIndex, endIndex);
		
		if (ret.startsWith(ChatColor.COLOR_CHAR + "") || ((beginIndex - 1) >= 0 && whole.charAt(beginIndex - 1) == ChatColor.COLOR_CHAR)
				|| ret.endsWith(ChatColor.COLOR_CHAR + "") || ((endIndex - 1) < whole.length() && whole.charAt(endIndex - 1) == ChatColor.COLOR_CHAR))
		{
			this.index++;
			
			return getDisplay(whole, this.index, width);
		}
		
		return ret;
	}
	
	private String getWindow(String whole, int beginIndex, int endIndex)
	{
		String ret = "";
		
		if (beginIndex < 0)
		{
			ret += whole.substring(whole.length() + beginIndex, whole.length());
			
			ret += whole.substring(0, endIndex);
		} else if (endIndex > whole.length())
		{
			ret += whole.substring(beginIndex, whole.length());
			
			ret += whole.substring(0, endIndex - whole.length());
		} else
		{
			ret += whole.substring(beginIndex, endIndex);
		}
		
		return ret;
	}
	
	private boolean isColor(char code)
	{
		return ChatColor.getByChar(code).isColor();
	}
}