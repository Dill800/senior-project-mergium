package projectPackage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// Only one instance will be created
public class Bag implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ArrayList<Creature> creatures;
	private ArrayList<Item> items;
	private int coins;
	
	public Bag() {
		
		creatures = new ArrayList<>();
		items = new ArrayList<>();
		coins = 0;
	}
	
	// Swaps 2 creatures in bag  (needs testing)
	public void swap(Creature creature1, Creature creature2) {
		
		// if bag doesnt contain either creature, exit
		if(!containsCreature(creature1) || !containsCreature(creature2)) {
			return;
		}
		
		int creature1Index = 0;
		for(int i = 0; i < creatures.size(); i++) {
			if(creature1.getCreatureID() == creatures.get(i).getCreatureID()) {
				creature1Index = i;
				i = creatures.size() + 1;
			}
		}
		
		int creature2Index = 0;
		for(int i = 0; i < creatures.size(); i++) {
			if(creature2.getCreatureID() == creatures.get(i).getCreatureID()) {
				creature2Index = i;
				i = creatures.size() + 1;
			}
		}
		
		creatures.remove(creature1Index);
		creatures.add(creature1Index, creature2);
		creatures.remove(creature2Index);
		creatures.add(creature2Index, creature1);
		
	}
	
	public void deposit(int amount) {
		this.coins += amount;
	}
	
	public void withdraw(int amount) {
		if(canWithdraw(amount)) {
			return;
		}
		this.coins -= amount;
		
	}
	
	public boolean canWithdraw(int amount) {
		if(this.coins - amount < 0) {
			return false;
		}
		return true;
	}
	
	public boolean containsCreature(Creature creature) {
		
		for(int i = 0; i < creatures.size(); i++) {
			if(creatures.get(i).getCreatureID() == creature.getCreatureID()) {
				return true;
			}
		}
		return false;
	}
	
	public boolean containsItem(Item item) {
		for(int i = 0; i < items.size(); i++) {
			if(items.get(i).getItemID() == item.getItemID()) {
				return true;
			}
		}
		return false;
	}
	
	public void addCreature(Creature creature) {
		
		// If creature already in bag, don't add it
		if(containsCreature(creature)) {
			return;
		}

		creatures.add(creature);
	}
	
	public void addItem(Item item) {
		
		items.add(item);
	}
	
	public void removeCreature(Creature creature) {
		
		// Scan through bag and delete creature with matching ID
		for(int i = 0; i < creatures.size(); i++) {
			if(creature.getCreatureID() == creatures.get(i).getCreatureID()) {
				creatures.remove(i);
				i = creatures.size();
			}
		}
		
	}
	
	public void removeItem(Item item) {
		
		// Scan through bag and delete item with matching ID
		for(int i = 0; i < items.size(); i++) {
			if(item.getItemID() == items.get(i).getItemID()) {
				items.remove(i);
				i = items.size();
			}
		}
	}
	
	public ArrayList<Creature> getCreatures() {
		return this.creatures;
	}
	
	public ArrayList<Item> getItems() {
		return this.items;
	}
	
	public String toString() {
		String output = "";
		output += "Creatures: ";
		for(int i = 0; i < creatures.size(); i++) {
			output += creatures.get(i).getName() + " ";
		}
		output +=  "Items: ";
		for(int i = 0; i < items.size(); i++) {
			output += items.get(i).getName() + " ";
		}
		return output;
	}
	
	// For ComboBoxes
	public List<String> getCreatureNames() {
		ArrayList<String> names = new ArrayList<>();
		for(int i = 0; i < creatures.size(); i++) {
			names.add(creatures.get(i).getName());
		}
		return names;
	}
	
	public List<String> getItemNames() {
		ArrayList<String> names = new ArrayList<>();
		for(int i = 0; i < items.size(); i++) {
			names.add(items.get(i).getName());
		}
		return names;
	}
	
	
}
