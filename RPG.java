import java.awt.*;
import java.io.*; // allows file access

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.event.*;  // Needed for ActionListener

class RPG extends JFrame implements KeyListener
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	static Map map = new Map (20, 25, 30, 30);
    JTextPane info = new JTextPane();
    JTextPane title = new JTextPane();
    JTextPane goldcount = new JTextPane();

    //======================================================== constructor
    public RPG () throws FileNotFoundException
    {

	// 1... Enable key listener for movement
	addKeyListener (this);
	setFocusable (true);
	setFocusTraversalKeysEnabled (false);

	// 2... Create content pane, set layout
	JPanel content = new JPanel ();
	info.setBackground(Color.BLACK);
	info.setForeground(Color.ORANGE);
	
	//textpanes on top
	info.setFont(new Font("Calibri", Font.PLAIN, 20));
	info.setEditable(false);
	info.setText("Level 1 - GOOD LUCK!");
	title.setForeground(Color.RED);
	
	title.setFont(new Font("Calibri", Font.PLAIN, 26));
	title.setEditable(false);
	title.setText("LINK'S WORLD");
	goldcount.setBackground(Color.BLACK);
	goldcount.setForeground(Color.ORANGE);
	
	goldcount.setFont(new Font("Calibri", Font.PLAIN, 20));
	goldcount.setEditable(false);
	goldcount.setText ("Gold: " + map.gold);
	
	content.add(info, "North");
	content.add(title, "North");
	content.add(goldcount, "North");

	DrawArea board = new DrawArea (770, 620); // Area to be displayed
	

	content.add (board, "Center"); // Deck display area

	// 4... Set this window's attributes.
	setContentPane (content);
	
	pack ();
	setTitle ("RPG Demo");
	setSize (770, 693);
	setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
	setLocationRelativeTo (null);           // Center window.

	map.load("map1.txt");
	
	board.addKeyListener (this);
    }


    public void keyTyped (KeyEvent e)
    {
	// nothing
    }


    /** Handle the key-pressed event from the text field. */
    public void keyPressed (KeyEvent e)
    {
	int key = e.getKeyCode ();
	switch (key)
	{
	//movement and defuse keys
	    case KeyEvent.VK_UP:
		map.move (0, -1);
		break;
	    case KeyEvent.VK_DOWN:
		map.move (0, 1);
		break;
	    case KeyEvent.VK_LEFT:
		map.move (-1, 0);
		break;
	    case KeyEvent.VK_RIGHT:
		map.move (1, 0);
		break;
	    case KeyEvent.VK_SPACE:
		map.defuse ();
		break;
	}
	info.setText (map.status);
	goldcount.setText ("Gold: " + map.gold);
	repaint ();
    }


    /** Handle the key-released event from the text field. */
    public void keyReleased (KeyEvent e)
    {
	// nothing
    }



    class DrawArea extends JPanel
    {
	/**
		 * 
		 */
		private static final long serialVersionUID = 1L;


	public DrawArea (int width, int height)
	{
	    this.setPreferredSize (new Dimension (width, height)); // size
	}


	public void paintComponent (Graphics g)
	{
	    map.print (g);
	}
    }


    //======================================================== method main
    public static void main (String[] args) throws FileNotFoundException
    {
	RPG window = new RPG ();
	window.setVisible (true);
    }
}


// -------------------------------------- Card Class ------------------------------------------------------

class Map
{
    private char map[] [];
    private int width, height, posx, posy, evenodd, level;
    protected int gold;
    protected String status;
    private Image link, treasure, tree, water, coin, grass, mine; //images

    public Map (int rows, int cols, int blockwidth, int blockheight)
    {
	width = blockwidth;
	height = blockheight;
	posx = 1;
	posy = 1;
	gold = 0;
	level = 1;
	status = "Level 1 - GOOD LUCK!";
	evenodd = 0; //counter to help with switching images
	
	try {
		link = ImageIO.read (new File ("pics\\LinkNorm.png"));
	} catch (IOException e) {
		System.out.print("File not found");
	}
	
	try {
		treasure = ImageIO.read (new File ("pics\\Treasure.png"));
	} catch (IOException e) {
		System.out.print("File not found");
	}
	
	try {
		tree = ImageIO.read(new File("pics\\Tree.png"));
	} catch (IOException e) {
		System.out.print("File not found");
	}
	
	try {
		water = ImageIO.read(new File("pics\\Water.png"));
	} catch (IOException e) {
		System.out.print("File not found");
	}
	
	try {
		coin = ImageIO.read(new File("pics\\Coin.png"));
	} catch (IOException e) {
		System.out.print("File not found");
	}
	
	try {
		grass = ImageIO.read(new File("pics\\Grass.png"));
	} catch (IOException e) {
		System.out.print("File not found");
	}
	
	try {
		mine = ImageIO.read(new File("pics\\Mine.png"));
	} catch (IOException e) {
		System.out.print("File not found");
	}
	
	map = new char [rows] [cols]; // define 2-d array size

	for (int row = 0 ; row < rows ; row++)
	    for (int col = 0 ; col < cols ; col++)
	    {
		if (row == 0 || row == rows - 1 || col == 0 || col == cols - 1) // border
		    map [row] [col] = 'W'; // put up a wall
		else
		    map [row] [col] = ' '; // blank space
	    }
    }


    public void print (Graphics g)    // displays the map on the screen
    {
	for (int row = 0 ; row < map.length ; row++) // number of rows
	{
	    for (int col = 0 ; col < map [0].length ; col++) // length of first row
	    {
	    	if (map [row] [col] == 'W') { // wall
                g.setColor (Color.black);
	    		g.fillRect (col * width + 10, row * height + 10, width, height); // draw block
	    	}
            else if (map [row] [col] == 'D') { //treasure chest
            	g.drawImage (treasure, col * width + 10, row * height + 10, null);
            }
            else if (map [row] [col] == 'G') { // gold
            	g.drawImage (coin, col * width + 10, row * height + 10, null);
            }
            else if (map [row][col] == 'T') { //tree
            	g.drawImage (tree, col * width + 10, row * height + 10, null);
            }
            else if (map [row][col] == 'M') { //mine
            	g.drawImage (mine, col * width + 10, row * height + 10, null);
            }
            else if (map [row][col] == 'B') { //water
            	g.drawImage (water, col * width + 10, row * height + 10, null);
            }
            else if (map [row][col] == 'C') { //link's character
            	g.drawImage (link, posx * width + 10, posy * height + 10, null);
            }
            else {
            	g.drawImage (grass, col * width + 10, row * height + 10, null);
            }
	    }
	}

    }

    public void move (int dx, int dy) //movement for link
    {
    	if (map [posy + dy] [posx + dx] != 'W' && map [posy + dy] [posx + dx] != 'T' && map [posy + dy] [posx + dx] != 'B') //if blocked, don't move
    	{
    		map[posy][posx] = ' '; //leave space behind in previous position
    		//update position
    	    posx += dx;
    	    posy += dy;
        	
    	    if (map[posy][posx] == 'G') //if gold found, add it
        	{
        		gold++;
        	}
    	    
    	    else if (map[posy][posx] == 'M') //if mine hit, reset level and gold count, then exit loop
    	    {
    	    	status = "YOU HIT A MINE AND LOST YOUR GOLD! TRY AGAIN!";
    	    	reset();
    	    	return;
    	    }
        	
    	    else if (map[posy][posx] == 'D') //if chest reached, go to next level
        	{
        		nextLevel();
        	}
    	    
    	    map[posy][posx] = 'C'; //add link to new position
    	}
	    //switch leg while link is moving
	    if (dx == -1 && dy == 0)
	    {
	    	try {
	    		link = ImageIO.read (new File ("pics\\LeftLink" + (evenodd % 2 + 1) + ".png"));
	    		evenodd++;
	    	}
	    	catch (IOException e) {
	    		System.out.println("File not found");
	    	}
	    }
	    else if (dx == 1 && dy == 0)
	    {
	    	try {
	    		link = ImageIO.read (new File ("pics\\RightLink" + (evenodd % 2 + 1) + ".png"));
	    		evenodd++;
	    	}
	    	catch (IOException e) {
	    		System.out.println("File not found");
	    	}
	    }
	    else if (dx == 0 && dy == -1)
	    {
	    	try {
	    		link = ImageIO.read (new File ("pics\\BackLink" + (evenodd % 2 + 1) + ".png"));
	    		evenodd++;
	    	}
	    	catch (IOException e) {
	    		System.out.println("File not found");
	    	}
	    }
	    else if (dx == 0 && dy == 1)
	    {
	    	try {
	    		link = ImageIO.read (new File ("pics\\LinkNorm" + (evenodd % 2 + 1) + ".png"));
	    		evenodd++;
	    	}
	    	catch (IOException e) {
	    		System.out.println("File not found");
	    	}
	    }
    }

    public void defuse () //defuse mines near link
    {
	for (int x = -1 ; x <= 1 ; x++)
	{
	    for (int y = -1 ; y <= 1 ; y++)
	    {
	    	try {
	    	if (map[posy + y][posx + x] == 'M')
	    		map [posy + y] [posx + x] = ' ';  // if bomb found within 1 block, remove it and replace with empty space
	    	} catch (ArrayIndexOutOfBoundsException e) {
	    		
	    	}
	    }
	}
	map [posy] [posx] = 'C';
    }
    
    public void nextLevel () //go to next level
    {	
    	if (level < 3)
    	{
    		//reset postion
    		posx = 1;
        	posy = 1;
        	level++; //update level counter
    		try {
				load ("map" + level + ".txt");
			} catch (FileNotFoundException e) {
				System.out.println ("File not Found");
			}
    		
    		if (level == 2) //2nd level has different start position
    		{
    			posx = 23;
    			posy = 1;
    		}
    		status = "Level " + level + " - GOOD LUCK!"; //update status
    	}
    	
    	else if (level >= 3)
    	{
    		status = "CONGRATULATIONS, YOU BEAT THE GAME!"; //if final level passed, display congrats message
    		
    		for (int x = 0; x < map.length; x++)
    			for (int y = 0; y < map[0].length; y++)
    				if (map[x][y] == 'M')
    					map[x][y] = ' '; //defuse all remaining mines automatically
    	}
    }
    
    public void reset () //reset level
    {
    	//reset position
    	posx = 1;
    	posy = 1;
    	
    	if (level == 2) //2nd level has different starting postion
    	{
    		posx = 23;
    		posy = 1;
    	}
    	
    	try {
    		link = ImageIO.read (new File ("pics\\LinkNorm.png")); //reset link
    	} catch (IOException e) {
    		System.out.print("File not found");
    	}
    	
    	try {
			load("map" + level + ".txt"); //reload level
		} catch (FileNotFoundException e) {
			System.out.print ("File not found");
		}
    	
    	gold = 0; //reset gold count
    }
    
    public void load (String file) throws FileNotFoundException //loads map from text file
    {
    	String line, total = "";

		try {
			FileReader fr = new FileReader (file);
			BufferedReader filein = new BufferedReader (fr);
			try {
				while ((line = filein.readLine ()) != null) // file has not ended
					total = total + line; // add every line to total
				filein.close (); // close file
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			System.out.println("File not found"); //display error message if no file found
		}
        
        for (int x = 0; x < map.length; x++)
        {
        	for (int y = 0; y < map[0].length; y++)
        	{
        		try {
        		map[x][y] = total.charAt(x*25 + y); //update map
        		} catch (StringIndexOutOfBoundsException z) {
        		}
        	}
        }
    }
}

