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
    private Set<BoardCell> targets;
    private Set<BoardCell> visited;
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
        } catch (FileNotFoundException e) {
            System.out.println("File not found! " + e.getMessage());
        } catch (BadConfigFormatException e) {
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
        		
        		if(!spaceType.equals("Room") && !spaceType.equals("Space")) {
        			myReader.close();
        			throw new BadConfigFormatException(spaceType);
        		}else {
        			roomMap.put(spaceSymbol, new Room(spaceName, spaceSymbol, null, null));
        		}
    			
    		}
    		

    	} catch(FileNotFoundException e) {
    		System.out.println(this.setupConfigFile + "could not be located");

    	}
        
    }

    public void loadLayoutConfig() throws FileNotFoundException, BadConfigFormatException {
        int fileRows = 0;
        
        try (Scanner myReader = new Scanner(new FileReader(this.layoutConfigFiles))) {
            while (myReader.hasNextLine()) {
                String readLine = myReader.nextLine().trim();
                
                if (readLine.isEmpty()) continue; 
                
                String[] locationInfo = readLine.split(",");
                
                if (fileRows == 0) {
                    numColumns = locationInfo.length;
                } else if (locationInfo.length != numColumns) {  
                    throw new BadConfigFormatException("Inconsistent column count at: " + readLine);
                }
                ++fileRows;  
            }
        } catch (FileNotFoundException e) {
            System.out.println(this.layoutConfigFiles + " could not be located.");
        }

        if (fileRows == 0 || numColumns == 0) {
            throw new BadConfigFormatException("Layout file is empty or improperly formatted.");
        }
        
        numRows = fileRows;
        grid = new BoardCell[numRows][numColumns]; 

        int rows = 0;
        //int numDoorWays = 0;
        try (Scanner myReaderNew = new Scanner(new FileReader(this.layoutConfigFiles))) {
            while (myReaderNew.hasNextLine()) {
                String readLine = myReaderNew.nextLine().trim();
                
                if (readLine.isEmpty()) continue;  
                
                String[] locationInfo = readLine.split(",");
                int cols = 0;  

                for (String token : locationInfo) {
                    if (roomMap.containsKey(token.charAt(0))) {
                        grid[rows][cols] = new BoardCell(rows, cols);  
                        grid[rows][cols].setIsRoom(true);
                        grid[rows][cols].setCellInitial(token.charAt(0));
                        
                        if (token.length() == 2) {
                            if (token.charAt(1) == '>') {
                                grid[rows][cols].setDoorDirection(DoorDirection.RIGHT);
                                grid[rows][cols].setDoorway(true);
                                //numDoorWays++;
                            } else if (token.charAt(1) == '<') {
                                grid[rows][cols].setDoorDirection(DoorDirection.LEFT);
                                grid[rows][cols].setDoorway(true);
                                //numDoorWays++;
                            } else if (token.charAt(1) == '^') {
                                grid[rows][cols].setDoorDirection(DoorDirection.UP);
                                grid[rows][cols].setDoorway(true);
                                //numDoorWays++;
                            } else if (token.charAt(1) == 'v') {
                                grid[rows][cols].setDoorDirection(DoorDirection.DOWN);
                                grid[rows][cols].setDoorway(true);
                                //numDoorWays++;
                            }
                            else if (token.charAt(1) == '#') {
                            	grid[rows][cols].setRoomLabel(true);
                            	roomMap.get(token.charAt(0)).setLabelCell(grid[rows][cols]);
                            }
                            else if (token.charAt(1) == '*') {
                            	grid[rows][cols].setRoomCenter(true);
                            	roomMap.get(token.charAt(0)).setCenterCell(grid[rows][cols]);
                            }
                            
                            //secret passage
                            else if (roomMap.containsKey(token.charAt(1))) {
                            	grid[rows][cols].setSecretPassage(token.charAt(1));
                            }
                        }
                    } else {
                        throw new BadConfigFormatException("Invalid token: " + token);
                    }
                    ++cols;
                }
                ++rows;
            }
            //System.out.println(numDoorWays);

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public void calcTargets(BoardCell someCell, int lengthToPath)
    {
    	if(targets == null) {
    		targets = new HashSet<>();
    	} 
    	if(visited == null) {
    		visited = new HashSet<>();
    	}
    	
    	visited.add(someCell);
    	
    	 for (BoardCell adjCell : getAdjList(someCell.getRow(), someCell.getColumn())) {
             if (visited.contains(adjCell) || adjCell.getIsOccupied()) {
                 continue;
             }
             if (adjCell.getIsRoom()) {
                 targets.add(adjCell);
                 continue;
             }
             visited.add(adjCell);
             if (lengthToPath == 1) {
                 targets.add(adjCell);
             } else {
                 calcTargets(adjCell, lengthToPath - 1);
             }
             visited.remove(adjCell);
         }
    	
    }
    
    public Set<BoardCell> getAdjList(int row, int col) {
        return new HashSet<>();
    }
    
    public Set<BoardCell> getTargets() {
        return (targets == null) ? new HashSet<>() : targets;
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
        return grid[rowNum][colNum];
    }

    public Room getRoom(BoardCell someCell)
    {
        return roomMap.get(someCell.cellInitial);
    }

    



}
