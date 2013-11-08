package com.prodning.turtlesim.combat;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import com.prodning.turtlesim.combat.CombatEntity.CombatEntityType;
import com.prodning.turtlesim.combat.data.MacroCombatResult;
import com.prodning.turtlesim.combat.data.MacroCombatResult.ResultType;
import com.prodning.turtlesim.combat.data.MicroCombatResult;
import com.prodning.turtlesim.combat.data.TechLevels;
import com.prodning.turtlesim.parse.EntityFileParser;


public class Fleet extends ArrayList<CombatEntity> {
	public static enum CombatGroup {
		ATTACKING,
		DEFENDING;
	}
	
	private int numberOfShips = 0;
	private String name;
	private CombatGroup combatGroup;
	private TechLevels techLevels;
	
	public Fleet(String name) {
		this.name = name;
	}

	public MacroCombatResult attackFleet(Fleet defendingFleet) {
		Random rand = new Random();
		
		ArrayList<CombatEntity> attackerLosses = new ArrayList<CombatEntity>();
		ArrayList<CombatEntity> defenderLosses = new ArrayList<CombatEntity>();
		
		//final int verbosity = 0;
		
		//Before the fight, make sure all combat entities have full hull, shields, and correct weapon values
		for(CombatEntity entity : this) {
			entity.setTechLevels(techLevels);
			entity.calculateEffectiveValues();
			entity.restoreAll();
		}
		for(CombatEntity entity : defendingFleet) {
			entity.setTechLevels(defendingFleet.getTechLevels());
			entity.calculateEffectiveValues();
			entity.restoreAll();
		}
		
		int round = 0;
		
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
			for(CombatEntity entity : this) {
				if(entity.getHull() > 0)
					entity.restoreShield();
			}
			for(CombatEntity entity : defendingFleet) {
				if(entity.getHull() > 0)
					entity.restoreShield();
			}
			
			for (CombatEntity attackingEntity : this) {
				Boolean rapidFireSuccess;
				
				if(attackingEntity.getType() == CombatEntityType.DEFENSE) {
					System.out.println("Ignoring defense object " + attackingEntity.getEntityID() + " in attacking fleet.");
				}
				
				do {
					// Pick a random defender to attack
					CombatEntity defendingEntity = defendingFleet.get(rand.nextInt(defendingFleet.size()));
					
					// defending unit receives the attack
					MicroCombatResult mcr = defendingEntity.receiveAttack(attackingEntity);
					
					//check for successful rapid fire roll
					rapidFireSuccess = mcr.getRapidFireSuccess();
					
					attackerFires++;
					attackerPower += attackingEntity.getEffectiveWeapon();
					defenderShieldTotal += mcr.getShieldDamage();
				} while (rapidFireSuccess);
				
				//end attacker volley
			}
			
			for (CombatEntity attackingEntity : defendingFleet) {
				Boolean rapidFireSuccess;
				
				/* 
				 * this section is weird because the "defenders" are actually firing shots (attacking) against the attackers
				 * it's kind of confusing but w/e
				 */
				do {
					// Pick a random defender to attack
					CombatEntity defendingEntity = this.get(rand.nextInt(this.size()));

					// defending unit receives the attack
					MicroCombatResult mcr = defendingEntity.receiveAttack(attackingEntity);

					//check for successful rapid fire roll
					rapidFireSuccess = mcr.getRapidFireSuccess();
					
					defenderFires++;
					defenderPower += attackingEntity.getEffectiveWeapon();
					attackerShieldTotal += mcr.getShieldDamage();
				} while (rapidFireSuccess);
				
				//end defender volley
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
	}
	
	public void saveFleetToXML(File saveFile) {
		//saveFile
	}
	
	public int addToFleet(CombatEntity c) {
		c.setUniqueID(numberOfShips);
		
		this.add(c);
		
		return numberOfShips++;
	}
	
	public HashMap<String, Integer> getFleetComposition() {
		HashMap<String, Integer> fleetComposition = new HashMap<String, Integer>();
		
		for(CombatEntity entity : this) {
			Integer n = fleetComposition.get(entity.getEntityID());
			if(n == null)
				n = 0;
			n++;
			fleetComposition.put(entity.getEntityID(), n);
		}
		
		return fleetComposition;
	}
	
	public String toString() {
		String result = "Fleet: " + name + "\nTotal ships: " + Integer.toString(this.size()) + "\n" + "Tech levels: " + techLevels.getWeaponsTech() + " " + techLevels.getShieldTech() + " " + techLevels.getArmorTech() + "\n";
		for(CombatEntity entity : this) {
			result = result + String.format("%4d %20s ", entity.getUniqueID(), EntityFileParser.getNameById(entity.getEntityID())) + entity.getHull() + "\t" + String.format("%1$,.2f",entity.getHullPercentage()*100) + "\t" + entity.getShield() + "\t" + entity.getEffectiveWeapon() + "\n";
		}
		
		return result;
	}

	public String getName() {
		return name;
	} public TechLevels getTechLevels() {
		return techLevels;
	} public void setTechLevels(TechLevels techLevels) {
		this.techLevels = techLevels;
	} public int getNumberOfShips() {
		return numberOfShips;
	} public CombatGroup getCombatGroup() {
		return combatGroup;
	} public void setCombatGroup(CombatGroup combatGroup) {
		this.combatGroup = combatGroup;
	}

	public Fleet deepClone() {
		Fleet result = new Fleet(name);
		
		result.setTechLevels(new TechLevels(techLevels.getWeaponsTech(), techLevels.getArmorTech(), techLevels.getShieldTech()));
		
		for(CombatEntity c : this) {
			result.add(c.clone());
		}
		
		return result;
	}
	
	public static HashMap<String,Integer> compositionIdToName(HashMap<String,Integer> composition) {
		HashMap<String, Integer> result = new HashMap<String, Integer>();
		
		for(String s : composition.keySet()) {
			result.put(EntityFileParser.getNameById(s), composition.get(s));
		}
		
		return result;
	}
}
