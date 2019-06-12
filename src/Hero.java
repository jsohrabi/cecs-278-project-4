import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

/**
 * Represents the player-controlled character.
 * 
 * @author Jonathan Sohrabi 2018
 */
public class Hero extends Entity implements Magical {
	/**
	 * Represents the Hero's current inventory.
	 */
	private ArrayList<Item> items;
	/**
	 * Represents the current game Map.
	 */
	private Map map;
	/**
	 * Represents the Hero's current Point location on the Map.
	 */
	private Point location;
	/**
	 * Represents the Hero's current gold.
	 */
	private int gold;
	
	/**
	 * Parameterized constructor of the Hero class.
	 * 
	 * @param n					The name of the hero.
	 * @param q					The quip of the hero.
	 * @param m					The current game map, used to set the starting location of the hero.
	 */
	public Hero( String n, String q, Map m ) {
		super( n, q, 1, 15 );						//Call the Entity superclass constructor with the given parameters.
		this.items = new ArrayList<Item>();			//Create a new ArrayList object for Item objects.
		this.map = m;								//Sets the Map field to the parameter 'm'.
		this.location = this.map.findStart();		//Sets the Hero's starting location on the Map to the Map's starting Point.
		map.reveal( location );
		this.gold = 10;								//Sets the gold field to 10.
	}
	
	/**
	 * Getter for the Hero's inventory
	 * 
	 * @return	The Hero's inventory.
	 */
	public ArrayList<Item> getItems() {
		return items;
	}
	
	/**
	 * Gets the number of items currently held in the hero's inventory.
	 * 
	 * @return The number of items currently held in the hero's inventory.
	 */
	public int getNumItems() {
		return this.items.size();				//Return the number of indices in the items ArrayList.
	}
	
	/**
	 * If the hero's inventory is not full, adds the parameterized item to their inventory.
	 * If the item is not a Health Potion, the item's value is added to the hero's current HP and maximum HP.
	 * 
	 * @param i					The item to add to the hero's inventory.
	 * @return					True if the hero had free inventory space to pick up the item, false otherwise.
	 */
	public boolean pickUpItem( Item i ) {
		if ( i.getName().equals( "Bag o' Gold" ) ) {			// If the Item picked up is a Bag o' Gold, add 25 gold to Hero and return true
				this.collectGold( 25 );
				return true;
		} else {
			if ( this.getNumItems() != 5 ) {					//If the Hero has less than 5 Items in their inventory:
				this.items.add( i );							//Add the parameter i to the items ArrayList.
				if ( !i.getName().equals( "Health Potion" ) ) {	//If i is not a Health Potion,
					this.increaseMaxHP( i.getValue() );			//Increase the Hero's max HP by the Item's value.
					this.heal( i.getValue() );					//Increase the Hero's HP by the Item's value.
				}
				return true;									//If the Hero was able to pick up the Item, return true.
			} else {
				return false;									//If the Hero was not able to pick up the Item, return false.
			}
		}
	}
	
	/**
	 * Removes an item from the hero's inventory, given by the parameterized name.
	 * If the item is not a Health Potion, the hero's maximum HP is reduced by the item's cost value, and the current HP is reduced by the same amount to a minimum of 1 HP.
	 * 
	 * @param n					The name of the item to remove.
	 * @return					The removed item object, null if the item could not be found in the hero's inventory.
	 */
	public Item removeItem( String n ) {
		for( Item i : this.items ) {								// Loop through each Item in the items ArrayList.
			if ( i.getName().equals( n ) ) {						// If the Item's name matches the String parameter n:
				if ( !i.getName().equals( "Health Potion" ) ) {		//	If the Item is not a Health Potion:
					if ( ( this.getHP() - i.getValue() ) > 0 ) {	//		If removing the Item would set the Hero's HP to less than or equal to 0, warn the player that they cannot remove the Item.
						this.decreaseMaxHP( i.getValue() );			//		Decrease the Hero's maxHP by the Item's value.
						this.takeDamage( i.getValue() );			//		Decrease the Hero's HP by the Item's value.
						this.items.remove( i );						//		Remove the Item from the items ArrayList.
					}
				} else {
					this.items.remove( i );							// If the Item is a Health Potion, then remove it.
				}
				return i;											// Return the removed Item.
			}
		}
		return null;												// If the Item was not found, return null.
	}
	
	/**
	 * Removes the item from the hero's inventory at the parameterized index.
	 * If the item is not a Health Potion, the hero's maximum HP is reduced by the item's cost value, and the current HP is reduced by the same amount to a minimum of 1 HP.
	 * 
	 * @param index					The index of the item to be removed.
	 * @return						The removed item object, null if the index is out of range.
	 */
	public Item removeItem( int index ) {
		if ( ( this.getNumItems() != 0 ) && ( index <= ( this.getNumItems() - 1 ) ) ) {	//If items has at least 1 Item in it and the parameter index is not out of items range.
			Item i = this.items.get( index );											//Get the Item at the parameter index of items.
			if ( !i.getName().equals( "Health Potion" ) ) {								//If the Item is not a Health Potion:
				
				//If removing the Item would set the Hero's HP to less than or equal to 0, return null.
				if ( ( this.getHP() - i.getValue() ) <= 0 ) {
					return null;
				} else {										//If the Item is safe to remove:
					this.decreaseMaxHP( i.getValue() );			//Decrease the Hero's maxHP by the Item's value.
					this.takeDamage( i.getValue() );			//Decrease the Hero's HP by the Item's value.
					this.items.remove( index );					//Remove the Item from the items ArrayList.
				}
			} else {
				this.items.remove( index );						//If the Item is a Health Potion, then remove it.
			}
			return i;				//Return the Item.
		} else {
			return null;			//In all other cases, return null.
		}
	}
	
	/**
	 * Checks if the hero has a potion in their inventory.
	 * 
	 * @return					True if the hero has a Health Potion in their inventory, false otherwise.
	 */
	public boolean hasPotion() {
		for( Item i: this.items ) {							//Loop through all Items in the items ArrayList.
			if ( i.getName().equals( "Health Potion" ) ) {	//If the Item is a Health Potion, return true.
				return true;
			}
		}
		return false;										//If a Health Potion was not found, return false.
	}
	
	/**
	 * Gets the current location of the hero as a Point object.
	 * 
	 * @return					The current location of the hero as a Point object.
	 */
	public Point getLocation() {
		return this.location;																	//Return the location field of the Hero.
	}
	
	/**
	 * Moves the hero's location up by one tile. If there are no tiles in the up direction, the hero stays at their current location.
	 * 
	 * @return					The character of the tile that the hero was moved to.
	 */
	public char goNorth() {
		if ( this.getLocation().getX() != 0 ) {																		//If the Hero is not at the top-most part of the Map.
			this.getLocation().move( ( int )( this.getLocation().getX() - 1 ), ( int )this.getLocation().getY() );	//Move the Hero's location up by 1.
			this.map.reveal( this.getLocation() );																	//Reveal the Map room at the Hero's new location.
		}
		return this.map.getCharAtLoc( this.getLocation() );															//Return the Map character at the Hero's location.
	}
	
	/**
	 * Moves the hero's location down by one tile. If there are no tiles in the down direction, the hero stays at their current location.
	 * 
	 * @return					The character of the tile that the hero was moved to.
	 */
	public char goSouth() {
		if ( this.getLocation().getX() != 4 ) {																		//If the Hero is not at the bottom-most part of the Map.
			this.getLocation().move( ( int )( this.getLocation().getX() + 1 ), ( int )this.getLocation().getY() );	//Move the Hero's location down by 1.
			this.map.reveal( this.getLocation() );																	//Reveal the Map room at the Hero's new location.
		}
		return this.map.getCharAtLoc( this.getLocation() );															//Return the Map character at the Hero's location.
	}
	
	/**
	 * Moves the hero's location right by one tile. If there are no tiles in the right direction, the hero stays at their current location.
	 * 
	 * @return					The character of the tile that the hero was moved to.
	 */
	public char goEast() {
		if ( this.getLocation().getY() != 4 ) {																		//If the Hero is not at the right-most part of the Map.
			this.getLocation().move( ( int )this.getLocation().getX(), ( int )( this.getLocation().getY() + 1 ) );	//Move the Hero's location right by 1.
			this.map.reveal( this.getLocation() );																	//Reveal the Map room at the Hero's new location.
		}
		return this.map.getCharAtLoc( this.getLocation() );															//Return the Map character at the Hero's location.
	}
	
	/**
	 * Moves the hero's location down by one tile. If there are no tiles in the down direction, the hero stays at their current location.
	 * 
	 * @return					The character of the tile that the hero was moved to.
	 */
	public char goWest() {
		if ( this.getLocation().getY() != 0 ) {																		//If the Hero is not at the left-most part of the Map.
			this.getLocation().move( ( int )this.getLocation().getX(), ( int )( this.getLocation().getY() - 1) );	//Move the Hero's location left by 1.
			this.map.reveal( this.getLocation() );																	//Reveal the Map room at the Hero's new location.
		}
		return this.map.getCharAtLoc( this.getLocation() );															//Return the Map character at the Hero's location.
	}
	
	/**
	 * Gets the currently held currency of the hero.
	 * 
	 * @return					The amount of gold held by the hero.
	 */
	public int getGold() {
		return this.gold;					//Return the gold field of the Hero.
	}
	
	/**
	 * Adds the parameterized int to the hero's currently held gold.
	 * 
	 * @param g					The amount of gold to add to the hero's currency.
	 */
	public void collectGold( int g ) {
		this.gold += g;						//Add the parameter g to the gold field of the Hero.
	}
	
	/**
	 * Removes the parameterized int from the hero's currently held gold.
	 * 
	 * @param g					The amount of gold to remove from the hero's currency.
	 */
	public void spendGold( int g ) {
		if ( ( this.getGold() - g ) >= 0 ) {	//If the result of spending gold results in having greater than or equal to 0 gold:
			this.gold -= g;						//Subtract the parameter g from the gold field of the Hero.
		} else {								//If gold goes below 0, write that they don't have enough gold and don't subtract anything.
			System.out.println("Not enough gold");
		}
	}

	/**
	 * Called when the hero is in combat with an enemy. This is the hero's "turn" in the combat phase.
	 * 
	 * @param e					The Entity object which the hero is in combat with.
	 */
	@Override
	public int attack( Entity e ) {
		Random random = new Random();											//Create new Random object to allow for random number generation.
		int damage = ( random.nextInt( 4 ) + 1 ) * this.getLevel();
		e.takeDamage( damage );

		return damage;
	}

	/**
	 * Calculates the damage of a Magic Missile spell. Damage is a random number from [1,5], multiplied by the hero's level.
	 * 
	 * @return					The damage of the Magic Missile spell.
	 */
	public int magicMissile() {
		Random random = new Random();								//Create a new Random object.
		return ( random.nextInt( 5 ) + 1 ) * this.getLevel();		//Generates a random number in the range of [1,5] and multiplies it by the Hero's level to get the Magic Missile damage and returns it.
	}

	/**
	 * Calculates the damage of a Fireball spell. Damage is a random number from [1,6], multiplied by the hero's level.
	 * 
	 * @return					The damage of the Fireball spell.
	 */
	@Override
	public int fireball() {
		Random random = new Random();								//Create a new Random object.
		return ( random.nextInt( 6 ) + 1 ) * this.getLevel();		//Generates a random number in the range of [1,6] and multiplies it by the Hero's level to get the Fireball damage and returns it.
	}
	

	/**
	 * Calculates the damage of a Thunderclap spell. Damage is a random number from [1,7], multiplied by the hero's level.
	 * 
	 * @return					The damage of the Thunderclap spell.
	 */
	@Override
	public int thunderclap() {
		Random random = new Random();								//Create a new Random object.
		return ( random.nextInt( 7 ) + 1 ) * this.getLevel();		//Generates a random number in the range of [1,7] and multiplies it by the Hero's level to get the Thunderclap damage and returns it.
	}
}