package projectPackage;

public abstract class Capsule extends Item{

	// Highest chance for Creature to be caught
	private int maxCatchChance;
	private boolean didCatch;

	public Capsule() {
		maxCatchChance = 0;
		didCatch = false;
	}

	@Override
	public void use(Creature creature, Bag bag) {

		// Check to see if capsule is in bag
		if(!bag.containsItem(this)) {
			System.out.println("Not found in bag");
			return;
		}

		// Delete this item from bag
		removeFromBag(bag);
		setCaughtState(creature);

	}

	public abstract void setCatchChance(int amount);

	protected void setCaughtState(Creature creature) {

		double healthRatio = creature.getHealth() / (double)creature.getMaxHealth();
		// Lower health, higher factor [0, 100)
		int healthFactor = (int)((1 - healthRatio)* 100);

		// Percent catch chance = healthFactor * maxCatchChance (percent to decimal form)
		int catchChance = (int)(healthFactor * (double)maxCatchChance/100);

		System.out.println(catchChance + 5);

		// If full health, still possible to catch creature (5%)
		if((int)(Math.random() * 100) <= catchChance + 5) {
			didCatch = true;
			creature.setHealth(creature.getMaxHealth());
		}
		else {
			didCatch = false;
		}

	}

	public int getMaxCatchChance() {
		return maxCatchChance;
	}

	public boolean didCatch() {
		return didCatch;
	}

	public void setMaxCatchChance(int maxCatchChance) {
		this.maxCatchChance = maxCatchChance;
	}

	public void setCaught(boolean value) {
		this.didCatch = value;
	}

	public String toString() {
		return "Name: " + name + " Cost: " + cost + " catchChance: " + maxCatchChance + " didCatch: " + didCatch;
	}

}
