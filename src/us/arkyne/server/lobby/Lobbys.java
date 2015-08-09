package us.arkyne.server.lobby;

import java.util.Map;

import us.arkyne.server.MinigameMain;
import us.arkyne.server.command.Command;
import us.arkyne.server.command.cmds.ArkyneCommand;
import us.arkyne.server.config.LobbysConfig;
import us.arkyne.server.loader.Loader;

public class Lobbys extends Loader implements ArkyneCommand
{
	private LobbysConfig lobbysConfig;
	
	public Lobbys(MinigameMain main)
	{
		super(main);
		
		lobbysConfig = new LobbysConfig();
		
		//Load all lobby's from the config file
	}
	
	@Override
	public void arkyneCommand(Command command)
	{
		
	}

	@Override
	public void onLoad()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUnload()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deserialize(Map<String, Object> map)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public Map<String, Object> serialize()
	{
		// TODO Auto-generated method stub
		return null;
	}
}