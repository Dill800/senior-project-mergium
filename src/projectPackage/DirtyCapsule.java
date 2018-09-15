package projectPackage;

import javafx.scene.image.Image;

public class DirtyCapsule extends Capsule {

	public DirtyCapsule() {
		setCatchChance(60);
		name = "Dirty Capsule";
		cost = 100;
		this.description = "A grimy, cheap capsule able to catch Creatures. It seems to be made of plastic.";
		setImage();
	}

	@Override
	public void setCatchChance(int amount) {

		this.setMaxCatchChance(amount);
	}

	private void setImage() {
		try {
			this.graphicPath = "/resources/dirtyCapsule.png";
			graphic = new Image(getClass().getResource(this.graphicPath).toURI().toString());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
