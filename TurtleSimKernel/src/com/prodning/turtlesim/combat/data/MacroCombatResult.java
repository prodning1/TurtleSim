package com.prodning.turtlesim.combat.data;

import java.util.ArrayList;

import com.prodning.turtlesim.combat.CombatEntity;
import com.prodning.turtlesim.combat.Fleet;

public class MacroCombatResult {
	public static enum ResultType {
		ATTACKER_WIN,
		DEFENDER_WIN,
		DRAW;
	}
	
	private ArrayList<CombatEntity> attackerLosses;
	private ArrayList<CombatEntity> defenderLosses;
	private Fleet attackerRemains;
	private Fleet defenderRemains;
	private ResultType resultType;
	private int rounds;
	
	public ArrayList<CombatEntity> getAttackerLosses() {
		return attackerLosses;
	} public void setAttackerLosses(ArrayList<CombatEntity> attackerLosses) {
		this.attackerLosses = attackerLosses;
	} public ArrayList<CombatEntity> getDefenderLosses() {
		return defenderLosses;
	} public void setDefenderLosses(ArrayList<CombatEntity> defenderLosses) {
		this.defenderLosses = defenderLosses;
	} public Fleet getAttackerRemains() {
		return attackerRemains;
	} public void setAttackerRemains(Fleet attackerRemains) {
		this.attackerRemains = attackerRemains;
	} public Fleet getDefenderRemains() {
		return defenderRemains;
	} public void setDefenderRemains(Fleet defenderRemains) {
		this.defenderRemains = defenderRemains;
	} public ResultType getResultType() {
		return resultType;
	} public void setResultType(ResultType resultType) {
		this.resultType = resultType;
	} public int getRounds() {
		return rounds;
	}
	public void setRounds(int rounds) {
		this.rounds = rounds;
	}
}
