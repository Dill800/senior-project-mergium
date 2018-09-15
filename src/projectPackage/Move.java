package projectPackage;

import java.io.Serializable;

public class Move implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int staminaDrain;
	private String name;
	private int moveAttack;
	private int accuracy;
	private int tier;
	private int rating;
	
	// Static move combinations
	private static String[] techAdjectives = {"Rusty", "Electrical", "Wired", "Sparky", "Metal", "Chrome", "Shiny", "Golden", "Powerful", 
			"Programmed", "Computational", "Zappy", "Charged", "Conductive", "Insulated", "Sleek", "Sharp", "Angular", "Unstable", "Advanced", "Void"};
	
	private static String[] primitiveAdjectives = {"Monkey", "Primitive", "Native", "Canine", "Wild", "Reckless", "Jurassic", "Old", 
			"Instinctive", "Ancient", "Primal", "Prehistoric", "Fundamental", "Archaic", "Destrictive", "Vicious", "Antedeluvian", "Primordial"};
	
	private static String[] elementalAdjectives = {"Rainy", "Sunny", "Cloudy", "Dirty", "Hail", "Natural", "Nuclear", "Earthly", "Poisonous", "Water",
			"Lava", "Blooming", "Fruitful", "Icy", "Summer", "Winter", "Autumn", "Pure", "Unrelenting", "Toxic", "Worn", "Solar", "Arctic", "Tropical"};
	
	private static String[] nouns = {"Kick", "Throw", "Punch", "Uppercut", "Screech", "Beam", "Shot", "Shove", "Attack", "Blow", "Stick", "Arrow", "Sword", 
			"Spear", "Strike", "Rush", "Hit", "Barrage", "Wind", "Sting", "Energy", "Blast", "Snipe", "Slam", "Choke", "Rocket", "Bomb", "Acid"};

	public Move(String type) {
		
		// Create name based off of type
		String name = "";
		switch(type) {
		case "Technological": 
			name += techAdjectives[(int)(Math.random() * techAdjectives.length)];
			break;
		case "Primitive":
			name += primitiveAdjectives[(int)(Math.random() * primitiveAdjectives.length)];
			break;
		case "Elemental":
			name += elementalAdjectives[(int)(Math.random() * elementalAdjectives.length)];
		}
		name += " ";
		name += nouns[(int)(Math.random() * nouns.length)];
		this.name = name;
		
		// Set balanced accuracy and moveAttack
		int randomTier = (int)(Math.random() * 100);
		int randomAcc = (int)(Math.random() * 100);
		
		// 70% Chance Tier 1
		if(randomTier < 70) {
			tier = 1;
			moveAttack = (int)(Math.random() * 30 + 30); //30-60
			if(randomAcc < 85) { //85%
				accuracy = (int)(Math.random() * 10 + 90);
			}
			else if(randomAcc < 95) { //10%
				accuracy = (int)(Math.random() * 10 + 80);
			}
			else { //5%
				accuracy = (int)(Math.random() * 5 + 75);
			}
			
			staminaDrain = 2;
		}
		// 20% Chance Tier 2
		else if(randomTier < 90) {
			tier = 2;
			moveAttack = (int)(Math.random() * 20 + 60); //60-80
			if(randomAcc < 85) { //85%
				accuracy = (int)(Math.random() * 10 + 80);
			}
			else if(randomAcc < 95) { //10%
				accuracy = (int)(Math.random() * 10 + 70);
			}
			else if(randomAcc < 99){ //4%
				accuracy = (int)(Math.random() * 5 + 65);
			}
			else { //1%
				accuracy = 100;
			}
			
			staminaDrain = 3;
		}
		// 10% Chance Tier 3
		else {
			tier = 3;
			moveAttack = (int)(Math.random() * 20 + 80); //80-100
			if(randomAcc < 85) { //85%
				accuracy = (int)(Math.random() * 10 + 70);
			}
			else if(randomAcc < 99) { //14%
				accuracy = (int)(Math.random() * 20 + 50);
			}
			else { //1%
				accuracy = 90;
			}
			staminaDrain = 4;
		}
		
		rating = accuracy * moveAttack;
		
	}
	
	
	// Empty Move
	public Move() {
		
		this.setName("Helpless Wail");
		this.setStaminaDrain(0);
	}
	
	public int getStaminaDrain() {
		return staminaDrain;
	}

	public String getName() {
		return name;
	}

	public void setStaminaDrain(int staminaDrain) {
		this.staminaDrain = staminaDrain;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getMoveAttack() {
		return moveAttack;
	}

	public void setMoveAttack(int moveAttack) {
		this.moveAttack = moveAttack;
	}

	public int getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(int accuracy) {
		this.accuracy = accuracy;
	}

	public int getTier() {
		return tier;
	}

	public int getRating() {
		return rating;
	}

	public void setTier(int tier) {
		this.tier = tier;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}
	
	@Override
	public String toString() {
		return "Name: " + getName() + " Tier:" + getTier() + " ATK:" + getMoveAttack() + " ACC:" + getAccuracy() + " ST DRAIN:" + getStaminaDrain() + "  Rating:" + rating;
	}
	
}
