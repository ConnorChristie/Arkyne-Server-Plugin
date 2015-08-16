package us.arkyne.server.game.arena;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

import us.arkyne.server.ArkyneMain;
import us.arkyne.server.game.Game;
import us.arkyne.server.game.status.GameSubStatus;

public class ArenaReset implements Runnable
{
	private Game game;
	
	private Map<Location, ArenaBlock> changedBlocks = new HashMap<Location, ArenaBlock>();
	
	public ArenaReset(Game game)
	{
		this.game = game;
	}
	
	public void addBlock(Block block)
	{
		if (changedBlocks.containsKey(block.getLocation())) return;
		
		changedBlocks.put(block.getLocation(), new ArenaBlock(block.getLocation(), block.getState()));
	}
	
	public void addBlock(Location location, BlockState blockState)
	{
		if (changedBlocks.containsKey(location)) return;
		
		changedBlocks.put(location, new ArenaBlock(location, blockState));
	}
	
	public void addBlock(Location location, Material material)
	{
		if (changedBlocks.containsKey(location)) return;
		
		changedBlocks.put(location, new ArenaBlock(location, material));
	}
	
	public void resetBlocks()
	{
		Bukkit.getScheduler().runTask(ArkyneMain.getInstance(), this);
	}
	
	public void run()
	{
		int rolledBack = 0;
		
		Iterator<Map.Entry<Location, ArenaBlock>> blocks = changedBlocks.entrySet().iterator();
		
		while (blocks.hasNext() && rolledBack < 70)
		{
			blocks.next().getValue().resetBlock();
			blocks.remove();
			
			rolledBack++;
		}
		
		if (changedBlocks.size() != 0)
		{
			Bukkit.getScheduler().runTaskLater(ArkyneMain.getInstance(), this, 2);
		} else
		{
			//Yay all done!
			
			game.setGameSubStatus(GameSubStatus.PREGAME_STANDBY);
		}
	}
}