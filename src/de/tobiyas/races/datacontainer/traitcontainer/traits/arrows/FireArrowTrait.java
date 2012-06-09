package de.tobiyas.races.datacontainer.traitcontainer.traits.arrows;

import java.util.HashSet;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import de.tobiyas.races.datacontainer.health.damagetickers.DamageTicker;
import de.tobiyas.races.datacontainer.traitholdercontainer.classes.ClassContainer;
import de.tobiyas.races.datacontainer.traitholdercontainer.race.RaceContainer;
import de.tobiyas.races.datacontainer.traitcontainer.eventmanagement.TraitEventManager;

public class FireArrowTrait extends AbstractArrow {
	
	public FireArrowTrait(RaceContainer raceContainer){
		this.raceContainer = raceContainer;
	}
	
	public FireArrowTrait(ClassContainer classContainer){
		this.classContainer = classContainer;
	}
	
	@Override
	public void generalInit(){
		HashSet<Class<?>> listenedEvents = new HashSet<Class<?>>();
		listenedEvents.add(EntityDamageByEntityEvent.class);
		listenedEvents.add(PlayerInteractEvent.class);
		listenedEvents.add(EntityShootBowEvent.class);
		TraitEventManager.getInstance().registerTrait(this, listenedEvents);
	}
	
	@Override
	public String getName() {
		return "FireArrowTrait";
	}

	@Override
	public String getValueString() {
		return this.totalDamage + " Fire-Damage over " + this.duration + " seconds.";
	}

	@Override
	public void setValue(Object obj) {
		String preValue = (String) obj;
		
		String[] split = preValue.split("#");
		if(split.length != 2)
			split = new String[]{"10", "10"};
		
		duration = Integer.valueOf(split[0]);
		this.totalDamage = Double.valueOf(split[0]);
	}

	
	@Override
	protected boolean onShoot(EntityShootBowEvent event){
		Location loc = event.getEntity().getLocation();
		loc.getWorld().playEffect(loc, Effect.MOBSPAWNER_FLAMES, 0);
		return false;
	}
	
	@Override
	protected boolean onHitEntity(EntityDamageByEntityEvent event){
		Entity hitTarget = event.getEntity();
		if(!(hitTarget instanceof LivingEntity)) return false;
		
		int damagePerTick = (int) Math.ceil(totalDamage / duration);
		DamageTicker ticker = new DamageTicker((LivingEntity) hitTarget, duration, damagePerTick, DamageCause.FIRE_TICK);
		ticker.playEffectOnDmg(Effect.MOBSPAWNER_FLAMES, 4);
		return true;
	}

	@Override
	public boolean isVisible(){
		return true;
	}

	@Override
	protected String getArrowName(){
		return "Fire Arrow";
	}

	@Override
	protected boolean onHitLocation(ProjectileHitEvent event) {
		//Not needed
		return false;
	}

	public static void pasteHelpForTrait(CommandSender sender) {
		sender.sendMessage(ChatColor.YELLOW + "If you hit an enemy with an arrow and choosen the Fire Arrow as current arrow,");
		sender.sendMessage(ChatColor.YELLOW + "He will start burning.");
	}
}