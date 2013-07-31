package de.tobiyas.racesandclasses.tutorial;

import java.util.Observable;

import de.tobiyas.racesandclasses.tutorial.TutorialManager;
import de.tobiyas.racesandclasses.tutorial.TutorialStepContainer;
import de.tobiyas.racesandclasses.util.tutorial.TutorialState;

public class TutorialNotifier extends Observable{

	public TutorialNotifier(){
		TutorialManager.registerObserver(this);
		this.setChanged();
	}
	
	
	public void generateContainer(String playerName, TutorialState state){
		TutorialStepContainer container = new TutorialStepContainer(playerName, state);
		fireContainer(container);
	}
	
	public void generateContainer(String playerName, TutorialState state, int subState){
		TutorialStepContainer container = new TutorialStepContainer(playerName, state, subState);
		fireContainer(container);
	}


	public void fireContainer(TutorialStepContainer container) {
		this.notifyObservers(container);
		this.setChanged();
	}
}