package projectPackage;

import javafx.scene.image.Image;

public class StrangeDrink extends Beverage { 
	
	private boolean positive;
	private int healthDecrement;
	
	public StrangeDrink() {
		
		this.name = "Strange Drink";
		this.cost = 25;
		this.description = "A concerningly cheap beverage. Not much is known about it.";
		
		
		setImage();
	}
	
	public void use(Creature creature, Bag bag) {
		
		// Check to see if beverage is in bag
		if(!bag.containsItem(this)) {
			System.out.println("Not found in bag");
			return;
		}
		
		int randomNum = (int)(Math.random() * 100);
		
		// 30% max stamina, 70% health decreased by 75% max health
		if(randomNum < 30) {
			creature.setStamina(creature.getStaminaCeil());
			this.positive = true;
		}
		else {
			//this.healthDecrement = (int)(creature.getMaxHealth() * .75);
			this.healthDecrement = creature.getMaxHealth();
			creature.decreaseHealth(this.healthDecrement);
			this.positive = false;
		}
		
		removeFromBag(bag);
		
	}
	
	private void setImage() {
		try {
			this.graphicPath = "/resources/strangeDrink.png";
			graphic = new Image(getClass().getResource(this.graphicPath).toURI().toString());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean isPositive() {
		return this.positive;
	}

	public int getHealthDecrement() {
		return healthDecrement;
	}

	public void setHealthDecrement(int healthDecrement) {
		this.healthDecrement = healthDecrement;
	}

}
