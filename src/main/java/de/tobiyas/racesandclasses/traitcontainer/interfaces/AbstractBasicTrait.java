package de.tobiyas.racesandclasses.traitcontainer.interfaces;

import static de.tobiyas.racesandclasses.translation.languages.Keys.trait_cooldown;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.entity.PlayerLeashEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleEntityCollisionEvent;
import org.bukkit.event.vehicle.VehicleEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.inventory.ItemStack;

import de.tobiyas.racesandclasses.RacesAndClasses;
import de.tobiyas.racesandclasses.APIs.LanguageAPI;
import de.tobiyas.racesandclasses.datacontainer.traitholdercontainer.AbstractTraitHolder;
import de.tobiyas.racesandclasses.eventprocessing.events.chatevent.PlayerSendChannelChatMessageEvent;
import de.tobiyas.racesandclasses.eventprocessing.events.holderevent.HolderSelectedEvent;
import de.tobiyas.racesandclasses.eventprocessing.events.leveling.LevelEvent;
import de.tobiyas.racesandclasses.eventprocessing.events.traittrigger.TraitTriggerEvent;
import de.tobiyas.racesandclasses.listeners.generallisteners.PlayerLastDamageListener;
import de.tobiyas.racesandclasses.traitcontainer.interfaces.annotations.configuration.TraitConfigurationField;
import de.tobiyas.racesandclasses.traitcontainer.interfaces.annotations.configuration.TraitConfigurationNeeded;
import de.tobiyas.racesandclasses.traitcontainer.interfaces.markerinterfaces.Trait;
import de.tobiyas.racesandclasses.traitcontainer.interfaces.markerinterfaces.TraitWithRestrictions;
import de.tobiyas.racesandclasses.util.bukkit.versioning.CertainVersionChecker;
import de.tobiyas.racesandclasses.util.bukkit.versioning.compatibility.CompatibilityModifier;
import de.tobiyas.racesandclasses.util.traitutil.TraitConfigParser;
import de.tobiyas.racesandclasses.util.traitutil.TraitConfigurationFailedException;

public abstract class AbstractBasicTrait implements Trait,
		TraitWithRestrictions {

	/**
	 * The plugin to call stuff on.
	 */
	protected static final RacesAndClasses plugin = RacesAndClasses.getPlugin();
	
	
	/**
	 * the minimum level to use this trait
	 */
	protected int minimumLevel = 1;
	
	
	/**
	 * the maximum level to use this trait
	 */
	protected int maximumLevel = 90000000;

	
	/**
	 * The Set of biomes restricted.
	 */
	protected final Set<Biome> biomes = new HashSet<Biome>(Arrays.asList(Biome.values()));
	
	/**
	 * The set of Items NEEDED to be weared.s
	 */
	protected final Set<Material> wearing = new HashSet<Material>();
	
	/**
	 * Tells if the Trait only works in the water
	 */
	protected boolean onlyInWater = false;
	
	/**
	 * Tells if the Trait only works on land
	 */
	protected boolean onlyOnLand = false;

	/**
	 * Tells if the Trait only works in lava
	 */
	protected boolean onlyInLava = false;
	
	/**
	 * Tells if the Trait only works on Snow
	 */
	protected boolean onlyOnSnow = false;
	
	/**
	 * Tells if the Trait can only trigger in Night.
	 */
	protected boolean onlyInNight = false;
	
	/**
	 * Tells if the Trait can only trigger on Day.
	 */
	protected boolean onlyOnDay = false;
	
	/**
	 * The Time the Trait has cooldown. (in Seconds)
	 */
	protected int cooldownTime = 0;
	
	/**
	 * The DisplayName to show.
	 */
	protected String displayName;
	
	/**
	 * The Elevation the player has to be above
	 */
	protected int aboveElevation = Integer.MIN_VALUE;
	
	/**
	 * The Elevation the player has to be below
	 */
	protected int belowElevation = Integer.MAX_VALUE;
	
	/**
	 * Tells if the Trait can only be triggered in the Rain.
	 */
	protected boolean onlyInRain = false;

	/**
	 * Tells if the Trait may only be used after player has been damaged.
	 * <br>Time in seconds.
	 * <br> onlyAfterDamage <= 0 = no check.
	 */
	protected int onlyAfterDamaged = -1;
	
	/**
	 * Tells if the Trait may only be used after player has NOT been damaged.
	 * <br>Time in seconds.
	 * <br> onlyAfterNotDamaged <= 0 = no check.
	 */
	protected int onlyAfterNotDamaged = -1;
	
	/**
	 * Tells the Trait can be activated only on certain blocks.
	 */
	protected final List<Material> onlyOnBlocks = new LinkedList<Material>();
	
	/**
	 * Tells the Trait can be activated while the player sneaks.
	 */
	protected boolean onlyWhileSneaking = false;
	
	/**
	 * Tells the Trait can be activated while the player does NOT sneaks.
	 */
	protected boolean onlyWhileNotSneaking = false;
	

	/**
	 * The Description of the Trait.
	 */
	protected String traitDiscription = "";
	
	
	
	/**
	 * The holder of the Trait.
	 */
	protected AbstractTraitHolder holder;
	
	
	/**
	 * The Config of the Trait
	 */
	protected Map<String, Object> currentConfig;
	

	@Override
	public void setTraitHolder(AbstractTraitHolder abstractTraitHolder) {
		this.holder = abstractTraitHolder;
	}

	@Override
	public AbstractTraitHolder getTraitHolder() {
		return holder;
	}

	
	@TraitConfigurationNeeded(fields = {
		@TraitConfigurationField(fieldName = MIN_LEVEL_PATH, classToExpect = Integer.class, optional = true),	
		@TraitConfigurationField(fieldName = MAX_LEVEL_PATH, classToExpect = Integer.class, optional = true),	
		@TraitConfigurationField(fieldName = BIOME_PATH, classToExpect = List.class, optional = true),
		@TraitConfigurationField(fieldName = ONLY_ON_BLOCK_PATH, classToExpect = List.class, optional = true),
		@TraitConfigurationField(fieldName = ONLY_IN_WATER_PATH, classToExpect = Boolean.class, optional = true),
		@TraitConfigurationField(fieldName = ONLY_ON_LAND_PATH, classToExpect = Boolean.class, optional = true),
		@TraitConfigurationField(fieldName = ONLY_IN_LAVA_PATH, classToExpect = Boolean.class, optional = true),
		@TraitConfigurationField(fieldName = ONLY_ON_SNOW, classToExpect = Boolean.class, optional = true),		
		@TraitConfigurationField(fieldName = ONLY_ON_DAY_PATH, classToExpect = Boolean.class, optional = true),
		@TraitConfigurationField(fieldName = ONLY_IN_NIGHT_PATH, classToExpect = Boolean.class, optional = true),
		@TraitConfigurationField(fieldName = ONLY_IN_RAIN_PATH, classToExpect = Boolean.class, optional = true),
		@TraitConfigurationField(fieldName = ONLY_AFTER_DAMAGED_PATH, classToExpect = Integer.class, optional = true),
		@TraitConfigurationField(fieldName = ONLY_AFTER_NOT_DAMAGED_PATH, classToExpect = Integer.class, optional = true),
		@TraitConfigurationField(fieldName = ONLY_WHILE_SNEAKING_PATH, classToExpect = Boolean.class, optional = true),
		@TraitConfigurationField(fieldName = ONLY_WHILE_NOT_SNEAKING_PATH, classToExpect = Boolean.class, optional = true),
		@TraitConfigurationField(fieldName = COOLDOWN_TIME_PATH, classToExpect = Integer.class, optional = true),
		@TraitConfigurationField(fieldName = ABOVE_ELEVATION_PATH, classToExpect = Integer.class, optional = true),
		@TraitConfigurationField(fieldName = BELOW_ELEVATION_PATH, classToExpect = Integer.class, optional = true),
		@TraitConfigurationField(fieldName = DISPLAY_NAME_PATH, classToExpect = String.class, optional = true),
		@TraitConfigurationField(fieldName = DESCRIPTION_PATH, classToExpect = String.class, optional = true),
	})
	
	@Override
	public void setConfiguration(Map<String, Object> configMap) throws TraitConfigurationFailedException {
		this.currentConfig = configMap;
		
		//Gets the Cooldown of the Trait.
		if(configMap.containsKey(TraitWithRestrictions.COOLDOWN_TIME_PATH)){
			this.cooldownTime = (Integer) configMap.get(TraitWithRestrictions.COOLDOWN_TIME_PATH);
		}
		
		//Description
		if(configMap.containsKey(TraitWithRestrictions.DESCRIPTION_PATH)){
			this.traitDiscription = (String) configMap.get(TraitWithRestrictions.DESCRIPTION_PATH);
		}
		
		//Reads the min level for the trait if present
		if(configMap.containsKey(TraitWithRestrictions.MIN_LEVEL_PATH)){
			this.minimumLevel = (Integer) configMap.get(TraitWithRestrictions.MIN_LEVEL_PATH);
		}

		//Reads the max level for the trait if present
		if(configMap.containsKey(TraitWithRestrictions.MAX_LEVEL_PATH)){
			this.maximumLevel = (Integer) configMap.get(TraitWithRestrictions.MAX_LEVEL_PATH);
		}
		
		//Reads the only after damage value
		if(configMap.containsKey(TraitWithRestrictions.ONLY_AFTER_DAMAGED_PATH)){
			this.onlyAfterDamaged = (Integer) configMap.get(TraitWithRestrictions.ONLY_AFTER_DAMAGED_PATH);
		}

		//Reads the only after damage value
		if(configMap.containsKey(TraitWithRestrictions.ONLY_AFTER_NOT_DAMAGED_PATH)){
			this.onlyAfterNotDamaged = (Integer) configMap.get(TraitWithRestrictions.ONLY_AFTER_NOT_DAMAGED_PATH);
		}

		//above elevation
		if(configMap.containsKey(TraitWithRestrictions.ABOVE_ELEVATION_PATH)){
			this.aboveElevation = (Integer) configMap.get(TraitWithRestrictions.ABOVE_ELEVATION_PATH);
		}
		
		//below elevation
		if(configMap.containsKey(TraitWithRestrictions.BELOW_ELEVATION_PATH)){
			this.belowElevation = (Integer) configMap.get(TraitWithRestrictions.BELOW_ELEVATION_PATH);
		}
		
		//Reads the biomes for the trait if present
		if(configMap.containsKey(TraitWithRestrictions.BIOME_PATH)){
			try{
				@SuppressWarnings("unchecked")
				List<String> stringBiomes = (List<String>) configMap.get(TraitWithRestrictions.BIOME_PATH);
				this.biomes.clear();
				
				for(String biome : stringBiomes){
					biome = biome.toUpperCase();
					Biome biom = Biome.valueOf(biome);
					
					if(biom != null){
						biomes.add(biom);
					}
				}
			}catch(Exception exp){}
		}

		//Reads the blocks for the trait if present
		if(configMap.containsKey(TraitWithRestrictions.ONLY_ON_BLOCK_PATH)){
			try{
				@SuppressWarnings("unchecked")
				List<String> stringBlocks = (List<String>) configMap.get(TraitWithRestrictions.ONLY_ON_BLOCK_PATH);
				this.onlyOnBlocks.clear();
				
				for(String block : stringBlocks){
					block = block.toUpperCase();
					Material type =  null;
					try{
						type = Material.valueOf(block);
					}catch(IllegalArgumentException exp){}
					
					if(type != null){
						onlyOnBlocks.add(type);
					}
				}
			}catch(Exception exp){}
		}

		//Reads the Armor for the trait to wear
		if(configMap.containsKey(TraitWithRestrictions.WEARING_PATH)){
			try{
				@SuppressWarnings("unchecked")
				List<String> stringBlocks = (List<String>) configMap.get(TraitWithRestrictions.WEARING_PATH);
				this.wearing.clear();
				
				for(String armor : stringBlocks){
					armor = armor.toUpperCase();
					Material type = Material.valueOf(armor);
					
					if(type != null){
						wearing.add(type);
					}
				}
			}catch(Exception exp){}
		}
		
		//Only in water
		if(configMap.containsKey(TraitWithRestrictions.ONLY_IN_WATER_PATH)){
			this.onlyInWater = (Boolean) configMap.get(TraitWithRestrictions.ONLY_IN_WATER_PATH);
		}
		
		//Only in Rain
		if(configMap.containsKey(TraitWithRestrictions.ONLY_IN_RAIN_PATH)){
			this.onlyInRain = (Boolean) configMap.get(TraitWithRestrictions.ONLY_IN_RAIN_PATH);
		}

		//Only on snow
		if(configMap.containsKey(TraitWithRestrictions.ONLY_ON_SNOW)){
			this.onlyOnSnow = (Boolean) configMap.get(TraitWithRestrictions.ONLY_ON_SNOW);
		}

		//Only on land
		if(configMap.containsKey(TraitWithRestrictions.ONLY_ON_LAND_PATH)){
			this.onlyOnLand = (Boolean) configMap.get(TraitWithRestrictions.ONLY_ON_LAND_PATH);
		}
		
		//Only on Day
		if(configMap.containsKey(TraitWithRestrictions.ONLY_ON_DAY_PATH)){
			this.onlyOnDay = (Boolean) configMap.get(TraitWithRestrictions.ONLY_ON_DAY_PATH);
		}
		
		//Only in Night
		if(configMap.containsKey(TraitWithRestrictions.ONLY_IN_NIGHT_PATH)){
			this.onlyInNight = (Boolean) configMap.get(TraitWithRestrictions.ONLY_IN_NIGHT_PATH);
		}
				
		//Display Name
		if(configMap.containsKey(TraitWithRestrictions.DISPLAY_NAME_PATH)){
			this.displayName = (String) configMap.get(TraitWithRestrictions.DISPLAY_NAME_PATH);
		}
		
		//Sneaking
		if(configMap.containsKey(TraitWithRestrictions.ONLY_WHILE_SNEAKING_PATH)){
			this.onlyWhileSneaking = (Boolean) configMap.get(TraitWithRestrictions.ONLY_WHILE_SNEAKING_PATH);
		}
		
		//Not sneaking
		if(configMap.containsKey(TraitWithRestrictions.ONLY_WHILE_NOT_SNEAKING_PATH)){
			this.onlyWhileNotSneaking = (Boolean) configMap.get(TraitWithRestrictions.ONLY_WHILE_NOT_SNEAKING_PATH);
		}
		
		
	}
	

	@Override
	public Map<String, Object> getCurrentconfig(){
		return currentConfig;
	}
	
	
	@Override
	public void deInit(){
		//Meant to be overwritten!!
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * <br>
	 * <br>To override, use {@link #getAdditionalOptionalConfigFields()}.
	 * <br>This adds the optional Fields to the one added here.
	 */
	@Override
	public final List<String> getOptionalConfigFields(){
		List<TraitConfigurationField> configFields = TraitConfigParser.getAllTraitConfigFieldsOfTrait(this);
		List<String> optionalFields = new LinkedList<String>();

		for(TraitConfigurationField field : configFields){
			if(field.optional() == true){
				optionalFields.add(field.fieldName());
			}
		}
		
		return optionalFields;
	}

	
	/**
	 * {@inheritDoc}
	 * 
	 * <br>
	 * <br>To override, use {@link #getReleventPlayerBefore(Event)}.
	 * <br>This is preProcessed before the default Events are done.
	 */
	@Override
	public final Player getReleventPlayer(Event event) {
		Player preProcessedPlayer = getReleventPlayerBefore(event);
		if(preProcessedPlayer != null){
			return preProcessedPlayer;
		}
		
		//block events like place / break
		if(event instanceof BlockEvent){
			if(event instanceof BlockPlaceEvent){
				return ((BlockPlaceEvent) event).getPlayer();
			}
			
			if(event instanceof BlockBreakEvent){
				return ((BlockBreakEvent) event).getPlayer();
			}
			
			if(event instanceof BlockDamageEvent){
				return ((BlockDamageEvent) event).getPlayer();
			}
		}
		
		//Projectile events. 
		//We need to get the shooter.
		if(event instanceof ProjectileHitEvent){
			ProjectileHitEvent launchEvent = (ProjectileHitEvent) event;
			LivingEntity shooter = CompatibilityModifier.Shooter.getShooter(launchEvent.getEntity());
			if(shooter instanceof Player) return (Player) shooter;
		}
		
		if(event instanceof ProjectileLaunchEvent){
			ProjectileLaunchEvent launchEvent = (ProjectileLaunchEvent) event;
			LivingEntity shooter = CompatibilityModifier.Shooter.getShooter(launchEvent.getEntity());
			if(shooter instanceof Player) return (Player) shooter;
		}
		
		//check if any projectile
		if(event instanceof EntityDamageByEntityEvent){
			EntityDamageByEntityEvent damageEvent = (EntityDamageByEntityEvent) event;
			Entity damager = damageEvent.getDamager();
			if(damager instanceof Projectile){
				LivingEntity shooter = CompatibilityModifier.Shooter.getShooter((Projectile) damager);
				if(shooter != null && shooter instanceof Player){
					return (Player) shooter;
				}
			}
		}
		
		
		if(event instanceof EntityEvent){			
			EntityEvent entityEvent = (EntityEvent) event;
			if(entityEvent.getEntityType() == EntityType.PLAYER){
				return (Player) entityEvent.getEntity();
			}
		}
		
		if(event instanceof InventoryEvent){
			InventoryEvent inventoryEvent = (InventoryEvent) event;
			if(inventoryEvent.getInventory().getHolder() instanceof Player){
				return (Player) inventoryEvent.getInventory().getHolder();
			}
		}
		
		if(event instanceof InventoryMoveItemEvent){
			InventoryMoveItemEvent inventoryMoveItemEvent = (InventoryMoveItemEvent) event;
			if(inventoryMoveItemEvent.getSource().getHolder() instanceof Player){
				return (Player) inventoryMoveItemEvent.getSource().getHolder();
			}
		}
		
		if(event instanceof InventoryPickupItemEvent){
			InventoryPickupItemEvent pickupItemEvent = (InventoryPickupItemEvent) event;
			if(pickupItemEvent.getInventory().getHolder() instanceof Player){
				return (Player) pickupItemEvent.getInventory().getHolder();
			}
		}
		
		if(event instanceof PlayerEvent){
			return ((PlayerEvent) event).getPlayer();
		}
		
		if(CertainVersionChecker.isAbove1_6()){
			if(event instanceof PlayerLeashEntityEvent){
				return ((PlayerLeashEntityEvent) event).getPlayer();
			}
		}
		
		
		if(event instanceof PlayerSendChannelChatMessageEvent){
			return ((PlayerSendChannelChatMessageEvent) event).getPlayer();
		}
		
		if(event instanceof VehicleEvent){
			if(event instanceof VehicleEntityCollisionEvent){
				VehicleEntityCollisionEvent vecevent = (VehicleEntityCollisionEvent) event;
				if(vecevent.getEntity() instanceof Player){
					return (Player) vecevent.getEntity();
				}
			}
			
			if(event instanceof VehicleEnterEvent){
				VehicleEnterEvent vehicleEnterEvent = (VehicleEnterEvent) event;
				if(vehicleEnterEvent.getEntered() instanceof Player){
					return (Player) vehicleEnterEvent.getEntered();
				}
			}
			
			if(event instanceof VehicleExitEvent){
				VehicleExitEvent vehicleExitEvent = (VehicleExitEvent) event;
				if(vehicleExitEvent.getExited() instanceof Player){
					return (Player) vehicleExitEvent.getExited();
				}
			}
		}
		

		//RaC-Plugin Events:		
		if(event instanceof LevelEvent){
			return Bukkit.getPlayer(((LevelEvent) event).getPlayerName());
		}
		
		if(event instanceof HolderSelectedEvent){
			return ((HolderSelectedEvent) event).getPlayer();
		}
		
		if(event instanceof PlayerSendChannelChatMessageEvent){
			return ((PlayerSendChannelChatMessageEvent)event).getPlayer();
		}
		
		if(event instanceof TraitTriggerEvent){
			return null; //This can not be interesting for a Trait.
		}
		
		
		
		return null;
	}
	
	/**
	 * This method can be Overriden when specific identifying
	 * of a player has to be done.
	 * 
	 * This method is called BEFORE checking the events in {@link #getReleventPlayer(Event)}.
	 * 
	 * Returning null proceeds with the default checks.
	 * Returning a player (not null) stops the search and returns the player.
	 * 
	 * @param event to check
	 * @return the Player found or null if not found or wanted.
	 */
	protected Player getReleventPlayerBefore(Event event){
		return null;
	}

	
	@Override
	public final String getPrettyConfiguration() {
		if("".equals(traitDiscription)){
			return getPrettyConfigIntern();
		}
		
		return traitDiscription;
	}
	
	/**
	 * Returns the Pretty config.
	 * @return pretty name
	 */
	protected abstract String getPrettyConfigIntern();
	
	@Override
	public boolean checkRestrictions(Player player, Event event) {
		if(player == null) return true;
		
		String playerName = player.getName();
		int playerLevel = plugin.getPlayerManager().getPlayerLevelManager(playerName).getCurrentLevel();
		if(playerLevel < minimumLevel || playerLevel > maximumLevel) return false;
		
		Biome currentBiome = player.getLocation().getBlock().getBiome();
		if(!biomes.contains(currentBiome)) return false;
		
		//Check if player is in water
		if(onlyInWater){
			Block feetblock = player.getLocation().getBlock().getRelative(BlockFace.UP);
			
			if(feetblock.getType() != Material.WATER && feetblock.getType() != Material.STATIONARY_WATER){
				return false;
			}
		}

		//Sneaking
		if(onlyWhileSneaking){
			if(!player.isSneaking()) return false;
		}
		
		//Not sneaking
		if(onlyWhileNotSneaking){
			if(player.isSneaking()) return false;
		}
		
		//check if player is on land
		if(onlyOnLand){
			Block feetblock = player.getLocation().getBlock().getRelative(BlockFace.UP);
			if(feetblock.getType() == Material.WATER || feetblock.getType() == Material.STATIONARY_WATER){
				return false;
			}
		}
		
		//check if player is in lava
		if(onlyInLava){
			Block feetblock = player.getLocation().getBlock().getRelative(BlockFace.UP);
			
			if(feetblock.getType() != Material.LAVA && feetblock.getType() != Material.STATIONARY_LAVA){
				return false;
			}
		}

		//check if player is on Snow
		if(onlyOnSnow){
			Block feetblock = player.getLocation().getBlock();
			
			if(!(feetblock.getType() == Material.SNOW || feetblock.getType() == Material.SNOW_BLOCK)){
				return false;
			}
		}
		
		//check if player is in Rain
		if(onlyInRain){
			if(!player.getWorld().hasStorm()) return false;
			Block feetblock = player.getLocation().getBlock();
			int ownY = feetblock.getY();
			int highestY = feetblock.getWorld().getHighestBlockYAt(feetblock.getX(), feetblock.getZ());
			//This means having a roof over oneself
			if(ownY != highestY) return false;
		}
		
		
		//checking for wearing
		if(!wearing.isEmpty()){
			boolean found = false;
			for(Material mat : wearing){
				found = false;
				for(ItemStack item : player.getInventory().getArmorContents()){
					if(item == null) continue;
					
					if(mat == item.getType()){
						found = true;
						break;
					}
				}
				
				if(!found) return false;
			}
		}
		
		//check above elevation
		if(aboveElevation != Integer.MIN_VALUE){
			Block feetblock = player.getLocation().getBlock();
			if(feetblock.getY() <= aboveElevation) return false;
		}

		//check below elevation
		if(belowElevation != Integer.MAX_VALUE){
			Block feetblock = player.getLocation().getBlock();
			if(feetblock.getY() <= belowElevation) return false;
		}

		//check onlyAfterDamaged
		if(onlyAfterDamaged > 0){
			int lastDamage = PlayerLastDamageListener.getTimePassedSinceLastDamageInSeconds(playerName);
			if(lastDamage > onlyAfterDamaged) return false;
		}
		
		//check onlyAfterDamaged
		if(onlyAfterNotDamaged > 0){
			int lastDamage = PlayerLastDamageListener.getTimePassedSinceLastDamageInSeconds(playerName);
			if(onlyAfterNotDamaged > lastDamage) return false;
		}
		
		//check blocks on
		if(!onlyOnBlocks.isEmpty()){
			Block feetblock = player.getLocation().getBlock();
			if(!onlyOnBlocks.contains(feetblock.getType())) return false;
		}
		
		//check cooldown
		String cooldownName = "trait." + getName();
		int playerUplinkTime = plugin.getCooldownManager().stillHasCooldown(playerName, cooldownName);
		
		if(playerUplinkTime > 0){
			if(!triggerButHasUplink(event)){
				if(notifyTriggeredUplinkTime()){
					LanguageAPI.sendTranslatedMessage(player, trait_cooldown, 
							"seconds", String.valueOf(playerUplinkTime),
							"name", getDisplayName());
				}
			}
			
			return false;
		}
		
		//Only check if we really need. Otherwise we would use resources we don't need
		if(onlyOnDay || onlyInNight){
			//Daytime check
			int hour = ((int) (player.getWorld().getTime() / 1000l) + 8) % 24;
			boolean isNight = hour > 18 || hour < 6;
			boolean isDay = hour > 6 && hour < 18;
			
			//Check day
			if(onlyOnDay && isNight && !onlyInNight){
				return false;
			}
			
			//Check night
			if(onlyInNight && isDay && !onlyOnDay){
				return false;
			}
		}
		
		return true;
	}
	
	@Override
	public boolean isStackable(){
		return true;
	}

	@Override
	public int getMaxUplinkTime() {
		return cooldownTime;
	}

	@Override
	public boolean triggerButHasUplink(Event event) {
		return false;
	}
	
	@Override
	public String toString(){
		return getDisplayName();
	}

	
	/**
	 * Can and should be overriden.
	 */
	@Override
	public boolean notifyTriggeredUplinkTime() {
		return true;
	}

	@Override
	public String getDisplayName() {
		return displayName == null ? getName() : displayName;
	}

	
}
