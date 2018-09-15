package projectPackage;

import java.io.Serializable;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class Item implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static int itemCount = 0;
	
	protected int cost;
	protected String name;
	protected int itemID;
	protected transient Image graphic;
	protected String graphicPath;
	protected String description;
	
	public Item() {
		itemCount++;
		itemID = itemCount;
	}
	
	// Abstract Use method on certain creature
	public abstract void use(Creature creature, Bag bag);

	protected void removeFromBag(Bag bag) {
		bag.removeItem(this);
	}
	
	public int getCost() {
		return cost;
	}

	public String getName() {
		return name;
	}

	public int getItemID() {
		return itemID;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setItemID(int itemID) {
		this.itemID = itemID;
	}

	public Image getGraphic() {
		return graphic;
	}

	public void setGraphic(Image graphic) {
		this.graphic = graphic;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public void initializeImage() {
		
		try {
			
			this.graphic = new Image(getClass().getResource(this.graphicPath).toURI().toString());
			
		}
		catch (NullPointerException e) {
			System.out.println("Image hasn't been initialized");
		}
		catch (Exception e2) {
			e2.printStackTrace();
		}
		
	}
	
}
