package com.prodning.turtlesim.testing;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

import com.prodning.turtlesim.combat.Fleet;
import com.prodning.turtlesim.combat.data.MacroCombatResult;
import com.prodning.turtlesim.combat.data.TechLevels;
import com.prodning.turtlesim.parse.EntityFileParser;

public class GenerateObjectsFromXMLTest {
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
		Fleet fleet1 = EntityFileParser.getFleetById(new File("resources/test_fleet_1.xml"));
		Fleet fleet2 = EntityFileParser.getFleetById(new File("resources/test_fleet_2.xml"));
		
		System.out.println(fleet1.getFleetComposition().toString());
		System.out.println(fleet2.getFleetComposition().toString());
		
		fleet1.setTechLevels(new TechLevels());
		fleet2.setTechLevels(new TechLevels());

		MacroCombatResult result = fleet1.attackFleet(fleet2);
		

		System.out.println();
		System.out.println("******** RESULT ********");
		System.out.println(result.getResultType().toString() + " after "
				+ result.getRounds() + " rounds.");
		System.out.println("Total attacker lost ships: "
				+ result.getAttackerLosses().size());
		System.out.println("Total defender lost ships: "
				+ result.getDefenderLosses().size());
		
		System.out.println();
		System.out.println(fleet1.getFleetComposition().toString());
		System.out.println(fleet2.getFleetComposition().toString());
		
		System.out.println(fleet1.toString());
	}
}