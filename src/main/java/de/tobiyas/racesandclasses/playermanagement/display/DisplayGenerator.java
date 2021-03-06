package de.tobiyas.racesandclasses.playermanagement.display;

import de.tobiyas.racesandclasses.RacesAndClasses;
import de.tobiyas.racesandclasses.configuration.member.file.MemberConfig;
import de.tobiyas.racesandclasses.playermanagement.display.Display.DisplayInfos;
import de.tobiyas.racesandclasses.playermanagement.display.Display.DisplayType;

public class DisplayGenerator {

	
	/**
	 * Creates a Display for the passed infos and Player.
	 * 
	 * @param playerName to create
	 * @param infos to create
	 * 
	 * @return the generated Display
	 */
	public static Display generateDisplay(String playerName, DisplayInfos infos){
		RacesAndClasses plugin = RacesAndClasses.getPlugin();
		
		MemberConfig config = plugin.getConfigManager().getMemberConfigManager().getConfigOfPlayer(playerName);
		Object displayTypeAsObject = config.getValueDisplayName("displayType");
		String displayType = "score";
		
		boolean disableScoreboard = plugin.getConfigManager().getGeneralConfig().isConfig_disableAllScoreboardOutputs();
				
		if(displayTypeAsObject != null && displayTypeAsObject instanceof String){
			displayType = (String) displayTypeAsObject;
		}
		
		DisplayType type = DisplayType.resolve(displayType);
		if(disableScoreboard && type == DisplayType.Scoreboard){
			type = DisplayType.Chat;
		}
		
		return generateFromType(type, playerName, infos);
	}
	
	
	/**
	 * Generates the Display finally for the passed args.
	 * 
	 * @param type to create
	 * @param name the playerName to create to
	 * @param infos the infos for the Display to configure
	 * 
	 * @return the generated Display
	 */
	private static Display generateFromType(DisplayType type, String name, DisplayInfos infos){
		switch (type) {
		case Chat:
			return new ChatDisplayBar(name, infos);
		
		case Scoreboard:
			return new NewScoreBoardDisplayBar(name, infos);
			
		default:
			return new ChatDisplayBar(name, infos);
		}
	}
}
