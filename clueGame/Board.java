package clueGame;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import experiment.TestBoardCell;

public class Board 
{
    private BoardCell[][] grid;
    int numRows;
    int numColumns;
    String layoutConfigFiles;
    String setupConfigFile;
    private Set<TestBoardCell> targets;
    private Set<TestBoardCell> visited;
    Map<Character, Room> roomMap = new HashMap<>();


    /*
    * variable and methods used for singleton pattern
    */
    private static Board theInstance = new Board();
    // constructor is private to ensure only one can be created
    private Board() {
        super();
        loadSetupConfig();
    }
    // this method returns the only Board
    public static Board getInstance() {
        return theInstance;
    }
    /*
    * initialize the board (since we are using singleton pattern)
    */
    public void initialize()
    {
        targets = new HashSet<>();
        visited = new HashSet<>();
       
    }

    public void loadSetupConfig()
    {
        roomMap.put('K', new Room("Conservatory"));
        roomMap.put('L', new Room("Ballroom"));
        roomMap.put('M', new Room("Billiard Room"));
        roomMap.put('B', new Room("Dining Room"));
        roomMap.put('C', new Room("Walkway"));
        roomMap.put('T', new Room("Walkway"));
        roomMap.put('I', new Room("Walkway"));
        roomMap.put('C', new Room("Walkway"));
    }

    public void loadLayoutConfig()
    {

    }

    public void setConfigFiles(String layOutConfigFiles, String setUpConfigFile)
    {
        try 
        {
        File myObj = new File("filename.txt");
        Scanner myReader = new Scanner(myObj);
        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            System.out.println(data);
        }
        myReader.close();
        } catch (FileNotFoundException e) {

        System.out.println("An error occurred.");
        e.printStackTrace();
        }


    }

    public Room getRoom(char initial) 
    {
        return roomMap.get(initial);
    }

    public int getNumRows()
    {
        return numRows;
    }

    public int getNumColumns()
    {
        return numColumns;
    }

    public BoardCell getCell(int rowNum, int colNum)
    {
        return new BoardCell(rowNum, colNum, false, false);
    }

    public Room getRoom(BoardCell cell)
    {
        return new Room("");
    }

    



}
