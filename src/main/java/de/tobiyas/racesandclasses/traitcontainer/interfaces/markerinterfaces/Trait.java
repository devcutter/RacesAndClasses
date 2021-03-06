package de.tobiyas.racesandclasses.traitcontainer.interfaces.markerinterfaces;

import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import de.tobiyas.racesandclasses.datacontainer.traitholdercontainer.AbstractTraitHolder;
import de.tobiyas.racesandclasses.traitcontainer.interfaces.annotations.configuration.TraitConfigurationNeeded;
import de.tobiyas.racesandclasses.traitcontainer.interfaces.annotations.configuration.TraitEventsUsed;
import de.tobiyas.racesandclasses.traitcontainer.interfaces.annotations.configuration.TraitInfos;
import de.tobiyas.racesandclasses.util.traitutil.TraitConfigurationFailedException;

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
	 * <br>The Config is already set.
	 * <br>
	 * <br>IMPORTATNT: This method has to be annotated with {@link TraitEventsUsed}
	 * <br>For Example: @TraitEventsUsed(registerdClasses = {EntityDamageByEntityDoubleEvent.class})
	 */
	public void generalInit();
	
	
	/**
	 * Indicates that the Trait is beeing destroyed and should unregister all stuff allocated.
	 */
	public void deInit();
	
	
	/**
	 * Returns the name of the Trait
	 * <br>This must be a atomar Action! 
	 * <br>Always return a String that is NO variable!!!
	 * 
	 * @return Name of the Trait.
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
	 * <br>For Example: @TraitConfigurationNeeded({ @TraitConfigurationField("value", "Integer.class") })
	 * 
	 * @param map to set the config with.
	 * 
	 * @throws throws an Config Exception if something gone wrong with configuring.
	 */
	public void setConfiguration(Map<String, Object> configurationMap) throws TraitConfigurationFailedException;
	
	
	/**
	 * Returns a list of optional Config fields.
	 * 
	 * @return the List of optional Config Fields.
	 */
	public List<String> getOptionalConfigFields();
	
	/**
	 * Returns the Config passed in {@link #setConfiguration(Map)}.
	 * 
	 * @return the map of the config already passed.
	 */
	public Map<String, Object> getCurrentconfig();
	
	/**
	 * The general modify that is called, when the event wanted triggered.
	 * 
	 * @param event
	 * @return
	 */
	public boolean trigger(Event event);
	
	/**
	 * Extracts the relevant player from the Event
	 * and returns it.
	 * 
	 * If not interested, return null;
	 * 
	 * @param event
	 * @return
	 */
	public Player getReleventPlayer(Event event);
	
	
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


	/**
	 * Checks if the Trait can be triggered.
	 * <br>Already checked are: 
	 * <br>* Trait belongs to the Player.
	 * <br>* If Trait has any Uplink / Mana Restrictions
	 * 
	 * @param event that is to be triggered
	 * 
	 * @return true if all Trait preconditions are checked
	 */
	public boolean canBeTriggered(Event event);
	
	
	/**
	 * Returns if the Trait is Stackable.
	 * 
	 * This means a player can have this Trait more that 1 time.
	 * 
	 * @return true if stackable, false if not.
	 */
	public boolean isStackable();
	
	/**
	 * Returns the Display name of the Trait.
	 * <br>The DisplayName is by default the TraitName
	 * 
	 * @return the DisplayName of the Trait.
	 */
	public String getDisplayName();
	
}
