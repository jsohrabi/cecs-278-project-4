import java.awt.Color;
import java.util.Random;
import javax.swing.JLabel;

/**
 * A Warlock decorator for an Enemy
 * 
 * @author Jonathan Sohrabi 2018
 *
 */
public class Warlock extends Decorator implements Magical {
	private Random rng;
	
	/**
	 * Constructs a Warlock object with the given Enemy.
	 * For every Warlock decorator on the Enemy, increases max HP by 1 and allows another attack.
	 * 
	 * @param e				The Enemy to create a Warlock with
	 */
	public Warlock( Enemy e ) {
			super( e, e.getName() + " Warlock", e.getMaxHP() + 1 );
			rng = new Random();
	}
	
	/**
	 * Calculates damage of a Magic Missile spell
	 * Random number in the range of [1,5] * level
	 */
	@Override
	public int magicMissile() {
		return ( rng.nextInt( 5 ) + 1 ) * this.getLevel();
	}

	/**
	 * Calculates damage of a Fireball spell.
	 * Random number in the range of [1,6] * level
	 */
	@Override
	public int fireball() {
		return ( rng.nextInt( 6 ) + 1 ) * this.getLevel();
	}

	/**
	 * Calculates damage of a Thunderclap spell
	 * Random number in the range of [1,7] * level
	 */
	@Override
	public int thunderclap() {
		return ( rng.nextInt( 7 ) + 1 ) * this.getLevel();
	}

	/**
	 * Calls the attack method of the superclass, then chooses a random attack and deals that attack's damage.
	 * 
	 * Attacks have the following probabilities:
	 * 		40% chance of regular attack
	 * 		30% chance of Magic Missile
	 * 		20% chance of Fireball
	 * 		10% chance of Thunderclap
	 * 
	 * @param e			The target of the attack
	 * @return			The total damage dealt at this point
	 */
	@Override
	public int attack( Entity e ) {
		int carriedDamage = super.attack( e );		// Call the superconstructor's attack method and set the output to a variable
		int decoratorDamage = 0; 					// Damage of this current attack
		int attackType = rng.nextInt( 100 ) + 1;	//Generates a random number in the range of [1,100] which we use to determine the type of attack.
		
		//If 1 < attackType <= 40, then MagicalEnemy does a regular attack.
		if ( ( attackType > 1 ) && ( attackType <= 40 ) ) {
			decoratorDamage += ( rng.nextInt( 4 ) + 1 ) * this.getLevel();		//Generates a Random number in the range of [1,4] and multiplies it by MagicalEnemy level to get attack damage.
			
		//If 40 < attackType <= 70, then MagicalEnemy does a Magic Missile attack.
		} else if (attackType > 40 && attackType <= 70) {
			decoratorDamage += magicMissile();									//Set damage equal to return value of MagicMissile().
			
		//If 70 < attackType <= 90, then MagicalEnemy does a Fireball attack.
		} else if ( attackType > 70 && attackType <= 90 ) {
			decoratorDamage += fireball();										//Set damage equal to return value of Fireball().
			
		//If attackType > 90, then MagicalEnemy does a Thunderclap attack.
		} else {
			decoratorDamage += thunderclap();									//Set damage equal to return value of Thunderclap().
		}
		e.takeDamage( decoratorDamage );		// Deal damage to the target
		return carriedDamage + decoratorDamage;	// Return the total damage is so far
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