/**
 * Used in the Item system of the game.
 * 
 * @author Jonathan Sohrabi 2018
 */
public class Item extends ItemPrototype {
	/**
	 * The name of the Item.
	 */
	private String name;
	/**
	 * The price and health value of the item.
	 */
	private int value;
	
	/**
	 * Path for the item's image.
	 */
	private String imgPath;
	
	/**
	 * Constructor for the Item class. Creates a new Item object with the given parameters.
	 * 
	 * @param n						Name of the Item.
	 * @param v						Value of the Item.
	 */
	public Item( String n, int v, String p ) {
		this.name = n;				//Set Item name to parameter 'n'.
		this.value = v;				//Set Item value to parameter 'v'.
		this.imgPath = p;
	}
	
	/**
	 * Copy constructor for the Item class.
	 * Creates a new Item object based on the values in the parameterized Item object.
	 * 
	 * @param i				The Item to copy information from.
	 */
	public Item( Item i ) {
		if ( i != null ) {
			name = i.getName();
			value = i.getValue();
			imgPath = i.getImgPath();
		}
	}
	
	/**
	 * Gets the name of the Item.
	 * 
	 * @return						The name of the Item.
	 */
	public String getName() {
		return this.name;			//Return Item name field.
	}
	
	/**
	 * Gets the value of the Item.
	 * 
	 * @return						The value of the Item.
	 */
	public int getValue() {
		return this.value;			//Return Item value field.
	}
	
	/**
	 * Gets the image path of the Item.
	 * 
	 * @return						The value of the Item.
	 */
	public String getImgPath() {
		return this.imgPath;			//Return Item imgPath field.
	}
	
	/**
	 * Returns a new Item that is a copy of this Item.
	 */
	@Override
	public Item clone() {
		return new Item( this );
	}
}