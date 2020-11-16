package me.kryniowesegryderiusz.KGenerators.Listeners;

import java.util.Map.Entry;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import me.kryniowesegryderiusz.KGenerators.Generator;
import me.kryniowesegryderiusz.KGenerators.KGenerators;
import me.kryniowesegryderiusz.KGenerators.Utils.LangUtils;

public class onCraftItemEvent implements Listener {
	
	@EventHandler
	public void CraftItemEvent(final CraftItemEvent e){
		if (!(e.getWhoClicked() instanceof Player)){
			return;
		}
		
		Player p = (Player) e.getWhoClicked();
		
		for(Entry<String, Generator> entry : KGenerators.generators.entrySet()) {
			
			String gName = entry.getKey();
			Generator g = entry.getValue();
			ItemStack item = g.getGeneratorItem();
			
			//sprawdzenie, czy nie craftuje sie z generatora
			ItemStack[] items = e.getInventory().getMatrix();
			for (ItemStack i : items) {
				if (i != null && i.equals(item)) {
					LangUtils.sendMessage(p, "generators.cant-craft");
					p.closeInventory();
					e.setCancelled(true);
					return;
				}
			}
			
			ItemStack itemRecipe = e.getRecipe().getResult();
			if (item.equals(itemRecipe)) {
				String permission = "kgenerators.craft."+gName;
				if (!p.hasPermission(permission)) {
					LangUtils.addReplecable("<generator>", g.getGeneratorItem().getItemMeta().getDisplayName());
					LangUtils.addReplecable("<permission>", permission);
					LangUtils.sendMessage(p, "generators.no-craft-permission");
					p.closeInventory();
					e.setCancelled(true);
					return;
				}
			}
		}
	}
}