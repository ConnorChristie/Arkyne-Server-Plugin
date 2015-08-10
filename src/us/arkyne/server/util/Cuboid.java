package us.arkyne.server.util;

import org.bukkit.Location;
import org.bukkit.World;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.regions.CuboidRegion;

import us.arkyne.server.player.ArkynePlayer;

public class Cuboid extends CuboidRegion
{
	public Cuboid(World world, Vector min, Vector max)
	{
		super((com.sk89q.worldedit.world.World) new BukkitWorld(world), min, max);
	}
	
	public boolean containsWithoutY(ArkynePlayer player)
	{
		Location pLoc = player.getLocation();
		
		if (((BukkitWorld) getWorld()).getWorld() == pLoc.getWorld())
		{
			if (pLoc.getX() <= getMaximumPoint().getX() && pLoc.getZ() <= getMaximumPoint().getZ())
			{
				if (pLoc.getX() >= getMinimumPoint().getX() && pLoc.getZ() >= getMinimumPoint().getZ())
				{
					return true;
				}
			}
		}
		
		return false;
	}
}