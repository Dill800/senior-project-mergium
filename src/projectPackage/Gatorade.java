package projectPackage;

import javafx.scene.image.Image;

public class Gatorade extends Beverage {

	public Gatorade() {
		this.name = "Gatorade";
		this.cost = 200;
		this.staminaIncrement = 25;
		this.description = "A quencing sports drink that increase your creature's stamina by 25.";
		
		setImage();
	}
	
	private void setImage() {
		try {
			this.graphicPath = "/resources/gatorade.png";
			graphic = new Image(getClass().getResource(this.graphicPath).toURI().toString());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
