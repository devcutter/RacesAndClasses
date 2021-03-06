package de.tobiyas.racesandclasses.APIs;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import de.tobiyas.racesandclasses.RacesAndClasses;
import de.tobiyas.racesandclasses.entitystatusmanager.stun.StunManager;

public class StunAPI {

	/**
	 * Returns the StunManager.
	 * 
	 * @return StunManager.
	 */
	private static StunManager getStunManager(){
		RacesAndClasses plugin = RacesAndClasses.getPlugin();
		return plugin.getStunManager();
	}
	
	/**
	 * The StunAPI for Players
	 * 
	 * @author tobiyas
	 */
	public static class StunPlayer{
		
		/**
		 * Stuns a player for x Ticks.
		 * 
		 * @param playerName to stun
		 * @param time to stun for
		 * 
		 * @return true if worked, false otherwise.
		 */
		public static boolean stunPlayerForTicks(String playerName, int time){
			return stunPlayerForTicks(Bukkit.getPlayer(playerName), time);
		}
	
		/**
		 * Stuns a player for x Seconds.
		 * 
		 * @param playerName to stun
		 * @param time to stun for
		 * 
		 * @return true if worked, false otherwise.
		 */
		public static boolean stunPlayerForSeconds(String playerName, int time){
			return stunPlayerForSeconds(Bukkit.getPlayer(playerName), time);
		}
	
		/**
		 * Stuns a player for x Ticks.
		 * 
		 * @param player to stun
		 * @param time to stun for
		 * 
		 * @return true if worked, false otherwise.
		 */
		public static boolean stunPlayerForTicks(Player player, int time){
			if(player == null || time <= 0){
				return false;
			}
			
			return getStunManager().stunEntity(player, time); 
		}
	
		/**
		 * Stuns a player for x Seconds.
		 * 
		 * @param player to stun
		 * @param time to stun for
		 * 
		 * @return true if worked, false otherwise.
		 */
		public static boolean stunPlayerForSeconds(Player player, int time){
			if(player == null || time <= 0){
				return false;
			}
			
			return getStunManager().stunEntity(player, time * 20); 
		}
		
		
		
		/**
		 * Removes the Stun from a player
		 * 
		 * @param playerName to remove from
		 * 
		 * @return true if worked, false otherwise
		 */
		public static boolean removeStun(String playerName){
			return removeStun(Bukkit.getPlayer(playerName));
		}
		
		/**
		 * Removes the Stun from a player
		 * 
		 * @param player to remove from
		 * 
		 * @return true if worked, false otherwise
		 */
		public static boolean removeStun(Player player){
			if(player == null){
				return false;
			}
			
			return getStunManager().removeStun(player);
		}
		
		
		/**
		 * Returns the remaining Stun time the player has left in seconds.
		 * <br>Returns -1 if no Stun is found.
		 * 
		 * @param player to check
		 * 
		 * @return remaining time in seconds.
		 */
		public static int getRemainingStunTimeInSeconds(Player player){
			return getRemainingStunTimeInTicks(player) / 20;
		}

		/**
		 * Returns the remaining Stun time the player has left in ticks.
		 * <br>Returns -1 if no Stun is found.
		 * 
		 * @param player to check
		 * 
		 * @return remaining time in ticks.
		 */
		public static int getRemainingStunTimeInTicks(Player player){
			if(player == null){
				return -1;
			}
			
			return getStunManager().getRestStunTime(player);
		}
	}
	
	/**
	 * The StunAPI for Entitys
	 * 
	 * @author tobiyas
	 */
	public static class StunEntity{
		
		/**
		 * Stuns an Entity for x Ticks.
		 * 
		 * @param entity to stun
		 * @param time to stun for
		 * 
		 * @return true if worked, false otherwise.
		 */
		public static boolean stunEntityForTicks(Entity entity, int time){
			if(entity == null || time <= 0){
				return false;
			}
			
			return getStunManager().stunEntity(entity, time); 
		}
		
		/**
		 * Stuns an Entity for x Seconds.
		 * 
		 * @param entity to stun
		 * @param time to stun for
		 * 
		 * @return true if worked, false otherwise.
		 */
		public static boolean stunEntityForSeconds(Entity entity, int time){
			if(entity == null || time <= 0){
				return false;
			}
			
			return getStunManager().stunEntity(entity, time * 20); 
		}
		
		
		/**
		 * Removes the Stun from an Entity
		 * 
		 * @param entity to remove from
		 * 
		 * @return true if worked, false otherwise
		 */
		public static boolean removeStun(Entity entity){
			if(entity == null){
				return false;
			}
			
			return getStunManager().removeStun(entity);
		}
		
		
		/**
		 * Returns the remaining Stun time the entity has left in seconds.
		 * <br>Returns -1 if no Stun is found.
		 * 
		 * @param entity to check
		 * 
		 * @return remaining time in seconds.
		 */
		public static int getRemainingStunTimeInSeconds(Entity entity){
			return getRemainingStunTimeInTicks(entity) / 20;
		}

		/**
		 * Returns the remaining Stun time the Entity has left in ticks.
		 * <br>Returns -1 if no Stun is found.
		 * 
		 * @param entity to check
		 * 
		 * @return remaining time in ticks.
		 */
		public static int getRemainingStunTimeInTicks(Entity entity){
			if(entity == null){
				return -1;
			}
			
			return getStunManager().getRestStunTime(entity);
		}
	}
}
