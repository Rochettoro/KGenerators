package me.kryniowesegryderiusz.kgenerators.listeners;

import org.bukkit.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;

import me.kryniowesegryderiusz.kgenerators.Main;

public class ChunkLoadListener implements Listener {

	@EventHandler
	public void onChunkLoad(final ChunkLoadEvent e) {
		
		if (Main.getDatabases().isMigratorRunning()) return;
		
		Chunk c = e.getChunk();
		
		Main.getPlacedGenerators().loadChunk(c, 0);
	}
}
