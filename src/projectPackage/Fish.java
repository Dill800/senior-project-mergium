package projectPackage;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Fish extends Creature{
	
	public Fish() {
		// alter stats (speed+, defense-)
		speed += (int)(Math.random() * 3 + 1);
		defense -= (int)(Math.random() * 3 + 1);
		setImage();
		
	}
	
	public Fish(int townNumber) {
		super(townNumber);
		setImage();
	}

	@Override
	public String getInstanceType() {
		return "Fish";
	}

	@Override
	public void levelUp() {
		
		// Change current level and xp ceil
		level++;
		xp -= xpCeil;
		xpCeil += 300;
		
		// Alter stats
		attack += (int)(Math.random() + 1.5); // +2 or 3
		defense += (int)(Math.random() + 1.5);
		speed += (int)(Math.random() + 2.5); // +2 or 3
		int healthIncrease = (int)(Math.random() + 3);
		maxHealth += healthIncrease; 
		health += healthIncrease;;
		int staminaIncrease = (int)(Math.random() + 1.5);
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
		int num = (int)(Math.random() * 8) + 1;

		try {
			
			this.imagePath = "/resources/fish" + num +  ".png";
			
			Image preGraphic = new Image(getClass().getResource(this.imagePath).toURI().toString());
			graphic = new ImageView(preGraphic);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		tintGraphic();
	}
	
}
