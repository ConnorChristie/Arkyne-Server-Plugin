package us.arkyne.server.lobby;

import java.util.HashMap;
import java.util.Map;

import us.arkyne.server.command.Command;
import us.arkyne.server.command.cmds.ArkyneCommand;
import us.arkyne.server.loader.Loadable;

public abstract class Lobby extends Loadable implements ArkyneCommand
{
	public void load()
	{
		onLoad();
	}
	
	public void unload()
	{
		onUnload();
	}
	
	@Override
	public void arkyneCommand(Command command)
	{
		if (command.isSubCommand("lobby", 2, true) && command.isSenderPlayer(true))
		{
			if (command.hasArg("create", 2))
			{
				command.sendSenderMessage("Called the lobby create command!");
			} else
			{
				command.sendSenderOptionalMessage("Usage: /arkyne lobby <command>");
			}
		}
	}
	
	public void deserialize(Map<String, Object> map)
	{
		
	}
	
	public Map<String, Object> serialize()
	{
		Map<String, Object> map = new HashMap<String, Object>();
		
		return map;
	}
}