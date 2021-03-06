package de.tobiyas.racesandclasses.configuration.member;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;

import de.tobiyas.racesandclasses.RacesAndClasses;
import de.tobiyas.racesandclasses.configuration.member.database.DBMemberConfig;
import de.tobiyas.racesandclasses.configuration.member.file.MemberConfig;
import de.tobiyas.racesandclasses.persistence.file.YAMLPersistenceProvider;


public class MemberConfigManager {

	/**
	 * The config of members: player -> Config 
	 */
	private Map<String, MemberConfig> memberConfigs;
	
	
	/**
	 * The plugin to call stuff on
	 */
	private RacesAndClasses plugin;
	
	
	private int bukkitTaskID = -1;
	
	/**
	 * Creates the member config
	 */
	public MemberConfigManager(){
		plugin = RacesAndClasses.getPlugin();
		memberConfigs = new HashMap<String, MemberConfig>();
	}
	
	
	public void shutDown(){
		if(bukkitTaskID > 0){
			Bukkit.getScheduler().cancelTask(bukkitTaskID);
			bukkitTaskID = -1;
		}
	}
	
	/**
	 * creates member config file + reload the config
	 */
	@SuppressWarnings("deprecation") //This is async scheduling for performance.
	public void reload(){
		memberConfigs = new HashMap<String, MemberConfig>();
		shutDown();
		
		if(!plugin.getConfigManager().getGeneralConfig().isConfig_savePlayerDataToDB()){
			loadConfigs();
		}
		
		int savingTime = 20 * 60 * 10;
		bukkitTaskID = Bukkit.getScheduler().scheduleAsyncRepeatingTask(plugin, new Runnable() {
			
			@Override
			public void run() {
				try{
					saveConfigs();
				}catch(Throwable exp){} //Ignore all saving errors.
			}
		}, savingTime, savingTime);
	}
	
	
	/**
	 * Creates a new MemberConfig for a player and saves it.
	 * If the Config of the player is already present, it will be returned instead.
	 * 
	 * @param player
	 * @return
	 */
	private MemberConfig getCreateNewConfig(String player){
		if(memberConfigs.containsKey(player)){
			return memberConfigs.get(player);
		}
		
		MemberConfig config = null;
		boolean useDB = plugin.getConfigManager().getGeneralConfig().isConfig_savePlayerDataToDB();
		if(useDB){
			config = DBMemberConfig.createMemberConfig(player);
		}else{
			config = MemberConfig.createMemberConfig(player).save();
		}
		
		memberConfigs.put(player, config);
		return config;
	}
	
	/**
	 * Loads all configs from the playerdata file
	 */
	private void loadConfigs(){
		Set<String> playerList = new HashSet<String>(YAMLPersistenceProvider.getAllPlayersKnown());
		
		for(String player : playerList){
			getCreateNewConfig(player);
		}
	}
	
	/**
	 * Forces save to all configs
	 */
	public void saveConfigs(){
		for(String player : memberConfigs.keySet()){
			MemberConfig config = memberConfigs.get(player);
			if(config != null) {
				if(config instanceof DBMemberConfig){
					((DBMemberConfig) config).save();
				}else{
					config.save();
				}
			}
		}
	}
	
	/**
	 * Returns the personal MemberConfig of a player
	 * If not existent a new one is created.
	 * 
	 * @param player to get the config from
	 * @return
	 */
	public MemberConfig getConfigOfPlayer(String player){
		return getCreateNewConfig(player);
	}
	
}
