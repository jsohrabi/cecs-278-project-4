/**
 * Froglok Enemy class.
 * 
 * @author Jonathan Sohrabi 2018
 */
public class Froglok extends Enemy {
	/**
	 * Constructs a new Froglok Enemy with the given level by calling the Enemy superconstructor.
	 * 
	 * @param hLevel			The level to set the Froglok to
	 */
	public Froglok( int hLevel ) {
		super( "Froglok", "Croooak", "./images/monsters/Frog.png", hLevel, 2, ItemGenerator.getInstance().generateItem() );
	}
}