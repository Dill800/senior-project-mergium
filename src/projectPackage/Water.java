package projectPackage;

import javafx.scene.image.Image;

public class Water extends Beverage {

	public Water( ) {
		this.name = "Water";
		this.cost = 100;
		this.staminaIncrement = 10;
		this.description = "A bottle of cold water for your Creature to drink. When ingested, it will increase a creatures stamina by 10.";
		
		setImage();
	}
	
	private void setImage() {
		try {
			this.graphicPath = "/resources/water.png";
			graphic = new Image(getClass().getResource(this.graphicPath).toURI().toString());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
