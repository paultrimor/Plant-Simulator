
import java.util.Collections;

import org.apache.commons.lang3.SystemUtils;

import processing.core.*; 

public class LSystem {
	PApplet processing; 
	
	int x_position; 
	float length; 
	
	int mode; 
	String sentence; 
	Rule[] ruleset; 
	int generation;
	
	float theta; 
	Turtle turtle; 
	
	LSystem(PApplet p, int mode, int generation, float theta, float scale, int x_position) {
		this.processing = p; 
		
		this.x_position = x_position;
		
		this.mode = mode;
		this.sentence = "F";
		this.length = 60 * scale;
		this.theta = theta; 
		
		turtle = new Turtle(this.processing, this.sentence, this.length, theta);
		this.generation = generation; 
		generateRuleset(mode); 	 		
		
	}
	
	public void generateRuleset(int mode) {			
					
		/** Experimental **/ 
		
		int maxTurns; 
		if (this.theta < PConstants.PI/6) {
			maxTurns = 3; 
		} else {
			maxTurns = 2; 
		}
		
		if (mode == 1) {	// Simple Branch Mode			
			int minBranches = 1; 
			int maxBranches = 5; 
			
			String mainRule = generateAuxLayer(maxTurns, minBranches, maxBranches); 
			
			// Main Rules
			ruleset = new Rule[1];		
			ruleset[0] = new Rule('F', mainRule); 				
			
		} else if (mode == 2) {		// Multiple Segments Mode 
			int minSegments = 1; 
			int maxSegments = 3; // Segments are are bracketless branches 
			
			StringBuffer mainRule = new StringBuffer();
			mainRule.append("F");

			// QUESTION 1 - How many Branches 			
			int numBranches = (int) processing.random(1,6);
			for (int i = 0; i < numBranches; i++) {
				mainRule.append("[");
				String mainString = generateAuxLayer(maxTurns, minSegments, maxSegments); 		
				mainRule.append(mainString); 
				mainRule.append("]");			
				
				//WTF Paul!? Yes, I know this is shitty solution. Change it afterwards
				if (processing.random(1) < 0.2) {
					mainRule.append("F");
				}			
			}			
			mainRule.append("F");
			
			ruleset = new Rule[1]; 		
			ruleset[0] = new Rule('F', mainRule.toString()); 			
		} else if (mode == 3) {		// Multiple Branches Mode			
			int minBranches = 8; 
			int maxBranches = 12; 
			
			String mainRule = generateAuxLayer(maxTurns, minBranches, maxBranches);
			
			ruleset = new Rule[1]; 
			ruleset[0] = new Rule('F', mainRule); 
		} else if (mode == 4) {		// Multiple Branches & Multiple Segments Mode
			int minSegments = 1; 
			int maxSegments = 4; // Segments are are bracketless branches
			
			int minBranches = 1;
			int maxBranches = 6; 
	
			String tempRule = generateAuxLayer(maxTurns, minBranches, maxBranches);
			
			Rule[] tempRules = new Rule[6];
			tempRules[0] = new Rule('Q', "+++F");
			tempRules[1] = new Rule('W', "++F");
			tempRules[2] = new Rule('E', "+F");	
			tempRules[3] = new Rule('R', "---F");
			tempRules[4] = new Rule('T', "--F");
			tempRules[5] = new Rule('Y', "-F");

			String mainRule = portableGenerate(tempRule, tempRules);
			
			ruleset = new Rule[1]; 
			ruleset[0] = new Rule('F', mainRule);
		} else if (mode == 5) { 	// Node Rewriting Mode (Special)
			
			String[] rules = {"F[+X][-X]FX",
			                 "F[[X]+X]+F[FX]-X",
			                 "F[+X]F[-X]+XX"};
			
			String rule = rules[(int)( processing.random(0, rules.length))];
			ruleset = new Rule[2];
			
			this.generation += 2; //Overwrie 
			this.sentence = "X"; //Overwrite
			
			ruleset[0] = new Rule('X', rule);
			ruleset[1] = new Rule('F', "FF");
		}
		
		// How the graphics change in relation to system 
		for (int i = 0; i < generation; i++) {			
			processing.pushMatrix();
				processing.stroke(0,128,0);
				turtle.multLength(0.6f);
				turtle.multStroke(0.8f);
				generate(); 
				processing.popMatrix();
			processing.redraw();
		}
					
	}	
	
	// Generate the next generation 
	// Assuming the previous 
	public void generate() {
		StringBuffer nextGeneration = new StringBuffer(); 		
		for (int i = 0; i < sentence.length(); i++) {			
			char currentChar = sentence.charAt(i); 			
			String replace = "" + currentChar; 
			
			for (int j = 0; j < ruleset.length; j++) {
				char predecessor = ruleset[j].getA();
					if (predecessor == currentChar) {
					replace = ruleset[j].getB(); 
				}				
			}
			nextGeneration.append(replace); 			
		}
		sentence = nextGeneration.toString(); 		
	}
	
		
	public String portableGenerate(String auxLayer, Rule[] ruleset) {
		
		StringBuffer nextGeneration = new StringBuffer(); 		
		for (int i = 0; i < auxLayer.length(); i++) {			
			char currentChar = auxLayer.charAt(i); 			
			String replace = "" + currentChar; 
			
			for (int j = 0; j < ruleset.length; j++) {
				char predecessor = ruleset[j].getA();
					if (predecessor == currentChar) {
					replace = ruleset[j].getB(); 
				}				
			}
			nextGeneration.append(replace); 			
		}
		return nextGeneration.toString(); 		
	}
	
	public String generateAuxLayer(int maxTurns, int minBranches, int maxBranches) {
		StringBuffer auxLayer = new StringBuffer(); 		
		
		// QUESTION 1 - How many Branches to Generate? 
		int numBranches = (int) processing.random(minBranches, maxBranches); 							
		
		for (int i = 0; i < numBranches; i++) {				
		
			/** Generate the Auxillary Layer for MODE */ 
			
			// How many turns? 
			int numTurns = (int) processing.random(1, maxTurns);
			
			// Left or Right?
			float left = processing.random(1);
			if (left > 0.5) {					
				switch(numTurns) {
				case 1: 
					auxLayer.append("Q"); 
					break; 
				case 2: 
					auxLayer.append("W"); 
					break; 
				case 3: 
					auxLayer.append("E"); 
					break; 
				}						
			} else {
				switch(numTurns) {
				case 1: 
					auxLayer.append("R"); 
					break; 
				case 2: 
					auxLayer.append("T"); 
					break; 
				case 3: 
					auxLayer.append("Y"); 
					break; 
				}
			}			
		}
		
		// QUESTION 2 - How many Spacers in the Tree? 
		if (mode == 1 || mode == 3 || mode == 4) {
			int numSpacers = (int) processing.random(1, 4); 
			for (int i = 0; i < numSpacers; i++) {
				auxLayer.append("S");
			}						
		} else if (mode == 2) {
			// Do nothing, Spacers are added outside this method
		}

		// Shuffle Branches and Spaces
		String tempString = shuffle(auxLayer.toString());			
		auxLayer.setLength(0);			
		
		if (mode == 1 || mode == 3 || mode == 4) {
			auxLayer.append("S"); 
			auxLayer.append(tempString);
			auxLayer.append("S");	
		} else if (mode == 2) {
			auxLayer.append(tempString);
		}
		
		// Set Axioms
		this.sentence = "F";			
		
		// Auxillary Rules 		
		Rule[] auxRules = null;
		if (this.mode == 1 || this.mode == 3) {
			auxRules = new Rule[7];
			auxRules[0] = new Rule('S', "F");
			auxRules[1] = new Rule('Q', "[+++F]");
			auxRules[2] = new Rule('W', "[++F]");
			auxRules[3] = new Rule('E', "[+F]");	
			auxRules[4] = new Rule('R', "[---F]");
			auxRules[5] = new Rule('T', "[--F]");
			auxRules[6] = new Rule('Y', "[-F]");	
		} else if (this.mode == 2) {
			auxRules = new Rule[6];
			auxRules[0] = new Rule('Q', "+++F");
			auxRules[1] = new Rule('W', "++F");
			auxRules[2] = new Rule('E', "+F");	
			auxRules[3] = new Rule('R', "---F");
			auxRules[4] = new Rule('T', "--F");
			auxRules[5] = new Rule('Y', "-F");
		} else if (this.mode == 4) {
			auxRules = new Rule[7]; 
			auxRules[0] = new Rule('S', "F");
			auxRules[1] = new Rule('Q', "[EY]"); 
			auxRules[2] = new Rule('W', "[WT]"); 
			auxRules[3] = new Rule('E', "[QYY]"); 
			auxRules[4] = new Rule('R', "[YE]");
			auxRules[5] = new Rule('T', "[TW]");
			auxRules[6] = new Rule('Y', "[REE]");
		}
		
		String mainRule = portableGenerate(auxLayer.toString(), auxRules); 
		return mainRule; 
	}
	
	
	public void display() {
		turtle.setInstructions(this.sentence);
		turtle.render();
	}
	
	public String getSentence() {
		return sentence;
	}
	
	public int getGeneration() {
		return generation;
	}
	
	// Position variable
	public void set_x_position(int x) {
		this.x_position = x; 
	}
	
	public void tick() {
		this.x_position -= 2; 
	}
	
	
	
	/** Utility Functions **/ 
	public static String shuffle(String string) {
	    StringBuilder sb = new StringBuilder(string.length());
	    double rnd;
	    for (char c: string.toCharArray()) {
	        rnd = Math.random();
	        if (rnd < 0.34)
	            sb.append(c);
	        else if (rnd < 0.67)
	            sb.insert(sb.length() / 2, c);
	        else
	            sb.insert(0, c);
	    }       
	    return sb.toString();
	}
	
}
