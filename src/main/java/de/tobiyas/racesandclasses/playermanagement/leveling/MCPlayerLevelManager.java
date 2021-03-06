package de.tobiyas.racesandclasses.playermanagement.leveling;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import de.tobiyas.racesandclasses.eventprocessing.events.leveling.PlayerLostEXPEvent;
import de.tobiyas.racesandclasses.eventprocessing.events.leveling.PlayerReceiveEXPEvent;
import de.tobiyas.racesandclasses.persistence.file.YAMLPersistenceProvider;
import de.tobiyas.racesandclasses.playermanagement.PlayerSavingContainer;
import de.tobiyas.util.config.YAMLConfigExtended;

public class MCPlayerLevelManager implements PlayerLevelManager{

	/**
	 * The Player this level Manager is belonging to.
	 */
	private final String playerName;
	
	
	/**
	 * Creates a default MC Level Manager with MC Player Levels.
	 * 
	 * @param playerName to create for
	 */
	public MCPlayerLevelManager(String playerName) {
		this.playerName = playerName;
	}
	
	
	@Override
	public int getCurrentLevel() {
		return getPlayer().getLevel();
	}

	@Override
	public int getCurrentExpOfLevel() {
		return (int) (getPlayer().getExp() * getPlayer().getExpToLevel());
	}

	@Override
	public String getPlayerName() {
		return playerName;
	}

	@Override
	public void setCurrentLevel(int level) {
		getPlayer().setLevel(level);
	}

	@Override
	public void setCurrentExpOfLevel(int currentExpOfLevel) {
		getPlayer().setExp(currentExpOfLevel / getPlayer().getExpToLevel());
	}

	@Override
	public boolean addExp(int exp) {
		PlayerReceiveEXPEvent expEvent = new PlayerReceiveEXPEvent(playerName, exp);
		
		Bukkit.getPluginManager().callEvent(expEvent);
		if(expEvent.isCancelled()){
			return false;
		}
		
		exp = expEvent.getExp();
		if(exp < 1){
			return false;
		}
		
		getPlayer().giveExp(exp);
		return true;
	}

	@Override
	public boolean removeExp(int exp) {
		PlayerLostEXPEvent expEvent = new PlayerLostEXPEvent(playerName, exp);
		
		Bukkit.getPluginManager().callEvent(expEvent);
		if(expEvent.isCancelled()){
			return false;
		}
		
		exp = expEvent.getExp();
		if(exp < 1){
			return false;
		}		
		
		int totalExp = getPlayer().getTotalExperience();
		int newTotalExp = totalExp - exp;
		
		if(newTotalExp < 0) newTotalExp = 0;
		getPlayer().setTotalExperience(newTotalExp);
		return true;
	}

	@Override
	public void save() {
		YAMLConfigExtended config = YAMLPersistenceProvider.getLoadedPlayerFile(playerName);
		if(!config.getValidLoad()){
			return;
		}
		
		Player player = getPlayer();
	
		config.set("playerdata." + playerName + CustomPlayerLevelManager.CURRENT_PLAYER_LEVEL_PATH, player.getLevel());
		config.set("playerdata." + playerName + CustomPlayerLevelManager.CURRENT_PLAYER_LEVEL_EXP_PATH, (int)(player.getExp() * player.getExpToLevel()));
	}

	@Override
	public void saveTo(PlayerSavingContainer container) {
		Player player = getPlayer();
		
		container.setPlayerLevel(player.getLevel());
		container.setPlayerLevelExp((int)(player.getExp() * player.getExpToLevel()));
		
	}

	@Override
	public void reloadFromPlayerSavingContaienr(PlayerSavingContainer container){
		Player player = getPlayer();
		
		player.setLevel(container.getPlayerLevel());
		player.setExp(container.getPlayerLevelExp() / player.getExpToLevel());
	}

	@Override
	public void checkLevelChanged() {
	}

	@Override
	public void reloadFromYaml() {
		YAMLConfigExtended config = YAMLPersistenceProvider.getLoadedPlayerFile(playerName);
		if(!config.getValidLoad()){
			return;
		}
		
		Player player = getPlayer();
		
		player.setLevel(config.getInt("playerdata." + playerName + CustomPlayerLevelManager.CURRENT_PLAYER_LEVEL_PATH, 1));
		player.setExp(config.getInt("playerdata." + playerName + CustomPlayerLevelManager.CURRENT_PLAYER_LEVEL_EXP_PATH, 1)
				/ player.getExpToLevel());
	}

	
	/**
	 * Returns the Player associated with this container.
	 * 
	 * @return
	 */
	private Player getPlayer(){
		return Bukkit.getPlayer(playerName);
	}


	@Override
	public void forceDisplay() {
		//we have no display to force... The EXP bar is our display.
	}


	@Override
	public boolean canRemove(int toRemove) {
		toRemove -= getCurrentExpOfLevel();
		return toRemove > 0;
	}
	
}
