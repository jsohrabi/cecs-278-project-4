/**
 * Orc Enemy class.
 * 
 * @author Jonathan Sohrabi 2018
 */
public class Orc extends Enemy {
	/**
	 * Constructs a new Orc Enemy with the given level by calling the Enemy superconstructor.
	 * 
	 * @param hLevel			The level to set the Orc to
	 */
	public Orc( int hLevel ) {
		super( "Orc", "Blarghh", "./images/monsters/Orc.png", hLevel, 4, ItemGenerator.getInstance().generateItem() );
	}
}