package me.kryniowesegryderiusz.kgenerators.multiversion;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.RegionContainer;

import me.kryniowesegryderiusz.kgenerators.Logger;
import me.kryniowesegryderiusz.kgenerators.Enums.EnumWGFlags;
import me.kryniowesegryderiusz.kgenerators.classes.GeneratorLocation;
import me.kryniowesegryderiusz.kgenerators.multiversion.WorldGuardUtils;

public class WorldGuardUtils_1_13 implements WorldGuardUtils {
	
	public static StateFlag PICK_UP_FLAG;
	public static StateFlag ONLY_GEN_BREAK_FLAG;
	
	@Override
	public boolean isWorldGuardHooked() {
		if (PICK_UP_FLAG == null || ONLY_GEN_BREAK_FLAG == null) {
			return false;
		}
		else
		{
			return true;
		}
	}
	
	@Override
	public void worldGuardFlagsAdd() {
		
		try {
			FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
			
			if (registry == null) {
				return;
			}
			
			for (EnumWGFlags eflag : EnumWGFlags.values())
			{
				Logger.info("WorldGuard: Registering " + eflag.getFlagId() + " flag");
				try {
					StateFlag flag = new StateFlag(eflag.getFlagId(), eflag.getFlagDefault());
					registry.register(flag);
					if (eflag == EnumWGFlags.PICK_UP) PICK_UP_FLAG = flag;
					if (eflag == EnumWGFlags.ONLY_GEN_BREAK) ONLY_GEN_BREAK_FLAG = flag;
					
				}
				catch (FlagConflictException e)
				{
					Logger.error("WorldGuard: FlagConflictException!");
					Flag<?> existing = registry.get(eflag.getFlagId());
			        if (existing instanceof StateFlag) {
			        	Logger.warn("WorldGuard: Overriding flag!");
			        	
			        	if (eflag == EnumWGFlags.PICK_UP) PICK_UP_FLAG = (StateFlag) existing;
						if (eflag == EnumWGFlags.ONLY_GEN_BREAK) ONLY_GEN_BREAK_FLAG = (StateFlag) existing;

			        } else {
			            Logger.error("WorldGuard: Flag overriding not possible! Types dont match!");
			        }
				}
				
			}
		} catch (NoClassDefFoundError e) {
			Logger.error("WorldGuard: An error occured, while adding WorldGuard flags!");
			Logger.error("WorldGuard: WorldGuard is installed, but didnt load properly!");
			//e.printStackTrace();
		}
	}

	/* Returns if pick up is allow/deny */
	@Override
	public boolean worldGuardFlagCheck(GeneratorLocation gLocation, Player player, EnumWGFlags flag) {
		
		Location location = gLocation.getLocation();
		
		RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
		
		RegionManager regions = container.get(BukkitAdapter.adapt(location.getWorld()));
		ApplicableRegionSet regionSet = regions.getApplicableRegions(BukkitAdapter.adapt(location).toVector().toBlockPoint());
		
		LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
		
		StateFlag stateFlag = null;
		if (flag == EnumWGFlags.PICK_UP) stateFlag = PICK_UP_FLAG;
		if (flag == EnumWGFlags.ONLY_GEN_BREAK) stateFlag = ONLY_GEN_BREAK_FLAG;
		
		if (flag != null && regionSet.testState(localPlayer, stateFlag)) return true;
		return false;
	}
}
