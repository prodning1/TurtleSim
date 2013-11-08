package com.prodning.turtlesim.testing;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

import com.prodning.turtlesim.combat.CombatSimulation;
import com.prodning.turtlesim.combat.Fleet;
import com.prodning.turtlesim.combat.data.SimulationResult;
import com.prodning.turtlesim.combat.data.TechLevels;
import com.prodning.turtlesim.parse.EntityFileParser;

public class CombatSimulationTest {
	public static void main(String[] args) throws XPathExpressionException, ParserConfigurationException, SAXException, IOException {
		System.out.print("Parsing fleets...");
		Fleet fleet1 = EntityFileParser.getFleetById("F0001");
		Fleet fleet2 = EntityFileParser.getFleetById("F0002");
		System.out.println("done\n");
		
		if(fleet1 == null || fleet2 == null)
			System.exit(1);
		
		fleet1.setTechLevels(new TechLevels());
		fleet2.setTechLevels(new TechLevels());
		
		System.out.println("Attacking fleet: " + Fleet.compositionIdToName(fleet1.getFleetComposition()).toString());
		System.out.println("Defending fleet: " + Fleet.compositionIdToName(fleet2.getFleetComposition()).toString());
		System.out.println();
		
		TechLevels macroCombatInformation = new TechLevels();

		System.out.print("Simulating combat...");
		SimulationResult result = CombatSimulation.SimulateFleetCombat(fleet1, fleet2, macroCombatInformation, 1000);
		System.out.println("done\n");
		
		System.out.println();
		System.out.println("Attacker wins:   " + result.getAttackerWinChance()*100 + "%");
		System.out.println("Defender wins:   " + result.getDefenderWinChance()*100 + "%");
		System.out.println("Draw:            " + result.getDrawChance()*100 + "%");
		System.out.println("In ~" + result.getRounds() + " rounds");
		System.out.println();
		System.out.println("Losses attacker: " + result.getAttackerLosses());
		System.out.println("Losses defender: " + result.getDefenderLosses());
		System.out.println();
		System.out.println("Debris field:    " + result.getDebrisField().toString());
		System.out.println();
		System.out.println("Attacking fleet: " + result.getAttackerFleetComposition());
		System.out.println("Defending fleet: " + result.getDefenderFleetComposition());
	}
}
