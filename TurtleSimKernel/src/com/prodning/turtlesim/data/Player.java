package com.prodning.turtlesim.data;

import java.util.HashMap;
import java.util.List;

public class Player {
	private HashMap<String, Integer> researchLevels;
	private List<Planet> planets;
	
	
	public HashMap<String, Integer> getResearchLevels() {
		return researchLevels;
	} public void setResearchLevels(HashMap<String, Integer> researchLevels) {
		this.researchLevels = researchLevels;
	} public List<Planet> getPlanets() {
		return planets;
	} public void setPlanets(List<Planet> planets) {
		this.planets = planets;
	}
}
