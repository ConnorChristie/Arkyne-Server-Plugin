package us.arkyne.server.lobby;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Location;

import com.sk89q.worldedit.bukkit.selections.Selection;

import us.arkyne.server.MinigameMain;
import us.arkyne.server.command.Command;
import us.arkyne.server.command.cmds.ArkyneCommand;
import us.arkyne.server.config.LobbysConfig;
import us.arkyne.server.loader.Loader;

public class Lobbys extends Loader<Lobby> implements ArkyneCommand
{
	private LobbysConfig lobbysConfig;
	
	private Map<String, Lobby> lobbys = new HashMap<String, Lobby>();
	
	public Lobbys(MinigameMain main)
	{
		super(main);
		
		lobbysConfig = new LobbysConfig();
		
		//Load all lobby's from the config file
		lobbys = lobbysConfig.getLobbys();
		
		for (Lobby lobby : lobbys.values())
		{
			addLoadable(lobby);
		}
	}
	
	@Override
	public boolean arkyneCommand(Command command)
	{
		if (command.isSubCommandMessageIfError("mainlobby", 2, false, "Usage: /{cmd} mainlobby <name> <id>"))
		{
			//Create a lobby based on the region the player selected
			
			if (command.isSenderPlayer(true))
			{
				Selection selection = getMain().getWorldEdit().getSelection(command.getPlayer());
				
				if (selection != null)
				{
					//Create lobby with name, id and selected region
					
					boolean created = createLobby(command.getArg(0), command.getArg(1), selection.getMinimumPoint(), selection.getMaximumPoint());;
					
					if (created)
					{
						command.sendSenderMessage("Successfully created a lobby!", ChatColor.GREEN);
					} else
					{
						command.sendSenderMessage("That lobby id is already in use!", ChatColor.RED);
					}
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
	
	public boolean containsLobby(String id)
	{
		return lobbys.containsKey(id);
	}
	
	public Lobby getLobby(String id)
	{
		return lobbys.get(id);
	}
	
	public boolean createLobby(String name, String id, Location min, Location max)
	{
		if (!containsLobby(id))
		{
			Lobby lobby = new Lobby(name, id, min, max);
			
			//Other lobby creation stuff
			
			lobbys.put(id, lobby);
			
			saveLobbys();
			
			return true;
		}
		
		return false;
	}
	
	public void saveLobbys()
	{
		for (String id : lobbys.keySet())
		{
			lobbysConfig.set("lobbys." + id, lobbys.get(id));
		}
		
		lobbysConfig.saveConfig();
	}
}