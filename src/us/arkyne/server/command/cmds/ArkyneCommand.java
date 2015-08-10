package us.arkyne.server.command.cmds;

import org.bukkit.ChatColor;

import com.sk89q.worldedit.bukkit.selections.Selection;

import us.arkyne.server.MinigameMain;
import us.arkyne.server.command.Command;
import us.arkyne.server.command.CommandExecutor;

public class ArkyneCommand implements CommandExecutor
{
	public static String[] commandNames = new String[] { "arkyne", "ark" };
	
	private MinigameMain main;
	
	public ArkyneCommand()
	{
		main = MinigameMain.getInstance();
	}
	
	public boolean arkyneCommand(Command command)
	{
		// TODO: Make it so the player can change a lobby later on
		
		if (command.isSubCommandMessageIfError("createlobby", 2, false, "First select the boundry's with worldedit", "Then stand where you you want the spawn point", "Then execute: /{cmd} createlobby <name> <id>"))
		{
			// Create a lobby based on the region the player selected
			
			if (command.isSenderPlayer(true))
			{
				Selection selection = main.getWorldEdit().getSelection(command.getPlayer().getBukkitPlayer());
				
				if (selection != null)
				{
					// Create lobby with name, id and selected region
					
					boolean created = main.getLobbys().createLobby(command.getArg(0), command.getArg(1), command.getPlayer().getLocation(), selection.getMinimumPoint(), selection.getMaximumPoint());;
					
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
		
		// Returns true if it was the correct sub command but incorrect amount of args
		return command.wasArgLengthError();
	}
}