//Copyright 2013 Philip Rodning

package com.prodning.turtlesim.combat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.prodning.turtlesim.combat.CombatEntity.CombatEntityType;
import com.prodning.turtlesim.combat.Fleet.CombatGroup;
import com.prodning.turtlesim.combat.data.MacroCombatResult;
import com.prodning.turtlesim.combat.data.MacroCombatResult.ResultType;
import com.prodning.turtlesim.combat.data.MicroCombatResult;
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
	
	public static MacroCombatResult fleetCombat(List<FleetCombatUnit> fleetCombatUnits) {
		Random rand = new Random();
		
		ArrayList<CombatEntity> attackerLosses = new ArrayList<CombatEntity>();
		ArrayList<CombatEntity> defenderLosses = new ArrayList<CombatEntity>();
		
		//Before the fight, make sure all combat entities have full hull, shields, and correct weapon values
		for (FleetCombatUnit fcu : fleetCombatUnits) {
			for (CombatEntity entity : fcu.getFleet()) {
				entity.setTechLevels(fcu.getTechLevels());
				entity.calculateEffectiveValues();
				entity.restoreAll();
			}
		}
		
		int round = 0;
		
		ArrayList<FleetCombatUnit> attackers = new ArrayList<FleetCombatUnit>();
		ArrayList<FleetCombatUnit> defenders = new ArrayList<FleetCombatUnit>();
		
		for (FleetCombatUnit fcu : fleetCombatUnits) {
			if(fcu.getCombatGroup() == CombatGroup.ATTACKING)
				attackers.add(fcu);
			else if(fcu.getCombatGroup() == CombatGroup.DEFENDING)
				defenders.add(fcu);
			//else error checking
		}
		
		//Simulate
		do {
			//begin combat round
			
			//for combat report
			int attackerFires = 0;
			int attackerPower = 0;
			int attackerShieldTotal = 0;
			
			int defenderFires = 0;
			int defenderPower = 0;
			int defenderShieldTotal = 0;
			
//			if(verbosity >= 2) {
//				System.out.println("Attacking fleet: " + Fleet.compositionIdToName(this.getFleetComposition()).toString());
//				System.out.println("Defending fleet: " + Fleet.compositionIdToName(defendingFleet.getFleetComposition()).toString());
//			}
			
			//at beginning of each round, restore shields of all remaining units
			for (FleetCombatUnit ft : fleetCombatUnits) {
				for (CombatEntity entity : ft.getFleet()) {
					if (entity.getHull() > 0)
						entity.restoreShield();
				}
			}
			
			for (FleetCombatUnit fcu : fleetCombatUnits) {
				for (CombatEntity attackingEntity : this) {
					Boolean rapidFireSuccess;

					if (attackingEntity.getType() == CombatEntityType.DEFENSE) {
						System.out.println("Ignoring defense object "
								+ attackingEntity.getEntityID()
								+ " in attacking fleet.");
					}

					do {
						// Pick a random defender to attack
						CombatEntity defendingEntity = chooseR

						// defending unit receives the attack
						MicroCombatResult mcr = defendingEntity
								.receiveAttack(attackingEntity);

						// check for successful rapid fire roll
						rapidFireSuccess = mcr.getRapidFireSuccess();

						attackerFires++;
						attackerPower += attackingEntity.getEffectiveWeapon();
						defenderShieldTotal += mcr.getShieldDamage();
					} while (rapidFireSuccess);

					// end attacker volley
				}
			}
			
			//check for losses and move them to the appropriate lists
			Iterator<CombatEntity> iter = this.iterator();
			while(iter.hasNext()) {
				CombatEntity c = iter.next();
				
				if(c.getHull() <= 0) {
					attackerLosses.add(c);
					iter.remove();
				}
			}
			
			iter = defendingFleet.iterator();
			while(iter.hasNext()) {
				CombatEntity c = iter.next();
				
				if(c.getHull() <= 0) {
					defenderLosses.add(c);
					iter.remove();
				}
			}
			
			
//			if (verbosity >= 2) {
//				System.out.println();
//				System.out.println("The attacking fleet fires a total of " + attackerFires + " times with the pwoer of " + attackerPower + " upon the defender.");
//				System.out.println("The defender's shields absorb " + defenderShieldTotal + " damage points");
//				System.out.println();
//				System.out.println("The defending fleet fires a total of " + defenderFires + " times with the pwoer of " + defenderPower + " upon the defender.");
//				System.out.println("The defender's shields absorb " + attackerShieldTotal + " damage points");
//				System.out.println();
//			}
			
		} while ((++round < 6) && (this.size() > 0) && (defendingFleet.size() > 0));
		
		MacroCombatResult mcr = new MacroCombatResult();
		
		mcr.setAttackerLosses(attackerLosses);
		mcr.setDefenderLosses(defenderLosses);
		mcr.setAttackerRemains(this.deepClone());
		mcr.setDefenderRemains(defendingFleet.deepClone());
		mcr.setRounds(round);
		
		//determine result type
		if((this.size() > 0) && (defendingFleet.size() == 0)) {
			mcr.setResultType(ResultType.ATTACKER_WIN);
		}
		if((defendingFleet.size() > 0) && (this.size() == 0)) {
			mcr.setResultType(ResultType.DEFENDER_WIN);
		}
		if(((defendingFleet.size() > 0) && (this.size() > 0)) ||
		   ((defendingFleet.size() == 0) && (this.size() == 0))) {
			mcr.setResultType(ResultType.DRAW);
		}
		
//		if (verbosity >= 1) {
//			System.out.println();
//			System.out.println("******** FINAL ********");
//			System.out.println();
//
//			System.out.println("Attacking Fleet:");
//			System.out.println(this.getFleetComposition());
//			System.out.println();
//
//			System.out.println("Defending Fleet:");
//			System.out.println(defendingFleet.getFleetComposition());
//			System.out.println();
//		}
		
		return mcr;
		
		return null;
	}
	
	private static CombatEntity chooseRandomEntityFromACS(List<FleetCombatUnit> fleetUnion) {
		//TODO check this
		
		Random rand = new Random();
		
		//find total size of fleet union 
		int totalSize = 0;
		for(FleetCombatUnit fcu : fleetUnion)
			totalSize += fcu.getFleet().size();
		
		//pick index within total size
		int index = rand.nextInt(totalSize);
		
		//iterate through fleets until you find the one that has that index in it
		for(FleetCombatUnit fcu : fleetUnion) {
			if(index > fcu.getFleet().size())
				index -= fcu.getFleet().size();
			else
				return fcu.getFleet().get(index);
		}
		
		System.out.println("You shouldn't be here");
		return null;
	}
}