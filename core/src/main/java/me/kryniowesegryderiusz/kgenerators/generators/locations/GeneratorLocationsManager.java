package me.kryniowesegryderiusz.kgenerators.generators.locations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import me.kryniowesegryderiusz.kgenerators.Main;
import me.kryniowesegryderiusz.kgenerators.generators.locations.objects.GeneratorLocation;
import me.kryniowesegryderiusz.kgenerators.logger.Logger;

public class GeneratorLocationsManager {
	
	private HashMap<Location, GeneratorLocation> locations = new HashMap<Location, GeneratorLocation>();
	
	public boolean exists(Location location)
	{
		if (locations.containsKey(location)) return true;
		return false;
	}
	
	/**
	 * @param location
	 * @return GeneratorLocation related to location
	 */
	@Nullable
	public GeneratorLocation get(Location location)
	{
		if (exists(location)) return locations.get(location);
		else if (exists(location.clone().add(0,-1,0))) return locations.get(location.clone().add(0,-1,0));
		else return null;
	}

	/**
	 * Keep in mind, that you should use GeneratorLocation#save
	 * @param gLocation
	 */
	public void add(GeneratorLocation gLocation)
	{
		locations.put(gLocation.getLocation(), gLocation);
	}
	
	public void remove(GeneratorLocation gLocation)
	{
		this.remove(gLocation.getLocation());
	}
	
	public void remove(Location location)
	{
		if (exists(location)) locations.remove(location);
	}

	public void clear() {
		locations.clear();
	}
	
	public Set<Entry<Location, GeneratorLocation>> getEntrySet()
	{
		return locations.entrySet();
	}
	
	/*
	 * Bulk updates
	 */
	
	public void bulkRemoveGenerators(World world, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, boolean dropGenerator)
	{
		Main.getInstance().getServer().getScheduler().runTaskAsynchronously(Main.getInstance(), new Runnable() {
			@Override
			public void run() {
				ArrayList<GeneratorLocation> generatorLocationsToRemove = new ArrayList<GeneratorLocation>();
				for (Entry<Location, GeneratorLocation> e : locations.entrySet())
				{
					//Detected BentoBox removing island with unknown owner in world bskyblock_world starting at -50,750 and ending at 50,850
					
					Location l = e.getKey();
					if (l.getWorld() == world
							&& l.getX() >= minX && l.getX() <= maxX
							&& l.getY() >= minY && l.getY() <= maxY
							&& l.getZ() >= minZ && l.getZ() <= maxZ)
					{
						generatorLocationsToRemove.add(e.getValue());
					}
				}
				Main.getInstance().getServer().getScheduler().runTask(Main.getInstance(), new Runnable() {
					@Override
					public void run() {
						for (GeneratorLocation gloc : generatorLocationsToRemove)
						{
							Logger.info("Bulk generator removal: " + gloc.toString());
							gloc.removeGenerator(dropGenerator, null);
						}
					}
				});
			}
		});
	}
	
	/*
	 * Converters
	 */
	
	public String locationToString(Location location)
	{
		int x = location.getBlockX();
		int y = location.getBlockY();
		int z = location.getBlockZ();
		String world  = location.getWorld().getName();
		String string = world + "," + x + "," + y + "," + z;
		
		return string;
	}
	
	public Location stringToLocation (String string)
	{
		String[] parts = string.split(",");

        final World w = Bukkit.getServer().getWorld(parts[0]);
        final int x = Integer.parseInt(parts[1]);
        final int y = Integer.parseInt(parts[2]);
        final int z = Integer.parseInt(parts[3]);

        return new Location(w, x, y, z);
	}
}