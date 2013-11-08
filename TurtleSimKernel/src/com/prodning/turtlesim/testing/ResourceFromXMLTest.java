package com.prodning.turtlesim.testing;

import com.prodning.turtlesim.data.Resource;
import com.prodning.turtlesim.parse.EntityFileParser;

public class ResourceFromXMLTest {

	public static void main(String[] args) {
		Resource r = EntityFileParser.getResourceById("1004");
		
		System.out.println(r.getMetal());
		System.out.println(r.getCrystal());
		System.out.println(r.getDeuterium());
	}
}
