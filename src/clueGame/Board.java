package clueGame;

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
    }
    // this method returns the only Board
    public static Board getInstance() {
        return theInstance;
    }
    /*
    * initialize the board (since we are using singleton pattern)
    */
    public void initialize() throws FileNotFoundException, BadConfigFormatException {
        targets = new HashSet<>();
        visited = new HashSet<>();
        loadSetupConfig();
        //loadLayoutConfig();
       
    }

    public void loadSetupConfig() throws FileNotFoundException, BadConfigFormatException {
        File file = new File("data/" + this.setupConfigFile);
        Scanner scanner = new Scanner(file);
        
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            // Skip blank lines or comments (if any)
            if (line.isEmpty() || line.startsWith("//"))
                continue;
            
            // Split the line by commas and trim each token.
            String[] tokens = line.split(",");
            if (tokens.length != 3) {
            	scanner.close();
            	throw new BadConfigFormatException();
            }
            
            String type = tokens[0];
            String name = tokens[1];
            char initial = tokens[2].charAt(0);

            roomMap.put(initial, new Room(name));
        }
        scanner.close();
        
    }

    public void loadLayoutConfig() throws FileNotFoundException {
    	File file = new File(this.layoutConfigFiles);
        Scanner scanner = new Scanner(file);

        int rowCount = 0;
        int colCount = 0;

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) continue;

            String[] cells = line.split(",");
            colCount = Math.max(colCount, cells.length);
            rowCount++;
        }
        scanner.close();

        this.numRows = rowCount;
        this.numColumns = colCount;
    }

    public void setConfigFiles(String layOutConfigFiles, String setUpConfigFile)
    {
        this.layoutConfigFiles = layOutConfigFiles;
        this.setupConfigFile = setUpConfigFile;

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
