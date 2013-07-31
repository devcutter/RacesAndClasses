package de.tobiyas.racesandclasses.traitcontainer.interfaces;

import java.util.Map;

import org.bukkit.event.Event;

import de.tobiyas.racesandclasses.datacontainer.traitholdercontainer.AbstractTraitHolder;

public interface Trait{
	
	/**
	 * This method is called when the Trait is load the first time.
	 * <br>
	 * <br>IMPORTATNT: This method has to be annotated with {@link TraitInfos}
	 * <br>For Example: @TraitInfos(traitName="DummyTrait", category="Useless")
	 */
	public void importTrait();
	
	
	/**
	 * This method is called after creation of the Trait.
	 * <br>The {@link AbstractTraitHolder} is already set to this time.
	 * <br>
	 * <br>IMPORTATNT: This method has to be annotated with {@link TraitEventsUsed}
	 * <br>For Example: @TraitEventsUsed(registerdClasses = {EntityDamageByEntityDoubleEvent.class})
	 */
	public void generalInit();
	
	
	/**
	 * Returns the name of the Trait
	 * @return
	 */
	public String getName();
	
	
	/**
	 * gets a string representation of the Configuration
	 * @return
	 */
	public String getPrettyConfiguration();
	
	

	/**
	 * Sets the Configuration of the Trait
	 * 
	 * <br>IMPORTATNT: This method has to be annotated with {@link TraitConfigurationNeeded}
	 * <br>For Example: @TraitConfigurationNeeded(neededFields = {"operation", "value"})
	 * 
	 * @param map to set the config with.
	 */
	public void setConfiguration(Map<String, String> configMap);
	
	
	/**
	 * The general modify that is called, when the event wanted triggered.
	 * 
	 * @param event
	 * @return
	 */
	public boolean modify(Event event);

	
	/**
	 * Indecates if the same trait, but with different config is better.
	 * 
	 * @param trait
	 * @return
	 */
	public boolean isBetterThan(Trait trait);

	
	/**
	 * Setter for the TraitHolder.
	 * This is called after Init.
	 * 
	 * @param abstractTraitHolder
	 */
	public void setTraitHolder(AbstractTraitHolder abstractTraitHolder);
	
	
	/**
	 * Returns the TraitHolder that this Trait belongs to.
	 * @return
	 */
	public AbstractTraitHolder getTraitHolder();
	
}
