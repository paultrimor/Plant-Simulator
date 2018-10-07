import processing.core.*;

public class Turtle {
	PApplet processing; 

	String instructions; 
	float length; 
	float theta; 
	float stroke;
	
	Turtle(PApplet p, String s, float length, float theta) {
		this.processing = p; 
		this.instructions = s; 
		this.length = length; 
		this.theta = theta; 
		this.stroke = 3;
	}
	
	public void render() {
				
		for (int i = 0; i < instructions.length(); i++) {
			char c = instructions.charAt(i); 
			if (c == 'F' || c == 'G') {
				processing.strokeWeight(stroke);
				processing.line(0,  0, length, 0);
				processing.translate(length, 0);
			} else if (c == '+') {
				processing.rotate(theta);
			} else if (c == '-') {
				processing.rotate(-theta);
			} else if (c == '[') {
				processing.pushMatrix();
			} else if (c == ']') {
				processing.popMatrix(); 
			}
		}
		
	}
	
	public void multLength(float percent) {
		this.length *= percent; 
	}
	
	public void multStroke(float percent) {
		this.stroke *= percent;
	}
	
	public void setInstructions(String instructions) {
		this.instructions = instructions; 
	}
	
}
