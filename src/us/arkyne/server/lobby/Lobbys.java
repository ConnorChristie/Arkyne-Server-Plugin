package us.arkyne.server.lobby;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.sk89q.worldedit.bukkit.selections.Selection;

import us.arkyne.server.Main;
import us.arkyne.server.command.Command;
import us.arkyne.server.command.cmds.ArkyneCommand;
import us.arkyne.server.loader.Loader;

public class Lobbys extends Loader implements ArkyneCommand
{
	public Lobbys(Main main)
	{
		super(main);
		
		Bukkit.getMessenger().registerOutgoingPluginChannel(main, "ArkyneServerManager");
	}
	
	@Override
	public void arkyneCommand(Command command)
	{
		if (command.isSubCommand("lobby", 1, false) && command.isSenderPlayer(true))
		{
			String cmd = "changeserver " + command.getPlayer().getName() + " battle";
			
			ByteArrayDataOutput out = ByteStreams.newDataOutput();
			out.writeUTF(cmd);
			
			command.getPlayer().sendPluginMessage(Main.getInstance(), "ArkyneServerManager", out.toByteArray());
		} else if (command.isSubCommand("lobby", 2, true) && command.isSenderPlayer(true))
		{
			if (command.hasArg("create", 2))
			{
				Selection selection = getMain().getWorldEdit().getSelection(command.getPlayer());
				
				if (selection != null)
				{
					
					command.sendSenderMessage("Successfully created a lobby!", ChatColor.GREEN);
				} else
					command.sendSenderMessage("You need to select a lobby region with worldedit first!", ChatColor.RED);
			} else
				command.sendSenderOptionalMessage("Usage: /arkyne lobby <command>", ChatColor.RED);
		}
	}
}