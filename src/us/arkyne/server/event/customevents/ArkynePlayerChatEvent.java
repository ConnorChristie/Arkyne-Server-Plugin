package us.arkyne.server.event.customevents;

import java.util.List;

import org.bukkit.event.Cancellable;

import us.arkyne.server.player.ArkynePlayer;

public class ArkynePlayerChatEvent extends ArkyneEvent implements Cancellable
{
	private ArkynePlayer sender;
	
	private String message;
	private List<ArkynePlayer> recipients;
	
	public ArkynePlayerChatEvent(ArkynePlayer sender, String message, List<ArkynePlayer> recipients)
	{
		this.sender = sender;
		
		this.message = message;
		this.recipients = recipients;
	}
	
	public ArkynePlayer getSender()
	{
		return sender;
	}
	
	public void setMessage(String message)
	{
		this.message = message;
	}

	public String getMessage()
	{
		return message;
	}
	
	public void setRecipients(List<ArkynePlayer> recipients)
	{
		this.recipients = recipients;
	}

	public List<ArkynePlayer> getRecipients()
	{
		return recipients;
	}

	@Override
	public boolean isCancelled()
	{
		return isCancelled;
	}

	@Override
	public void setCancelled(boolean isCancelled)
	{
		this.isCancelled = isCancelled;
	}
}