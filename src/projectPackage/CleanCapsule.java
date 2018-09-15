package projectPackage;

import javafx.scene.image.Image;

public class CleanCapsule extends Capsule {

	public CleanCapsule() {
		setCatchChance(80);
		name = "Clean Capsule";
		cost = 150;
		this.description = "A clean, typical Capsule. When thrown at a Creature, it has a decent chance at catching it.";
		setImage();
	}
	
	@Override
	public void setCatchChance(int amount) {

		this.setMaxCatchChance(amount);
	}
	
	private void setImage() {
		try {
			this.graphicPath = "/resources/cleanCapsule.png";
			graphic = new Image(getClass().getResource(this.graphicPath).toURI().toString());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
