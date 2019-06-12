import java.util.Random;				//Allows for generation of random numbers.

/**
 * Used for generating Enemies.
 * 
 * @author Jonathan Sohrabi 2018
 */
public class EnemyGenerator {
	/**
	 * Holds a single instance of an EnemyGenerator to fit a Singleton design pattern.
	 */
	private static EnemyGenerator instance = null;
	
	/**
	 * Default constructor for the EnemyGenerator class.
	 * Initializes an ArrayList of type Enemy.
	 * Adds one level 1 Enemy of each type as templates.
	 */
	private EnemyGenerator() {
		
	}
	
	/**
	 * Generates an Enemy from the ArrayList of Enemy templates and returns a new instance of that Enemy.
	 * 
	 * @param level					The level of the Enemy to be generated.
	 * @return						The generated Enemy.
	 */
	public Enemy generateEnemy( int level ) {
		Random rng = new Random();
		int randNum;
		Enemy randEnemy;
		
		randNum = rng.nextInt(4);
		if ( randNum == 0 )
			randEnemy = new Orc( level );
		else if ( randNum == 1 )
			randEnemy = new Troll( level );
		else if ( randNum == 2 )
			randEnemy = new Goblin( level );
		else
			randEnemy = new Froglok( level );
		
		// Decorate the Enemy with either Warrior or Warlock
		// Assign one decorator per level above 1
		if ( rng.nextInt( 2 ) == 0 ) {
			for ( int i = 1; i < level; i++ ) {
				randEnemy = new Warrior( randEnemy );
			}
		} else {
			for ( int i = 1; i < level; i++ ) {
				randEnemy = new Warlock( randEnemy );
			}
		}
		
		return randEnemy;
	}

	/**
	 * Returns an instance of an EnemyGenerator object.
	 * 
	 * @return The instance of the EnemyGenerator object.
	 */
	public static EnemyGenerator getInstance() {
		if ( instance == null ) {				// If there is no instance of an EnemyGenerator, make one
			instance = new EnemyGenerator();
		}
		return instance;
	}
}
