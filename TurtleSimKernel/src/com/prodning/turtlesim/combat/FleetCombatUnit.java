package com.prodning.turtlesim.combat;

import java.util.ArrayList;

import com.prodning.turtlesim.combat.Fleet.CombatGroup;
import com.prodning.turtlesim.combat.data.TechLevels;

public class FleetCombatUnit {
	private Fleet fleet;
	private ArrayList<CombatEntity> losses = new ArrayList<CombatEntity>();
	private String name;
	private CombatGroup combatGroup;
	private TechLevels techLevels;
	
	public FleetCombatUnit(Fleet fleet) {
		this.fleet = fleet;
		this.name = fleet.getId();
	}
	
	public CombatGroup getCombatGroup() {
		return combatGroup;
	} public void setCombatGroup(CombatGroup combatGroup) {
		this.combatGroup = combatGroup;
	} public TechLevels getTechLevels() {
		return techLevels;
	} public void setTechLevels(TechLevels techLevels) {
		this.techLevels = techLevels;
	} public Fleet getFleet() {
		return fleet;
	} public String getName() {
		return name;
	} public ArrayList<CombatEntity> getLosses() {
		return losses;
	}
}
