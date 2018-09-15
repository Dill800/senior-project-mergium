package projectPackage;

import javafx.scene.image.Image;

public class PotatoSoup extends Soup{
	
	public PotatoSoup() {
		this.healthIncrement = 20;
		this.name = "Potato Soup";
		this.cost = 250;
		this.description = "A hearty can of potato soup for your Creature. When eaten, it raises the Creature's health by 20 points.";
		
		setImage();
	}
	
	private void setImage() {
		try {
			this.graphicPath = "/resources/potatoSoup.png";
			graphic = new Image(getClass().getResource(this.graphicPath).toURI().toString());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
