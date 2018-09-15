package projectPackage;

import javafx.scene.image.Image;

public class Banana extends Fruit{

	public Banana() {
		
		this.name = "Banana";
		this.cost = 50;
		
		this.description = "A fruity snack that your creature will certainly enjoy. When used, it increases the Creature's health by a random amount from 1-10.";
		
		//40% 1-4, 40% 4-7, 20% 7-10
		int num = (int)(Math.random() * 100);
		if(num < 40) {
			healthIncrement = (int)(Math.random() * 3+ 1);
		}
		else if(num < 80) {
			healthIncrement = (int)(Math.random() * 3 + 4);
		}
		else {
			healthIncrement = (int)(Math.random() * 3 + 7);
		}
		
		setImage();
		
	}
	
	private void setImage() {
		try {
			this.graphicPath = "/resources/banana.png";
			graphic = new Image(getClass().getResource(this.graphicPath).toURI().toString());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
