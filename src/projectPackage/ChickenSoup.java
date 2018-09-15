package projectPackage;

import javafx.scene.image.Image;

public class ChickenSoup extends Soup{

	public ChickenSoup() {
		this.healthIncrement = 30;
		this.cost = 400;
		this.name = "Chicken Soup";
		this.description = "The ultimate soup for making your Creaure feel better. This soup will increase your Creature's health by 30 points.";
		
		setImage();
	}
	
	private void setImage() {
		try {
			this.graphicPath = "/resources/chickenSoup.png";
			graphic = new Image(getClass().getResource(this.graphicPath).toURI().toString());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
