package com.prodning.turtlesim.combat.data;

public class MicroCombatResult {
	private Boolean rapidfireSuccess;
	private Boolean targetDestroyed;
	private double shieldDamage;
	
	

	public MicroCombatResult(Boolean rapidfireSuccess, Boolean targetDestroyed) {
		this.rapidfireSuccess = rapidfireSuccess;
		this.targetDestroyed = targetDestroyed;
	}
	
	public MicroCombatResult() {
		this.rapidfireSuccess = null;
		this.targetDestroyed = null;
	}

	public Boolean getRapidFireSuccess() {
		return rapidfireSuccess;
	} public void setRapidfireSuccess(Boolean rapidfireSuccess) {
		this.rapidfireSuccess = rapidfireSuccess;
	} public Boolean getTargetDestroyed() {
		return targetDestroyed;
	} public void setTargetDestroyed(Boolean targetDestroyed) {
		this.targetDestroyed = targetDestroyed;
	} public double getShieldDamage() {
		return shieldDamage;
	} public void setShieldDamage(double shieldDamage) {
		this.shieldDamage = shieldDamage;
	}

	@Override
	public String toString() {
		return "MicroCombatResult [rapidfireSuccess=" + rapidfireSuccess + ", targetDestroyed=" + targetDestroyed + "]";
	}
}