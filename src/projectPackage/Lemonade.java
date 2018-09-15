package projectPackage;

import javafx.scene.image.Image;

public class Lemonade extends Beverage{

	public Lemonade() {
		this.name = "Lemonade";
		this.cost = 150;
		this.staminaIncrement = 15;
		this.description = "A refreshing glass of lemonade for your creature. When ingested it increases your creature's stamina by 15.";
		
		
		setImage();
	}
	
	private void setImage() {
		try {
			this.graphicPath = "/resources/lemonade.png";
			graphic = new Image(getClass().getResource(this.graphicPath).toURI().toString());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
