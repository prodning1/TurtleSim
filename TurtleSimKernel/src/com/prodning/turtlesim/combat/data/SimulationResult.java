package com.prodning.turtlesim.combat.data;

import java.util.HashMap;

import com.prodning.turtlesim.combat.CombatEntity;
import com.prodning.turtlesim.combat.Fleet;
import com.prodning.turtlesim.combat.CombatEntity.CombatEntityType;
import com.prodning.turtlesim.combat.data.MacroCombatResult.ResultType;
import com.prodning.turtlesim.data.Resource;
import com.prodning.turtlesim.parse.EntityFileParser;

public class SimulationResult {
	private int numberOfSimulations = 0;
	
	private Resource attackerLosses = new Resource();
	private Resource defenderLosses = new Resource();
	private Resource debrisField = new Resource();
	private HashMap<String, Double> attackerFleetComposition = new HashMap<String, Double>();
	private HashMap<String, Double> defenderFleetComposition = new HashMap<String, Double>();
	private double attackerWinChance = 0;
	private double defenderWinChance = 0;
	private double drawChance = 0;
	private double rounds = 0;

	private Resource attackerLossesSum = new Resource();
	private Resource defenderLossesSum = new Resource();
	private Resource debrisFieldSum = new Resource();
	private HashMap<String, Double> attackerFleetCompositionSum = new HashMap<String, Double>();
	private HashMap<String, Double> defenderFleetCompositionSum = new HashMap<String, Double>();
	private double attackerWins = 0;
	private double defenderWins= 0;
	private double draws = 0;
	private double roundsSum = 0;
	
	public void addCombatResult(MacroCombatResult macroCombatResult) {
		numberOfSimulations++;
		
		Resource attackerLossesThisCombat = new Resource();
		Resource defenderLossesThisCombat = new Resource();
		Resource debrisFieldThisCombat = new Resource();
		
		//Get attacker and defender resource losses
		for(CombatEntity entity : macroCombatResult.getAttackerLosses()) {
			attackerLossesThisCombat.addThis(EntityFileParser.getResourceById(entity.getEntityID()));
		}
		for(CombatEntity entity : macroCombatResult.getDefenderLosses()) {
			defenderLossesThisCombat.addThis(EntityFileParser.getResourceById(entity.getEntityID()));
		}
		
		//Get attacker and defender debris field contributions
		for(CombatEntity entity : macroCombatResult.getAttackerLosses()) {
			if(entity.getType() == CombatEntityType.SHIP)
				debrisFieldThisCombat.addThis(EntityFileParser.getResourceById(entity.getEntityID()).scalar(0.3));
		}
		for(CombatEntity entity : macroCombatResult.getDefenderLosses()) {
			if(entity.getType() == CombatEntityType.SHIP)
				debrisFieldThisCombat.addThis(EntityFileParser.getResourceById(entity.getEntityID()).scalar(0.3));
		}
		
		attackerLossesSum.addThis(attackerLossesThisCombat);
		defenderLossesSum.addThis(defenderLossesThisCombat);
		debrisFieldSum.addThis(debrisFieldThisCombat);
		
		attackerLosses = new Resource(attackerLossesSum.getMetal()/numberOfSimulations,
									  attackerLossesSum.getCrystal()/numberOfSimulations,
									  attackerLossesSum.getDeuterium()/numberOfSimulations);
		
		defenderLosses = new Resource(defenderLossesSum.getMetal()/numberOfSimulations,
									  defenderLossesSum.getCrystal()/numberOfSimulations,
									  defenderLossesSum.getDeuterium()/numberOfSimulations);
	
		
		debrisField = new Resource(debrisFieldSum.getMetal()/numberOfSimulations,
								  debrisFieldSum.getCrystal()/numberOfSimulations,
								  debrisFieldSum.getDeuterium()/numberOfSimulations);
		
		attackerWins += (macroCombatResult.getResultType() == ResultType.ATTACKER_WIN ? 1 : 0);
		defenderWins += (macroCombatResult.getResultType() == ResultType.DEFENDER_WIN ? 1 : 0);
		draws += (macroCombatResult.getResultType() == ResultType.DRAW ? 1 : 0);
		
		attackerWinChance = attackerWins/numberOfSimulations;
		defenderWinChance = defenderWins/numberOfSimulations;
		drawChance = draws/numberOfSimulations;
		
		HashMap<String, Integer> attackerNewComposition = Fleet.compositionIdToName(macroCombatResult.getAttackerRemains().getFleetComposition());
		HashMap<String, Integer> defenderNewComposition = Fleet.compositionIdToName(macroCombatResult.getDefenderRemains().getFleetComposition());
		
		for(String s : attackerNewComposition.keySet()) {
			if(attackerFleetCompositionSum.containsKey(s))
				attackerFleetCompositionSum.put(s, attackerFleetCompositionSum.get(s) + attackerNewComposition.get(s));
			else
				attackerFleetCompositionSum.put(s, (double) attackerNewComposition.get(s));
			
			attackerFleetComposition.put(s, attackerFleetCompositionSum.get(s)/numberOfSimulations);
		}
		
		for(String s : defenderNewComposition.keySet()) {
			if(defenderFleetCompositionSum.containsKey(s))
				defenderFleetCompositionSum.put(s, defenderFleetCompositionSum.get(s) + defenderNewComposition.get(s));
			else
				defenderFleetCompositionSum.put(s, (double) defenderNewComposition.get(s));
			
			defenderFleetComposition.put(s, defenderFleetCompositionSum.get(s)/numberOfSimulations);
		}
		
		roundsSum += macroCombatResult.getRounds();
		
		rounds = roundsSum/numberOfSimulations;

	}

	public int getNumberOfSimulations() {
		return numberOfSimulations;
	} public Resource getAttackerLosses() {
		return attackerLosses;
	} public Resource getDefenderLosses() {
		return defenderLosses;
	} public Resource getDebrisField() {
		return debrisField;
	} public double getAttackerWinChance() {
		return attackerWinChance;
	} public double getDefenderWinChance() {
		return defenderWinChance;
	} public double getDrawChance() {
		return drawChance;
	} public HashMap<String, Double> getAttackerFleetComposition() {
		return attackerFleetComposition;
	} public HashMap<String, Double> getDefenderFleetComposition() {
		return defenderFleetComposition;
	} public double getRounds() {
		return rounds;
	}
}
