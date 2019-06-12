import java.awt.Color;
import java.util.Random;
import javax.swing.JLabel;

/**
 * Represents an Enemy.
 * 
 * @author Jonathan Sohrabi 2018
 */
public abstract class Enemy extends Entity {
	/**
	 * The held Item of the Enemy.
	 */
	private Item item;
	
	/**
	 * The filepath to the Enemy's display image.
	 */
	private String imgPath;
	
	/**
	 * Constructor for the Enemy object.
	 * 
	 * @param n					The name of the Enemy.
	 * @param q					The quip of the Enemy.
	 * @param l					The level of the Enemy.
	 * @param m					The max HP of the Enemy.
	 * @param i					The held Item of the Enemy.
	 */
	public Enemy( String n, String q, String img, int l, int m, Item i ) {
		super( n, q, l, m );					//Calls the Entity superclass constructor with the given parameters.
		imgPath = img;
		item = i;							//Sets this object's item field to the parameter i in the constructor's arguments.
	}
	
	/**
	 * Copy constructor for the Enemy
	 * 
	 * @param e					The Enemy to copy from
	 */
	public Enemy( Enemy e ) {
		super( e.getName(), e.getQuip(), e.getLevel(), e.getMaxHP() );
		item = e.getItem();
	}
	
	/**
	 * Gets the Item held by the Enemy.
	 * 
	 * @return					The Enemy's held Item.
	 */
	public Item getItem() {
		return this.item;				//Returns this Enemy's held Item.
	}
	
	/**
	 * Gets the filepath to the Enemy's display image
	 * 
	 * @return					The filepath to the Enemy's display image.
	 */
	public String getImgPath() {
		return this.imgPath;			//Returns this Enemy's held Item.
	}

	/**
	 * Called for the attack "turn" of the Enemy in the combat phase.
	 * 
	 * @param e					The target of the Enemy's attacks.
	 * @return					The damage that was dealt.
	 */
	@Override
	public int attack( Entity e ) {
		Random random = new Random();									//Creates new Random object to allow for random number generation.
		int damage = ( random.nextInt( 4 ) + 1 ) * this.getLevel();		//Generates a Random number in the range of [1,4] and multiplies it by Enemy's level to get attack damage.

		e.takeDamage( damage );			// Deal damage to the target

		return damage;
	}

	/**
	 * Generates a JLabel that displays the enemy's attack.
	 * @param e					The target of the Enemy's attack
	 * @return					The JLabel that was generated
	 */
	public JLabel combatTurn( Entity e ) {
		// Call attack method to deal damage and write it to the JLabel
		JLabel returnedLabel = new JLabel( "<html><center>The enemy hits you 1 time for " + attack( e ) + " damage.</center></html>" );
		returnedLabel.setBounds( 20, 70, 220, 30 );
		returnedLabel.setForeground( Color.WHITE );
		
		return returnedLabel;
	}
}