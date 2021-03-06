package de.tobiyas.racesandclasses.entitystatusmanager.poison;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import de.tobiyas.racesandclasses.RacesAndClasses;
import de.tobiyas.racesandclasses.util.bukkit.versioning.compatibility.CompatibilityModifier;

public class PoisonManager {

	/**
	 * The plugin to call stuff on.
	 */
	private final RacesAndClasses plugin;
	
	/**
	 * The taskID for the Bukkit task
	 */
	private int bukkitTaskID = -1;
	
	/**
	 * The Poison Map for the Plugin
	 */
	private final Map<LivingEntity, DamageOption> poisonMap = new ConcurrentHashMap<LivingEntity, DamageOption>(); 
	
	
	/**
	 * Creates the PoisonManager
	 */
	public PoisonManager() {
		plugin = RacesAndClasses.getPlugin();
	}
	
	
	/**
	 * Inits the Manager
	 */
	public void init(){
		poisonMap.clear();
		
		if(bukkitTaskID < 0){
			Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
				
				@Override
				public void run() {
					Iterator<Map.Entry<LivingEntity, DamageOption>> poisonTickerIterator = poisonMap.entrySet().iterator();
					while(poisonTickerIterator.hasNext()){
						Map.Entry<LivingEntity, DamageOption> entry = poisonTickerIterator.next();
						DamageOption options = entry.getValue();
						LivingEntity entity = entry.getKey();
						
						if(entity.isDead()){
							poisonTickerIterator.remove();
							continue;
						}
						
						int newTicks = options.ticks - 1;
						options.ticks = newTicks;
						
						if(newTicks % options.damageEveryTicks == 0){
							damageEntity(entity, options);
						}
						
						if(newTicks < 0){
							poisonTickerIterator.remove();
						}
					}
				}
				
			}, 1, 1);
		}
	}
	
	/**
	 * Damages an Entity for the Options passed
	 * 
	 * @param entity to damage
	 * @param options to apply
	 */
	protected void damageEntity(LivingEntity entity, DamageOption options) {
		double damagePerTick = options.damagePerTick;
		double damageForTickSpane = damagePerTick * options.damageEveryTicks;
		
		EntityDamageEvent event = CompatibilityModifier.EntityDamage.safeCreateEvent(entity, options.type, damageForTickSpane);
		damageFromPoisonManager = true;
		plugin.fireEventToBukkit(event);
		damageFromPoisonManager = false;
		
		if(!event.isCancelled()){
			double newDamage = CompatibilityModifier.EntityDamage.safeGetDamage(event);
			CompatibilityModifier.LivingEntity.safeDamageEntity(entity, newDamage);
		}
	}
	
	/**
	 * This indicates that the Plugin manager
	 */
	private boolean damageFromPoisonManager = false;
	
	@EventHandler
	public void onEntityDamage(EntityDamageEvent event){
		if(!(event.getEntity() instanceof LivingEntity)) return;
		
		LivingEntity entity = (LivingEntity) event.getEntity();
		if(isPoisoned(entity) && !damageFromPoisonManager){
			event.setCancelled(true);
		}
	}


	/**
	 * Removes all Stuff not needed any more.
	 */
	public void deinit(){
		if(bukkitTaskID > 0){
			Bukkit.getScheduler().cancelTask(bukkitTaskID);
			bukkitTaskID = -1;
		}
	}
	

	
	/**
	 * Returns the Rest stun time of the Entity.
	 * 
	 * @param entity to get from
	 * 
	 * @return time left in ticks or -1 if NOT poisoned.
	 */
	public int getRestPoisonTime(LivingEntity entity){
		if(entity == null){
			return -1;
		}
		
		if(!isPoisoned(entity)){
			return -1;
		}
		
		int time = poisonMap.get(entity).ticks;
		return time;
	}
	
	/**
	 * Returns the Rest damage from the Poison for the Entity.
	 * 
	 * @param entity to check
	 * 
	 * @return rest damage for the entity or 0 if not poisoned.
	 */
	public double getRestPoisonDamage(LivingEntity entity){
		if(!poisonMap.containsKey(entity)){
			return 0;
		}
		
		DamageOption options = poisonMap.get(entity);
		double damagePerTicks = options.damagePerTick;
		return damagePerTicks * options.ticks;
	}
	
	
	/**
	 * Poisons an Entity for x ticks.
	 * 
	 * @param entity to poison
	 * @param time to poison in ticks
	 * 
	 * @return if it worked.
	 */
	public boolean poisonEntity(LivingEntity entity, int ticks, double totalDamage, int damageEveryTicks, EntityDamageEvent.DamageCause cause){
		if(entity == null || cause == null || totalDamage <= 0 || damageEveryTicks <= 0 || ticks <= 0){
			return false;
		}
		
		double restDamage = getRestPoisonTime(entity);
		double newRestDamage = totalDamage;
		if(newRestDamage < restDamage){
			return false;
		}else{
			entity.removePotionEffect(PotionEffectType.POISON);
			entity.addPotionEffect(new PotionEffect(PotionEffectType.POISON, ticks, 1), true);
			
			if(!entity.hasPotionEffect(PotionEffectType.POISON)){
				return false;
			}
			
			DamageOption damageOptions = new DamageOption();
			damageOptions.ticks = ticks;
			damageOptions.type = cause;
			damageOptions.damageEveryTicks = damageEveryTicks;
			damageOptions.damagePerTick = totalDamage / ticks;
			
			poisonMap.put(entity, damageOptions);
			
			
			if(entity instanceof Player){
				int timeInSeconds = ticks / 20;
				
				((Player) entity).sendMessage(ChatColor.RED + "[RaC] You are poisoned for " 
						+ ChatColor.AQUA + timeInSeconds + ChatColor.RED + " Seconds.");
			}
			
			return true;
		}
	}
	
	/**
	 * Removes all Poison effects from the entity.
	 * 
	 * @param entity to un poison.
	 * @return true if worked, false otherwise.
	 */
	public boolean removePoisons(LivingEntity entity){
		if(entity == null){
			return false;
		}
		
		if(!isPoisoned(entity)){
			return false;
		}
		
		poisonMap.remove(entity);
		entity.removePotionEffect(PotionEffectType.POISON);
		
		return true;
	}
	
	
	/**
	 * Returns if the Entity is poisoned.
	 * 
	 * @param entity to check
	 * 
	 * @return true if poisoned, false otherwise.
	 */
	public boolean isPoisoned(LivingEntity entity){
		return poisonMap.containsKey(entity) || entity.hasPotionEffect(PotionEffectType.POISON);
	}
	
	
	
	
	/**
	 * The DamageOption for the PoisonManager to tick.
	 * 
	 * @author tobiyas
	 */
	private class DamageOption{
		
		/**
		 * The Effect to trigger
		 */
		private DamageCause type;
		
		/**
		 * ticks left to trigger
		 */
		private int ticks;
		
		/**
		 * the DamagePer Tick
		 */
		private double damagePerTick;

		/**
		 * The timings the Effect ticks.
		 */
		private int damageEveryTicks = 30;
	}
	
}
