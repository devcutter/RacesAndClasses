package de.tobiyas.racesandclasses.eventprocessing.events.holderevent.classevent;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import de.tobiyas.racesandclasses.datacontainer.traitholdercontainer.classes.ClassContainer;
import de.tobiyas.racesandclasses.eventprocessing.events.holderevent.HolderPreSelectEvent;

/**
 * This Event is fired, when a Player tries to select a Class. 
 * 
 * @author Tobiyas
 */
public class PreClassSelectEvent extends HolderPreSelectEvent{

	/**
	 * The static list of all handlers that are interested in this event
	 */
	private static final HandlerList handlers = new HandlerList();
	
	
	/**
	 * A player has selected a class.
	 * 
	 * @param player that selected the class
	 * @param classToSelect that was selected
	 */
	public PreClassSelectEvent(Player player, ClassContainer classToSelect) {
		super(player,  classToSelect);
	}

	/**
	 * A player has selected a class.
	 * 
	 * @param player that selected the class
	 * @param raceToSelect that was selected
	 * @param checkPermissions if the permissions should be checked
	 * @param checkCooldown if the Cooldown should be checked
	 */
	public PreClassSelectEvent(Player player, ClassContainer classToSelect, boolean checkPermissions, boolean checkCooldown) {
		super(player, classToSelect, checkCooldown, checkPermissions);
	}
	

	public ClassContainer getClassToSelect() {
		return (ClassContainer) holderToSelect;
	}



	/**
	 * needed for Bukkit to get the list of Handlers interested
	 * @return
	 */
	public static HandlerList getHandlerList() {
        return handlers;
    }
	
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

}
