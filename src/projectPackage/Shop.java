package projectPackage;

import java.io.Serializable;
import java.util.ArrayList;

public class Shop implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<Item> availableItems = new ArrayList<Item>();
	private Item[] firstTown = {new RamenSoup(), new Banana(), new Starfruit(), new Water(), new DirtyCapsule(), new Pineapple()};
	private Item[] secondTown = {new PotatoSoup(), new Lemonade(), new CleanCapsule()};
	private Item[] thirdTown = {new ChickenSoup(), new Gatorade(), new ElementalCapsule(), new TechCapsule(), new PrimitiveCapsule()};
	private Item[] fourthTown = {new StrangeDrink()};
	private Item[] fifthTown = {new PristineCapsule()};
	
	public Shop() {
		updateShop(1);
	}
	
	public void updateShop(int townNum) {
		
		availableItems.clear();
		
		switch(townNum) {
		case 5: 
			for(Item e : fifthTown) {
				availableItems.add(e);
			}
		case 4: 
			for(Item e : fourthTown) {
				availableItems.add(e);
			}
		case 3: 
			for(Item e : thirdTown) {
				availableItems.add(e);
			}
		case 2: 
			for(Item e : secondTown) {
				availableItems.add(e);
			}
		case 1:
			for(Item e : firstTown) {
				availableItems.add(e);
			}
		}
		
	}
	
	public ArrayList<Item> getAvailableItems() {
		return this.availableItems;
	}
	
	public ArrayList<String> getAvailableItemNames() {
		ArrayList<String> names = new ArrayList<>();
		for(int i = 0; i < this.availableItems.size(); i++) {
			names.add(this.availableItems.get(i).getName());
		}
		return names;
	}
	
	
	
}
