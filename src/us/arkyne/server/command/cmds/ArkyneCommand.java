package us.arkyne.server.command.cmds;

import org.bukkit.ChatColor;
import org.bukkit.World;

import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import com.sk89q.worldedit.bukkit.selections.Selection;

import us.arkyne.server.ArkyneMain;
import us.arkyne.server.command.Command;
import us.arkyne.server.command.CommandExecutor;
import us.arkyne.server.util.Cuboid;

public class ArkyneCommand implements CommandExecutor
{
	public static String[] commandNames = new String[] { "arkyne", "ark" };
	
	private ArkyneMain main;
	
	public ArkyneCommand()
	{
		main = ArkyneMain.getInstance();
	}
	
	public boolean arkyneCommand(Command command)
	{
		// TODO: Make it so the player can change a lobby later on
		
		if (command.isSubCommandMessageIfError("createlobby", 2, false, "First select the boundry's with worldedit", "Then stand where you you want the spawn point to be", "Then execute: /{cmd} createlobby <name> <id>"))
		{
			// Create a lobby based on the region the player selected
			
			if (command.isSenderPlayer(true))
			{
				createLobby(command, false);
			}
			
			return true;
		} else if (command.isSubCommandMessageIfError("mainlobby", 0, false, "First select the boundry's with worldedit", "Then stand where you you want the spawn point to be", "Then execute: /{cmd} mainlobby"))
		{
			if (command.isSenderPlayer(true))
			{
				createLobby(command, true);
			}
			
			return true;
		} else if (command.isSubCommandMessageIfError("unload", 1, false, "Usage: /{cmd} unload <name|id>"))
		{
			main.getMinigameHandler().unloadMinigamePlugin(main.getMinigameHandler().getMinigame(command.getArg(0)));
			
			return true;
		} else if (command.isSubCommandMessageIfError("load", 1, false, "Usage: /{cmd} load <name|id>"))
		{
			main.getMinigameHandler().loadMinigamePlugin(command.getArg(0));
			
			return true;
		} else if (command.isSubCommandMessageIfError("reload", 1, false, "Usage: /{cmd} reload <name|id>"))
		{
			main.getMinigameHandler().reloadMinigamePlugin(main.getMinigameHandler().getMinigame(command.getArg(0)));
			
			return true;
		}
		
		// Returns true if it was the correct sub command but incorrect amount of args
		return command.wasArgLengthError();
	}
	
	private void createLobby(Command command, boolean mainLobby)
	{
		Selection selection = main.getWorldEdit().getSelection(command.getPlayer().getOnlinePlayer());
		
		if (selection != null && selection instanceof CuboidSelection)
		{
			// Create lobby with name, id and selected region
			
			Cuboid cuboid = new Cuboid((World) selection.getWorld(), selection.getNativeMinimumPoint(), selection.getNativeMaximumPoint());
			
			boolean created = false;
			
			if (mainLobby)
			{
				created = main.getLobbyHandler().createMainLobby(command.getPlayer().getLocation(), cuboid);
			} else
			{
				created = main.getLobbyHandler().createLobby(command.getArg(0), command.getArg(1), command.getPlayer().getLocation(), cuboid);
			}
			
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
}