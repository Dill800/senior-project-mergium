package projectPackage;

import javafx.scene.image.Image;

public class PrimitiveCapsule extends Capsule {

	public PrimitiveCapsule() {
		setCatchChance(75);
		name = "Primitive Capsule";
		cost = 125;
		this.description = "A capsule created by the Beasts long ago. Increases the catch rate when used against a Primitive creature.";
		setImage();
	}
	
	@Override
	public void setCatchChance(int amount) {

		this.setMaxCatchChance(amount);
	}
	
	@Override
	protected void setCaughtState(Creature creature) {
		
		double healthRatio = creature.getHealth() / (double)creature.getMaxHealth();
		// Lower health, higher factor [0, 100)
		int healthFactor = (int)((1 - healthRatio)* 100);
		
		// Increase max catch chance by 10 if creature is Primitive
		if(creature.getType().equals("Primitive")) {
			setMaxCatchChance(this.getMaxCatchChance() + 10);
		}
		
		// Decrease max catch chance by 10 if creature is Technological
		if(creature.getType().equals("Technological")) {
			setMaxCatchChance(this.getMaxCatchChance() - 10);
		}
		
		// Percent catch chance = healthFactor * maxCatchChance (percent to decimal form)
		int catchChance = (int)(healthFactor * (double)this.getMaxCatchChance()/100);
		
		System.out.println(catchChance + 5);
		
		// If full health, still possible to catch creature (5%)
		if((int)(Math.random() * 100) <= catchChance + 5) {
			this.setCaught(true);
		}
		else {
			this.setCaught(false);
		}
		
	}
	
	private void setImage() {
		try {
			this.graphicPath = "/resources/primitiveCapsule.png";
			graphic = new Image(getClass().getResource(this.graphicPath).toURI().toString());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
