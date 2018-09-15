package projectPackage;

import java.io.Serializable;
import java.util.ArrayList;

import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public abstract class Creature implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static String[] types = {"Technological", "Primitive", "Elemental"};
	public static int creatureCount = 0;

	protected String name;
	protected int health;
	protected int maxHealth;
	protected int level;
	protected int attack;
	protected int defense;
	protected String type;
	protected int stamina;
	protected int staminaCeil;
	protected int xp;
	protected int xpCeil;
	protected Move[] moves;
	protected int mergePoints;
	protected int maxMergePoints;
	protected int speed;
	protected int creatureID;
	protected int critChance;
	protected boolean foreign;
	protected transient ImageView graphic;
	protected String imagePath;

	// Create a new level 5 creature with base stat advantages
	public Creature() {
		creatureCount++;
		this.level = 5;
		this.name = initName();
		initStats();

		// Apply Advantages
		attack += (int)(Math.random() * 3 + 8);
		defense += (int)(Math.random() * 3 + 8);
		speed += (int)(Math.random() * 2);
		staminaCeil += (int)(Math.random() * 5 + 5);
		maxHealth += (int)(Math.random() * 4 + 4);
		health = maxHealth;
		stamina = staminaCeil;

		foreign = false;
	}

	// Creature a new creature based off of current town number
	public Creature(int townNum) {

		// Algorithms for level determination based off of town level
		creatureCount++;
		this.name = initName();
		level = 5; // temp
		initStats();
		
		foreign = true;
	}

	// Returns instance type of Creature class
	public abstract String getInstanceType();

	// Alters stats based off of instance types (specific use)
	public abstract void levelUp();

	// Alters stats (general use)
	private void levelDown() {

		attack -= (int)(Math.random() + 2.5); // -2 or 3
		defense -= (int)(Math.random() + 2.5);
		maxHealth -= (int)(Math.random() + 2.5);
		staminaCeil -= (int)(Math.random() * 3 + 3);
		xpCeil -= 30;
	}

	// Return random instance of a Creature based off of Player town number
	public static Creature createRandomCreature(Player player) {
		Creature creature = null;
		switch((int)(Math.random() * 3)) {
		case 0: creature = new Humanoid(player.getTownNumber());
		break;
		case 1: creature = new Beast(player.getTownNumber());
		break;
		case 2: creature = new Fish(player.getTownNumber());
		break;
		}

		return creature;
	}

	// Generate random Creature name
	private static String initName() {
		String[] consonants = {"b", "c", "d", "f", "g", "h", "k", "l", "m", "n", "p", "r", "s", "t", "v", "w", "z"};
		String[] vowels = {"a", "e", "i", "o", "u"};

		String name = "";
		int length = (int)(Math.random() * 4) + 4;

		boolean consonantStart = false;
		if((int)(Math.random() * 100) < 50) {
			consonantStart = true;
		}
		
		for(int i = 0; i < length; i++) {
			if(i == 0) {
				if(consonantStart) {
					name += consonants[(int)(Math.random() * consonants.length)].toUpperCase();
				}
				else {
					name += vowels[(int)(Math.random() * vowels.length)].toUpperCase();
				}
			}
			if(i % 2 == 1) {
				if(consonantStart) {
					name += vowels[(int)(Math.random() * vowels.length)];
				}
				else {
					name += consonants[(int)(Math.random() * consonants.length)];
				}
			}
			else if(i != 0){
				if(consonantStart) {
					name += consonants[(int)(Math.random() * consonants.length)];
				}
				else {
					name += vowels[(int)(Math.random() * vowels.length)];
				}
			}
		}

		return name;
	}

	// Attacks Creature argument based off of Creature Move, returns Object[] {Damage, isCritHit, Enough Stamina, notMissed}
	public Object[] attack(Move move, Creature enemy) {

		// Type Damage Modifier
		double typeMod = 1;
		if(this.type.equals("Elemental")) {
			if(enemy.getType().equals("Technological")) {
				typeMod += .25;
			}
			else if(enemy.getType().equals("Primitive")) {
				typeMod -= .25;
			}
		}
		else if(this.type.equals("Technological")) {
			if(enemy.getType().equals("Primitive")) {
				typeMod += .25;
			}
			else if(enemy.getType().equals("Elemental")) {
				typeMod -= .25;
			}
		}
		else {
			if(enemy.getType().equals("Elemental")) {
				typeMod += .25;
			}
			else if(enemy.getType().equals("Technological")) {
				typeMod -= .25;
			}
		}

		// Critical Hit Algorithm
		boolean isCriticalHit = false;
		double critMultiplier = (Math.random() * .3) + 1.3;

		// Damage Algorithm
		double damage = ( ((((2*level+10)/(double)250) * ((double)this.attack/enemy.getDefense()) * move.getMoveAttack()) + 2) * (Math.random() * .15  + .85) * typeMod) ;

		if((int)(Math.random() * 100) < this.critChance) {
			isCriticalHit = true;
			damage *= critMultiplier;
		}

		// Rounded version of Damage amount
		int damageDealt = (int)Math.round(damage);

		boolean landedHit = true;

		// Return default value Object[]
		if(this.stamina < move.getStaminaDrain()) {
			return new Object[] {(Integer)0, (Boolean)false, (Boolean)false, (Boolean)false};
		}

		// Last entry in Object[] is false, make attacked creature lose no health
		if((int)(Math.random() * 100) < (100 - move.getAccuracy())) {
			landedHit= false;
			damageDealt = 0;
			isCriticalHit = false;
		}

		this.decreaseStamina(move.getStaminaDrain());
		enemy.decreaseHealth(damageDealt);

		// Return damage dealt (index 0) and if it was a critical hit (index 1)
		// [Damage, Critical Hit, Enough Stamina, Missed/Hit]
		Object[] attackInfo = {(Integer)damageDealt, (Boolean)isCriticalHit, (Boolean)true, (Boolean)landedHit};
		return attackInfo;
	}

	private void decreaseStamina(int amount) {
		stamina -= amount;
		if(stamina < 0) {
			stamina = 0;
		}
	}
	
	// Altering Health Methods
	public void decreaseHealth(int amount) {
		this.health -= amount;
		if(this.health < 0) {
			this.health = 0;
		}
	}

	public void increaseHealth(int amount) {
		
		this.health += amount;
		if(this.health > maxHealth) {
			health = maxHealth;
		}
	}
	
	public void increaseStamina(int amount) {
		this.stamina += amount;
		if(this.stamina > staminaCeil) {
			stamina = staminaCeil;
		}
	}


	// Initialize basic stats based off of level
	private void initStats() {
		// Initialize Type
		type = types[(int)(Math.random() * 3)];

		// Create moves
		initMoves();

		// Set ID
		creatureID = creatureCount;

		// Non-changing amount of merge points 4-8
		maxMergePoints = (int)(Math.random() * 4 + 8);
		mergePoints = 0;
		maxHealth = 15 + (int)(Math.random() * 5); // Level 5 Health 15-20
		critChance = (int)(Math.random() * 8 + 4); // Critical Hit 4-12 percent
		defense = (5 * (int)(Math.random() + 6.5));  // 30-35 + simulated level up <- initial
		attack = (5 * (int)(Math.random() + 6.5));  // 30-35 + simulated level up
		staminaCeil = 30;

		// Stamina is at max when created
		stamina = staminaCeil;

		// Health is also max when created
		health = maxHealth;

		xp = 0;
		xpCeil = 30 * level;
		
		// Level up creature to compensate
		for(int i = 0; i < level - 5; i++) {
			levelUp();
		}

		// Opposite for if Creature is less than level 5
		if(level < 5) {
			for(int i = 0; i < 5 - level; i++) {
				levelDown();
			}
		}

	}

	// Initialize moves based off type, recursively replace if there are duplicate move names
	private void initMoves() {
		moves = new Move[4];

		for(int i = 0; i < moves.length; i++) {
			moves[i] = new Move(this.type);
		}

		for(int i = 0; i < moves.length; i++) {
			for(int j = 0; j < moves.length; j++) {
				if(i != j && moves[i].getName().equals(moves[j].getName())) {
					initMoves();
				}
			}
		}

	}

	// Merge 2 Creatures, output creature having best traits of each parent
	public static Creature merge(Creature creature1, Creature creature2) {

		Creature resultantCreature = null;
		Creature[] parents = {creature1, creature2};

		// Assign the creature a specific instance type from parent
		switch(parents[(int)(Math.random() * 2)].getInstanceType()) {
		case "Humanoid": resultantCreature = new Humanoid();
		break;
		case "Fish": resultantCreature = new Fish();
		break;
		case "Beast": resultantCreature = new Beast();
		}

		// Assign parent types to resultant creature
		switch(parents[(int)(Math.random() * 2)].getType()) {
		case "Elemental": resultantCreature.setType("Elemental");
		break;
		case "Technological": resultantCreature.setType("Technological");
		break;
		case "Primitive": resultantCreature.setType("Primitive");
		break;
		}

		// Best stats + random number
		if(creature1.getAttack() > creature2.getAttack()) {
			resultantCreature.setAttack(creature1.getAttack());
		}
		else {
			resultantCreature.setAttack(creature2.getAttack());
		}
		if(creature1.getDefense() > creature2.getDefense()) {
			resultantCreature.setDefense(creature1.getDefense());
		}
		else {
			resultantCreature.setDefense(creature2.getDefense());
		}
		if(creature1.getLevel() > creature2.getLevel()) {
			resultantCreature.setLevel(creature1.getLevel());
		}
		else {
			resultantCreature.setLevel(creature2.getLevel());
		}
		if(creature1.getStaminaCeil() > creature2.getStaminaCeil()) {
			resultantCreature.setStaminaCeil(creature1.getStaminaCeil());
		}
		else {
			resultantCreature.setStaminaCeil(creature2.getStaminaCeil());
		}
		if(creature1.getMaxHealth() > creature2.getMaxHealth()) {
			resultantCreature.setMaxHealth(creature1.getMaxHealth());
		}
		else {
			resultantCreature.setMaxHealth(creature2.getMaxHealth());
		}
		if(creature1.getSpeed() > creature2.getSpeed()) {
			resultantCreature.setSpeed(creature1.getSpeed());
		}
		else {
			resultantCreature.setSpeed(creature2.getSpeed());
		}
		if(creature1.getCritChance() > creature2.getCritChance()) {
			resultantCreature.setCritChance(creature1.getCritChance());
		}
		else {
			resultantCreature.setCritChance(creature2.getCritChance());
		}

		// Apply advantages
		resultantCreature.setAttack(resultantCreature.getAttack() + (int)(Math.random() * 4 + 2));
		resultantCreature.setDefense(resultantCreature.getDefense() + (int)(Math.random() * 4 + 2));
		resultantCreature.setMaxHealth(resultantCreature.getMaxHealth() + (int)(Math.random() * 2 + 2));
		resultantCreature.setStaminaCeil(resultantCreature.getStaminaCeil() + (int)(Math.random() * 4 + 2));

		resultantCreature.setXpCeil(resultantCreature.getLevel() * 30);
		resultantCreature.setMaxMergePoints((int)Math.ceil(((creature1.getMaxMergePoints() + creature2.getMaxMergePoints())/2.0)) + (int)(Math.random() * 2 + 1));
		resultantCreature.setHealth(resultantCreature.getMaxHealth());
		resultantCreature.setStamina(resultantCreature.getStaminaCeil());


		// All Parent Moves
		Move[] possibleMoves = new Move[8];
		for(int i = 0; i < possibleMoves.length; i++) {
			if(i < 4) {
				possibleMoves[i] = creature1.getMoves()[i];
			}
			else {
				possibleMoves[i] = creature2.getMoves()[i - 4];
			}
		}

		// Sort all moves by rating
		sortMoves(possibleMoves);

		// Create Resultant creature moves based on best Parent creature moves (target tiers: 1, 1, 2, 3)
		Move[] resultantMoves = new Move[4];
		ArrayList<Integer> usedIndicies = new ArrayList<>();
		for(int count = 0; count < resultantMoves.length; count++) {
			int[] targetTiers = {1, 1, 2, 3};
			for(int index = possibleMoves.length - 1; index >= 0; index--) {
				if(possibleMoves[index].getTier() == targetTiers[count] && !usedIndicies.contains((Integer)index)) {
					usedIndicies.add(index);
					resultantMoves[count] = possibleMoves[index];
					index = -1;
				}
				else if(index == 0) {
					for(int j = possibleMoves.length - 1; j >= 0; j--) {
						if(!usedIndicies.contains((Integer)j)) {
							resultantMoves[count] = possibleMoves[j];
							usedIndicies.add(j);
							j = -1;
						}
					}
				}
			}
		}
		resultantCreature.setMoves(resultantMoves);

		resultantCreature.tintGraphic();
		
		return resultantCreature;

	}

	// Bubble Sort Parent Moves [high index = higher rating]
	private static void sortMoves(Move[] moves) {

		for(int cycles = 0; cycles < moves.length - 1; cycles++) {
			for(int index = moves.length - 1; index >= 1 + cycles; index--) {
				if(moves[index].getRating() < moves[index-1].getRating()) {
					Move temp = moves[index];
					moves[index] = moves[index - 1];
					moves[index - 1] = temp;
				}

			}
		}
	}
	
	public void addToBag(Bag bag) {
		bag.addCreature(this);
	}
	
	public void removeFromBag(Bag bag) {
		bag.removeCreature(this);
	}

	// Getters and Setters
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void incrementMergePoints(int value) {
		this.mergePoints += value;
		if(mergePoints > maxMergePoints) {
			mergePoints = maxMergePoints;
		}
	}
	
	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public int getMaxHealth() {
		return maxHealth;
	}

	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		
		for(int i = 0; i < level - this.level; i++) {
			this.levelUp();
		}
		
		this.level = level;
		this.xp = 0;
	}

	public int getAttack() {
		return attack;
	}

	public void setAttack(int attack) {
		this.attack = attack;
	}

	public int getDefense() {
		return defense;
	}

	public void setDefense(int defense) {
		this.defense = defense;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getStamina() {
		return stamina;
	}

	public void setStamina(int stamina) {
		this.stamina = stamina;
	}

	public int getStaminaCeil() {
		return staminaCeil;
	}

	public void setStaminaCeil(int staminaCeil) {
		this.staminaCeil = staminaCeil;
	}

	public int getXp() {
		return xp;
	}

	public void setXp(int xp) {
		this.xp = xp;
	}

	public int getXpCeil() {
		return xpCeil;
	}

	public void setXpCeil(int xpCeil) {
		this.xpCeil = xpCeil;
	}

	public Move[] getMoves() {
		return moves;
	}

	public void setMoves(Move[] moves) {
		this.moves = moves;
	}

	public int getMergePoints() {
		return mergePoints;
	}

	public void setMergePoints(int mergePoints) {
		this.mergePoints = mergePoints;
	}

	public int getMaxMergePoints() {
		return maxMergePoints;
	}

	public void setMaxMergePoints(int maxMergePoints) {
		this.maxMergePoints = maxMergePoints;
	}

	public int getSpeed() {
		return speed;
	}

	public int getCreatureID() {
		return creatureID;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public void setCreatureID(int creatureID) {
		this.creatureID = creatureID;
	}

	public int getCritChance() {
		return critChance;
	}

	public void setCritChance(int critChance) {
		this.critChance = critChance;
	}

	public boolean isForeign() {
		return foreign;
	}

	public void setForeign(boolean foreign) {
		this.foreign = foreign;
	}
	
	public void incrementXp(int amount) {
		this.xp += amount;
	}

	@Override
	public String toString() {
		return "Name:" + this.name + " Level:" + this.level + " Instance:"+ this.getInstanceType() + " Type:" + this.type + " ATK:" + this.attack + " DEF:" + this.defense + " Health:" + this.health + "/" + this.maxHealth + " Stamina:" + this.stamina + "/" + this.staminaCeil+ " XP:" + this.xp + "/" + this.xpCeil + " Merge:" + mergePoints + "/" + maxMergePoints + " Foreign:" + foreign;
	}

	public ImageView getGraphic() {
		return graphic;
	}

	public void setGraphic(ImageView graphic) {
		this.graphic = graphic;
	}
	
	//  Tints Creature graphic based on it's type (green = Elemental, silver = Technological, brown = Primitive)
	public void tintGraphic() {
		
		Lighting l = new Lighting();
		
		switch (this.type) {
		case "Elemental" :
			l.setLight(new Light.Distant(45, 60, Color.rgb(187, 255, 173)));
			break;
		case "Technological":
			l.setLight(new Light.Distant(45, 60, Color.LIGHTBLUE));
			break;
		case "Primitive":
			l.setLight(new Light.Distant(45, 60, Color.BURLYWOOD));
			break;
		}
		
		ColorAdjust c = new ColorAdjust();
		c.setBrightness(1);
		c.setContrast(.5);
		
		graphic.setEffect(c);
		graphic.setEffect(l);
		
	}
	
	public void initializeImage() {
		
		try {
			
			Image preGraphic = new Image(getClass().getResource(this.imagePath).toURI().toString());
			graphic = new ImageView(preGraphic);
			tintGraphic();
			
		}
		catch (NullPointerException e) {
			System.out.println("Image hasn't been initialized");
		}
		catch (Exception e2) {
			e2.printStackTrace();
		}
		
	}
	
	

}
