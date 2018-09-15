package projectPackage;

import javafx.scene.image.Image;

public class Pineapple extends Fruit {

	private boolean positive;
	
	public Pineapple() {
		this.name = "Pineapple";
		this.cost = 75;
		this.description = "Eating a Pineapple will most likely increase the Creature's health by 7-15 points. However, some "
				+ "Creatures have trouble eating it and get hurt from the spikes.";
		
		// Health increment 7-15
		healthIncrement = (int)(Math.random() * 8 + 7);
		
		setImage();
	}
	
	@Override
	public void use(Creature creature, Bag bag) {
		
		// Check to see if fruit is in bag
		if(!bag.containsItem(this)) {
			System.out.println("Not found in bag");
			return;
		}
		
		removeFromBag(bag);
		
		// 80% chance increase health, 20% decrease health
		int num = (int)(Math.random() * 100);
//		if(num < 80) {
		if(num < 50) {
			creature.increaseHealth(healthIncrement);
			this.positive = true;
		}
		else {
			creature.decreaseHealth(healthIncrement);
			this.positive = false;
		}
		
		
		
		
	}
	
	private void setImage() {
		try {
			this.graphicPath = "/resources/pineapple.png";
			graphic = new Image(getClass().getResource(this.graphicPath).toURI().toString());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean isPositive() {
		return positive;
	}

	public void setPositive(boolean positive) {
		this.positive = positive;
	}
	
}
