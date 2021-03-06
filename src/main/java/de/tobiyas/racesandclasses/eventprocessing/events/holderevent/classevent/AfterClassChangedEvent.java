package de.tobiyas.racesandclasses.eventprocessing.events.holderevent.classevent;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import de.tobiyas.racesandclasses.datacontainer.traitholdercontainer.classes.ClassContainer;

public class AfterClassChangedEvent extends AfterClassSelectedEvent {

	/**
	 * The static list of all handlers that are interested in this event
	 */
	private static final HandlerList handlers = new HandlerList();
	
	/**
	 * The class to change from
	 */
	private ClassContainer oldClass;
	
	/**
	 * Player changed his class from another class.
	 * 
	 * @param player that changed
	 * @param classToSelect new Class
	 * @param oldClass old Class
	 */
	public AfterClassChangedEvent(Player player, ClassContainer classToSelect, ClassContainer oldClass) {
		super(player, classToSelect);
		
		this.oldClass = oldClass;
	}
	
	
	public ClassContainer getOldClass() {
		return oldClass;
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
