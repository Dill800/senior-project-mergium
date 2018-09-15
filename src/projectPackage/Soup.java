package projectPackage;

public abstract class Soup extends Item {

	protected int healthIncrement;
	
	public Soup() {
		healthIncrement = 0;
		
	}
	
	@Override
	public void use(Creature creature, Bag bag) {
		
		// Check to see if soup is in bag
		if(!bag.containsItem(this)) {
			System.out.println("Not found in bag");
			return;
		}
		
		creature.increaseHealth(this.healthIncrement);
		removeFromBag(bag);
	}

	public int getHealthIncrement() {
		return healthIncrement;
	}

	public void setHealthIncrement(int healthIncrement) {
		this.healthIncrement = healthIncrement;
	}
	
}
