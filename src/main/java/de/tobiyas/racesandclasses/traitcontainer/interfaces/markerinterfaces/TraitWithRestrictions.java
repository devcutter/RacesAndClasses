package de.tobiyas.racesandclasses.traitcontainer.interfaces.markerinterfaces;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public interface TraitWithRestrictions {

	/**
	 * The Config-Path to the Min Level to use
	 */
	public static final String MIN_LEVEL_PATH = "minLevel";
	
	/**
	 * The Config-Path to the Max Level to use
	 */
	public static final String MAX_LEVEL_PATH = "maxLevel";
	
	/**
	 * The Biomes to use.
	 */
	public static final String BIOME_PATH = "biomes";

	/**
	 * The items needed to be weared
	 */
	public static final String WEARING_PATH = "wearing";

	/**
	 * Trait works only in water
	 */
	public static final String ONLY_IN_WATER_PATH = "onlyInWater";

	/**
	 * Trait works only on Land
	 */
	public static final String ONLY_ON_LAND_PATH = "onlyOnLand";

	/**
	 * Trait works only in Lava
	 */
	public static final String ONLY_IN_LAVA_PATH = "onlyInLava";
	
	/**
	 * Trait works only in Lava
	 */
	public static final String ONLY_ON_SNOW = "onlyOnSnow";	
	
	/**
	 * Trait has a Cooldown
	 */
	public static final String COOLDOWN_TIME_PATH = "cooldown";
	
	/**
	 * Trait works only on Day.
	 */
	public static final String ONLY_ON_DAY_PATH = "onlyOnDay";
	
	/**
	 * Trait only works in the Night.
	 */
	public static final String ONLY_IN_NIGHT_PATH = "onlyInNight";

	/**
	 * Trait only when it is raining.
	 */
	public static final String ONLY_IN_RAIN_PATH = "onlyInRain";

	/**
	 * Trait only trigger when gotten damaged.
	 */
	public static final String ONLY_AFTER_DAMAGED_PATH = "onlyAfterDamaged";

	/**
	 * Trait only trigger when gotten damaged.
	 */
	public static final String ONLY_AFTER_NOT_DAMAGED_PATH = "onlyAfterNotDamaged";

	/**
	 * Trait only trigger when gotten damaged.
	 */
	public static final String ONLY_WHILE_SNEAKING_PATH = "onlyWhileSneaking";
	
	/**
	 * Trait only trigger when gotten damaged.
	 */
	public static final String ONLY_WHILE_NOT_SNEAKING_PATH = "onlyWhileNotSneaking";
	
	/**
	 * Trait only when standing on a certain block.
	 */
	public static final String ONLY_ON_BLOCK_PATH = "onlyOnBlock";
	
	/**
	 * Trait only when above elevation
	 */
	public static final String ABOVE_ELEVATION_PATH = "aboveElevation";
	
	/**
	 * Trait only when below elevation
	 */
	public static final String BELOW_ELEVATION_PATH = "belowElevation";
	
	/**
	 * The Path to the Display Name
	 */
	public static final String DISPLAY_NAME_PATH = "displayName";
	
	/**
	 * The Path to the Description Name
	 */
	public static final String DESCRIPTION_PATH = "description";
	
	

	/**
	 * Checks the Restrictions of the Trait
	 * for a player passed.
	 * 
	 * @param player to check
	 * @param event that called the trigger
	 * 
	 * @return true if restrictions met, false otherwise
	 */
	public boolean checkRestrictions(Player player, Event event);
	
	/**
	 * Returns the total uplink time in seconds
	 * 
	 * @return the time in seconds
	 */
	public int getMaxUplinkTime();
	
	
	/**
	 * This is triggered when the User has uplink on the Skill,
	 * But would be triggered.
	 * <br> Returning TRUE does NOT paste the Uplink message!
	 * 
	 * @param event the event that would be triggered
	 * @return true to NOT display uplink message!
	 */
	public boolean triggerButHasUplink(Event event);
	
	
	/**
	 * Returns if the Trait should notify it's user everytime on trigger for the Uplink.
	 * 
	 * @return true if should, false if not.
	 */
	public boolean notifyTriggeredUplinkTime();
	
}
