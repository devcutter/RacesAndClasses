package de.tobiyas.races.datacontainer.traitcontainer.traits.resistance;

import java.util.LinkedList;

import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import de.tobiyas.races.datacontainer.race.RaceContainer;
import de.tobiyas.races.datacontainer.traitcontainer.TraitEventManager;
import de.tobiyas.races.datacontainer.traitcontainer.traits.health.HealthManager;

public class FallResistanceTrait extends Resistance {
	
	public FallResistanceTrait(RaceContainer raceContainer){
		this.raceContainer = raceContainer;
		TraitEventManager.getTraitEventManager().registerTrait(this);
		
		resistances = new LinkedList<DamageCause>();
		resistances.add(DamageCause.FALL);
		
		addObserver(HealthManager.getHealthManager());
	}

	@Override
	public String getName() {
		return "FallResistanceTrait";
	}

	@Override
	public Object getValue() {
		return value;
	}

	@Override
	public String getValueString() {
		return String.valueOf(value);
	}

	@Override
	public void setValue(Object obj) {
		value = (Double) obj;
	}

}
