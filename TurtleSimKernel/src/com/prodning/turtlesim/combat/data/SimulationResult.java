package com.prodning.turtlesim.combat.data;

import java.util.HashMap;

import com.prodning.turtlesim.combat.CombatEntity;
import com.prodning.turtlesim.combat.CombatEntity.CombatEntityType;
import com.prodning.turtlesim.combat.CombatSettings;
import com.prodning.turtlesim.combat.Fleet;
import com.prodning.turtlesim.combat.data.MacroCombatResult.ResultType;
import com.prodning.turtlesim.data.Resource;
import com.prodning.turtlesim.parse.EntityFileParser;

public class SimulationResult {
	private int numberOfSimulations = 0;

	private HashMap<String, FCUSimulationResult> fcuSimResults = new HashMap<String, FCUSimulationResult>();

	private Resource debrisField = new Resource();
	private double attackerWinChance = 0;
	private double defenderWinChance = 0;
	private double drawChance = 0;
	private double rounds = 0;

	private Resource debrisFieldSum = new Resource();
	private double attackerWins = 0;
	private double defenderWins = 0;
	private double draws = 0;
	private double roundsSum = 0;

	public void addCombatResult(MacroCombatResult macroCombatResult) {
		numberOfSimulations++;
		for (FleetCombatUnit fcu : macroCombatResult.getFleetCombatUnits()) {

			FCUSimulationResult fcuSimResult = new FCUSimulationResult();

			Resource debrisFieldThisCombat = new Resource();

			// Get attacker and defender resource losses
			for (CombatEntity entity : fcu.getLosses()) {
				fcuSimResult.getTotalLosses().addThis(
						EntityFileParser.getResourceById(entity.getEntityID()));
			}

			// Get attacker and defender debris field contributions
			for (CombatEntity entity : fcu.getLosses()) {
				if (entity.getType() == CombatEntityType.SHIP
						|| CombatSettings.getDefenseToDebris()) {
					Resource debrisResource;

					if (entity.getType() == CombatEntityType.SHIP)
						debrisResource = EntityFileParser.getResourceById(
								entity.getEntityID()).scalar(
								CombatSettings.getShipDebrisRatio());
					else if (entity.getType() == CombatEntityType.DEFENSE)
						debrisResource = EntityFileParser.getResourceById(
								entity.getEntityID()).scalar(
								CombatSettings.getDefenseDebrisRatio());
					else {
						// TODO error checking
						debrisResource = new Resource(-100000000, -100000000,
								-100000000);
					}

					// no deut in debris field
					debrisResource.setDeuterium(0);

					debrisFieldThisCombat.addThis(debrisResource);
				}
			}

			debrisFieldSum.addThis(debrisFieldThisCombat);

			debrisField = new Resource(debrisFieldSum.getMetal()
					/ numberOfSimulations, debrisFieldSum.getCrystal()
					/ numberOfSimulations, debrisFieldSum.getDeuterium()
					/ numberOfSimulations);

			HashMap<String, Integer> newComposition = Fleet.compositionIdToName(fcu.getFleet().getFleetComposition());
			HashMap<String, Integer> fleetCompositionSum;
			if(fcuSimResults.containsKey(fcu.getId()))
				fleetCompositionSum = fcuSimResults.get(fcu.getId()).getFleetCompositionSum();
			else
				fleetCompositionSum = new HashMap<String, Integer>();

			for (String s : newComposition.keySet()) {
				if (fleetCompositionSum.containsKey(s))
					fleetCompositionSum.put(s, fleetCompositionSum.get(s)
							+ newComposition.get(s));
				else
					fleetCompositionSum.put(s, newComposition.get(s));
			}
		}

		attackerWins += (macroCombatResult.getResultType() == ResultType.ATTACKER_WIN ? 1
				: 0);
		defenderWins += (macroCombatResult.getResultType() == ResultType.DEFENDER_WIN ? 1
				: 0);
		draws += (macroCombatResult.getResultType() == ResultType.DRAW ? 1 : 0);

		attackerWinChance = attackerWins / numberOfSimulations;
		defenderWinChance = defenderWins / numberOfSimulations;
		drawChance = draws / numberOfSimulations;

		roundsSum += macroCombatResult.getRounds();

		rounds = roundsSum / numberOfSimulations;

	}

	public int getNumberOfSimulations() {
		return numberOfSimulations;
	}

	public Resource getDebrisField() {
		return debrisField;
	}

	public double getAttackerWinChance() {
		return attackerWinChance;
	}

	public double getDefenderWinChance() {
		return defenderWinChance;
	}

	public double getDrawChance() {
		return drawChance;
	}

	public double getRounds() {
		return rounds;
	}
}
