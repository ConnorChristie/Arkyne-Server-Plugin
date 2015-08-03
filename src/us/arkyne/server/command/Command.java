package us.arkyne.server.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import us.arkyne.server.util.Util;

public class Command
{
	private CommandSender sender;
	private String[] args;
	
	private boolean sentMessage;
	
	public Command(CommandSender sender, String[] args)
	{
		this.sender = sender;
		this.args = args;
	}
	
	public CommandSender getSender()
	{
		return sender;
	}
	
	public String[] getArgs()
	{
		return args;
	}
	
	public void sendSenderMessage(String msg)
	{
		Util.sendMessage(sender, msg);
		
		sentMessage = true;
	}
	
	public void sendSenderMessage(String msg, ChatColor color)
	{
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
		if ((canBeMore && args.length >= argLength) || (!canBeMore && args.length == argLength))
		{
			if (args[0].equalsIgnoreCase(cmd))
			{
				return true;
			}
		}
		
		return false;
	}
	
	public boolean hasArg(String arg, int argPos)
	{
		if (argPos <= args.length)
		{
			return args[argPos - 1].equalsIgnoreCase(arg);
		}
		
		return false;
	}
}