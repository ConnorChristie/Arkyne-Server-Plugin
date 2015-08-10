package us.arkyne.server.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import us.arkyne.server.util.Util;

public class Command
{
	private String label;
	
	private CommandSender sender;
	private String[] args;
	
	private boolean sentMessage;
	private boolean argLengthError;
	
	public Command(String label, CommandSender sender, String[] args)
	{
		this.label = label;
		
		this.sender = sender;
		this.args = args;
	}
	
	public CommandSender getSender()
	{
		return sender;
	}
	
	public String getArg(int arg)
	{
		return args[arg + 1];
	}
	
	public boolean wasArgLengthError()
	{
		//If it was the correct sub command but the wrong arg length
		
		return argLengthError;
	}
	
	public void sendSenderMessage(String msg)
	{
		sendSenderMessage(msg, ChatColor.AQUA);
	}
	
	public void sendSenderMessage(String msg, ChatColor color)
	{
		msg = msg.replace("{cmd}", label);
		
		Util.sendMessage(sender, msg, color);
		
		sentMessage = true;
	}
	
	public void sendSenderOptionalMessage(String msg)
	{
		if (!sentMessage)
		{
			sendSenderMessage(msg);
		}
	}
	
	public void sendSenderOptionalMessage(String msg, ChatColor color)
	{
		if (!sentMessage)
		{
			sendSenderMessage(msg, color);
		}
	}
	
	public Player getPlayer()
	{
		return isSenderPlayer() ? ((Player) sender) : null;
	}
	
	public boolean isSenderPlayer()
	{
		return sender instanceof Player;
	}
	
	public boolean isSenderPlayer(boolean sendMessageIfNot)
	{
		boolean isPlayer = sender instanceof Player;
		
		if (!isPlayer && sendMessageIfNot)
		{
			sendSenderMessage("You have to be a player to use this command");
		}
		
		return isPlayer;
	}
	
	public boolean isSubCommand(String cmd, int argLength, boolean canBeMore)
	{
		return isSubCommandMessageIfError(cmd, argLength, canBeMore);
	}
	
	public boolean isSubCommandMessageIfError(String cmd, int argLength, boolean canBeMore, String... msgs)
	{
		argLength++;
		
		if (args.length > 0)
		{
			if (args[0].equalsIgnoreCase(cmd))
			{
				if ((canBeMore && args.length >= argLength) || (!canBeMore && args.length == argLength))
				{
					return true;
				} else
				{
					// Send message
					
					argLengthError = true;
					
					if (msgs.length > 0)
					{
						for (String msg : msgs)
						{
							sendSenderMessage(msg, ChatColor.RED);
						}
					}
				}
			}
		}
		
		return false;
	}
}