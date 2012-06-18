package de.tobiyas.races.datacontainer.traitcontainer.traits.passive;

import java.util.HashMap;
import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;
import de.tobiyas.races.Races;
import de.tobiyas.races.configuration.traits.TraitConfig;
import de.tobiyas.races.configuration.traits.TraitConfigManager;
import de.tobiyas.races.datacontainer.health.HealthManager;
import de.tobiyas.races.datacontainer.traitcontainer.eventmanagement.TraitEventManager;
import de.tobiyas.races.datacontainer.traitcontainer.eventmanagement.events.EntityDamageDoubleEvent;
import de.tobiyas.races.datacontainer.traitcontainer.traits.TraitsWithUplink;
import de.tobiyas.races.datacontainer.traitholdercontainer.TraitHolderCombinder;
import de.tobiyas.races.datacontainer.traitholdercontainer.classes.ClassContainer;
import de.tobiyas.races.datacontainer.traitholdercontainer.race.RaceContainer;

public class DwarfSkinTrait implements TraitsWithUplink {

	private double value;
	private String Operation;
	
	private RaceContainer raceContainer = null;
	private ClassContainer classContainer = null;
	
	private HashMap<String, Integer> uplinkMap = new HashMap<String, Integer>();
	
	private static int uplinkTime = 60*20;
	private static int duration = 10*20;
	private static double activationLimit = 0.3;
	

	public DwarfSkinTrait(RaceContainer raceContainer){
		this.raceContainer = raceContainer;
	}
	
	public DwarfSkinTrait(ClassContainer classContainer){
		this.classContainer = classContainer;
	}
	
	@Override
	public void generalInit() {
		HashSet<Class<?>> listenedEvents = new HashSet<Class<?>>();
		listenedEvents.add(EntityDamageEvent.class);
		listenedEvents.add(EntityDamageDoubleEvent.class);
		TraitEventManager.getInstance().registerTrait(this, listenedEvents);
		
		TraitConfig config = TraitConfigManager.getInstance().getConfigOfTrait(getName());
		if(config != null){
			uplinkTime = (int) config.getValue("trait.uplink", 60) * 20;
			duration = (int) config.getValue("trait.duration", 10) * 20;
			activationLimit = (double) config.getValue("trait.activationLimit", 0.3);
		}
	}

	@Override
	public String getName() {
		return "DwarfSkinTrait";
	}

	@Override
	public RaceContainer getRace() {
		return raceContainer;
	}

	@Override
	public ClassContainer getClazz() {
		return classContainer;
	}

	@Override
	public Object getValue() {
		
		return value;
	}

	@Override
	public String getValueString() {
		return "damage Reduce: " + Operation + value;
	}

	@Override
	public void setValue(Object obj) {
		String opAndVal = String.valueOf(obj);
		value = evaluateValue(opAndVal);
	}
	
	private double evaluateValue(String val){
		char firstChar = val.charAt(0);
		
		Operation = "";
		
		if(firstChar == '+')
			Operation = "+";
		
		if(firstChar == '*')
			Operation = "*";
		
		if(firstChar == '-')
			Operation = "-";
		
		if(Operation == "")
			Operation = "*";
		else
			val = val.substring(1, val.length());
		
		return Double.valueOf(val);
	}

	@Override
	public boolean modify(Event event) {
		if(!(event instanceof EntityDamageEvent || event instanceof EntityDamageDoubleEvent)) return false;
		
		Entity entity = null;
		if(event instanceof EntityDamageDoubleEvent)
			entity = ((EntityDamageDoubleEvent) event).getEntity();
		else
			entity = ((EntityDamageEvent) event).getEntity();
		
		if(entity.getType() != EntityType.PLAYER) return false;
		Player player = (Player) entity;
		
		if(TraitHolderCombinder.checkContainer(player.getName(), this)){
			double maxHealth = HealthManager.getHealthManager().getMaxHealthOfPlayer(player.getName());
			double currentHealth =  HealthManager.getHealthManager().getHealthOfPlayer(player.getName());
			double healthPercent = currentHealth / maxHealth;
			if(healthPercent > activationLimit) return false;
			
			if(checkUplinkOrActive(player, false)) return false;
			
			if(event instanceof EntityDamageDoubleEvent){
				double damage = ((EntityDamageDoubleEvent) event).getDoubleValueDamage();
				double newDamage = getNewValue(damage);
				((EntityDamageDoubleEvent) event).setDoubleValueDamage(newDamage);
			}else{
				int damage = ((EntityDamageEvent) event).getDamage();
				double newDamage = getNewValue(damage);
				((EntityDamageEvent) event).setDamage((int) newDamage);
			}
	
			return true;
		}
		return false;
	}
	
	private boolean checkUplinkOrActive(Player player, boolean notify){
		if(uplinkMap.containsKey(player.getName())){
			int remainingTime = uplinkMap.get(player.getName());
			if((remainingTime - uplinkTime) > 0)
				return false;
			
			if(notify){	
				player.sendMessage(ChatColor.RED + "You still have " + ChatColor.LIGHT_PURPLE + remainingTime + 
									ChatColor.RED + " seconds uplink on " + ChatColor.LIGHT_PURPLE + getName());
			}
			return true;
		}
		
		uplinkMap.put(player.getName(), uplinkTime + duration);
		player.sendMessage(ChatColor.LIGHT_PURPLE + getName() + ChatColor.GREEN + " toggled.");
		return false;
	}
	
	private double getNewValue(double oldDmg){
		double newDmg = 0;
		switch(Operation){
			case "+": newDmg = oldDmg + value; break;
			case "-" : newDmg = oldDmg - value; break;
			case "*": newDmg = oldDmg * value; break;
			default:  newDmg = oldDmg * value; break;
		}
		
		if(newDmg < 0) newDmg = 0;
		return newDmg;
	}
	@Override
	public boolean isVisible() {
		return true;
	}

	@Override
	public void tickReduceUplink() {
		for(String player : uplinkMap.keySet()){
			int remainingTime = uplinkMap.get(player);
			remainingTime -= Races.getPlugin().interactConfig().getconfig_globalUplinkTickPresition();
			
			if(remainingTime == uplinkTime){
				Player tempPlayer = Bukkit.getPlayer(player);
				if(tempPlayer != null)
					tempPlayer.sendMessage(ChatColor.LIGHT_PURPLE + getName() + ChatColor.RED + " has faded.");
			}
				
			if(remainingTime <= 0)
				uplinkMap.remove(player);
			else
				uplinkMap.put(player, remainingTime);
		}
	}


}
