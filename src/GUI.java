import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;

/*
 * Note about this class:
 * 		Most of the code used here is setting up components and adding them to one another.
 * 		In an attempt to reduce having to rewrite essentially the same comments, these are some of the commonly used methods:
 * 			add() - Adds this component to its parent
 * 			revalidate(), repaint() - Called in succession to update whatever invokes it
 * 			removeAll() - Removes all components from the JPanel that calls it
 * 			setBackground( color ) - Sets the background color to what is in the parameter
 * 			setBounds( x, y, width, height ) - Sets the component's position at (x,y) on its parent, width dimensions width and height
 * 			setFont( font ) - Sets the text font to what is in the parameter
 * 			setForeground( color ) - Sets text color to what is in the parameter
 * 			setLayout( layout ) - Sets the layout to the layout manager given in the parameter
 * 			setPreferredSize( dimensions ) - Sets the preferred size of the component to the dimensions in the parameter
 * 
 * 			sideDisplay.updateRoomInfo( title, panel ) - In the side panel, sets the bottom display title to the title parameter, other room information to the panel parameter
 */

/**
 * The main GUI display of the game.
 * 
 * @author Jonathan Sohrabi
 */
public class GUI extends JFrame implements KeyListener {
	/** The Map object for the game.*/
	private Map curMap;

	/** The number of the currently loaded map.*/
	private int mapNum;

	/** The Hero object for the game.*/
	private Hero heroObj;

	/** The JPanel that displays the map.*/
	private MapPanel guiMap;

	/** The Panel on the side that displays the Hero and room information.*/
	private SidePanel sideDisplay;

	/** The main JFrame that holds all GUI oomponents.*/
	private JFrame display;

	/** Whether the player is in the shop.*/
	private boolean shopState;

	/** Whether the player is fighting something.*/
	private boolean fightState;

	/** Whether the player is selling something.*/
	private boolean sellingState;

	/** Whether the player just entered a new floor.*/
	private boolean floorEntered;
	
	/**
	 * Constructor for setting up the GUI
	 */
	public GUI() {
		curMap = Map.getInstance();					// Get an instance of the Map class
		mapNum = 1;									// Sets the current map to map 1
		curMap.loadMap( mapNum );					// Load map 1
		display = new JFrame( "Dungeon Master" );	// Make a new JFrame with the title "Dungeon Master"
		
		// Set all boolean states to false
		shopState = false;
		fightState = false;
		sellingState = false;
		floorEntered = true;
		String heroName = "";
		String heroQuip = "";
		
		// Get the Hero's name and quip through JOptionPane popups. If the user enters nothing, ask again
		while ( heroName.equals( "" ) ) {
			heroName = JOptionPane.showInputDialog( this, "What's your name, adventurer?", "What's Your Name?", JOptionPane.QUESTION_MESSAGE );
		}
		while ( heroQuip.equals( "" ) ) {
			heroQuip = JOptionPane.showInputDialog( this, "Every tough adventurer needs a strong battlecry. What's yours?", "What's Your Battlecry?", JOptionPane.QUESTION_MESSAGE );
		}
		
		heroObj = new Hero( heroName, heroQuip, curMap );	// Construct a new Hero object with the name and quip
		
		JOptionPane.showMessageDialog( this, "Welcome, " + heroObj.getName() + "! To navigate these dark dungeons, use the WASD keys. Good luck, and emerge victorious!", "Welcome, Adventurer", JOptionPane.PLAIN_MESSAGE );
		
		guiMap = new MapPanel();		// Make a new display object for the map
		sideDisplay = new SidePanel();	// Make a new display object for the hero and room information
		display.setLayout( new FlowLayout( FlowLayout.LEFT, 0, 0 ) );
		display.setSize( 1001, 738 );
		display.add( guiMap );
		display.add( sideDisplay );
		display.addKeyListener( this );	// Add a keyboard listener to this object
		display.setFocusable( true );
		display.setVisible( true );		// Set this object as visible
		display.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );	// Quit the program when the window is closed
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// Unused implemented method
	}

	@Override
	public void keyPressed( KeyEvent e ) {
		// Unused implemented method
	}

	// Things to do when the user releases a key
	@Override
	public void keyReleased(KeyEvent e) {
		// If the "w" key was released, move the hero north if they aren't in a fight or shop
		// Check the new room they are in
		if ( e.getKeyCode() == KeyEvent.VK_W ) {
			if ( !shopState && !fightState ) {
				sideDisplay.updateRoomInfo( "", new BlankPanel() );	//
				heroObj.goNorth();
				roomCheck();
				floorEntered = false;
			}
		}

		// If the "a" key was released, move the hero west if they aren't in a fight or shop
		// Check the new room they are in
		if ( e.getKeyCode() == KeyEvent.VK_A ) {
			if ( !shopState && !fightState ) {
				sideDisplay.updateRoomInfo( "", new BlankPanel() );
				heroObj.goWest();
				roomCheck();
				floorEntered = false;
			}
		}

		// If the "s" key was released, move the hero south if they aren't in a fight or shop
		// Check the new room they are in
		if ( e.getKeyCode() == KeyEvent.VK_S ) {
			if ( !shopState && !fightState ) {
				sideDisplay.updateRoomInfo( "", new BlankPanel() );
				heroObj.goSouth();
				roomCheck();
				floorEntered = false;
			}
		}

		// If the "d" key was released, move the hero east if they aren't in a fight or shop
		// Check the new room they are in
		if ( e.getKeyCode() == KeyEvent.VK_D ) {
			if ( !shopState && !fightState ) {
				sideDisplay.updateRoomInfo( "", new BlankPanel() );
				heroObj.goEast();
				floorEntered = false;
				roomCheck();
			}
		}
		
		if ( e.getKeyCode() == KeyEvent.VK_1 ) {
			if ( shopState && sellingState ) {
				if ( heroObj.getNumItems() >= 1 ) {
					Item soldItem = heroObj.removeItem( 0 );
					if ( soldItem != null && soldItem.getValue() < heroObj.getHP() ) {
						heroObj.collectGold( soldItem.getValue() );
					} else {
						JOptionPane.showMessageDialog( this, "You can't sell this item, it would kill you to do so", "Failed Selling Item", JOptionPane.PLAIN_MESSAGE );
					}
					
					sideDisplay.updateHeroInfo();
				}
			}
		}
		
		if ( e.getKeyCode() == KeyEvent.VK_2 ) {
			if ( shopState && sellingState ) {
				if ( heroObj.getNumItems() >= 2 ) {
					Item soldItem = heroObj.removeItem( 1 );
					if ( soldItem != null && soldItem.getValue() < heroObj.getHP() ) {
						heroObj.collectGold( soldItem.getValue() );
					} else {
						JOptionPane.showMessageDialog( this, "You can't sell this item, it would kill you to do so", "Failed Selling Item", JOptionPane.PLAIN_MESSAGE );
					}
					
					sideDisplay.updateHeroInfo();
				}
			}
		}
		
		if ( e.getKeyCode() == KeyEvent.VK_3 ) {
			if ( shopState && sellingState ) {
				if ( heroObj.getNumItems() >= 3 ) {
					Item soldItem = heroObj.removeItem( 2 );
					if ( soldItem != null && soldItem.getValue() < heroObj.getHP() ) {
						heroObj.collectGold( soldItem.getValue() );
					} else {
						JOptionPane.showMessageDialog( this, "You can't sell this item, it would kill you to do so", "Failed Selling Item", JOptionPane.PLAIN_MESSAGE );
					}
					
					sideDisplay.updateHeroInfo();
				}
			}
		}
		
		if ( e.getKeyCode() == KeyEvent.VK_4 ) {
			if ( shopState && sellingState ) {
				if ( heroObj.getNumItems() >= 4 ) {
					Item soldItem = heroObj.removeItem( 3 );
					if ( soldItem != null && soldItem.getValue() < heroObj.getHP() ) {
						heroObj.collectGold( soldItem.getValue() );
					} else {
						JOptionPane.showMessageDialog( this, "You can't sell this item, it would kill you to do so", "Failed Selling Item", JOptionPane.PLAIN_MESSAGE );
					}
					
					sideDisplay.updateHeroInfo();
				}
			}
		}
		
		if ( e.getKeyCode() == KeyEvent.VK_5 ) {
			if ( shopState && sellingState ) {
				if ( heroObj.getNumItems() == 5 ) {
					Item soldItem = heroObj.removeItem( 4 );
					if ( soldItem != null && soldItem.getValue() < heroObj.getHP() ) {
							heroObj.collectGold( soldItem.getValue() );
					} else {
						JOptionPane.showMessageDialog( this, "You can't sell this item, it would kill you to do so", "Failed Selling Item", JOptionPane.PLAIN_MESSAGE );
					}
					
				sideDisplay.updateHeroInfo();
				}
			}
		}
	}
	
	/**
	 * Checks the room that the Hero is currently in and does appropriate actions.
	 */
	private void roomCheck() {
		JPanel nextFloor = new JPanel();
		JLabel roomStatus = new JLabel( "<html><center>You stumble upon the next floor and have leveled up. Your foes also become stronger.</center></html>", SwingConstants.CENTER );
		
		nextFloor.setLayout( null );
		nextFloor.setBackground( Color.BLACK );
		nextFloor.setBounds( 0, 20, 250, 60 );
		
		roomStatus.setFont( new Font( "Helvetica", Font.PLAIN, 20 ) );
		roomStatus.setBounds( 10, 10, 230, 100 );
		roomStatus.setForeground( Color.WHITE );
		nextFloor.add( roomStatus );
		
		//If the Hero is standing on an 'f' tile, load the next map
		if ( curMap.getCharAtLoc( heroObj.getLocation() ) == 'f' ) {
			sideDisplay.updateRoomInfo( "", nextFloor );
			
			heroObj.increaseLevel();						//Increase Hero level.
			heroObj.increaseMaxHP( 10 );					//Add 10 to Hero maxHP.
			heroObj.heal( 10 );								//Add 10 to Hero HP.
			fightState = false;								// Set Hero as not fighting anything
			floorEntered = true;
			if ( mapNum == 3 ) {							//If the current Map is Map3, load Map1.
				mapNum = 1;
				curMap.loadMap( 1 );
			} else {										//Load the next Map in order.
				mapNum++;
				curMap.loadMap( mapNum );
			}
			curMap.reveal( heroObj.getLocation() );			//Set the Hero's location on the Map to revealed.
		}
		
		//If the Hero is standing on an 'i' tile, call itemRoom method.
		if ( curMap.getCharAtLoc( heroObj.getLocation() ) == 'i' ) {
			fightState = false;								// Set Hero as not fighting anything
			itemRoom();
		}
		
		//If the Hero is standing on an 's' tile, call store method.
		if ( curMap.getCharAtLoc( heroObj.getLocation() ) == 's' && !floorEntered ) {
			fightState = false;								// Set Hero as not fighting anything		
			store();
		}
		
		//If the Hero is standing on an 'm' tile, call monsterRoom method.
		if ( curMap.getCharAtLoc( heroObj.getLocation() ) == 'm' ) {
			fightState = true;								// Set Hero as fighting something
			monsterRoom();
		}
		
		guiMap.updateMap();					// Update the map display
		sideDisplay.updateHeroInfo();		// Update the Hero information display
	}
	
	/**
	 * Performs shop actions requested by the player when the Hero is on a Map tile notated by the character 's'.
	 */
	private void store() {
		shopState = true;					// Set Hero as in the shop
		
		JPanel shopPanelMain = new ShopPanelMain();		// Creates a new JPanel for the initial shopping display
		
		sideDisplay.updateRoomInfo( "Shop", shopPanelMain );
	}
	
	/**
	 * Adds an Item to the Hero's inventory when they enter a tile notated by the character 'i', provided the Hero is able to pick up the Item.
	 */
	private void itemRoom() {
		JPanel itemPicked = new JPanel();
		JLabel roomStatus;
		
		itemPicked.setLayout( null );
		itemPicked.setBackground( Color.BLACK );
		itemPicked.setBounds( 0, 20, 250, 70 );
		
		if ( heroObj.getNumItems() != 5 ) {							//If Hero has inventory space:
			Item i = ItemGenerator.getInstance().generateItem();	//	Generate a random Item.
			heroObj.pickUpItem( i );								//	Hero picks up Item.
			roomStatus = new JLabel( "<html><center>You picked up a " + i.getName() + "</center></html>", SwingConstants.CENTER );	// Tell the user about the item they picked up
			curMap.removeCharAtLoc( heroObj.getLocation() );		//	Sets tile at Hero location to 'n'.
		// If the user has 5 items, they do not pick anything up and map tile stays the same.
		} else {
			roomStatus = new JLabel( "<html><center>There's an item here, but your inventory is full.</center></html>", SwingConstants.CENTER );	// Tell the user they don't have inventory space
		}
		
		roomStatus.setFont( new Font( "Helvetica", Font.PLAIN, 20 ) );
		roomStatus.setForeground( Color.WHITE );
		roomStatus.setBounds( 0, 20, 250, 40 );
		itemPicked.add( roomStatus );
		sideDisplay.updateRoomInfo( "Item Room", itemPicked  );
	}
	
	/**
	 * Begins a fight with some monster when the Hero enters a tile notated by the character 'm'.
	 */
	private void monsterRoom() {
		Enemy e = EnemyGenerator.getInstance().generateEnemy( heroObj.getLevel() );	// Generate a random Enemy with the Hero's level
		MonsterRoomPanel entry = new MonsterRoomPanel( e );							// Create a new fight display to show on the side
		sideDisplay.updateRoomInfo( e.getName(), entry );
	}
	
	/**
	 * Used as graphical display of the map
	 * 
	 * @author Jonathan Sohrabi 2018
	 */
	private class MapPanel extends JPanel {
		/**
		 * Creates 25 MapTiles for displaying the map
		 */
		private MapPanel() {
			setLayout( new GridLayout( 5, 5, 2, 2 ) );
			setPreferredSize( new Dimension( 700, 700 ) );
			String map = curMap.toString( heroObj.getLocation() );	// Gets the current map data as a String
			
			for ( int i = 0; i < 25; i++ ) {			// Adds 25 MapTile JPanels, initialized with a character from the map String
				add( new MapTile( map.charAt( i ) ) );
			}
		}
		
		/**
		 * Updates the displayed map.
		 */
		private void updateMap() {
			String map = curMap.toString( heroObj.getLocation() );	// Gets the current map data as a String
			
			removeAll();
			
			for ( int i = 0; i < 25; i++ ) {			// Adds 25 MapTile JPanels, initialized with a character from the map String
				add( new MapTile( map.charAt( i ) ) );
			}
			
			revalidate();
			repaint();
		}
	}
	
	/**
	 * Displays information about a room in the map
	 * 
	 * @author Jonathan Sohrabi 2018
	 */
	private class MapTile extends JPanel {
		/** The character that this room represents */
		private char tile;
		
		/**
		 * Sets tile to the parameter character.
		 * 
		 * @param tile			What character to set the tile to.
		 */
		private MapTile( char tile ) {
			this.tile = tile;
		}
		
		/**
		 * Draws things to the JPanel.
		 */
		protected void paintComponent( Graphics g ) {
			super.paintComponent( g );
			g.setColor( Color.BLACK );
			g.fillRect( 0, 0, getWidth(), getHeight() );
			
			// If tile is an asterisk, display the Hero's image if it can be found.
			if ( tile == '*' ) {
				try {
					BufferedImage img = ImageIO.read( new File( "./images/map/Redmage.png" ) );
					g.drawImage( img, 37, 30, this );
				} catch ( IOException e ) {
					System.out.println( "Hero image not found." );
				}
			}

			// If tile is an 's', display the Shop's image if it can be found.
			if ( tile == 's' ) {
				try {
					BufferedImage img = ImageIO.read( new File( "./images/map/shop.png" ) );
					g.drawImage( img, 37, 30, this );
				} catch ( IOException e ) {
					System.out.println( "Shop room image not found." );
				}
			}

			// If tile is an 'i', display the ItemRoom's image if it can be found.
			if ( tile == 'i' ) {
				try {
					BufferedImage img = ImageIO.read( new File( "./images/map/itemtile.png" ) );
					g.drawImage( img, 37, 30, this );
				} catch ( IOException e ) {
					System.out.println( "Item room image not found." );
				}
			}

			// If tile is an 'm', display the MonsterRoom's image if it can be found.
			if ( tile == 'm' ) {
				try {
					BufferedImage img = ImageIO.read( new File( "./images/map/monstertile.png" ) );
					g.drawImage( img, 37, 30, this );
				} catch ( IOException e ) {
					System.out.println( "Monster room image not found." );
				}
			}

			// If tile is a space, display the a blank tile
			if ( tile == 'n' ){
				g.setColor( Color.WHITE );
				g.setFont( new Font( "Helvetica", Font.PLAIN, 30 ) );
				g.drawString( " ", ( getWidth() / 2 ) - 5, ( getHeight() / 2 ) + 5 );
			}

			// If tile is an 'x', display a question mark.
			if ( tile == 'x' ) {
				g.setColor( Color.WHITE );
				g.setFont( new Font( "Helvetica", Font.PLAIN, 30 ) );
				g.drawString( "?", ( getWidth() / 2 ) - 5, ( getHeight() / 2 ) + 5 );
			}
		}
	}
	
	/**
	 * Holds information about the Hero and the room they are currently in.
	 * 
	 * @author Jonathan Sohrabi 2018
	 */
	private class SidePanel extends JPanel {
		/** JPanel for displaying information about the Hero */
		private HeroInfoPanel heroInfo;
		
		/** JPanel for displaying information about the room the Hero is in */
		private RoomInfoPanel roomInfo;
		
		/**
		 * Adds the Hero and room information JPanels to this JPanel.
		 */
		private SidePanel() {
			heroInfo = new HeroInfoPanel();
			roomInfo = new RoomInfoPanel();
			
			setPreferredSize( new Dimension( 285, 700 ) );
			setLayout( new BoxLayout( this, BoxLayout.Y_AXIS ) );
			add( heroInfo );
			add( roomInfo );
		}
		
		/**
		 * Calls the Hero information JPanel updateInfo method to update information about the Hero.
		 */
		private void updateHeroInfo() {
			heroInfo.updateInfo();
		}
		
		/**
		 * Calls the room information JPanel updateRoomInfo method to update information about the room.
		 * 
		 * @param title			The title of the room.
		 * @param info			The JPanel that displays the room information.
		 */
		private void updateRoomInfo( String title, JPanel info ) {
			roomInfo.updateRoomInfo( title, info );
		}
	}
	
	/**
	 * Displays information about the Hero.
	 * 
	 * @author Jonathan Sohrabi 2018
	 */
	private class HeroInfoPanel extends JPanel {
		/**  Title for this display ("Hero Information") */
		private JLabel panelTitle;
		/** JLabel for the Hero's name and level */
		private JLabel heroLevel;
		/** JLabel for Hero's health */
		private JLabel heroHealth;
		/** JLabel for Hero's quip */
		private JLabel heroQuip;
		/** JLabel for Hero's gold */
		private JLabel heroGold;
		/** JLabel for indicating where the inventory is */
		private JLabel heroInvTitle;
		/** JPanel that shows the Hero's inventory */
		private InventoryPanel invPanel;
		
		/**
		 * Sets up this JPanel by adding components to it.
		 */
		private HeroInfoPanel() {
			setLayout( null );
			
			panelTitle = new JLabel( "Hero Information" );
			panelTitle.setBounds( 35, 23, 250, 22 );
			panelTitle.setFont( new Font( "Helvetica", Font.PLAIN, 28 ) );
			panelTitle.setForeground( Color.WHITE );
			add( panelTitle );
			
			heroLevel = new JLabel( heroObj.getName() + ", Level " + heroObj.getLevel() );
			heroLevel.setBounds( 25, 55, 180, 25 );
			heroLevel.setFont( new Font( "Helvetica", Font.PLAIN, 21 ) );
			heroLevel.setForeground( Color.WHITE );
			add( heroLevel );
			
			heroQuip = new JLabel( "\"" + heroObj.getQuip() + "\"" );
			heroQuip.setBounds( 25, 80, 180, 25 );
			heroQuip.setFont( new Font( "Helvetica", Font.PLAIN, 21 ) );
			heroQuip.setForeground( Color.WHITE );
			add( heroQuip );
			
			heroHealth = new JLabel( "HP: " + heroObj.getHP() + "/" + heroObj.getMaxHP() );
			heroHealth.setBounds( 25, 105, 180, 25 );
			heroHealth.setFont( new Font( "Helvetica", Font.PLAIN, 21 ) );
			heroHealth.setForeground( Color.WHITE );
			add( heroHealth );
			
			heroGold = new JLabel( "Gold: " + heroObj.getGold() );
			heroGold.setBounds( 25, 130, 180, 25 );
			heroGold.setFont( new Font( "Helvetica", Font.PLAIN, 21 ) );
			heroGold.setForeground( Color.WHITE );
			add( heroGold );
			
			heroInvTitle = new JLabel( "Inventory:" );
			heroInvTitle.setBounds( 25, 180, 180, 25 );
			heroInvTitle.setFont( new Font( "Helvetica", Font.PLAIN, 21 ) );
			heroInvTitle.setForeground( Color.WHITE );
			add( heroInvTitle );
			
			invPanel = new InventoryPanel();
			invPanel.setBounds( 38, 180, 220, 150 );
			add( invPanel );
			
			setBackground( Color.BLACK );
		}
		
		/**
		 * Draws rectangles to the panel for aesthetic purposes.
		 */
		protected void paintComponent( Graphics g ) {
			super.paintComponent( g );
			g.setColor( Color.WHITE );
			g.drawRect( 20, 20, 245, 320 );
			g.drawRect( 20, 20, 245, 30 );
		}
		
		/**
		 * Updates the Hero information panel.
		 */
		private void updateInfo() {
			removeAll();
			
			add( panelTitle );
			
			heroLevel.setText( heroObj.getName() + ", Level " + heroObj.getLevel() );
			add( heroLevel );
			
			heroHealth.setText( "HP: " + heroObj.getHP() + "/" + heroObj.getMaxHP() );
			add( heroHealth );
			
			heroGold.setText( "Gold: " + heroObj.getGold() );
			add( heroGold );
			
			add( heroInvTitle );
			
			invPanel = new InventoryPanel();
			invPanel.setBounds( 38, 180, 220, 150 );
			add( invPanel );
			
			setBackground( Color.BLACK );
			
			revalidate();	
			repaint();
		}
		
		/**
		 * Panel for keeping track of all of the Hero's inventory.
		 * 
		 * @author Jonathan Sohrabi 2018
		 */
		private class InventoryPanel extends JPanel {
			/** Holds a copy of the Hero's items. */
			private ArrayList<JPanel> items;
			
			/**
			 * Sets up this JPanel by adding the desired components to it.
			 */
			private InventoryPanel() {
				items = new ArrayList<JPanel>();
				int itemCount = 0;
				int itemX = 0;
				
				setLayout( null );
				setBackground( Color.BLACK );
				
				// Loop 5 times to set up the individual Item panes.
				for ( int i = 0; i < 5; i++ ) {
					items.add( new ItemPanel( i ) );
					items.get( itemCount ).setBounds( itemX, 70, 40, 40 );
					add( items.get( itemCount ) );
					itemCount++;
					itemX += 42;
				}
			}
		}
	}
	
	/**
	 * Displays an Item and allows for interaction with the panel.
	 * 
	 * @author Jonathan Sohrabi 2018
	 */
	private class ItemPanel extends JPanel implements MouseListener {
		/** The Item that the panel references. */
		private Item panelItem;
		
		/**
		 * Sets up this JPanel and sets the class field Item to the parameter.
		 * 
		 * @param i				The tested index of the player's inventory.
		 */
		private ItemPanel( int i ) {
			if ( i < heroObj.getNumItems() ) {
				panelItem = heroObj.getItems().get( i );
			} else {
				panelItem = null;
			}
			
			setBackground( Color.BLACK );
			
			addMouseListener( this );		// Adds a mouse listener to this object
			setFocusable( true );
		}
		
		/**
		 * Draws a tile with the Item's image inscribed in it.
		 */
		protected void paintComponent( Graphics g ) {
			super.paintComponent( g );
			g.setColor( Color.WHITE );
			g.drawRect( 0, 0, getWidth() - 1, getHeight() - 1 );
			
			// If this inventory slot has an Item, draw it.
			if ( panelItem != null ) {
				try {
					BufferedImage img = ImageIO.read( new File( panelItem.getImgPath() ) );
					g.drawImage( img, 1, 1, this );
				} catch ( IOException e ) {
					System.out.println( "Item image not found." );
				}
			}
		}

		/**
		 * Allows the Item to be sold.
		 */
		@Override
		public void mouseClicked(  MouseEvent e ) {
			// If in selling state and there is an item in this panel, sell it 
			if ( sellingState && panelItem != null ) {
				Item soldItem = heroObj.removeItem( panelItem.getName() );
				if ( soldItem.getValue() < heroObj.getHP() ) {
					heroObj.collectGold( soldItem.getValue() );
				} else {
					JOptionPane.showMessageDialog( this, "You can't sell this item, it would kill you to do so", "Failed Selling Item", JOptionPane.PLAIN_MESSAGE );
				}
				sideDisplay.updateHeroInfo();
			}
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	/**
	 * Holds info for the room that the Hero is in.
	 * 
	 * @author Jonathan Sohrabi 2018
	 */
	private class RoomInfoPanel extends JPanel {
		/** The title of this display. */
		private JLabel roomLabel;
		/** The information to display. */
		private JPanel infoPanel;
		
		/**
		 * Sets up this JPanel with the desired properties and components.
		 */
		private RoomInfoPanel() {
			setLayout( null );
			
			roomLabel = new JLabel();
			roomLabel.setBounds( 20, 23, 245, 25 );
			roomLabel.setFont( new Font( "Helvetica", Font.PLAIN, 25 ) );
			roomLabel.setForeground( Color.WHITE );
			add( roomLabel );
			
			infoPanel = new JPanel();
			
			setBackground( Color.BLACK );
		}

		/**
		 * Draws a couple of rectangles to this panel
		 */
		protected void paintComponent( Graphics g ) {
			super.paintComponent( g );
			g.setColor( Color.WHITE );
			g.drawRect( 20, 20, 245, 320 );
			g.drawRect( 20, 20, 245, 30 );
		}
		
		/**
		 * Updates the room information by putting the title and panel on it.
		 * 
		 * @param title				The title of the room.
		 * @param info				The information of the room.
		 */
		private void updateRoomInfo( String title, JPanel info ) {
			removeAll();
			
			roomLabel.setText( title );
			roomLabel.setHorizontalAlignment( SwingConstants.CENTER );
			add( roomLabel );
			
			infoPanel = info;
			infoPanel.setBounds( 21, 53, 240, 280 );
			add( infoPanel );
			
			revalidate();
			repaint();
		}
	}
	
	/**
	 * A blank JPanel.
	 * 
	 * @author Jonathan Sohrabi 2018
	 */
	private class BlankPanel extends JPanel {
		/**
		 * Creates a JPanel with nothing in it.
		 */
		private BlankPanel() {
			setBounds( 0, 0, 0, 0 );
			setBackground( Color.BLACK );
		}
	}
	
	/**
	 * A panel for the main menu of the shop.
	 * 
	 * @author Jonathan Sohrabi 2018
	 */
	private class ShopPanelMain extends JPanel implements ActionListener {
		/** A welcome message. */
		private JLabel welcomeLabel = new JLabel( "<html><center>Welcome, adventurer!<br/>Please, browse my wares.</center></html>" );
		/** A button for buying potions */
		private JButton buyButton = new JButton( "Buy a Potion (25 Gold)" );
		/** A button for selling items */
		private JButton sellButton = new JButton( "Sell Items" );
		/** A button for exiting the shop */
		private JButton exitButton = new JButton( "Exit" );
		
		/**
		 * Sets up the shop JPanel by adding the desired components to it.
		 */
		private ShopPanelMain() {
			setBackground( Color.BLACK );
			setLayout( null );
			
			welcomeLabel.setFont( new Font( "Helvetica", Font.PLAIN, 17 ) );
			welcomeLabel.setForeground( Color.WHITE );
			welcomeLabel.setBounds( 17, 10, 250, 60 );
			add( welcomeLabel );	

			buyButton.setBounds( 25, 130, 180, 30 );
			buyButton.addActionListener( this );
			add( buyButton );

			sellButton.setBounds( 25, 170, 180, 30 );
			sellButton.addActionListener( this );
			add( sellButton );

			exitButton.setBounds( 25, 210, 180, 30 );
			exitButton.addActionListener( this );
			add( exitButton );
		}

		/**
		 * Performs an action when a button is clicked.
		 */
		@Override
		public void actionPerformed( ActionEvent e ) {
			// If the button for buying potions triggered an event
			if ( e.getSource() == buyButton ) {
				JLabel boughtItem = new JLabel();
				
				removeAll();
				
				// If the hero has over 25 or more gold, let them buy a potion.
				if ( heroObj.getGold() >= 25 ) {
					if ( heroObj.pickUpItem( ItemGenerator.getInstance().getPotion() ) ) {
						boughtItem.setText( "<html><center>Here's your potion, " + heroObj.getName() + "</center></html>" );
						boughtItem.setBounds( 40, 65, 250, 60 );
						heroObj.spendGold( 25 );
						sideDisplay.updateHeroInfo();
					} else {
						boughtItem.setText( "<html><center>You don't have enough inventory space for a potion.</center></html>" );
						boughtItem.setBounds( 0, 65, 250, 60 );
					}
				} else {
					boughtItem.setText( "<html><center>You don't have enough money for a potion.</center></html>" );
					boughtItem.setBounds( 0, 65, 250, 60 );
				}
				add( welcomeLabel );	
				add( buyButton );
				add( sellButton );
				add( exitButton );
				
				boughtItem.setFont( new Font( "Helvetica", Font.PLAIN, 17 ) );
				boughtItem.setForeground( Color.WHITE );
				add( boughtItem );
				
				revalidate();
				repaint();
			}
			// If the button for selling items triggered the event
			if ( e.getSource() == sellButton ) {
				// If the Hero has items, show the selling menu.
				if ( heroObj.getNumItems() > 0 ) {	
					JPanel shopPanelSell = new ShopPanelSell();
					
					sideDisplay.updateRoomInfo( "Shop", shopPanelSell );
				// If the Hero doesn't have items, dont let them sell.
				} else {
					removeAll();
					
					JLabel failedSell = new JLabel( "<html><center>You can't sell your items if you don't have any!</center></html>" );
					failedSell.setFont( new Font( "Helvetica", Font.PLAIN, 17 ) );
					failedSell.setForeground( Color.WHITE );
					failedSell.setBounds( 0, 65, 250, 60 );
					add( failedSell );	
					add( welcomeLabel );	
					add( buyButton );
					add( sellButton );
					add( exitButton );
					
					revalidate();
					repaint();
				}
			}
			// If the exit button triggered an avent, exit the shop.
			if ( e.getSource() == exitButton ) {
				shopState = false;
				sideDisplay.updateRoomInfo( "",  new BlankPanel() );
			}
		}
	}
	/**
	 * Selling menu for the shop.
	 * 
	 * @author Jonathan Sohrabi 2018
	 */
	private class ShopPanelSell extends JPanel implements ActionListener {
		/** Button for exiting out of the sell menu. */
		private JButton exitButton;
		
		/**
		 * Creates a new menu for selling.
		 */
		private ShopPanelSell() {
			setLayout( null );
			setBackground( Color.BLACK );
			sellingState = true;			// Set state to say Hero is selling.
			
			JLabel prompt = new JLabel( "<html><center>Whaddya wanna sell?<br/>(Use 1-5 keys or click the item to sell it.)</center></html>" );
			prompt.setFont( new Font( "Helvetica", Font.PLAIN, 17 ) );
			prompt.setForeground( Color.WHITE );
			prompt.setBounds( 0, 20, 250, 60 );
			add( prompt );

			exitButton = new JButton( "Exit" );
			exitButton.setBounds( 25, 210, 180, 30 );
			exitButton.addActionListener( this );
			
			add( exitButton );
		}

		/**
		 * Performs actions when a button is clicked.
		 */
		@Override
		public void actionPerformed( ActionEvent e ) {
			// If the exit button triggered the event, set Hero is not selling anymore. Return to the main shop msnu.
			if ( e.getSource() == exitButton ) {
				sellingState = false;
				sideDisplay.updateRoomInfo( "Shop", new ShopPanelMain() );
			}
		}
	}
	
	/**
	 * Panel for fighting a monster.
	 * 
	 * @author Jonathan Sohrabi 2018
	 */
	private class MonsterRoomPanel extends JPanel implements ActionListener {
		/** Enemy that the hero is fighting */
		private Enemy enemy;
		/** Shows the health of the Enemy */
		private JLabel eHealth;
		/** Shows the quip of the Enemy */
		private JLabel eQuip;
		/** Main screen of a battle */
		private JPanel fightMain = new JPanel();
		/** Menu for when player wants to fight */
		private JPanel fightOptions = new JPanel();
		/** Menu for when player wants to use magic */
		private JPanel magicOptions = new JPanel();
		/** Button to get to fight menu */
		private JButton fightButton = new JButton( "Fight" );
		/** Button to run away */
		private JButton runButton = new JButton( "Run Away" );
		/** Button for a physical attack. */
		private JButton physical = new JButton( "Physical Attack" );
		/** Button to get to magic menu. */
		private JButton magical = new JButton( "Magical Attack" );
		/** Button to use a potion */
		private JButton potion = new JButton( "Use Potion" );
		/** JLabel to display that the player drank a potion. */
		private JLabel usedPotion = new JLabel( "<html><center>You hastily chug a potion<br>in the middle of combat.</center></html>" );
		/** Button to use a magic missile. */
		private JButton magicMissile = new JButton( "Magic Missile" );
		/** Button to use a fireball */
		private JButton fireball = new JButton( "Fireball" );
		/** Button to use a thunderclap */
		private JButton thunderclap = new JButton( "Thunderclap" );
		/** The damage that the hero dealt */
		private int heroDamage;
		/** Label to show what the Hero did in combat */
		private JLabel heroCombatLabel = new JLabel( "" );
		/** Label to show what the enemy did in combat */
		private JLabel enemyDamage = new JLabel();
		
		/**
		 * Sets up the JPanel by adding components to it.
		 * Enemy also attacks Hero when called.
		 * 
		 * @param e				The enemy that the hero is fighting.
		 */
		private MonsterRoomPanel( Enemy e ) {
			enemy = e;
			eHealth = new JLabel( "HP: " + e.getHP() + "/" + e.getMaxHP() );	// Set text for enemy health label
			eQuip = new JLabel( "\"" + e.getQuip() + "\"" );					// Set text for enemy quip label
			
			setLayout( null );
			setBackground( Color.BLACK );
			
			// Enemy deals damage to the hero
			enemyDamage = enemy.combatTurn( heroObj );
			add( enemyDamage );
			fightEnd();

			// Label properties for enemy quip
			eQuip.setBounds( 70, 10, 150, 30 );
			eQuip.setFont( new Font( "Helvetica", Font.PLAIN, 24 ) );
			eQuip.setForeground( Color.WHITE );
			add( eQuip );
			
			// Label properties for hero damage label
			heroCombatLabel.setBounds( 20, 110, 220, 30 );
			heroCombatLabel.setForeground( Color.WHITE );
			heroCombatLabel.setForeground( Color.WHITE );

			// Label properties for enemy health
			eHealth.setBounds( 70, 40, 150, 30);
			eHealth.setFont( new Font( "Helvetica", Font.PLAIN, 24 ) );
			eHealth.setForeground( Color.WHITE );
			add( eHealth );
			
			// If the hero has 0 hp, write that they died.
			if ( heroObj.getHP() == 0 ) {
				fightEnd();
			} else {
				/******** Setup for fightMain panel ********/
				fightMain.setLayout( null );
				fightMain.setBounds( 0, 150, 250, 250 );
				fightMain.setBackground( Color.BLACK );

				fightButton.setBounds( 50, 20, 150, 30 );
				fightButton.addActionListener( this );
				fightMain.add( fightButton );
				
				runButton.setBounds( 50, 60, 150, 30 );
				runButton.addActionListener( this );
				fightMain.add( runButton );
				/*******************************************/
				
				/******** Setup for fightOptions panel ********/
				fightOptions.setLayout( null );
				fightOptions.setBounds( 0, 150, 250, 250 );
				fightOptions.setBackground( Color.BLACK );

				physical.setBounds( 50, 20, 150, 30 );
				physical.addActionListener( this );
				fightOptions.add( physical );
				
				magical.setBounds( 50, 60, 150, 30 );
				magical.addActionListener( this );
				fightOptions.add( magical );
				/*******************************************/

				/******** Setup for magicOptions panel ********/
				magicOptions.setLayout( null );
				magicOptions.setBounds( 0, 150, 250, 250 );
				magicOptions.setBackground( Color.BLACK );

				magicMissile.setBounds( 50, 20, 150, 30 );
				magicMissile.addActionListener( this );
				magicOptions.add( magicMissile );
				
				fireball.setBounds( 50, 60, 150, 30 );
				fireball.addActionListener( this );
				magicOptions.add( fireball );
				
				thunderclap.setBounds( 50, 100, 150, 30 );
				thunderclap.addActionListener( this );
				magicOptions.add( thunderclap );
				/*******************************************/
				
				add( fightMain );
			}
		}
		
		/**
		 * Draws the enemy's image.
		 */
		protected void paintComponent( Graphics g ) {
			super.paintComponent( g );
			
			try {
				BufferedImage img = ImageIO.read( new File( enemy.getImgPath() ) );
				g.drawImage( img, 0, 0, this );
			} catch ( IOException e ) {
				System.out.println( "Could not find image file for enemy." );
			}
		}

		/**
		 * Performs actions depending on what triggered the event.
		 */
		@Override
		public void actionPerformed( ActionEvent e ) {
			// If the fight button triggered the event, change to fightOptions JPanel
			if ( e.getSource() == fightButton ) {
				remove( fightMain );
				
				// If the hero has a potion, let them use it.
				if ( heroObj.hasPotion() ) {
					potion.setBounds( 50, 100, 150, 30 );
					potion.addActionListener( this );
					fightOptions.add( potion );
				} else {
					fightOptions.remove( potion );
				}
				
				add( fightOptions );
				
				remove( enemyDamage );
				remove( usedPotion );
				remove( heroCombatLabel );
				remove( usedPotion );
				
				revalidate();
				repaint();
			}
			// If the run button triggered the event, move in a random direction.
			if ( e.getSource() == runButton ) {
				boolean moved = false;
				Random rng = new Random();
				sideDisplay.updateRoomInfo( "", new BlankPanel() );
				
				do {
					switch( rng.nextInt( 4 ) + 1 ) {
						case 1:
							if ( heroObj.getLocation().getX() != 0 ) {	//If the Hero is not at top of Map, move the Hero north.
								heroObj.goNorth();
								moved = true;						//Set moved to true to break out of loop.
							} else {								//Else, set moved to false to try moving again.
								moved = false;
							}
							break;	 								//Break out of this switch statement.
							
						//If random int was 2:
						case 2:
							if ( heroObj.getLocation().getX() != 4 ) {	//If Hero is not at bottom of Map, move Hero south.
								heroObj.goSouth();
								moved = true;						//Set moved to true to break out of loop.
							} else {								//Else, set moved to false to try moving again.
								moved = false;
							}
							break;									//Break out of this switch statement.
							
						//If random int was 3:
						case 3:
							if ( heroObj.getLocation().getY() != 0 ) {	//If Hero is not at left-most part of Map, move Hero west.
								heroObj.goWest();
								moved = true;						//Set moved to true to break out of loop.
							} else {								//Else, set moved to false and try moving again.
								moved = false;
							}
							break;									//Break out of this switch statement.
							
						//If random int was 4:
						case 4:
							if ( heroObj.getLocation().getY() != 4 ) {	//If Hero is not at right-most part of Map, move Hero east.
								heroObj.goEast();
								moved = true;						//Set moved to true to break out of loop.
							} else {								//Else, set moved to false and try moving again.
								moved = false;
							}
							break;									//Break out of this switch statement.
						}
				} while ( !moved );
				fightState = false;
				roomCheck();
				guiMap.updateMap();
			}
			// If the physical button triggered the event, enemy and hero attack each other.
			if ( e.getSource() == physical ) {
				heroDamage = heroObj.attack( enemy );		// Get damage dealt to the enemy
				heroCombatLabel.setText( "You attack the enemy for " + heroDamage + " damage." );
				enemyDamage = enemy.combatTurn( heroObj );
				
				remove( fightOptions );
				
				eHealth.setText( "HP: " + enemy.getHP() + "/" + enemy.getMaxHP() );
				add( eHealth );
				add( enemyDamage );
				
				sideDisplay.updateHeroInfo();
				
				// If the fight is not over, show the hero's damage and return to the main fight menu.
				// If the fight is over and the hero is still alive, just show their damage.
				if ( !fightEnd() ) {
					add( heroCombatLabel );
					add( fightMain );
				} else {
					if ( heroObj.getHP() > 0 )
						add( heroCombatLabel );
				}
				
				revalidate();
				repaint();
			// If the magical button triggered the event, display the magic menu
			}
			if ( e.getSource() == magical ) {
				remove( fightOptions );
				add( magicOptions );
				
				remove( enemyDamage );
				remove( heroCombatLabel );
				
				revalidate();
				repaint();
			}
			// If one of the spell buttons triggered the event, deal damage with that spell and return to the main fight screen.
			if ( e.getSource() == magicMissile || e.getSource() == fireball || e.getSource() == thunderclap ) {
				int damage;
				
				if ( e.getSource() == magicMissile ) {
					damage = heroObj.magicMissile();
					enemy.takeDamage( damage );
					heroCombatLabel.setText( "<html><center>You attack the enemy with a Magic Missile for " + damage + " damage</center></html>");
				}
				if ( e.getSource() == fireball ) {
					damage = heroObj.fireball();
					enemy.takeDamage( damage );
					heroCombatLabel.setText( "<html><center>You attack the enemy with a Fireball for " + damage + " damage</center></html>");
				}
				if ( e.getSource() == thunderclap ) {
					damage = heroObj.thunderclap();
					enemy.takeDamage( damage );
					heroCombatLabel.setText( "<html><center>You attack the enemy with a Thunderclap for " + damage + " damage</center></html>");
				}
				
				// Enemy deals damage to the hero
				enemyDamage = enemy.combatTurn( heroObj );
				enemyDamage.setForeground( Color.WHITE );
				
				remove( magicOptions );
				
				eHealth.setText( "HP: " + enemy.getHP() + "/" + enemy.getMaxHP() );
				add( eHealth );
				add( enemyDamage );
				
				sideDisplay.updateHeroInfo();

				// If the fight is not over, show the hero's damage and return to the main fight menu.
				// If the fight is over and the hero is still alive, just show their damage.
				if ( !fightEnd() ) {
					add( heroCombatLabel );
					add( fightMain );
				} else {
					if ( heroObj.getHP() > 0 )
						add( heroCombatLabel );
				}
				
				revalidate();
				repaint();
			}
			// If the potion button triggered the event, consume the potion
			if ( e.getSource() == potion ) {
				heroObj.heal( 25 );
				heroObj.removeItem( "Health Potion" );
				sideDisplay.updateHeroInfo();
				
				usedPotion.setBounds( 40, 110, 220, 30 );
				usedPotion.setForeground( Color.WHITE );
				
				// Enemy attacks the hero
				enemyDamage = enemy.combatTurn( heroObj );
				enemyDamage.setForeground( Color.WHITE );
				
				remove( fightOptions );
				
				eHealth.setText( "HP: " + enemy.getHP() + "/" + enemy.getMaxHP() );
				add( eHealth );
				add( enemyDamage );
				
				sideDisplay.updateHeroInfo();
				

				// If the fight is not over, write the enemy's damage and hero's potion drink.
				if ( !fightEnd() ) {
					add( usedPotion );
					add( fightMain );
				}
				
				revalidate();
				repaint();
			}
		}
		
		/**
		 * Checks whether a fight between the hero and an enemy is finished.
		 * 
		 * @return				Whether the fight is finished.
		 */
		private boolean fightEnd() {
			BlankPanel bp = new BlankPanel();
			JLabel fightEndLabel = new JLabel();
			bp.setBounds( 0, 160, 250, 100 );
			fightEndLabel.setBounds( 0, 0, 250, 100 );
			
			// If the hero has 0 hp, write that they died and return true.
			if ( heroObj.getHP() == 0 ) {
				fightEndLabel.setText( "<html><center>Oh dear,<br>you have died.</center></html>" );
				fightEndLabel.setFont( new Font( "Helvetica", Font.PLAIN, 30 ) );
				fightEndLabel.setForeground( Color.RED );
				bp.add( fightEndLabel );
				
				add( bp );
				
				return true;
			// If the enemy has 0 hp, the fight is finished and the hero loots the enemy if possible.
			} else if ( enemy.getHP() == 0 ) {
				Item reward = ItemGenerator.getInstance().generateItem();
				JLabel itemGet = new JLabel();
				
				fightEndLabel.setText( "<html><center>You defeated the enemy!</center></html>" );
				fightEndLabel.setFont( new Font( "Helvetica", Font.PLAIN, 17 ) );
				fightEndLabel.setForeground( Color.WHITE );
				bp.add( fightEndLabel );
				
				if ( heroObj.pickUpItem( reward ) ) {
					itemGet.setText( "<html><center>You receive a " + reward.getName() + "<br>from its corpse.</center></html>" );
					itemGet.setBounds( 0, 50, 250, 100 );
					itemGet.setFont( new Font( "Helvetica", Font.PLAIN, 17 ) );
					itemGet.setForeground( Color.WHITE );
					bp.add( itemGet );
					sideDisplay.updateHeroInfo();
					curMap.removeCharAtLoc( heroObj.getLocation() );
				} else {
					itemGet.setText( "<html><center>The enemy had an item,<br>but your inventory is full.</center></html>" );
					itemGet.setBounds( 0, 50, 250, 100 );
					itemGet.setFont( new Font( "Helvetica", Font.PLAIN, 17 ) );
					itemGet.setForeground( Color.WHITE );
					bp.add( itemGet );
					curMap.setItemRoom( heroObj.getLocation() );
				}
				add( bp );
				fightState = false;
				
				return true;
			} else {
				return false;
			}
		}
	}
}