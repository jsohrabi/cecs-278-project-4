import java.awt.Color;
import java.util.Random;
import javax.swing.JLabel;

/**
 * Concrete Decorator for an Enemy.
 * Increases max HP by 2 and allows for an extra attack.
 * 
 * @author Jonathan Sohrabi 2018
 */
public class Warrior extends Decorator {
	/**
	 * Constructs a Warrior decorator for the given Enemy.
	 * 
	 * @param e			The Enemy to decorate with a Warrior decorator
	 */
	public Warrior( Enemy e ) {
		super( e, e.getName() + " Warrior", e.getMaxHP() + 2 );
	}

	/**
	 * Calls the attack method of the superclass, then deals some amount of random damage to the Hero.
	 * 
	 * @param e				The target of the attack.
	 * @return				The total damage that was dealt so far in this attack sequence.
	 */
	@Override
	public int attack( Entity e ) {
		int carriedDamage = super.attack( e );
		Random random = new Random();
		
		int decoratorDamage = ( random.nextInt( 4 ) + 1 ) * this.getLevel();	//Generates a Random number in the range of [1,4] and multiplies it by Enemy's level to get attack damage.
		e.takeDamage( decoratorDamage );			// Deal random damage to the target.
		
		return decoratorDamage + carriedDamage;
	}

	/**
	 * Attacks the Hero and writes the damage to a JLabel.
	 * 
	 * @param e			The target of the attack.
	 * @return			The constructed JLabel that displays the attack info.
	 */
	@Override
	public JLabel combatTurn( Entity e ) {
		JLabel returnedLabel = new JLabel( "<html><center>The enemy hits you " + e.getLevel() + " times for " + attack( e ) + " damage.</center></html>" );
		returnedLabel.setBounds( 20, 70, 220, 30 );
		returnedLabel.setForeground( Color.WHITE );
		
		return returnedLabel;
	}
}