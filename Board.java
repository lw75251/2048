/*
 * Name:  Leon Wu
 * Login: cs8bfh
 * Date:  April 28, 2015
 * File:  Board.java
 * Sources of Help: - Class Scanner Java Doc
 *                   docs.oracle.com/javase/7/docs/api/java/util/Scanner.html
 *                  - Array Java Doc
 *                   docs.oracle.com/javase/7/docs/api/java/util/Arrays.html
 *                  - String Java Doc
 *                   docs.oracle.com/javase/6/docs/api/java/lang/String.html
 *                  - PrintWriter Java Doc
 *                   docs.oracle.com/javase/7/docs/api/java/io/PrintWriter.html
 *                  - Random Java Doc
 *                   docs.oracle.com/javase/7/docs/api/java/util/Random.html
 *                  - Integer Java Doc
 *                   docs.oracle.com/javase/7/docs/api/java/lang/Integer.html
 *                  - ArrayList Java Doc
 *                   docs.oracle.com/javase/7/docs/api/java/util/ArrayList.html
 *                  - Introduction to Java Programming 10th Edition
 *                    by Y. Daniel Liang (Chapter 12.11, Chapter 8.8)
 */

/**     Sample Board
 *
 *      0   1   2   3
 *  0   -   -   -   -
 *  1   -   -   -   -
 *  2   -   -   -   -
 *  3   -   -   -   -
 *
 *  The sample board shows the index values for the columns and rows
 *  Remember that you access a 2D array by first specifying the row
 *  and then the column: grid[row][column]
 */

// Imports needed classes
import java.util.*;
import java.io.*;

/*
 * Name:    Board
 * Purpose: Creates a board to play the game 2048
 */

public class Board
{
    ///// Instance Variables /////
    
    /** Variables whos values can never be changed once set */
    public final int NUM_START_TILES = 2;
    public final int TWO_PROBABILITY = 90;
    /** Length of one side of Board */
    public final int GRID_SIZE;

    /** Random Generator with specified seed*/
    private final Random random;
    /** Grid of current Board */
    private int[][] grid;
    /** Score of Game */
    private int score;

    ///// Constructors /////

   /* 
    * Name:      Board
    * Purpose:   Constructs a fresh square board with random tiles. 
                 All instance variables are set.The score is set as 0.
                 Also, specified board length and random generator with 
                 specified seed is saved.
    * Parameter: int boardSize - Size that is used to create a square grid
                 Random random - Random number generator with specified seed
    */

    public Board(int boardSize, Random random)
    {
        // Initializes Variables
        this.score = 0;
        this.random = random;
        GRID_SIZE = boardSize; 
        
        //Constructs Board
        grid = new int[GRID_SIZE][GRID_SIZE];
        
        // Adds random tiles to the board 
        for ( int i = 0; i < NUM_START_TILES; i++)
        {
            addRandomTile();
        }
    }

   /* 
    * Name:      Board
    * Purpose:   Constructs an already existing board by reading existing 
    *            board. The score is set as the previously saved score. 
    *            The same random generator with specified seed is saved too.
    * Parameter: String inputBoard - The filename of the saved board
                 Random random - Random number generator with specified seed
    */

    public Board(String inputBoard, Random random) throws IOException
    {
        // Creates a scanner to read file
        Scanner input = new Scanner (new File (inputBoard));

        // Initalizes Variables
        GRID_SIZE = input.nextInt();
        grid = new int[GRID_SIZE][GRID_SIZE];
        score = input.nextInt();
        this.random = random;

        // Reads each Integer in file, and places them in the Board at
        // respective places
        for ( int x = 0; x < GRID_SIZE; x++)
        {
            for ( int y = 0; y < GRID_SIZE; y++)
            {             
                grid[x][y] = input.nextInt();
            }
        }
    }

    ///// Methods ///// 

   /* 
    * Name:      saveBoard
    * Purpose:   Saves the current board to a file    
    * Parameter: String outputBoard - Filename where board will be saved
    */

    public void saveBoard(String outputBoard) throws IOException
    {
        // Creates a file
        try(PrintWriter output = new PrintWriter(outputBoard);)
        {
            // Writes integer values of board length and current score
            // into file
            output.println( GRID_SIZE );
            output.println( score );

            // Writes integer values of each tile of the board into file
            for (int x = 0; x < GRID_SIZE; x++)
            {
                for ( int y = 0; y < GRID_SIZE; y++)
                {
                    output.print(grid[x][y] + " ");
                }
                
                output.println();
            }
        }
    }

   /* 
    * Name:      addRandomTile()
    * Purpose:   Randomly adds a tile (of value 2 or 4) to a random 
                 empty space on the board by using the defined random 
                 generator
    */

    public void addRandomTile()
    {
        // Initializes variables
        int emptycount = 0;

        // Loops through entire grid of board
        for ( int x = 0; x < GRID_SIZE; x++ )
        {
            for ( int y = 0; y < GRID_SIZE; y++)
            {
                // Keeps track of how many 0s are in board.
                if ( grid[x][y] == 0)
                {
                    emptycount++;
                }
            }
        }

        // If there are no empty spaces, then exit method
        if ( emptycount == 0 )
        {
            return;
        }

        // Initializes Variables 
        int totaltiles = GRID_SIZE * GRID_SIZE;
        int randlocation = random.nextInt(emptycount); // Gets random number
                                                       // based on emptycount

        randlocation = randlocation%totaltiles; // Reduces to a number between
                                                // 0 and total number of tiles

        int randvalue = random.nextInt(100); // Gets a random number between
                                             // 0 and 99
        int finalnum = 0;

        // Determines whether inputed value should be 2 or 4
        if ( randvalue < TWO_PROBABILITY )
        {
            finalnum = 2;
        }
        else
        {
            finalnum = 4; 
        }

        // Loops through entire grid of board
        for ( int x = 0; x < GRID_SIZE; x++ )
        {
            for ( int y = 0; y < GRID_SIZE; y++)
            {
                // Decrements randlocation when finds an empty tile
                if ( grid[x][y] == 0 )
                {
                    // Places 2 or 4 when randlocation is 0
                    if ( randlocation == 0 )
                    {
                        grid[x][y] = finalnum;
                        return;
                    }

                    randlocation--;
                }
            }
        }
    }

   /* 
    * Name:      isGameOver
    * Purpose:   Tests to see whether there are any more valid moves, and
                 prints GameOver if there are no more moves
    * Return:    boolean - True or False
    */

    public boolean isGameOver()
    {
        // Loops through entire grid of board
        for( int x = 0; x < GRID_SIZE; x++)
        {
            for( int y = 0; y < GRID_SIZE; y++)
            {
                // Checks if you can move in all possible directions
                if( canMoveUp() || canMoveDown() ||
                    canMoveLeft() || canMoveRight() )
                {
                    return false; // Still possible moves
                }
            }
        }
        
        return true; // No more possible moves
    }

   /* 
    * Name:      canMove
    * Purpose:   Tests to see whether user can move in a certain direction
    * Parameter: Direction direction - Direction user wants to move in
    * Return:    boolean - True or False
    */

    public boolean canMove(Direction direction)
    {
        if ( direction.equals( Direction.UP ) )
        {
            return canMoveUp(); // Possible to move UP
        }

        else if ( direction.equals( Direction.DOWN ) )
        {
            return canMoveDown(); // Possible to move DOWN
        }

        else if ( direction.equals( Direction.LEFT ) )
        {
            return canMoveLeft(); // Possible to move LEFT
        }

        else if ( direction.equals( Direction.RIGHT ) )
        {
            return canMoveRight(); // Possible to move RIGHT
        }

        else
        {
            return false; // No possible moves
        }
    }

   /* 
    * Name:      canMoveUp
    * Purpose:   Tests to see whether there are any more valid moves
                 in the upwards direction
    * Return:    boolean - True or False
    */

    private boolean canMoveUp()
    {
        // Loops through entire grid except for top row
        for ( int x = 1; x < GRID_SIZE; x++)
        {
            for ( int y = 0; y < GRID_SIZE; y++)
            {
                // Initializes Variables
                int test = grid[x][y];      // Tile to test
                int compare = grid[x-1][y]; // Tile above test
                int copyX = x;              // Copy of x-coordinate
                                            // index value

                // Checks to see whether tile above is equal to test
                if ( test == compare && test != 0)
                {
                    return true; // Possible move exists
                }

                // Checks to see if there are any empty spaces above tile
                if ( test != 0)
                {
                    while ( copyX > 0 )
                    {
                        copyX--;
                        int testempty = grid[copyX][y]; // Tile(s) above test
                        if ( testempty == 0 )
                        {
                            return true; // Possible move exists
                        }
                    }
                }
            }
        }

        return false; // No possible move
    }

   /* 
    * Name:      canMoveDown
    * Purpose:   Tests to see whether there are any more valid moves
                 in the downward direction
    * Return:    boolean - True or False
    */

    private boolean canMoveDown()
    {
        // Loops through entire grid except for bottom row
        for ( int x = 0; x < GRID_SIZE - 1; x++)
        {
            for ( int y = 0; y < GRID_SIZE; y++)
            {
                // Initializes Variables
                int test = grid[x][y];       // Tile to test
                int compare = grid[x+1][y];  // Tile below test
                int copyX = x;               // Copy of x-coordinate
                                             // index value

                // Checks to see whether tile below is equal to test
                if ( test == compare && test!= 0 )
                {
                    return true; // Possible move exists
                }

                // Checks to see if there are any empty spaces below tile
                if ( test != 0)
                {
                    while ( copyX < GRID_SIZE -1 )
                    {
                        copyX++;
                        int testempty = grid[copyX][y]; // Tile(s) above test
                        if ( testempty == 0 )
                        {
                            return true; // Possible move exists
                        }
                    }
                }
            }
        }

        return false; // No possible move
    }

   /* 
    * Name:      canMoveLeft
    * Purpose:   Tests to see whether there are any more valid moves
                 in the leftward direction
    * Return:    boolean - True or False
    */

    private boolean canMoveLeft()
    {
        // Loops through entire grid except for left-most column
        for ( int x = 0; x < GRID_SIZE; x++)
        {
            for ( int y = 1; y < GRID_SIZE; y++)
            {
                // Initializes Variables
                int test = grid[x][y];       // Tile to test
                int compare = grid[x][y-1];  // Tile left of test
                int copyY = y;               // Copy of y-coordinate
                                             // index value

                // Checks to see whether tile left of test is equal
                if ( test == compare && test != 0 )
                {
                    return true; // Possible move exists
                }

                // Checks to see if there are any empty spaces left of tile
                if (test != 0)
                {
                    while ( copyY > 0)
                    {
                        copyY--;
                        int testempty = grid[x][copyY];
                        if ( testempty == 0 )
                        {
                            return true; // Possible move exists
                        }
                    }
                }
            }
        }

        return false; // No possible move
    }

   /* 
    * Name:      canMoveRight
    * Purpose:   Tests to see whether there are any more valid moves
                 in the rightward direction
    * Return:    boolean - True or False
    */

    private boolean canMoveRight()
    {
        // Loops through entire grid except for right-most column
        for ( int x = 0; x < GRID_SIZE; x++)
        {
            for ( int y = 0; y < GRID_SIZE - 1; y++)
            {
                // Initializes Variables
                int test = grid[x][y];       // Tile to test
                int compare = grid[x][y+1];  // Tile right of test
                int copyY = y;               // Copy of y-coordinate
                                             // index value

                // Checks to see whether tile right of test is equal
                if ( test == compare && test != 0)
                {
                    return true; // Possible move exists
                }

                // Checks to see if there are any empty spaces right of tile
                if ( test != 0)
                {
                    while ( copyY < GRID_SIZE-1 )
                    {
                        copyY++;
                        int testempty = grid[x][copyY];
                        if ( testempty == 0 )
                        {
                            return true; // Possible move exists
                        }
                    }
                }
            }   
        }

        return false; // No possible move
    }

   /* 
    * Name:      canMove
    * Purpose:   Moves tiles in given direction
    * Parameter: Direction direction - Direction user wants to move in
    * Return:    boolean - True or False
    */

    // Perform a move Operation
    public boolean move(Direction direction)
    {
        if ( direction.equals( Direction.UP ) )
        {
            return moveUp(); // Moves UP
        }
        
        else if ( direction.equals( Direction.DOWN ) )
        {
            return moveDown(); // Moves DOWN
        }
        
        else if ( direction.equals( Direction.LEFT ) )
        {
            return moveLeft(); // Moves LEFT
        }

        else if ( direction.equals( Direction. RIGHT ) )
        {
            return moveRight(); // Moves RIGHT
        }

        else
        {
            return false; // Could not move
        }
    }

   /* 
    * Name:      moveUp
    * Purpose:   Moves tiles in the upward direction until it hits a wall or
                 encounters another number with a different value. If the
                 numbers have the same value, they are added together and
                 a zero pops up at the bottom of that same column.
    * Return:    boolean - True or False
    */

    private boolean moveUp()
    {
        // Loops through each column of grid
        for (int y = 0; y < GRID_SIZE; y++)
        {
            // Initializes Variables
            ArrayList<Integer> copycolumn = new ArrayList<Integer>();
            int removecount = 0;

            // Copies all elements in column to Arraylist from top to bottom
            for ( int x = 0 ; x < GRID_SIZE ; x++)
            {
                Integer addInt = new Integer(grid[x][y]);
                copycolumn.add(addInt);
            }
            // Looks for any 0s in the column
            for ( int i = 0; i < copycolumn.size(); i++)
            {
                // Initializes Variables
                Integer checkZero = copycolumn.get(i);
                int zero = checkZero.intValue();

                // Remove All Integers with values of 0 in the column
                if ( zero == 0)
                {
                    copycolumn.remove(i);
                    removecount++;
                    i--; // Necessary to check all elements after 
                         // an element is removed
                }
            }
            // Looks for any adjacent tiles with same values
            for ( int i = 0; i < copycolumn.size()-1; i++)
            {
                // Initializes Variables
                Integer test = copycolumn.get(i);
                Integer compare = copycolumn.get(i+1);
                int testValue = test.intValue();
                int compareValue = compare.intValue();

                // If Values are equal, adds and removes one of the tiles
                if (testValue == compareValue && testValue != 0)
                {
                    // Initializes Variables
                    int combined = testValue + compareValue;
                    Integer combinedInt = new Integer(combined);

                    // Sets first tile as combined values and removes other
                    copycolumn.set(i,combinedInt);
                    copycolumn.remove(i+1);
                    removecount++;
                    this.score += combined; // Adds combined value to score
                }
            }

            // Adds back 0s equal to the number of tiles removed
            for ( int i = 0; i < removecount; i++)
            {
                Integer zero = new Integer(0);
                copycolumn.add(zero);
            }

            // Copies Arraylist column back into array
            for ( int i = 0; i < copycolumn.size(); i++)
            {
                Integer copy = copycolumn.get(i);
                int copyValue = copy.intValue();
                grid[i][y] = copyValue;
            }
        }

    return true; // Move Succesful
    }

   /* 
    * Name:      moveDown
    * Purpose:   Moves tiles in the downward direction until it hits a wall or
                 encounters another number with a different value. If the
                 numbers have the same value, they are added together and
                 a zero pops up at the bottom of that same column.
    * Return:    boolean - True or False
    */

    private boolean moveDown()
    {
        // Loops through each column of grid
        for (int y = 0; y < GRID_SIZE; y++)
        {
            // Initializes Variables
            ArrayList<Integer> copycolumn = new ArrayList<Integer>();
            int removecount = 0;

            // Copies all elements in column to Arraylist from bottom to top
            for ( int x = GRID_SIZE-1 ; x >= 0 ; x--)
            {
                Integer addInt = new Integer(grid[x][y]);
                copycolumn.add(addInt);
            }

            // Remove All Integers with values of 0
            for ( int i = 0; i < copycolumn.size(); i++)
            {
                // Initializes Variables
                Integer checkZero = copycolumn.get(i);
                int zero = checkZero.intValue();

                // Remove All Integers with values of 0 in the column
                if ( zero == 0)
                {
                    copycolumn.remove(i);
                    removecount++;
                    i--; // Necessary to check all elements after 
                         // an element is removed
                }
            }

            // Looks for any adjacent tiles with same values
            for ( int i = 0; i < copycolumn.size()-1; i++)
            {
                // Initializes Variables
                Integer test = copycolumn.get(i);
                Integer compare = copycolumn.get(i+1);
                int testValue = test.intValue();
                int compareValue = compare.intValue();

                // If Values are equal, adds and removes one of the tiles
                if (testValue == compareValue && testValue != 0)
                {
                    // Initializes Variables
                    int combined = testValue + compareValue;
                    Integer combinedInt = new Integer(combined);

                    // Sets first tile as combined values and removes other
                    copycolumn.set(i,combinedInt);
                    copycolumn.remove(i+1);
                    removecount++;
                    this.score += combined; // Adds combined value to score
                }
            }

            // Adds back 0s equal to the number of tiles removed
            for ( int i = 0; i < removecount; i++)
            {
                Integer zero = new Integer(0);
                copycolumn.add(zero);
            }

            // Copies Arraylist column back into array
            for ( int i = 0; i < copycolumn.size(); i++)
            {
                Integer copy = copycolumn.get(i);
                int copyValue = copy.intValue();
                grid[GRID_SIZE-1-i][y] = copyValue;
            }
        }

    return true; // Move Succesful
    }

   /* 
    * Name:      moveLeft
    * Purpose:   Moves tiles in the leftward direction until it hits a wall or
                 encounters another number with a different value. If the
                 numbers have the same value, they are added together and
                 a zero pops up at the right of that same row.
    * Return:    boolean - True or False
    */

    private boolean moveLeft()
    {
        // Loops through each row of grid
        for (int x = 0; x < GRID_SIZE; x++)
        {
            // Initializes Variables
            ArrayList<Integer> copyrow = new ArrayList<Integer>();
            int removecount = 0;

            // Copies all elements in row to Arraylist from left to right
            for ( int y = 0 ; y < GRID_SIZE ; y++)
            {
                Integer addInt = new Integer(grid[x][y]);
                copyrow.add(addInt);
            }

            // Remove All Integers with values of 0
            for ( int i = 0; i < copyrow.size(); i++)
            {
                // Initializes Variables
                Integer checkZero = copyrow.get(i);
                int zero = checkZero.intValue();

                // Remove All Integers with values of 0 in the row
                if ( zero == 0)
                {
                    copyrow.remove(i);
                    removecount++;
                    i--;
                }
            }

            // Looks for any adjacent tiles with same values
            for ( int i = 0; i < copyrow.size()-1; i++)
            {
                // Initializes Variables
                Integer test = copyrow.get(i);
                Integer compare = copyrow.get(i+1);
                int testValue = test.intValue();
                int compareValue = compare.intValue();

                // If Values are equal, adds and removes one of the tiles
                if (testValue == compareValue && testValue != 0)
                {
                    // Initializes Variables
                    int combined = testValue + compareValue;
                    Integer combinedInt = new Integer(combined);

                    // Sets first tile as combined values and removes other
                    copyrow.set(i,combinedInt);
                    copyrow.remove(i+1);
                    removecount++;
                    this.score += combined; // Adds combined value to score
                }
            }

            // Adds back 0s equal to the number of tiles removed
            for ( int i = 0; i < removecount; i++)
            {
                Integer zero = new Integer(0);
                copyrow.add(zero);
            }

            // Copies Arraylist row back into array
            for ( int i = 0; i < copyrow.size(); i++)
            {
                Integer copy = copyrow.get(i);
                int copyValue = copy.intValue();
                grid[x][i] = copyValue;
            }
        }

    return true; // Move Succesful
    }

   /* 
    * Name:      moveRight
    * Purpose:   Moves tiles in the rightward direction until it hits a wall or
                 encounters another number with a different value. If the
                 numbers have the same value, they are added together and
                 a zero pops up at the left of that same row.
    * Return:    boolean - True or False
    */

    private boolean moveRight()
    {
        // Loops through each row of grid
        for (int x = 0; x < GRID_SIZE; x++)
        {
            // Initializes Variables
            ArrayList<Integer> copyrow = new ArrayList<Integer>();
            int removecount = 0;

            // Copies all elements in column to Arraylist from right to left
            for ( int y = GRID_SIZE-1 ; y >= 0 ; y--)
            {
                Integer addInt = new Integer(grid[x][y]);
                copyrow.add(addInt);
            }

            // Remove All Integers with values of 0
            for ( int i = 0; i < copyrow.size(); i++)
            {
                // Initializes Variables
                Integer checkZero = copyrow.get(i);
                int zero = checkZero.intValue();

                // Remove All Integers with values of 0 in the row
                if ( zero == 0)
                {
                    copyrow.remove(i);
                    removecount++;
                    i--;
                }
            }

            // Looks for any adjacent tiles with same values
            for ( int i = 0; i < copyrow.size()-1; i++)
            {
                // Initializes Variables
                Integer test = copyrow.get(i);
                Integer compare = copyrow.get(i+1);
                int testValue = test.intValue();
                int compareValue = compare.intValue();
                
                // If Values are equal, adds and removes one of the tiles
                if (testValue == compareValue && testValue != 0)
                {
                    // Initializes Variables
                    int combined = testValue + compareValue;
                    Integer combinedInt = new Integer(combined);

                    // Sets first tile as combined values and removes other
                    copyrow.set(i,combinedInt);
                    copyrow.remove(i+1);
                    removecount++;
                    this.score += combined; // Adds combined value to score
                }
            }

            // Adds back 0s equal to the number of tiles removed
            for ( int i = 0; i < removecount; i++)
            {
                Integer zero = new Integer(0);
                copyrow.add(zero);
            }
            // Copies Arraylist row back into array
            for ( int i = 0; i < copyrow.size(); i++)
            {
                Integer copy = copyrow.get(i);
                int copyValue = copy.intValue();
                grid[x][GRID_SIZE-1-i] = copyValue;
            }
        }

    return true; // Move Successful
    }


   /* 
    * Name:      getGrid()
    * Purpose:   Return the reference to the 2048 Grid
    * Return:    int[][] grid - Grid that board uses for 2048
    */

    public int[][] getGrid()
    {
        return grid;
    }

   /* 
    * Name:      getScore()
    * Purpose:   Return the score of 2048 game
    * Return:    int score - Score of game
    */

    public int getScore()
    {
        return score;
    }

   /* 
    * Name:      toString()
    * Purpose:   Prints out the current board
    */

    @Override
    public String toString()
    {
        StringBuilder outputString = new StringBuilder();
        outputString.append(String.format("Score: %d\n", score));
        for (int row = 0; row < GRID_SIZE; row++)
        {
            for (int column = 0; column < GRID_SIZE; column++)
                outputString.append(grid[row][column] == 0 ? "    -" :
                                    String.format("%5d", grid[row][column]));

            outputString.append("\n");
        }
        return outputString.toString();
    }
}
