package com.prodning.turtlesim.test.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class FleetCombatTestGUI {
	public static JTextField attackingFleetTextField = new JTextField(30);
	public static JTextField defendingFleetTextField = new JTextField(30);
	
	public static JTextArea resultsTextArea = new JTextArea();
	public static JScrollPane resultsScrollPane = new JScrollPane(resultsTextArea);
	
	public static JButton simulateButton = new JButton("Simulate");
	
	public static void main(String[] args) {
		JFrame jf = new JFrame();
		
		jf.setLayout(new GridBagLayout());
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.NONE;
		c.weightx = 0;
		c.weighty = 0;
		c.gridwidth = 1;
		jf.add(new JLabel("Attacking Fleet ID"), c);
		
		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 1;
		jf.add(attackingFleetTextField, c);
		
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 1;
		c.fill = GridBagConstraints.NONE;
		c.weightx = 0;
		c.weighty = 0;
		c.gridwidth = 1;
		jf.add(new JLabel("Defending Fleet ID"), c);
		
		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 1;
		jf.add(defendingFleetTextField, c);
		
		
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 2;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 1;
		c.gridwidth = 2;
		jf.add(resultsScrollPane, c);
		
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 2;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 1;
		c.gridwidth = 2;
		jf.add(resultsScrollPane, c);
		
		
		jf.pack();
		jf.setVisible(true);
	}
}
