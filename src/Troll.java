/**
 * Troll Enemy class.
 * 
 * @author Jonathan Sohrabi 2018
 */
public class Troll extends Enemy {
	/**
	 * Constructs a new Troll Enemy with the given level by calling the Enemy superconstructor.
	 * 
	 * @param hLevel			The level to set the Troll to
	 */
	public Troll( int hLevel ) {
		super( "Troll", "Ugh uh", "./images/monsters/Troll.png", hLevel, 5, ItemGenerator.getInstance().generateItem() );
	}
}