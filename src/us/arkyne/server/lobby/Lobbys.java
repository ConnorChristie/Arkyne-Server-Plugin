package us.arkyne.server.lobby;

import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;

import com.sk89q.worldedit.bukkit.selections.Selection;

import us.arkyne.server.MinigameMain;
import us.arkyne.server.command.Command;
import us.arkyne.server.command.cmds.ArkyneCommand;
import us.arkyne.server.config.LobbysConfig;
import us.arkyne.server.loader.Loader;

public class Lobbys extends Loader<Lobby> implements ArkyneCommand
{
	private LobbysConfig lobbysConfig;
	
	public Lobbys(MinigameMain main)
	{
		super(main);
		
		lobbysConfig = new LobbysConfig();
		
		//Load all lobby's from the config file
		
		List<Lobby> lobbys = lobbysConfig.getLobbys();
		
		for (Lobby lobby : lobbys)
		{
			addLoadable(lobby);
		}
	}
	
	@Override
	public boolean arkyneCommand(Command command)
	{
		if (command.isSubCommandMessageIfError("createlobby", 1, false, "Usage: /{cmd} createlobby <name>"))
		{
			//Create a lobby based on the region the player selected
			
			if (command.isSenderPlayer(true))
			{
				Selection selection = getMain().getWorldEdit().getSelection(command.getPlayer());
				
				if (selection != null)
				{
					command.sendSenderMessage("Successfully created a lobby!", ChatColor.GREEN);
				} else
				{
					command.sendSenderMessage("Please select a region with worldedit first!", ChatColor.RED);
				}
			}
			
			return true;
		}
		
		//Returns true if it was the correct sub command but incorrect amount of args
		return command.wasArgLengthError();
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