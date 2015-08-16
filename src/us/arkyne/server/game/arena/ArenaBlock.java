package us.arkyne.server.game.arena;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.block.BlockState;
import org.bukkit.block.CommandBlock;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.block.NoteBlock;
import org.bukkit.block.Sign;
import org.bukkit.block.Skull;
import org.bukkit.inventory.InventoryHolder;

public class ArenaBlock
{
	private Location location;
	
	private Material material;
	private BlockState blockState;
	
	public ArenaBlock(Location location, BlockState blockState)
	{
		this.location = location;
		
		this.material = blockState.getType();
		this.blockState = blockState;
	}
	
	public ArenaBlock(Location location, Material material)
	{
		this.location = location;
		this.material = material;
	}
	
	public void resetBlock()
	{
		location.getBlock().setType(material);
		BlockState state = location.getBlock().getState();
		
		if (blockState != null)
		{
			state.setData(blockState.getData());
			
			if (state instanceof InventoryHolder)
			{
				resetContainer().reset((InventoryHolder) blockState, (InventoryHolder) state);
			} else if (state instanceof Sign)
			{
				resetSign().reset((Sign) state, (Sign) blockState);
			} else if (state instanceof Skull)
			{
				resetSkull().reset((Skull) state, (Skull) blockState);
			} else if (state instanceof Banner)
			{
				resetBanner().reset((Banner) state, (Banner) blockState);
			} else if (state instanceof CreatureSpawner)
			{
				resetSpawner().reset((CreatureSpawner) blockState, (CreatureSpawner) state);
			} else if (state instanceof NoteBlock)
			{
				resetNoteBlock().reset((NoteBlock) blockState, (NoteBlock) state);
			} else if (state instanceof CommandBlock)
			{
				resetCommandBlock().reset((CommandBlock) blockState, (CommandBlock) state);
			}
		}
		
		state.update(false, false);
	}
	
	private BlockStates<InventoryHolder> resetContainer()
	{
		return (oldState, newState) ->
		{
			newState.getInventory().setContents(oldState.getInventory().getContents());
		};
	}
	
	private BlockStates<Sign> resetSign()
	{
		return (oldState, newState) ->
		{
			String[] lines = oldState.getLines();
			
			for (int i = 0; i < lines.length; i++) newState.setLine(i, lines[i]);
		};
	}
	
	private BlockStates<Skull> resetSkull()
	{
		return (oldState, newState) ->
		{
			newState.setOwner(oldState.getOwner());
			newState.setRotation(oldState.getRotation());
			newState.setSkullType(oldState.getSkullType());
		};
	}
	
	private BlockStates<Banner> resetBanner()
	{
		return (oldState, newState) ->
		{
			newState.setBaseColor(oldState.getBaseColor());
			newState.setPatterns(oldState.getPatterns());
		};
	}
	
	private BlockStates<CreatureSpawner> resetSpawner()
	{
		return (oldState, newState) ->
		{
			newState.setSpawnedType(oldState.getSpawnedType());
			newState.setCreatureTypeByName(oldState.getCreatureTypeName());
			newState.setDelay(oldState.getDelay());
		};
	}
	
	private BlockStates<NoteBlock> resetNoteBlock()
	{
		return (oldState, newState) ->
		{
			newState.setNote(oldState.getNote());
		};
	}
	
	private BlockStates<CommandBlock> resetCommandBlock()
	{
		return (oldState, newState) ->
		{
			newState.setName(oldState.getName());
			newState.setCommand(oldState.getCommand());
		};
	}
	
	private interface BlockStates<T>
	{
		public void reset(T oldState, T newState);
	}
}