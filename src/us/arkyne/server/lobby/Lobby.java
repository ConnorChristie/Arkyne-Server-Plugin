package us.arkyne.server.lobby;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.CommandSender;

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
	public void arkyneCommand(CommandSender sender, String[] args)
	{
		System.out.println("Arkyne command 1!");
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