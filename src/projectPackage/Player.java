package projectPackage;

import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import javafx.scene.image.Image;

public class Player implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private int townNumber;
	private String townName;
	private int coins;
	private int seconds;
	
	

	// staircase 2d array of creatures
	/*
	 * Town 1: 2
	 * Town 2: 3
	 * Town 3: 4
	 * Town 4: 5
	 * Town 5: 6
	 * 
	 */
	private static Creature[][] townCreatures = new Creature[5][];
	
	
	public Player() {
		initTownCreatures();
		
		townNumber = 1;
		coins = 500;
		updateTownName();
		name = "Dillon";
		
	}
	
	public void initTownCreatures() {
		for(int i = 0; i < townCreatures.length; i++) {
			townCreatures[i] = new Creature[i + 2];
			for(int j = 0; j < i + 1; j++) {
				townCreatures[i][j] = Creature.createRandomCreature(this);
			}
		}
		
		// Town 1 Creature Creation
		for(int i = 0; i < townCreatures[0].length; i++) {
			Creature creature = Creature.createRandomCreature(this);
			creature.setLevel(6 + i);
			townCreatures[0][i] = creature;
			
		}
		
		// Town 2 Creature Creation
		for(int i = 0; i < townCreatures[1].length; i++) {
			Creature creature = Creature.createRandomCreature(this);
			creature.setLevel(6 + i);
			townCreatures[1][i] = creature;
			
		}
		
		// Town 3 Creature Creation
		for(int i = 0; i < townCreatures[2].length; i++) {
			Creature creature = Creature.createRandomCreature(this);
			creature.setLevel(6 + i);
			townCreatures[2][i] = creature;
			
		}
		
		// Town 4 Creature Creation
		for(int i = 0; i < townCreatures[3].length; i++) {
			Creature creature = Creature.createRandomCreature(this);
			creature.setLevel(6 + i);
			townCreatures[3][i] = creature;
			
		}
		
		// Town 5 Creature Creation
		for(int i = 0; i < townCreatures[4].length; i++) {
			Creature creature = Creature.createRandomCreature(this);
			creature.setLevel(6 + i);
			townCreatures[4][i] = creature;
			
		}
		
	}
	
	public Image getTownImage() {
		Image image = null;
		try {
			image = new Image(getClass().getResource("/resources/town" + townNumber +  ".jpg").toURI().toString());
		}
		catch(NullPointerException e) {
			System.err.println("****Picture path does not exist****");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return image;
	}
	
	public void incrementTownNumber() {
		townNumber += 1;
		updateTownName();
	}

	private void updateTownName() {
		
		switch(townNumber) {
		case 1: townName = "Porbury Town";
		break;
		case 2: townName = "Sheckle Town";
		break;
		case 3: townName = "Gloudy Town";
		break;
		case 4: townName = "Misty Town";
		break;
		case 5: townName = "Galactic Town";
		break;
		}
		
	}
	
	public String getName() {
		return name;
	}
	
	public String getTownName() {
		return townName;
	}

	public int getTownNumber() {
		return townNumber;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setTownNumber(int townNumber) {
		this.townNumber = townNumber;
	}
	
	public void withdraw(int amount) {
		this.coins -= amount;
	}
	
	public void deposit(int amount) {
		this.coins += amount;
	}
	
	public boolean canWithdraw(int amount) {
		if(this.coins >= amount) {
			return true;
		}
		return false;
	}

	public int getCoins() {
		return coins;
	}

	public Creature[][] getTownCreatures() {
		return townCreatures;
	}

	public void setTownName(String townName) {
		this.townName = townName;
	}

	public void setCoins(int coins) {
		this.coins = coins;
	}
	
	
	
}
