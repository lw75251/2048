/*
 * Name:  Leon Wu
 * Login: cs8bfh
 * Date:  May 28, 2015
 * File:  Gui2048.java
 * Sources of Help: 
 - docs.oracle.com/javafx/2/text/jfxpub-text.htm
 - docs.oracle.com/javase/8/javafx/api/javafx/scene/layout/GridPane.html
 - docs.oracle.com/javase/8/javafx/api/javafx/scene/layout/StackPane.html
 - docs.oracle.com/javafx/2/api/javafx/scene/paint/Color.html
 - docs.oracle.com/javase/8/javafx/api/javafx/scene/input/KeyEvent.html
 - Introduction to Java Programming 10th Edition
          by Y. Daniel Liang 
 */

/** Gui2048.java */
/** PA8 Release */

// Imports Packages
import javafx.application.*;
import javafx.scene.control.*;
import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.event.*;
import javafx.scene.input.*;
import javafx.scene.text.*;
import javafx.geometry.*;
import java.util.*;
import java.io.*;

/*
 * Name:    Gui2048
 * Purpose: Creates the graphical user interface for the Game 2048
 */

public class Gui2048 extends Application
{
    private String outputBoard; // The filename for where to save the Board
    private Board board; // The 2048 Game Board

    // Fill colors for each of the Tile values
    private static final Color COLOR_EMPTY = Color.rgb(238, 228, 218, 0.35);
    private static final Color COLOR_2 = Color.rgb(168, 185, 255);
    private static final Color COLOR_4 = Color.rgb(191, 168, 255);
    private static final Color COLOR_8 = Color.rgb(168, 232, 255);
    private static final Color COLOR_16 = Color.rgb(168, 185, 255);
    private static final Color COLOR_32 = Color.rgb(191, 168, 255);
    private static final Color COLOR_64 = Color.rgb(168, 232, 255);
    private static final Color COLOR_128 = Color.rgb(177, 230, 134);
    private static final Color COLOR_256 = Color.rgb(184, 98, 255);
    private static final Color COLOR_512 = Color.rgb(187, 134, 230);
    private static final Color COLOR_1024 = Color.rgb(255, 184, 98);
    private static final Color COLOR_2048 = Color.rgb(161, 152, 255);
    private static final Color COLOR_OTHER = Color.BLACK;
    private static final Color COLOR_GAME_OVER = Color.rgb(238, 228, 218, 0.73);

    private static final Color COLOR_VALUE_LIGHT = Color.BLACK; // For tiles >= 8
    private static final Color COLOR_VALUE_DARK = Color.BLACK; // For tiles < 8

    /** Background Color*/
    private static final Color COLOR_BKGROUND = Color.rgb(143, 201, 255);

    /** Add your own Instance Variables here */
    // Number Constants
    private static final int TWO = 2;
    private static final int FOUR = 4;
    private static final int EIGHT = 8;
    private static final int ONE_SIX = 16;
    private static final int THREE_TWO = 32;
    private static final int SIX_FOUR = 64;
    private static final int ONE_TWO_EIGHT = 128;
    private static final int TWO_FIVE_SIX = 256;
    private static final int FIVE_ONE_TWO = 512;
    private static final int ONE_O_TWO_FOUR= 1024;
    private static final int TWO_O_FOUR_EIGHT = 2048;
    private static final int SIDE = 100;
    private static final int OTHER_WIDTH = 205;
    private static final int OTHER_HEIGHT = 75;
    private static final double GAP_VALUE = 5.5;
    private static final double PAD_1 = 11.5;
    private static final double PAD_2 = 12.5;
    private static final double PAD_3 = 13.5;
    private static final double PAD_4 = 14.5;
    private static final int FONT_SIZE = 30;

    // Board Instance Variables
    private int[][] grid;
    private Direction direction;
    private int GRID_SIZE;
    
    // Stage Instance Variables
    private Stage primaryStage;
    private GridPane pane;
    private Rectangle[][] copyRect;
    private Text[][] copyText;
    private Text scoreText;
    private Color valueColor;
    private Color textColor;
    
   /* 
    * Name:      start
    * Purpose:   Overrides the default start method. Creates the GUI for 
                 the Game 2048 Board. It creates a board, with the correct
                 colors and text.
      Parameter: Stage primaryStage - Window to create 2048
    */
    
    @Override
    public void start(Stage primaryStage)
    {
        // Process Arguments and Initialize the Game Board
        processArgs(getParameters().getRaw().toArray(new String[0]));

        /** Add your Code for the GUI Here */
        // Initializes Variables
        this.primaryStage = primaryStage;
        grid = board.getGrid();
        GRID_SIZE = board.GRID_SIZE;

        // Keeps track of all the created Rectangles and Texts
        copyRect = new Rectangle[GRID_SIZE][GRID_SIZE];
        copyText = new Text[GRID_SIZE][GRID_SIZE];

        // Creates a Pane with correct formatting
        pane = new GridPane();
        pane.setAlignment(Pos.CENTER);
        pane.setPadding (new Insets(PAD_1,PAD_2,PAD_3,PAD_4));
        pane.setHgap(GAP_VALUE);
        pane.setVgap(GAP_VALUE);

        // Sets Color of Background
        pane.setStyle("-fx-background-color: rgb(83,159,230)");
        // Adds Pane to Scene
        Scene scene = new Scene(pane);
        primaryStage.setTitle("Gui2048");
        primaryStage.setScene(scene);
        primaryStage.show();
        scene.setOnKeyPressed ( new myKeyHandler() );

        // Title Rectangle
        Rectangle titleBox = new Rectangle();
        titleBox.setWidth(OTHER_WIDTH);
        titleBox.setHeight(OTHER_HEIGHT);
        titleBox.setFill(COLOR_BKGROUND); // Color of Title Fill

        // Title Text
        Text titleText = new Text();
        titleText.setText("2048");
        titleText.setFont(Font.font("Forte",
                          FontPosture.ITALIC,FONT_SIZE));
        titleText.setFill(Color.BLACK); // Color of 2048 Text

        // Score Rectangle
        Rectangle scoreBox = new Rectangle();
        scoreBox.setWidth(OTHER_WIDTH);
        scoreBox.setHeight(OTHER_HEIGHT);
        scoreBox.setFill(COLOR_BKGROUND); // Color of Score Fill

        //Score Text
        scoreText = new Text();
        scoreText.setText("Score: " + board.getScore() );
        scoreText.setFill(Color.BLACK); // Color of Score Text
        scoreText.setFont(Font.font("Forte", 
                          FontWeight.NORMAL, FONT_SIZE));
        
        
        // Adds Rectangle and text to Grid Pane
        pane.add(titleBox, 0, 0, TWO, 1);
        pane.add(titleText, 0, 0, TWO, 1);
        pane.add(scoreBox, TWO, 0, TWO, 1);
        pane.add(scoreText, TWO, 0, TWO, 1);

        // Alligns to Middle of Grid
        GridPane.setHalignment(titleText, HPos.CENTER);
        GridPane.setValignment(titleText, VPos.CENTER);
        GridPane.setHalignment(scoreText, HPos.CENTER);
        GridPane.setValignment(scoreText, VPos.CENTER);

        // Creates the Board
        for ( int j = 0; j < GRID_SIZE; j++)
        {
            for ( int i = 0; i < GRID_SIZE; i++)
            {
                // Creates Tiles for each Number
                Rectangle valueBox = new Rectangle();
                valueBox.setWidth(SIDE);
                valueBox.setHeight(SIDE);
                
                // Prints Numbers on each Tile
                Text valueText = new Text();
                Integer value = new Integer( grid[i][j] );
                
                // Does not print if number is 0
                if ( value.intValue() != 0 )
                {
                    valueText.setText( value.toString() );
                }
                // Sets Font
                valueText.setFont(Font.font("Forte",FontWeight.NORMAL
                                            , FONT_SIZE));
                // Sets Tile Colors and Text Colors
                setValue ( value.intValue() );
                valueBox.setFill(valueColor);
                scoreText.setFill(textColor);
                // Keeps track of Rectangles and Texts Created
                copyRect[i][j] = valueBox;
                copyText[i][j] = valueText;
                // Adds into Pane
                pane.add(valueBox, j, i+1 );
                pane.add(valueText, j, i+1 );
                // Centers every Tile in each Node
                GridPane.setHalignment(valueText, HPos.CENTER);
                GridPane.setValignment(valueText, VPos.CENTER);
            }
        }
    }

   /* 
    * Name:      myKeyHandler
    * Purpose:   Overrides the default handle method. It also implements 
                 EventHandler. It can read user inputs and act
                 accordingly. 
    */

    private class myKeyHandler implements EventHandler<KeyEvent>
    {
       /* 
        * Name:      handle
        * Purpose:   Overrides the default handle method. Reads 
                     User Keystroke is read, and check whether game 
                     is over. It also translate the keystroke and moves 
                     in the appropriate direction. It then adds a random tile,
                     and then updates the GUI Table. 
          Parameter: KeyEvent event - User Keystroke
        */

        @Override
        public void handle ( KeyEvent event)
        {
            KeyCode pressed = event.getCode();
            // Checks whether is Game Over
            if ( board.isGameOver() != true )
            {
                // Checks whether User inputed valid key
                if ( validInput( pressed ) )
                {
                    // If User Keystroke was UP arrow, and going up was a
                    // valid move, then it moves all tiles upward, and adds
                    // a random tile. It then tells User, and then updates
                    // the board.
                    if ( pressed == KeyCode.UP && board.canMove(Direction.UP) )
                    {
                        board.move ( Direction.UP );
                        board.addRandomTile();
                        System.out.println ("Moving UP");
                        boardUpdate();
                    }
                    // If User Keystroke was DOWN arrow, and going down was a
                    // valid move, then it moves all tiles upward, and adds
                    // a random tile. It then tells User, and then updates
                    // the board. 
                    if ( pressed == KeyCode.DOWN && 
                              board.canMove(Direction.DOWN))
                    {
                        board.move ( Direction.DOWN );
                        board.addRandomTile();
                        System.out.println ("Moving <DOWN>");
                        boardUpdate();
                    }
                    // If User Keystroke was LEFT arrow, and going left was a
                    // valid move, then it moves all tiles upward, and adds
                    // a random tile. It then tells User, and then updates
                    // the board.
                    if ( pressed == KeyCode.LEFT 
                              && board.canMove(Direction.LEFT) )
                    {
                        board.move ( Direction.LEFT );
                        board.addRandomTile();
                        System.out.println ("Moving <LEFT>");
                        boardUpdate();
                    }
                    // If User Keystroke was RIGHT arrow, and going right was a
                    // valid move, then it moves all tiles upward, and adds
                    // a random tile. It then tells User, and then updates
                    // the board.
                    if ( pressed == KeyCode.RIGHT && 
                              board.canMove(Direction.RIGHT) )
                    {
                        board.move ( Direction.RIGHT );
                        board.addRandomTile();
                        System.out.println ("Moving <RIGHT>");
                        boardUpdate();
                    }
                    // If User Keystroke was S, then it saves the board, 
                    // and protects against exceptions
                    if ( pressed == KeyCode.S )
                    {
                        try 
                        {
                            board.saveBoard("2048.board");
                        } 
                    
                        catch (IOException e) 
                        {
                            System.out.println("saveBoard threw an Exception");
                        }

                        System.out.println ("Saving Board to <2048.board>");
                    }
                }
            }
            
            // Game Over
            else
            {
                // Allows Overlap of Rectangles
                StackPane gameOver = new StackPane();
                // Creates GameOver Box
                Rectangle gameOverBox = new Rectangle();
                gameOverBox.setFill(COLOR_GAME_OVER); // Color of Game Over Box
                // Creates Text for Game Over
                Text gameOverText = new Text();
                gameOverText.setText("Game Over!");
                // Sets the Font
                gameOverText.setFont(Font.font("Forte",
                                        FontPosture.ITALIC,30));
                gameOverText.setFill(Color.BLACK);
                // Adds Box and Text to Pane
                gameOver.getChildren().add(gameOverBox);
                gameOver.getChildren().add(gameOverText);
                // Changes Scene to Game Over
                Scene gameOverScene = new Scene(gameOver);
                primaryStage.setScene(gameOverScene);
                primaryStage.show();
            }
        }

       /* 
        * Name:      validInput
        * Purpose:   Sees whether User inputed Keystroke 
                     is either UP,DOWN,LEFT,RIGHT Arrow Key
        * Parameter: KeyCode input - User Keystroke
        */

        private boolean validInput( KeyCode input )
        {
            // Tests whether or not Inputed Value is valid
            // (Meaning either 'UP', 'DOWN', 'LEFT', 'RIGHT', or 'S')
            if ( input == KeyCode.UP )
            {
                return true; // Valid Move
            }

            else if ( input == KeyCode.DOWN )
            {
                return true; // Valid Move
            }

            else if ( input == KeyCode.LEFT )
            {
                return true; // Valid Move
            }

            else if ( input == KeyCode.RIGHT )
            {
                return true; // Valid Move
            }

            else if ( input == KeyCode.S )
            {
                return true; // Valid Move
            }
        
            else
            {
                return false; // Invalid Move
            }
        }      
    }

  /* 
   * Name:      setValue
   * Purpose:   Determines the tile background color
                and text Color
   * Parameter: int num - Tile Number Value
   */

    // Checks what color to make tile
    private void setValue ( int num )
    {
        if ( num == 0 )
        {
            this.valueColor = COLOR_EMPTY;
            this.textColor = COLOR_VALUE_DARK;
        }

        else if ( num == TWO)
        {
            this.valueColor = COLOR_2;
            this.textColor = COLOR_VALUE_DARK;
        }

        else if ( num == FOUR)
        {
            this.valueColor = COLOR_4;
            this.textColor = COLOR_VALUE_DARK;
        }

        else if ( num == EIGHT )
        {
            this.valueColor = COLOR_8;
            this.textColor = COLOR_VALUE_LIGHT;
        }

        else if ( num == ONE_SIX )
        {
            this.valueColor = COLOR_16;
            this.textColor = COLOR_VALUE_LIGHT;
        }

        else if ( num == THREE_TWO )
        {
            this.valueColor = COLOR_32;
            this.textColor = COLOR_VALUE_LIGHT;
        }

        else if ( num == SIX_FOUR )
        {
            this.valueColor = COLOR_64;
            this.textColor = COLOR_VALUE_LIGHT;
        }

        else if ( num == ONE_TWO_EIGHT )
        {
            this.valueColor = COLOR_128;
            this.textColor = COLOR_VALUE_LIGHT;
        }

        else if ( num == TWO_FIVE_SIX )
        {
            this.valueColor = COLOR_256;
            this.textColor = COLOR_VALUE_LIGHT;
        }

        else if ( num == FIVE_ONE_TWO )
        {
            this.valueColor = COLOR_512;
            this.textColor = COLOR_VALUE_LIGHT;
        }

        else if ( num == ONE_O_TWO_FOUR )
        {
            this.valueColor = COLOR_1024;
            this.textColor = COLOR_VALUE_LIGHT;
        }

        else if ( num == TWO_O_FOUR_EIGHT )
        {
            this.valueColor = COLOR_2048;
            this.textColor = COLOR_VALUE_LIGHT;
        }

        else 
        {
            this.valueColor = COLOR_OTHER;
            this.textColor = COLOR_VALUE_LIGHT;
        }
    }

  /* 
   * Name:      boardUpdate
   * Purpose:   Updates the GUI Board and Score
   */

    private void boardUpdate()
    {
        // Sets Score to Current Score
        scoreText.setText("Score: " + board.getScore() );

        // Finds Appropriate Rectangle and Text in Arrays
        // to update
        for ( int j = 0; j < GRID_SIZE; j++)
        {
            for ( int i = 0; i < GRID_SIZE; i++)
            {
                // Declaring Variables
                Text updateText = copyText[i][j];
                Rectangle updateRect = copyRect[i][j];
                Integer value = new Integer( grid[i][j] );
                // Sets Color of Tile and Color of Text depending
                // on value
                setValue ( value.intValue() );
                // Does not print if number is zero
                if ( value.intValue() != 0 )
                {
                    updateText.setText( value.toString() );
                }
                // Clears Text
                else
                {
                    updateText.setText( "" );
                }
                // Sets Text Font
                updateText.setFont(Font.font("Forte", 
                                             FontWeight.NORMAL, 30));
                // Updates Tile Color and Text Color
                updateText.setFill(textColor);
                updateRect.setFill(valueColor);
            }
        }
    }        
        
    /** DO NOT EDIT BELOW */

    // The method used to process the command line arguments
    private void processArgs(String[] args)
    {
        String inputBoard = null;   // The filename for where to load the Board
        int boardSize = 0;          // The Size of the Board

        // Arguments must come in pairs
        if((args.length % 2) != 0)
        {
            printUsage();
            System.exit(-1);
        }

        // Process all the arguments 
        for(int i = 0; i < args.length; i += 2)
        {
            if(args[i].equals("-i"))
            {   // We are processing the argument that specifies
                // the input file to be used to set the board
                inputBoard = args[i + 1];
            }
            else if(args[i].equals("-o"))
            {   // We are processing the argument that specifies
                // the output file to be used to save the board
                outputBoard = args[i + 1];
            }
            else if(args[i].equals("-s"))
            {   // We are processing the argument that specifies
                // the size of the Board
                boardSize = Integer.parseInt(args[i + 1]);
            }
            else
            {   // Incorrect Argument 
                printUsage();
                System.exit(-1);
            }
        }

        // Set the default output file if none specified
        if(outputBoard == null)
            outputBoard = "2048.board";
        // Set the default Board size if none specified or less than 2
        if(boardSize < 2)
            boardSize = 4;

        // Initialize the Game Board
        try{
            if(inputBoard != null)
                board = new Board(inputBoard, new Random());
            else
                board = new Board(boardSize, new Random());
        }
        catch (Exception e)
        {
            System.out.println(e.getClass().getName() + " was thrown while creating a " +
                               "Board from file " + inputBoard);
            System.out.println("Either your Board(String, Random) " +
                               "Constructor is broken or the file isn't " +
                               "formated correctly");
            System.exit(-1);
        }
    }

    // Print the Usage Message 
    private static void printUsage()
    {
        System.out.println("Gui2048");
        System.out.println("Usage:  Gui2048 [-i|o file ...]");
        System.out.println();
        System.out.println("  Command line arguments come in pairs of the form: <command> <argument>");
        System.out.println();
        System.out.println("  -i [file]  -> Specifies a 2048 board that should be loaded");
        System.out.println();
        System.out.println("  -o [file]  -> Specifies a file that should be used to save the 2048 board");
        System.out.println("                If none specified then the default \"2048.board\" file will be used");
        System.out.println("  -s [size]  -> Specifies the size of the 2048 board if an input file hasn't been");
        System.out.println("                specified.  If both -s and -i are used, then the size of the board");
        System.out.println("                will be determined by the input file. The default size is 4.");
    }
}
