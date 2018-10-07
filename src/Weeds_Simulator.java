
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
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		main("Weeds_Simulator");
	}
	
	public void settings () {
		size(this.displayWidth, this.displayHeight);
	}
	
	public void setup() {	
		weeds = new ArrayList<LSystem>(); 
		
		// Initialize first 20 weeds to prevent overload. 
	}
	
	public void draw() {		
		background(9,5,72); 
		
		fill(0,128,0); 
		rect(0, this.displayHeight - this.displayHeight/10, this.displayWidth, this.displayHeight);  
		
		weedsScan();
	}
	
	public void weedsScan() {
			System.out.println("SEED SIZE: " + weeds.size());
		
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
	        	weeds.add(new LSystem(this, read_mode, read_generation, read_angle, read_scale, x_position));   
	    		
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
			// Label the weed Mode
			translate(weed.x_position, this.displayHeight - this.displayHeight/10);
			rotate(-PI/2);
			
			this.fill(0);
			text("Mode " + weed.mode, -60, 0);			
			
			weed.display();	
			
			// Animation 
			weed.tick();
			
			popMatrix();
		}
		
		
	}	
	
}
