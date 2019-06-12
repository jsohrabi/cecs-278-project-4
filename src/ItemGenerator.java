import java.io.*;											//Allows for usage of files.
import java.util.Scanner;									//Allows for reading of textfiles.
import java.util.ArrayList;									//Allows for usage of ArrayList objects.
import java.util.Random;									//Allows for generation of random numbers.

/**
 * Used for generating Items.
 * 
 * @author Jonathan Sohrabi 2018
 */
public class ItemGenerator {
	/**
	 * Used to store templates of different Items.
	 */
	private ArrayList<Item> itemList;
	
	/**
	 * Holds a single instance of an ItemGenerator to fit a Singleton design pattern.
	 */
	private static ItemGenerator instance = null;
	
	/**
	 * Default constructor of the ItemGenerator class. Reads in Item information from ItemList.txt and adds them to an ArrayList of Item templates.
	 */
	private ItemGenerator() {
		itemList = new ArrayList<Item>();					//Creates a new ArrayList object for Item objects.
		final String file = "./textfiles/ItemList.txt";		//Filepath of the ItemList.txt file.
		Scanner read = null;								//Creates a null Scanner object.
		try {
			read = new Scanner( new File( file ) );			//Allows for reading of ItemList.txt file.
			String line;
			
			//Loop at least once while there are unread lines in the textfile.
			do {
				line = read.nextLine();						//Read in the next line of the file.
				String itemParams[] = line.split( "," );	//Split the line at each comma and set each value to an index of itemParams String array.
				
				//Adds new indices to the itemList ArrayList for each different Item object.
				// Index 0 = name, index 1 = value, index 2 = image filepath
				itemList.add( new Item( itemParams[ 0 ], Integer.parseInt( itemParams[ 1 ] ), itemParams[ 2 ] ) );
			} while ( read.hasNext() );
			
			read.close();																				//Close the Scanner object.
		//If there was an error with file reading, print an error to the console and end the program.
		} catch ( IOException e ) {
			System.out.println( "An error occured while loading the item list. The program will now exit." );
			System.exit( 0 );
		}
	}
	
	/**
	 * Generates a random Item from the ArrayList of Item templates.
	 * 
	 * @return					A new instance of the generated Item.
	 */
	public Item generateItem() {
		Random random = new Random();					//Creates a new Random object.
		int rng = random.nextInt( itemList.size() );	//Gets a random index of the itemList ArrayList.
		return itemList.get( rng ).clone();				//Clone the randomly chosen Item and return it
	}
	
	/**
	 * Generates a Health Potion from the ArrayList of Item templates.
	 * 
	 * @return					A new instance of a Health Potion Item.
	 */
	public Item getPotion() {
		// Loop through the items ArrayList, find the Health Potion template, and return a clone of it
		for ( Item i : itemList ) {
			if ( i.getName().equals( "Health Potion" ) ) {
				return i.clone();
			}
		}
		return null;
	}
	
	/**
	 * Returns an instance of an ItemGenerator object.
	 * 
	 * @return The instance of the ItemGenerator object.
	 */
	public static ItemGenerator getInstance() {
		if ( instance == null ) {			// If there is no ItemGenerator instance, create one
			instance = new ItemGenerator();
		}
		return instance;
	}
}
