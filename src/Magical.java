/**
 * Interface for Entities who are able to use magic.
 * 
 * @author Jonathan Sohrabi 2018
 */
public interface Magical {
	/**
	 * String to show the Magical attack options.
	 */
	public static final String MAGIC_MENU = "1. Magic Missile\n2. Fireball\n3. Thunderclap";
	
	/**
	 * Calculates the damage of a Magic Missile spell.
	 * 
	 * @return					The damage of the Magic Missile spell.
	 */
	public int magicMissile();

	/**
	 * Calculates the damage of a Fireball spell.
	 * 
	 * @return					The damage of the Fireball spell.
	 */
	public int fireball();
	
	/**
	 * Calculates the damage of a Thunderclap spell.
	 * 
	 * @return					The damage of the Thunderclap spell.
	 */
	public int thunderclap();
}
