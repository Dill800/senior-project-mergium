package projectPackage;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Beast extends Creature{


	public Beast() {
		// alter stats (attack+, defense-)
		attack += (int)(Math.random() * 3 + 1);
		defense -= (int)(Math.random() * 3 + 1);


		setImage();
	}

	public Beast(int townNum) {
		super(townNum);
		setImage();
	}

	@Override
	public String getInstanceType() {
		return "Beast";
	}

	@Override
	public void levelUp() {

		// Change current level and xp ceil
		level++;
		xp -= xpCeil;
		xpCeil += 300;

		// Alter stats
		attack += (int)(Math.random() + 2.5); // +2 or 3
		defense += (int)(Math.random() + 1.5);// +1 or 2
		speed += (int)(Math.random() + 1.5); // +1 or 2
		int healthIncrease = (int)(Math.random() + 1.5);
		maxHealth += healthIncrease; // +1 or 2
		health += healthIncrease;
		int staminaIncrease = (int)(Math.random() * 3) + 1;
		staminaCeil += staminaIncrease;
		stamina += staminaIncrease;

		// Increase health reward (full health)
		if(health < maxHealth/2) {
			health = maxHealth/2;
		}

		// Increase stamina reward (3/4 stamina replenish)
		if(stamina <= 3*staminaCeil/4.0) {
			stamina = (int)(3*staminaCeil/4.0);
		}

		// recursively re-level up if xp is over xpCeil
		if(xp >= xpCeil) {
			levelUp();
		}

	}

	private void setImage() {
		int num = (int)(Math.random() * 7) + 1;
		try {

			this.imagePath = "/resources/beast" + num + ".png";

			Image preGraphic = new Image(getClass().getResource(this.imagePath).toURI().toString());
			graphic = new ImageView(preGraphic);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		tintGraphic();
	}

}
