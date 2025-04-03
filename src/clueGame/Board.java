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
        Set<BoardCell> adjList = new HashSet<>();
        BoardCell cell = getCell(row, col);

        // -------------------------------------
        // 1) ROOM CENTER CASE
        // -------------------------------------
        if (cell.isRoomCenter()) {
            // 1a) Add secret passage (if any)
            if (cell.getSecretPassage() != '\0') {
                Room secretRoom = getRoom(cell.getSecretPassage());
                if (secretRoom != null && secretRoom.getCenterCell() != null) {
                    adjList.add(secretRoom.getCenterCell());
                }
            }
            // 1b) Find all door cells that lead INSIDE this room center
            for (int r = 0; r < numRows; r++) {
                for (int c = 0; c < numColumns; c++) {
                    BoardCell potentialDoor = getCell(r, c);
                    if (potentialDoor.isDoorway()) {
                        // The "inside" of a door is one step opposite its arrow.
                        int doorRow = potentialDoor.getRow();
                        int doorCol = potentialDoor.getColumn();
                        int insideRow = doorRow, insideCol = doorCol;
                        switch (potentialDoor.getDoorDirection()) {
                            case UP:    insideRow = doorRow + 1; break;
                            case DOWN:  insideRow = doorRow - 1; break;
                            case LEFT:  insideCol = doorCol + 1; break;
                            case RIGHT: insideCol = doorCol - 1; break;
                            default:    break;
                        }
                        if (insideRow >= 0 && insideRow < numRows &&
                            insideCol >= 0 && insideCol < numColumns) {
                            BoardCell insideCell = getCell(insideRow, insideCol);
                            // If insideCell's initial matches this room center's initial, add the door.
                            if (insideCell.getCellInitial() == cell.getCellInitial()) {
                                adjList.add(potentialDoor);
                            }
                        }
                    }
                }
            }
        }
        // -------------------------------------
        // 2) DOOR CASE
        // -------------------------------------
        else if (cell.isDoorway()) {
            // 2a) First try: Use the door's own letter to find the room.
            Room doorRoom = getRoom(cell.getCellInitial());
            // If that fails or there's no valid center, do the "inside-cell" fallback.
            if (doorRoom == null || doorRoom.getCenterCell() == null) {
                int insideRow = row, insideCol = col;
                // Move opposite the door arrow to find the inside cell.
                switch (cell.getDoorDirection()) {
                    case UP:    insideRow = row + 1; break;
                    case DOWN:  insideRow = row - 1; break;
                    case LEFT:  insideCol = col + 1; break;
                    case RIGHT: insideCol = col - 1; break;
                    default:    break;
                }
                if (insideRow >= 0 && insideRow < numRows &&
                    insideCol >= 0 && insideCol < numColumns) {
                    BoardCell insideCell = getCell(insideRow, insideCol);
                    doorRoom = getRoom(insideCell.getCellInitial());
                }
            }
            // If we found a valid room center, add it.
            if (doorRoom != null && doorRoom.getCenterCell() != null) {
                adjList.add(doorRoom.getCenterCell());
            }
            // 2b) Outside neighbor: move 1 step in the direction of the door's arrow.
            int outRow = row, outCol = col;
            switch (cell.getDoorDirection()) {
                case UP:    outRow = row - 1; break;
                case DOWN:  outRow = row + 1; break;
                case LEFT:  outCol = col - 1; break;
                case RIGHT: outCol = col + 1; break;
                default:    break;
            }
            if (outRow >= 0 && outRow < numRows &&
                outCol >= 0 && outCol < numColumns) {
                adjList.add(getCell(outRow, outCol));
            }
        }
        // -------------------------------------
        // 3) WALKWAY CASE
        // -------------------------------------
        else if (!cell.getIsRoom()) {
            // Standard walkway adjacency: check up, down, left, and right.
            int[][] dirs = { {-1, 0}, {1, 0}, {0, -1}, {0, 1} };
            for (int[] d : dirs) {
                int r = row + d[0], c = col + d[1];
                if (r >= 0 && r < numRows && c >= 0 && c < numColumns) {
                    BoardCell neighbor = getCell(r, c);
                    // Only add neighbor if it is a walkway.
                    if (!neighbor.getIsRoom()) {
                        adjList.add(neighbor);
                    }
                    // (Do not add door cells in the walkway case.)
                }
            }
        }

        return adjList;
    }


    public Set<BoardCell> getTargets() {
    	if (targets == null) {
    	    return new HashSet<>();
    	} else {
    	    return targets;
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
        return grid[rowNum][colNum];
    }

    public Room getRoom(BoardCell someCell)
    {
        return roomMap.get(someCell.cellInitial);
    }

    



}
