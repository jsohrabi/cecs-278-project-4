/**
 * The main class of the dungeonmaster game.
 * Implements Runnable interface to allow for Threads to be run.
 * 
 * @author Jonathan Sohrabi 2018
 */
public class Main implements Runnable {
	/**
	 * The main method of the dungeonmaster program. Takes in non-combat commands from the player and performs the requested actions.
	 * @param args					The command-line arguments of the dungeonmaster game.
	 */
	public static void main( String[] args ) {
		Thread game = new Thread( new Main() );	// New Thread for the Main class
		game.start();							// Calls the Main class run method
	}
	
	/** 
	 * Creates a new GUI when a Main class Thread is started
	 */
	@Override
	public void run() {
		new GUI();
	}
}