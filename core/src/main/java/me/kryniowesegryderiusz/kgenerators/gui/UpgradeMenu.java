package me.kryniowesegryderiusz.kgenerators.gui;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.kryniowesegryderiusz.kgenerators.Lang;
import me.kryniowesegryderiusz.kgenerators.enums.MenuInventoryType;
import me.kryniowesegryderiusz.kgenerators.enums.MenuItemType;
import me.kryniowesegryderiusz.kgenerators.classes.Generator;
import me.kryniowesegryderiusz.kgenerators.classes.MenuItem;
import me.kryniowesegryderiusz.kgenerators.managers.Generators;
import me.kryniowesegryderiusz.kgenerators.managers.Upgrades;

public class UpgradeMenu implements Listener {
	
	public static Inventory get(Player player, Generator generator)
	{		
		ArrayList<MenuItemType> exludedEnumMenuItems = new ArrayList<MenuItemType>();
		exludedEnumMenuItems.add(MenuItemType.UPGRADE_MENU_INGREDIENT);
		exludedEnumMenuItems.add(MenuItemType.UPGRADE_MENU_RESULT);
		
		Generator previousGenerator = Generators.get(Upgrades.getPreviousGeneratorId(generator.getId()));
		
		Inventory menu = Lang.getMenuInventory(MenuInventoryType.UPGRADE).getInv(MenuInventoryType.UPGRADE, player, exludedEnumMenuItems, "<cost>", String.valueOf(previousGenerator.getUpgrade().getCost()));
		
		/*
		 * Ingredient item
		 */
		MenuItem ingredientItem = MenuItemType.UPGRADE_MENU_INGREDIENT.getMenuItem();
		
		ingredientItem.setItemStack(previousGenerator.getGeneratorItem());
		
		if (ingredientItem.getItemType().contains("<generator>"))
			ingredientItem.setItemStack(previousGenerator.getGeneratorItem());
		ingredientItem.replace("<generator_name>", previousGenerator.getGeneratorItem().getItemMeta().getDisplayName());
		
		ItemStack readyItem = ingredientItem.build();

		for (int i : ingredientItem.getSlots())
			menu.setItem(i, readyItem);
		
		/*
		 * Result Item
		 */
				
		MenuItem resultItem = MenuItemType.UPGRADE_MENU_RESULT.getMenuItem();
		
		resultItem.setItemStack(generator.getGeneratorItem());
		
		if (resultItem.getItemType().contains("<generator>"))
			resultItem.setItemStack(generator.getGeneratorItem());
		resultItem.replace("<generator_name>", generator.getGeneratorItem().getItemMeta().getDisplayName());
		
		readyItem = resultItem.build();

		for (int i : resultItem.getSlots())
			menu.setItem(i, readyItem);
		
		return menu;
	}
	
	@EventHandler
	public void onClick(final InventoryClickEvent e)
	{
		if(e.isCancelled()) return;
		if (!Menus.isVieving((Player) e.getWhoClicked(), MenuInventoryType.UPGRADE)) return;
		
		int slot = e.getSlot();
		if (MenuItemType.UPGRADE_MENU_BACK.getMenuItem().getSlots().contains(slot) && Lang.getMenuItem(MenuItemType.UPGRADE_MENU_BACK).isEnabled())
		{
			Menus.openMainMenu((Player) e.getWhoClicked());
		}
		e.setCancelled(true);
	}

}
