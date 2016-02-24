import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.*;
import java.io.IOException;
import java.util.Scanner;

public class MineSweeper extends JApplet implements MouseListener, ActionListener
{
    boolean activeTimer;

    //------game-config-------------
    private int currentTime = 0;
    private final int ROWS = 10;            //number of rows in this game
    private final int COLUMNS = 10;         //number of columns in this game
    private final int NUMMINES = 10;        //number of mines on the field

    //-----mineArray-logic----------
    private Button mineArray[][];           //a 2D array of mine cells
    private Integer numFlagsRemaining;      //counts how many "M"'s are on the field
    private Integer miss;                   //counter of how many times player did NOT land on a mine

    //-----counter/reset/timer------
    private JLabel minesRemaining;          //JLabel to hold numFlagsRemaining
    private JButton resetButton;            //JButton that resets the game
    private JLabel timeLabel;               //JLabel that displays the time
    private Timer timer;                    //ongoing timer
    private int milliSecPassed;             //number of milliseconds that has passed since game starts
    private int secondsPassed;              //number of seconds that has passed since game starts
    private int minutesPassed;              //number of minutes that have passed since game starts

    //------menuBar-----------------
    private JMenuBar menuBar;
    private JMenu gameMenu;
    private JMenuItem resetSubMenu;
    private JMenuItem topTenSubMenu;
    private JMenuItem exitSubMenu;
    private JMenu helpMenu;
    private JMenuItem helpSubMenu;
    private JMenuItem aboutSubMenu;
    private JMenuItem resetScoresSubMenu;

    //------panels------------------
//    private MenuBar menubar;                //hold the menu at the top of the game
    private JPanel GUIpanel;                //panel that holds minesRemaining, Reset JButton, and timer JLabel
    private JPanel minePanel;               //holds mineArray[][]

    //-----------------------------------------
    private FileReader f;
    private int bestScore;


    @Override
    /*starter function: set the layout/size, and call set()*/
    public void init()
    {
        setLayout(new BorderLayout());
        this.setSize(250, 300);

        set();
    }//end init()


    /*initial set*/
    public void set()
    {
        activeTimer = true;
        miss = 0;

        minePanel = new JPanel();
        mineArray = new Button[ROWS][COLUMNS];
        minePanel.setLayout(new GridLayout(ROWS, COLUMNS));
        GUIpanel = new JPanel();

        for (int i = 0; i < ROWS; i++)
            for (int j = 0; j < COLUMNS; j++)
            {
                mineArray[i][j] = new Button();
                minePanel.add(mineArray[i][j].getButton());
                mineArray[i][j].getButton().addMouseListener(this);
            }

        minePanel.setPreferredSize(new Dimension(200, 200));

        numFlagsRemaining = NUMMINES;
        placeMines();
        countAdjacentMines();

        //--------------------------------------
        timeLabel = new JLabel("0000");

        minesRemaining = new JLabel(numFlagsRemaining.toString());
        JPanel left = new JPanel(new FlowLayout());
        left.add(minesRemaining, BorderLayout.WEST);

        resetButton = new JButton("RESET");
        resetButton.addActionListener(this);

        JPanel center = new JPanel(new FlowLayout());
        center.add(resetButton, BorderLayout.CENTER);

        JPanel right = new JPanel(new BorderLayout());
        right.add(timeLabel, BorderLayout.EAST);

        GUIpanel.add(left, FlowLayout.LEFT);
        GUIpanel.add(center, BorderLayout.CENTER);
        GUIpanel.add(right, BorderLayout.EAST);

        timer = new Timer(1000, this);


        createMenuBar();
        setJMenuBar(menuBar);

        add(GUIpanel, BorderLayout.NORTH);
        add(minePanel, BorderLayout.CENTER);

    }//end set()


    /*After reset button is pressed, remove and re-add panels to the Applet*/
    public void reset()
    {
        activeTimer = false;
        GUIpanel.removeAll();
        minePanel.removeAll();
        currentTime = 0;
        remove(GUIpanel);
        remove(minePanel);

        set();
        timeLabel.setText("00" + currentTime);
        activeTimer = true;
    }//end reset()


    public void createMenuBar()
    {
        menuBar = new JMenuBar();

        gameMenu = new JMenu("Game");
        gameMenu.setMnemonic('G');
        resetSubMenu = new JMenuItem("Reset");
        resetSubMenu.setMnemonic('R');
        resetScoresSubMenu = new JMenuItem("Reset Scores");
        resetScoresSubMenu.setMnemonic('S');
        topTenSubMenu = new JMenuItem("Top ten");
        topTenSubMenu.setMnemonic('T');
        exitSubMenu = new JMenuItem("eXit");
        exitSubMenu.setMnemonic('X');

        resetSubMenu.addActionListener(this);
        topTenSubMenu.addActionListener(this);
        exitSubMenu.addActionListener(this);
        resetScoresSubMenu.addActionListener(this);

        gameMenu.add(resetScoresSubMenu);
        gameMenu.add(resetSubMenu);
        gameMenu.add(topTenSubMenu);
        gameMenu.add(exitSubMenu);
        menuBar.add(gameMenu);

        helpMenu = new JMenu("Help");
        helpMenu.setMnemonic('H');
        helpSubMenu = new JMenuItem("heLp");
        helpSubMenu.setMnemonic('L');
        aboutSubMenu = new JMenuItem("About");
        aboutSubMenu.setMnemonic('A');

        helpSubMenu.addActionListener(this);
        aboutSubMenu.addActionListener(this);

        helpMenu.add(helpSubMenu);
        helpMenu.add(aboutSubMenu);
        menuBar.add(helpMenu);
    }//end createMenuBar()


    /*Randomly place NUMMINES mines on the grid*/
    public void placeMines()
    {
        int minesPlaced = 0;

        while (minesPlaced < NUMMINES) //generate random coordinates until NUMMINES have been placed
        {
            int randomx = (int) (Math.random() * (ROWS));
            int randomy = (int) (Math.random() * (COLUMNS));

            if (!mineArray[randomx][randomy].isMine())
            {
                mineArray[randomx][randomy].setMine();
                minesPlaced++;
            }
        }
    }//end placeMines()


    /*Go to each cell in the 2D array and count how many mines directly surround it*/
    public void countAdjacentMines()
    {
        for (int i = 0; i < COLUMNS; i++)                  //checking left and right
            for (int j = 0; j < ROWS; j++)                 //checking up and down
                if (!(mineArray[i][j].isMine()))
                {
                    int count = 0;

                    for (int p = i - 1; p <= i + 1; p++)     //checking diagonal up-left and up-right
                        for (int q = j - 1; q <= j + 1; q++) //checking diagonal down-left and down-right
                            if (0 <= p && p < COLUMNS && 0 <= q && q < ROWS)
                                if (mineArray[p][q].isMine())
                                    ++count;
                    mineArray[i][j].setAdjacentMines(count);
                }
    }// end countAdjacentMines()


    /*Given an x and y coordinate, recurse through the surrounding 8 mines and reveal them if 0 mines surround them*/
    void revealNeighboringMines(int x, int y)
    {
        if ((x < 0 || x >= ROWS || y < 0 || y >= COLUMNS) //base case: outOfBoundsCheck
            || mineArray[x][y].revealed()                 //           stop if this cell has been revealed
            || !mineArray[x][y].getButton().isEnabled())  //           stop if this cell has been disabled
            return;

        mineArray[x][y].reveal();                         //reveal this mine and increment the miss counter
        miss++;

        if (mineArray[x][y].getAdjacentMines() == 0)     //if 0 mines surround it, check the 8 surrounding mines
            for (int i = x - 1; i <= x + 1; i++)
                for (int j = y - 1; j <= y + 1; j++)
                    revealNeighboringMines(i, j);        //recursive call
    }//end revealNeighboringMines(...)


    /*At the end of a game, won or lost, disable all buttons*/
    public void disableButtons()
    {
        for (Button[] b : mineArray)
            for (Button b1 : b)
                b1.getButton().setEnabled(false);
    }//end disableButtons()


    /*Reveal all the mines, with "M" or with "X" depending on whether player lost or not*/
    public void revealAllMines(Character c)
    {
        for (Button[] b : mineArray)
            for (Button b1 : b)
                if (b1.isMine())
                    b1.setText(c.toString());
    }//end revealAllMines(...)


    /*Switch from BLANK to M to ? and back again, using indexes 0, 1, 2*/
    void advanceState(Button button)
    {
        if (button.getCurrentState() == 2) //cycle from 2 to 0
            button.setCurrentState((char) 0);
        else
            button.setCurrentState((char) (button.getCurrentState() + 1)); //otherwise just advance state

        if (button.getCurrentState() == 1)   //if state M was entered, decrement num total flags available
            numFlagsRemaining--;
        else if (button.getCurrentState() == 2) //if state M was left, increment
            numFlagsRemaining++;

        button.setText(button.getStates()[button.getCurrentState()].toString()); //change the text of the button
        minesRemaining.setText(numFlagsRemaining.toString()); //update the mine counter if neessary
    }//end advanceState(...)


    /*After every move, check to see if the player has won, and disable the timer and buttons*/
    public void checkWin()
    {
        if (miss == (ROWS * COLUMNS) - NUMMINES)
        {
            timer.stop();
            minesRemaining.setText("0");
            revealAllMines('M');
            JOptionPane.showMessageDialog(null, "Congratulations, You Win!!");
            disableButtons();
            writeFile();
        }
    }//end checkWin()


    private void writeFile()
    {
        String name = JOptionPane.showInputDialog(null, "What's your name?");

        try
        {
            String filename = "./highscores.txt";
            FileWriter fw = new FileWriter(filename, true); //the true will append the new data
            fw.write("Name: " + name + ", time: " + currentTime + "\n");//appends the string to the file
            fw.close();
        }
        catch (IOException ioe)
        {
            System.err.println("IOException: " + ioe.getMessage());
        }
    }


    public String getTopScores() throws IOException
    {
        File f = new File("highscores.txt");
        if (!f.exists() && !f.isDirectory())
        {
            f.createNewFile();
            return "File does not exist, creating.";
        }
        else
        {
            int i = 1;
            String topScores = "";

            Scanner s = new Scanner(new File("highscores.txt"));

            //sort
            while (s.hasNextLine())
            {
                topScores += i + ") " + s.nextLine() + "\n";
                i++;
            }
            s.close();

            return topScores;
        }
    }


    //----------------------------------------------------------------------------------------------
    /*Handle reset button clicked*/
    public void actionPerformed(ActionEvent e)
    {
        /*The Reset menu items is to perform the same function as the reset button.*/
        if (e.getSource() == resetSubMenu || e.getSource() == resetButton)
        {
            timer.stop();
            reset();           //reset the game
            return;
        }

        if (e.getSource() == timer)
        {
            currentTime++;

            if (currentTime < 10)
                timeLabel.setText("00" + currentTime);
            else if (currentTime < 100)
                timeLabel.setText("0" + currentTime);
            else
                timeLabel.setText("" + currentTime);
        }

        /*  The top ten scorers is a list of the top ten fastest users to solve a game.
            There must also be a way to reset/clear the top ten scorers
            (perhaps another button on the Game drop down menu). When a user
            makes the top ten scorers list, you must prompt for the user's name and
            record both the user's name and elapsed time for the game.
            This information must be kept from each time your program is run;
            therefore, you will need to keep this information in a file
            (you may assume it is in the java runtime default directory).
            This file will be need to be read in when the program
             begins and saved out this information as the program ends.
            If the file doesn’t exist at start time, assume the program
            is running for the first time and the file does not yet exist. */
        else if (e.getSource() == topTenSubMenu)
        {
            String scores = "";

            try
            {
                scores = getTopScores();
            }
            catch (IOException e1)
            {
                e1.printStackTrace();
            }
            JOptionPane.showMessageDialog(null, scores);
        }

        /*The eXit menu item should end the program.*/
        else if (e.getSource() == exitSubMenu)
        {
            System.exit(0);
        }

        /*The Help menu items should display some information about how to play the game*/
        else if (e.getSource() == helpSubMenu)
        {
            JOptionPane.showMessageDialog(null,
                    "The goal of the game is to uncover all the squares that do not contain mines without being \n" +
                    "\"blown up\" by clicking on a square with a mine underneath. The location of the mines is \n" +
                    "discovered by a process of logic. Clicking on the game board will reveal what is hidden underneath the \n" +
                    "chosen square or squares (a large number of blank squares may be revealed in one go if \n" +
                    " they are adjacent to each other).\n\n Some squares are blank but some contain numbers (1 to 8)," +
                    " \n" +
                    " each number being the number of mines adjacent to the uncovered square. To help avoid hitting a mine,\n" +
                    " the location of a suspected mine can be marked by flagging it with the right mouse button. \n" +
                    "\nThe game is won once all blank squares have been uncovered without hitting a mine, \n" +
                    "any remaining mines not identified by flags being automatically flagged by the computer. \n");
        }

        /*
         About menu item should give some information about the development team
        (name(s), user-id(s), etc) and course/project information.
         */
        else if (e.getSource() == aboutSubMenu)
        {
            String classInfo = "UIC Spring 2016, CS 342: Software Design \n";
            String profInfo = "Professor: Pat Troy | TA: Muhammad Khan \n";
            String Names = "Programmers: Filip Variciuc | Yunxiao Cui \n";
            String userIDs = "NetIDs: variciu2 | ycui22 \n";
            String projectInfo = "Project 2: Minesweeper, a spin-off programmed using Java Swing API";
            JOptionPane.showMessageDialog(null, classInfo + profInfo + Names + userIDs + projectInfo);
        }

        else if (e.getSource() == resetScoresSubMenu)
        {
            try
            {
                BufferedWriter pw = new BufferedWriter(new FileWriter("./highscores.txt", false));
                pw.close();
            }
            catch (IOException ioe)
            {
                System.err.println("IOException: " + ioe.getMessage());
            }
        }
    }//end actionPerformed(...)


    /*Handle left and right clicks on the individual mine cells*/
    public void mouseReleased(MouseEvent e)
    {
        if (!timer.isRunning()) //start the timer when the user first clicks a mine
            timer.start();

        for (int x = 0; x < ROWS; x++)
            for (int y = 0; y < COLUMNS; y++)
                if (e.getSource() == mineArray[x][y].getButton()) //find the button that was pressed
                {
                    Button currentMine = mineArray[x][y];

                    if (SwingUtilities.isLeftMouseButton(e))
                    {
                        if (currentMine.getCurrentState() == 0) //make sure user can only click on blank mine
                            if (currentMine.isMine())
                            {
                                revealAllMines('X');

                                timer.stop();

                                disableButtons();
                                JOptionPane.showMessageDialog(null, "**GAME OVER**\n YOU HIT A MINE");
                                // writeFile();
                            }
                            else
                            {
                                revealNeighboringMines(x, y);
                                checkWin();
                            }
                    }//end if (left button)
                    else if (SwingUtilities.isRightMouseButton(e))
                    {
                        if (currentMine.getButton().isEnabled() && numFlagsRemaining > 0
                            || currentMine.getCurrentState() == 1
                            || currentMine.getCurrentState() == 2)
                            advanceState(currentMine);
                    }//end if (right button)
                }//end if (found correct button)
    }//end mouseReleased(...)


    public void mouseExited(MouseEvent e)
    {
    }


    public void mouseClicked(MouseEvent e)
    {
    }


    public void mouseEntered(MouseEvent e)
    {
    }


    public void mousePressed(MouseEvent e)
    {
    }
}