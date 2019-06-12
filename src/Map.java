import java.awt.Point;						//Allows for usage of Point objects.
import java.io.*;							//Allows for usage of files.
import java.util.Scanner;					//Allows for reading of text files.

/**
 * Used as the mapping system of the dungeon floors.
 * 
 * @author Jonathan Sohrabi 2018
 */
public class Map {
	/**
	 * Holds information for each tile on the map.
	 */
	private char[][] map;
	/**
	 * Holds information on whether each tile has been revealed.
	 */
	private boolean[][] revealed;

	/**
	 * Holds a single instance of an Map to fit a Singleton design pattern.
	 */
	private static Map instance = null;
	
	/**
	 * Default constructor for the Map class. Creates new arrays for map tiles and discovered rooms.
	 */
	private Map() {
		map = new char[ 5 ][ 5 ];				//Instantiate map to a 5x5 character array.
		revealed = new boolean[ 5 ][ 5 ];		//Instantiate revealed to a 5x5 boolean array.
	}
	
	/**
	 * Reads in Map tiles from "MapX.txt" textfiles (where X is the map number) and parses them into an array.
	 * 
	 * @param mapNum				The number of the map to load.
	 */
	public void loadMap( int mapNum ) {
		Scanner read = null;										//Create null Scanner object.
		final String file = "./textfiles/Map" + mapNum + ".txt";	//Filepath of each map.
		try {
			read = new Scanner( new File( file ) );					//Allows for reading of the map textfile.
			String line;
			int i = 0;												//Keeps track of each "row" in the map.
			
			//Do while file has unread lines.
			do {
				line = read.nextLine();								//Read in the next line of the file.
				if ( line != null ) {								//If the line is not null:
					char[] mapVals = line.toCharArray();			//Create a new 1D character array to hold each character that was read.
					for ( int j = 0; j < map.length; j++ ) {		//Loop for each index of the map array.
						map[ i ][ j ] = mapVals[ j ];				//Sets map index value to the respective character in mapVals array.
						revealed[ i ][ j ] = false;					//Sets revealed index value to false.
					}
					i++;											//Increment to the next map "row".
				}
			} while ( read.hasNext() );
			read.close();											//Close the Scanner object.
		//If an error occured during file reading, print an error to the console and exit the program.
		} catch ( IOException e ) {
			System.out.println( "An error occured while loading the next map. The program will now exit." );
			System.exit( 0 );
		}
	}
	
	/**
	 * Gets the character value of the Map at the parameterized Point.
	 * 
	 * @param p						The Point of the map to get the character of.
	 * @return						The character of the map at the parameterized Point.
	 */
	public char getCharAtLoc( Point p ) {
		return map[ ( int )p.getX() ][ ( int )p.getY() ];			//Return the map character value at the index given by the Point parameter.
	}
	
	/**
	 * Finds the Point on the Map where with an index value of 's'.
	 * 
	 * @return						The Point on the Map with an 's' index value.
	 */
	public Point findStart() {
		Point start = new Point();
		for ( int i = 0; i < map.length; i++ ) {
			for ( int j = 0; j < map[i].length; j++ ) {
				if ( map[ i ][ j ] == 's' )
					start.setLocation( i, j );
			}
		}
		return start;
	}
	
	/**
	 * Sets the Map index value to discovered at the location of the parameterized Point.
	 * 
	 * @param p						The Point on the Map that is to be marked as discovered.
	 */
	public void reveal( Point p ) {
		revealed[ ( int )p.getX() ][( int )p.getY() ] = true;	//Set the revealed array value to true at the given parameter Point p.
	}
	
	/**
	 * Sets the Map index value as 'n' at the location of the parameterized Point.
	 * 
	 * @param p						The Point where the Map index value is to be changed to an 'n'.
	 */
	public void removeCharAtLoc( Point p ) {
		map[ ( int )p.getX() ][ ( int )p.getY() ] = 'n';		//Replace the map array value at the given parameter Point p with the character 'n'.
	}
	
	/**
	 * Sets the Map room at the given Point to an Item room.
	 * 
	 * @param p		The location to turn into an Item room.
	 */
	public void setItemRoom( Point p ) {
		map[ ( int )p.getX() ][ ( int )p.getY() ] = 'i';
	}

	/**
	 * Returns an instance of a Map object.
	 * 
	 * @return The instance of the Map object.
	 */
	public static Map getInstance() {
		if ( instance == null ) {		// If the Map instance does not exist, create it.
			instance = new Map();
		}
		return instance;
	}
	
	/**
	 * Returns the currently loaded Map as a String
	 * In the String:
	 * 		* = The Hero's current location
	 * 		x = A room that has not been revealed yet
	 * 		m = A monster room
	 * 		i = An Item room
	 * 		s = A shop
	 * 		f = The next floor
	 * 
	 * @param p		The Hero's current location
	 * @return		The currently loaded Map as a String
	 */
	public String toString( Point p ) {
		String returnString = "";
		
		for ( int i = 0; i < map.length; i++ ) {				// Loop through each "row" of the map.
			for ( int j = 0; j < map[i].length; j++ ) {			// Loop through each "column" of the map.
				if ( ( p.getX() == i ) && ( p.getY() == j ) )	// If the Hero's location is at that index, append an asterisk to the String
					returnString += "*";
				else if ( !revealed[ i ][ j ] )					// If the room has not yet been revealed, append an x to the String
						returnString += "x";
				else if ( revealed[ i ][ j ] && map[ i ][ j ] == 'n' )		// If the room has been discovered and is empty, append a space to the String
					returnString += " ";
				else											// If all other cases, append that room's character value to the String
					returnString += map[ i ][ j ];
			}
		}
		
		return returnString;
	}
}