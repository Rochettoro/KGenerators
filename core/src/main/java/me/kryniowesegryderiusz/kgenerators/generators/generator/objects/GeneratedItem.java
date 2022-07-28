package me.kryniowesegryderiusz.kgenerators.generators.generator.objects;

import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemMergeEvent;
import org.bukkit.inventory.ItemStack;

import lombok.Getter;
import me.kryniowesegryderiusz.kgenerators.Main;
import me.kryniowesegryderiusz.kgenerators.api.interfaces.IGeneratorLocation;
import me.kryniowesegryderiusz.kgenerators.api.objects.AbstractGeneratedObject;
import me.kryniowesegryderiusz.kgenerators.utils.FilesUtils;

public class GeneratedItem extends AbstractGeneratedObject implements Listener {

	public GeneratedItem() {
		super("item");
	}

	@Getter
	private ItemStack item;

	@Getter
	private boolean waitForPickUp = false;

	private Item entity;

	@Override
	protected boolean compareSameType(AbstractGeneratedObject generatedObject) {
		GeneratedItem gi = (GeneratedItem) generatedObject;
		return gi.getItem().equals(this.getItem());
	}

	@Override
	protected boolean loadTypeSpecific(Map<?, ?> generatedObjectConfig) {

		if (generatedObjectConfig.containsKey("wait-for-pick-up"))
			this.waitForPickUp = (boolean) generatedObjectConfig.get("wait-for-pick-up");

		this.item = FilesUtils.loadItemStack((Map<?, ?>) generatedObjectConfig, "item",
				"Generators file: GeneratedItem", false);

		if (this.item != null) {
			if (this.waitForPickUp)
				Main.getInstance().getServer().getPluginManager().registerEvents(this, Main.getInstance());
			return true;
		}
		return false;
	}

	@Override
	public void regenerate(IGeneratorLocation generatorLocation) {
		Location generateLocation = generatorLocation.getGeneratedBlockLocation().clone().add(0.5, 0, 0.5);
		if (!Main.getMultiVersion().getBlocksUtils().isAir(generatorLocation.getGeneratedBlockLocation().getBlock()))
			generateLocation.add(0, 1, 0);
		generateLocation.setPitch(-90);
		this.entity = generatorLocation.getGeneratedBlockLocation().getWorld().dropItem(generateLocation, item);
		generatorLocation.scheduleGeneratorRegeneration();
	}

	@Override
	public ItemStack getGuiItem() {
		return this.item.clone();
	}

	@Override
	protected String toStringSpecific() {

		String s = "None";
		if (this.getItem() != null)
			s = "Item: " + this.getItem().toString();
		if (this.waitForPickUp)
			s += " [waitForPickUp]";

		return s;
	}

	@Override
	public boolean isReady() {
		return !(this.waitForPickUp && !this.entity.isDead());
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onItemMerge(ItemMergeEvent e) {
		if (!e.isCancelled() && e.getEntity() == this.entity)
			this.entity = e.getTarget();
	}

}
