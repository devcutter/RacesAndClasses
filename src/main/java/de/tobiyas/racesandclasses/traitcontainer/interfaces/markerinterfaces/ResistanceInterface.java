package de.tobiyas.racesandclasses.traitcontainer.interfaces.markerinterfaces;

import java.util.List;

import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public interface ResistanceInterface{
	
	/**
	 * The Resistences the Trait is modifying
	 * @return
	 */
	public List<DamageCause> getResistanceTypes();
}