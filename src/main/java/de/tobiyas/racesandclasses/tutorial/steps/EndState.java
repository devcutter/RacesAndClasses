package de.tobiyas.racesandclasses.tutorial.steps;

import org.bukkit.ChatColor;

import de.tobiyas.racesandclasses.tutorial.TutorialPath;
import de.tobiyas.racesandclasses.util.tutorial.TutorialState;

public class EndState extends AbstractStep {

	public EndState(String playerName, TutorialPath writeBack){
		this.playerName = playerName;
		this.writeBack = writeBack;
		this.currentState = TutorialState.end;
		
		this.startMessage = ChatColor.YELLOW + "[Tutorial]: Congratulations! You completed the Tutorial. Use " + ChatColor.RED +
							"/racestutorial stop" + ChatColor.YELLOW + " to end the Tutorial.";
		this.doneMessage = ChatColor.YELLOW + "[Tutorial]: Very good!";
		
		finished = false;
		postStartMessage();
	}
}
