/**
 * Represents an Entity in the game, either the hero or an Enemy.
 * 
 * @author Jonathan Sohrabi 2018
 */
public abstract class Entity {
	
	/**
	 * The name of the Entity.
	 */
	private String name;
	/**
	 * The quip of the Entity.
	 */
	private String quip;
	/**
	 * The level of the Entity.
	 */
	private int level;
	/**
	 * The max HP of the Entity.
	 */
	private int maxHp;
	/**
	 * The current HP of the Entity.
	 */
	private int hp;
	
	/**
	 * Parameterized constructor of the Entity class.
	 * 
	 * @param n					The name of the Entity.
	 * @param q					The quip of the Entity.
	 * @param l					The level of the Entity.
	 * @param m					The max HP of the Entity.
	 */
	public Entity( String n, String q, int l, int m ) {
		this.name = n;			//Sets Entity name to parameter 'n'.
		this.quip = q;			//Sets Entity quip to parameter 'q'.
		this.level = l;			//Sets Entity level to parameter 'l'.
		this.maxHp = m;			//Sets Entity max HP to parameter 'm'.
		this.hp = maxHp;		//Sets Entity HP to their max HP.
	}
	
	/**
	 * Called for the attack "turn" of the Entity in the combat phase.
	 * 
	 * @param e					The target of the attack.
	 * @return 
	 */
	public abstract int attack( Entity e );
	
	/**
	 * Gets the name of the Entity.
	 * 
	 * @return					The name of the Entity.
	 */
	public String getName() {
		return this.name;				//Return Entity's name field.
	}
	
	/**
	 * Gets the quip of the Entity.
	 * 
	 * @return					The quip of the Entity.
	 */
	public String getQuip() {
		return this.quip;				//Return Entity's quip field.
	}
	
	/**
	 * Gets the level of the Entity.
	 * 
	 * @return					The level of the Entity.
	 */
	public int getLevel() {
		return this.level;				//Return Entity's level field.
	}
	
	/**
	 * Gets the current HP of the Entity.
	 * 
	 * @return					The current HP of the Entity.
	 */
	public int getHP() {
		return this.hp;					//Return Entity's HP field.
	}
	
	/**
	 * Gets the max HP of the Entity.
	 * 
	 * @return					The max HP of the Entity.
	 */
	public int getMaxHP() {
		return this.maxHp;				//Return Entity's maxHP field.
	}
	
	/**
	 * Increases the level of the Entity by 1.
	 */
	public void increaseLevel() {
		this.level += 1;				//Adds 1 to Entity level.
	}
	
	/**
	 * Adds the parameterized int to the Entity's current HP.
	 * If the sum of the Entity's max HP and the parameterized int exceed their max HP, their current HP is set to the max HP.
	 * 
	 * @param h					The amount of HP to add.
	 */
	public void heal( int h ) {
		this.hp += h;					//Adds parameter h to Entity's HP field.
		//If Entity has more HP than maxHP, set their HP to their maxHP.
		if ( this.getHP() > this.getMaxHP() ) {
			this.hp = this.getMaxHP();
		}
	}
	
	/**
	 * Subtracts the parameterized int from the Entity's max HP.
	 * If the resulting HP is less than 0, the current HP is set to 0.
	 * 
	 * @param h					The amount of HP to subtract.
	 */
	public void takeDamage( int h ) {
		this.hp -= h;					//Subtracts parameter h from Entity's HP field.
		
		//If Entity's HP is negative, set it equal to 0.
		if ( this.getHP() < 0 ) {
			this.hp = 0;
		}
	}
	
	/**
	 * Increases the Entity's max HP by the parameterized int.
	 * 
	 * @param h					The amount to add to the max HP.
	 */
	public void increaseMaxHP( int h ) {
		this.maxHp += h;				//Add parameter h to Entity's maxHP.
	}
	
	/**
	 * Decreases the Entity's max HP by the parameterized int.
	 * If the result is less than or equal to 0, the max HP is set to 1.
	 * 
	 * @param h					The amount to decrease the Entity's max HP by.
	 */
	public void decreaseMaxHP( int h ) {
		this.maxHp -=h;					//Subtract parameter h from Entity's HP field.
		
		//If the Entity's maxHP is less than or equal to 0, set their maxHP to 1.
		if ( this.getMaxHP() <= 0 )
			this.maxHp = 1;
	}
}
