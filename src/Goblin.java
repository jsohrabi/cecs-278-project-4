/**
 * Goblin Enemy class.
 * 
 * @author Jonathan Sohrabi 2018
 */
public class Goblin extends Enemy {
	/**
	 * Constructs a new Goblin Enemy with the given level by calling the Enemy superconstructor.
	 * 
	 * @param hLevel			The level to set the Goblin to
	 */
	public Goblin( int hLevel ) {
		super( "Goblin", "Ack Ack", "./images/monsters/Goblin.png", hLevel, 2, ItemGenerator.getInstance().generateItem() );
	}
}