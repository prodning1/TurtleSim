package com.prodning.turtlesim.combat;

import java.util.List;

import com.prodning.turtlesim.combat.data.MacroCombatResult;
import com.prodning.turtlesim.combat.data.SimulationResult;
import com.prodning.turtlesim.combat.data.TechLevels;

public class CombatSimulation {
	public static SimulationResult SimulateFleetCombat(Fleet attackingFleet, Fleet defendingFleet, TechLevels macroCombatInformation, int numberOfSimulations) {
		SimulationResult result = new SimulationResult();
		
		for(int i = 0; i < numberOfSimulations; i++) {
			Fleet atkSimFleet = (Fleet) attackingFleet.deepClone();
			Fleet defSimFleet = (Fleet) defendingFleet.deepClone();
			
			MacroCombatResult mcr = atkSimFleet.attackFleet(defSimFleet);
			
			result.addCombatResult(mcr);
		}
		
		return result;
	}
	
	public static MacroCombatResult ACSAttack(List<Fleet> fleets) {
		
		return null;
	}
}