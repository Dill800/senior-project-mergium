package projectPackage;

import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;

public class PristineCapsule extends Capsule{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PristineCapsule() {
		setCatchChance(100);
		name = "Pristine Capsule";
		cost = 200;
		this.description = "The ultimate tool for catching Creatures. Its composition is unknown.";
		setImage();
	}
	
	@Override
	public void setCatchChance(int amount) {

		this.setMaxCatchChance(amount);
	}
	
	private void setImage() {
		try {
			this.graphicPath = "/resources/pristineCapsule.png";
			graphic = new Image(getClass().getResource(this.graphicPath).toURI().toString());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
