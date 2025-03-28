package clueGame;

import java.io.*;
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
    private String layoutConfigFiles;
    private String setupConfigFile;
    private Set<TestBoardCell> targets;
    private Set<TestBoardCell> visited;
    private Map<Character, Room> roomMap = new HashMap<>();


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
    public void initialize() {
        //targets = new HashSet<>();
        //visited = new HashSet<>();
        try {
			loadSetupConfig();
			loadLayoutConfig();
		} catch (BadConfigFormatException e) {
			// TODO Auto-generated catch block
			System.out.println(e);
		} 
        
       
    }

    public void loadSetupConfig() throws BadConfigFormatException {
    	try {
    		Scanner myReader = new Scanner(new FileReader(this.setupConfigFile));
    		
    		while(myReader.hasNextLine()){
    			String readLine = myReader.nextLine().trim();
    			
    			if(readLine.startsWith("//")) {
    				continue;
    			}
    			
        		String[] locationInfo = readLine.split(",");
        		String spaceType = locationInfo[0].trim();
        		String spaceName = locationInfo[1].trim();
        		char spaceSymbol = locationInfo[2].trim().charAt(0);
        		
        		if(spaceType != "Room" || spaceType != "Space") {
        			myReader.close();
        			throw new BadConfigFormatExcpetion(spaceType);
        		}else {
        			roomMap.put(spaceSymbol, new Room(spaceName, spaceSymbol, null, null));
        		}
    			
    		}
    		

    	} catch(FileNotFoundException e) {
    		System.out.println(this.setupConfigFile + "could not be located");

    	}
        
    }

    public void loadLayoutConfig() throws FileNotFoundException {
    	int fileRows = 0;
    	int fileCols = 0;
    	
    	try {
    		Scanner myReader = new Scanner(new FileReader(this.layoutConfigFiles));
    		
    		while(myReader.hasNextLine()) {
    			String readLine = myReader.nextLine().trim();
    			String[] locationInfo = readLine.split(",");
    			
    			if (fileRows == 0) {
    				numColumns = locationInfo.length;
    			} else if(locationInfo.length != fileCols){
    				myReader.close();
    				throw new BadConfigFormatException();
    			}
    			++fileCols; 
    		}
    		myReader.close();
    		numRows = fileRows;
    		numColumns = fileCols;
 
    		
    	} catch(FileNotFoundException e) {
    		System.out.println(e);
    	}
    	
    	grid = new BoardCell[numRows][numColumns];
    	
    	for(int rows = 0; rows < numRows; ++rows) {
    		for(int cols = 0; cols < numColumns; ++cols) {
                BoardCell cell = grid[rows][cols];
                if (rows > 0) { 
                    cell.addAdj(grid[rows - 1][cols]);
                }
                if (rows < numRows - 1) {
                    cell.addAdj(grid[rows + 1][cols]);
                }
                if (cols > 0) {
                    cell.addAdj(grid[rows][cols - 1]);
                }
                if (cols < numColumns - 1) {
                    cell.addAdj(grid[rows][cols + 1]);
                }
    		}
    	}
    	
    	int rows = 0;
    	int cols = 0;
    	try {
    		Scanner myReader = new Scanner(new FileReader(this.layoutConfigFiles));
    		while(myReader.hasNextLine()) {
    			String readLine = myReader.nextLine().trim();
    			String[] locationInfo = readLine.split(",");
    			
    			for(String token : locationInfo) {
    				if(roomMap.containsKey(token.charAt(0))) {
    					grid[rows][cols].setIsRoom(true);
    					grid[rows][cols].setCellInitial(token.charAt(0));
    					
    					if( token.length() == 2 && roomMap.containsKey(token.charAt(0))) {
    						grid[rows][cols].setIsRoom(true);
    						grid[rows][cols].setCellInitial(token.charAt(0));
    					}
    					
    				} else {
    					
    					myReader.close();
    					throw new BadConfigFormatException("Invalid token: " + token);
    					
    				}
    				
    				if(token.length() == 2) {
    					if(token.charAt(1) == '#') {
    						
    					}
    				}
    			}
    		}
    	}
    	
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
