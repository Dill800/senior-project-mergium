package projectPackage;

public abstract class Beverage extends Item {

	protected int staminaIncrement;
	
	public Beverage() {
		staminaIncrement = 0;
	}
	
	@Override
	public void use(Creature creature, Bag bag) {
		
		// Check to see if beverage is in bag
		if(!bag.containsItem(this)) {
			System.out.println("Not found in bag");
			return;
		}
		
		creature.increaseStamina(staminaIncrement);
		removeFromBag(bag);
	}

	public int getStaminaIncrement() {
		return staminaIncrement;
	}

	public void setStaminaIncrement(int staminaIncrement) {
		this.staminaIncrement = staminaIncrement;
	}
	
}
