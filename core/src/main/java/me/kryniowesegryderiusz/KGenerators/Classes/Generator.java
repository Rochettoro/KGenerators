package me.kryniowesegryderiusz.KGenerators.Classes;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Generator {
	
	private String id;
	
	private ItemStack generatorBlock;
	  
	private ItemStack generatorItem;
	  
	private int delay;
	  
	private String type;
	
	private ItemStack placeholder;
	
	private int afterPlaceWaitModifier = 0;
	
	private boolean allowPistonPush = false;
	
	private Integer placeLimit = -1;
    private Boolean onlyOwnerPickUp = false;
    private Boolean onlyOwnerUse = false;
	
	HashMap<ItemStack, Double> chances = new HashMap<ItemStack, Double>();
	  
	public Generator(String id, ItemStack generatorBlock, ItemStack generatorItem, int delay, String type, HashMap<ItemStack, Double> chances) {
		this.id = id;
	    this.generatorBlock = generatorBlock;
	    this.generatorItem = generatorItem;
	    this.delay = delay;
	    this.type = type;
	    this.chances = chances;
	}
	
	public boolean doesChancesContain(ItemStack item)
	{
		for (Entry<ItemStack, Double> e : chances.entrySet())
		{
			if (e.getKey().equals(item)) return true;
		}
		return false;
	}
	
	public void setPlaceholder(ItemStack placeholder) {
		this.placeholder = placeholder;
	}
	  
	public ItemStack getGeneratorBlock() {
		return this.generatorBlock;
	}
	  
	public ItemStack getGeneratorItem() {
		return this.generatorItem;
	}
	  
	public int getDelay() {
		return this.delay;
	}
	  
	public String getType() {
		return this.type;
	}
	
	public ItemStack getPlaceholder() {
		return this.placeholder;
	}
	  
	public HashMap<ItemStack, Double> getChances(){
		  return this.chances;
	}
	
	public int getAfterPlaceWaitModifier() {
		return this.afterPlaceWaitModifier;
	}
	
	public void setAfterPlaceWaitModifier (int afterPlaceWaitModifier) {
		this.afterPlaceWaitModifier = afterPlaceWaitModifier;
	}
	
	public boolean isPistonPushAllowed() {
		return this.allowPistonPush;
	}
	
	public void setPistonPushAllowed (boolean allowPistonPush) {
		this.allowPistonPush = allowPistonPush;
	}
	
	public Integer getPlaceLimit()
	{
		return this.placeLimit;
	}
	
	public void setPlaceLimit(int placeLimit)
	{
		this.placeLimit = placeLimit;
	}
	
	public Boolean isOnlyOwnerPickUp()
	{
		return this.onlyOwnerPickUp;
	}
	
	public void setOnlyOwnerPickUp(Boolean onlyOwnerPickUp)
	{
		this.onlyOwnerPickUp = onlyOwnerPickUp;
	}
	
	public Boolean isOnlyOwnerUse()
	{
		return this.onlyOwnerUse;
	}
	
	public void setOnlyOwnerUse(Boolean onlyOwnerUse)
	{
		this.onlyOwnerUse = onlyOwnerUse;
	}
	
	public String getId()
	{
		return this.id;
	}
}