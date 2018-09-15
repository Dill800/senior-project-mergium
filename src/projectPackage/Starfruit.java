package projectPackage;

import javafx.scene.image.Image;

public class Starfruit extends Fruit{
	
	private boolean positive;
	
	public Starfruit() {
		this.name = "Starfruit";
		this.cost = 50;
		this.description = "A risky fruit that could either save you or doom you. All creatures that eat Starfruit are completely healed, but"
				+ " not all are able to survive the initial posioning.";
		setImage();
	}
	
	@Override
	public void use(Creature creature, Bag bag) {
		
		// Check to see if soup is in bag
		if(!bag.containsItem(this)) {
			System.out.println("Not found in bag");
			return;
		}
		
		removeFromBag(bag);
		
		// 75% chance creature dies, 25% chance completely healed stamina and health
		int num = (int)(Math.random() * 100);
		System.out.println();
		if(num < 75) {
			creature.setHealth(0);
			this.positive = false;
		}
		else {
			creature.setHealth(creature.getMaxHealth());
			creature.setStamina(creature.getStaminaCeil());
			this.positive = true;
		}
		
	}
	
	private void setImage() {
		try {
			this.graphicPath = "/resources/starfruit.png";
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
