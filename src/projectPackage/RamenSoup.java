package projectPackage;

import javafx.scene.image.Image;

public class RamenSoup extends Soup {

	public RamenSoup() {
		healthIncrement = 10;
		this.name = "Ramen Soup";
		this.cost = 100;
		this.description = "A classic, cheap soup. When eaten, this soup will increase a Creature's health by 10 points.";
		
		setImage();
	}
	
	private void setImage() {
		try {
			this.graphicPath = "/resources/ramenSoup.png";
			graphic = new Image(getClass().getResource(this.graphicPath).toURI().toString());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
