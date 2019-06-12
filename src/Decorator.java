/**
 * Abstact Decorator class for Enemy
 * 
 * @author Jonathan Sohrabi 2018
 */
public abstract class Decorator extends Enemy {
	private Enemy e;
	
	/**
	 * Calls the Enemy constructor for the given Enemy.
	 * 
	 * @param passedEnemy			The Enemy to invoke the superconstructor on.
	 * @param newName				The new of the passedEnemy with its decorator type appended to it
	 * @param newHP					The max HP of the passedEnemy with the appropriate hp added
	 */
	public Decorator( Enemy passedEnemy, String newName, int newHP ) {
		super( newName, passedEnemy.getQuip(), passedEnemy.getImgPath(), passedEnemy.getLevel(), newHP, passedEnemy.getItem()  );
		e = passedEnemy;
	}
	
	/**
	 * Calls and returns the attack method of the class variable e.
	 * 
	 * @param target				The target of e's attack.
	 */
	@Override
	public int attack( Entity target ) {
		return e.attack( target );
	}
}