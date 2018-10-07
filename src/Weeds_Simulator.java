
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import processing.core.*; 
import com.opencsv.CSVReader; 

public class Weeds_Simulator extends PApplet{
	private CSVReader csvReader; 
	
	LSystem lsystem; 
	Turtle turtle; 
	List<LSystem> weeds;
	
	int MAX_WEEDS = 50;
	int weed_count; 
	
	// Fonts 
	PFont bold;
	PFont regular; 
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		main("Weeds_Simulator");
	}
	
	public void settings () {
		size(this.displayWidth, this.displayHeight);
	}
	
	public void setup() {	
		weeds = new ArrayList<LSystem>(); 
		String[] fontList = PFont.list();
		printArray(fontList);
		
		// Fonts 
		bold = createFont("Myriad Hebrew Bold", 30); 
		regular = createFont("Myriad Hebrew", 30);
		
		
		
		// Initialize first 20 weeds to prevent overload. 
	}
	
	public void draw() {		
		background(9,5,72);
		// Draw Background Graph
		strokeWeight(0);
		textSize(10);
		for (int i = 0; i < 20; i++) {
			stroke(10, 22, 168);
			if (i == 7) 
				stroke(225);
			
			pushMatrix(); 
			translate((this.displayWidth/20) * i, 0);
			line(0, 0, 0, this.displayHeight); 
			text(-70 + (10*i), 5, 670);
			popMatrix(); 
			
			pushMatrix(); 
			translate(0, (this.displayWidth/20) * i);
			line(0, 0, this.displayWidth, 0); 
			text(-70 + (10*i), 681, -4);
			popMatrix(); 
			
			
			
		} 
		
		fill(0,128,0); 
		rect(0, this.displayHeight - this.displayHeight/10, this.displayWidth, this.displayHeight);  
		
		stroke(0,128,0); 
		weedsScan();
		
		textFont(regular);
		textSize(100);
		text("Visualie Data As", 0, 0, displayWidth, displayHeight);
		
	}
	
	public void weedsScan() {
			
		try {
			FileReader filereader = new FileReader("C:\\Users\\trimo\\eclipse-workspace\\WeedsSimulator\\src\\data.csv");
			csvReader = new CSVReader(filereader); 
	        String[] nextRecord; 	        
	        csvReader.skip(1);  // Skip Header
		        
	        while( (nextRecord = csvReader.readNext()) != null) {	
	        	
	    		// Create weeds if they are appended 
	    		if (weeds.size() < MAX_WEEDS) {
	    		
	    		// Load CSV File
	        	
	        	int read_mode = Integer.parseInt(nextRecord[0]); 
	        	int read_generation = Integer.parseInt(nextRecord[1]); 
	        	float read_angle = radians(Float.parseFloat(nextRecord[2])); 
	        	float read_scale = Float.parseFloat(nextRecord[3]);	
	        	
	        	int x_position =  this.displayWidth + (int) (randomGaussian()*this.displayWidth); 
	        	System.out.println();
	        	weeds.add(new LSystem(this, read_mode, read_generation, read_angle, read_scale, x_position, weed_count));   
	    		weed_count++; 
	        	
	    		} else {
	    			break; 
	    		}      
	    	}
				
		} catch (Exception e) {
			e.printStackTrace();
		}				
			
		
		// SCAN Weeds and see if any are off the screen. 
		// If there is a weed off the screen, go append a 
		// weed off the screen in the entry. 
		// Add the weeds 
		
		for (Iterator<LSystem> iterator = weeds.iterator(); iterator.hasNext();) {
			LSystem weed = iterator.next(); 
			
			// Remove weeds if they are off the screen 
			if (weed.x_position < -50) {
				System.out.println("A weed just died");
				iterator.remove(); 
			}			
			
			pushMatrix(); 				
			translate(weed.x_position, this.displayHeight - this.displayHeight/10);
			rotate(-PI/2);	
			
			
			this.fill(255);
			textSize(15);
			text("#" + weed.id, -50, 0);			
			
			weed.display();	
			
			// Animation 
			weed.tick();
			
			popMatrix();
		}
		
		
	}	
	
}
