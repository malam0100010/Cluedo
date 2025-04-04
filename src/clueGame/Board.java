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
            // (a) Secret Passage: Check if the room center (or another cell in the room) has a secret passage.
            char secret = cell.getSecretPassage();
            if (secret == '\0') {
                for (int r = 0; r < numRows; r++) {
                    for (int c = 0; c < numColumns; c++) {
                        BoardCell other = getCell(r, c);
                        if (other.getCellInitial() == cell.getCellInitial() && other.getSecretPassage() != '\0') {
                            secret = other.getSecretPassage();
                            break;
                        }
                    }
                    if (secret != '\0') break;
                }
            }
            if (secret != '\0') {
                BoardCell destCenter = findRoomCenter(secret);
                if (destCenter != null) {
                    adjList.add(destCenter);
                }
            }
            
            // (b) Add door cells that lead inside this room.
            // For each cell on the board that is a doorway, we compute its "inside" cell
            // (one step in the same direction as the door's arrow) and if that cell belongs
            // to the same room, we add the door cell.
            for (int r = 0; r < numRows; r++) {
                for (int c = 0; c < numColumns; c++) {
                    BoardCell potentialDoor = getCell(r, c);
                    if (potentialDoor.isDoorway()) {
                        int doorRow = potentialDoor.getRow();
                        int doorCol = potentialDoor.getColumn();
                        int insideRow = doorRow;
                        int insideCol = doorCol;
                        switch (potentialDoor.getDoorDirection()) {
                            case UP:    insideRow = doorRow - 1; break;
                            case DOWN:  insideRow = doorRow + 1; break;
                            case LEFT:  insideCol = doorCol - 1; break;
                            case RIGHT: insideCol = doorCol + 1; break;
                            default:    break;
                        }
                        if (isValid(insideRow, insideCol)) {
                            BoardCell insideCell = getCell(insideRow, insideCol);
                            if (insideCell.getCellInitial() == cell.getCellInitial()) {
                                adjList.add(potentialDoor);
                            }
                        }
                    }
                }
            }
        }
        // Cell is a doorway
        else if (cell.isDoorway()) {
        	// Add center cell as adjacency
        	if(cell.getDoorDirection() == DoorDirection.UP)
        	{
        		// Go inside the door to the room
        		int doorCellRow = cell.getRow() - 1;
        		int doorCellCol = cell.getColumn();
        		BoardCell tempBoardCell = getCell(doorCellRow, doorCellCol);
        		char targetCellInitial = tempBoardCell.getCellInitial();
        		
        		adjList.add(getRoom(targetCellInitial).getCenterCell());
        		
        		
        		// Loop until cell initial && isRoomCenter
        		for(int rows = 0; rows < numRows; ++rows) {
        			for(int cols = 0; cols < numColumns; ++cols) {
        				tempBoardCell = new BoardCell(rows, cols);
        				if(tempBoardCell.getCellInitial() == targetCellInitial && tempBoardCell.isRoomCenter()) {
        					adjList.add(tempBoardCell);
        				}
        			}
        		}
        		
        		// Add other walkway cells: check left, right, down
        		// Down
        		doorCellRow = cell.getRow() + 1;
        		doorCellCol = cell.getColumn();
        		tempBoardCell = getCell(doorCellRow, doorCellCol);
        		if(tempBoardCell.getCellInitial() == 'W') {
        			adjList.add(tempBoardCell);
        		}
        		
        		// Right
        		doorCellRow = cell.getRow();
        		doorCellCol = cell.getColumn() + 1;
        		tempBoardCell = getCell(doorCellRow, doorCellCol);
        		if(tempBoardCell.getCellInitial() == 'W') {
        			adjList.add(tempBoardCell);
        		}
        		
        		// Left
        		doorCellRow = cell.getRow();
        		doorCellCol = cell.getColumn() - 1;
        		tempBoardCell = getCell(doorCellRow, doorCellCol);
        		if(tempBoardCell.getCellInitial() == 'W') {
        			adjList.add(tempBoardCell);
        		}
        		
        	}else if(cell.getDoorDirection()== DoorDirection.DOWN){
        		// Go inside the door to the room
        		int doorCellRow = cell.getRow() + 1;
        		int doorCellCol = cell.getColumn();
        		BoardCell tempBoardCell = getCell(doorCellRow, doorCellCol);
        		char targetCellInitial = tempBoardCell.getCellInitial();
        		
        		
        		// Loop until cell initial && isRoomCenter
        		adjList.add(getRoom(targetCellInitial).getCenterCell());
        		
        		// Add other walkway cells: check left, right, up
        		// Down
        		doorCellRow = cell.getRow() + 1;
        		doorCellCol = cell.getColumn();
        		tempBoardCell = getCell(doorCellRow, doorCellCol);
        		if(tempBoardCell.getCellInitial() == 'W') {
        			adjList.add(tempBoardCell);
        		}
        		
        		// Right
        		doorCellRow = cell.getRow();
        		doorCellCol = cell.getColumn() + 1;
        		tempBoardCell = getCell(doorCellRow, doorCellCol);
        		if(tempBoardCell.getCellInitial() == 'W') {
        			adjList.add(tempBoardCell);
        		}
        		
        		// Left
        		doorCellRow = cell.getRow();
        		doorCellCol = cell.getColumn() - 1;
        		tempBoardCell = getCell(doorCellRow, doorCellCol);
        		if(tempBoardCell.getCellInitial() == 'W') {
        			adjList.add(tempBoardCell);
        		}
        		
        	}else if(cell.getDoorDirection()== DoorDirection.LEFT){
        		// Go inside the door to the room
        		int doorCellRow = cell.getRow();
        		int doorCellCol = cell.getColumn() - 1;
        		BoardCell tempBoardCell = getCell(doorCellRow, doorCellCol);
        		char targetCellInitial = tempBoardCell.getCellInitial();
        		
        		
        		// Loop until cell initial && isRoomCenter
        		adjList.add(getRoom(targetCellInitial).getCenterCell());
        		
        		// Add other walkway cells: check up, right, down
        		// Down
        		doorCellRow = cell.getRow() + 1;
        		doorCellCol = cell.getColumn();
        		tempBoardCell = getCell(doorCellRow, doorCellCol);
        		if(tempBoardCell.getCellInitial() == 'W') {
        			adjList.add(tempBoardCell);
        		}
        		
        		// Right
        		doorCellRow = cell.getRow();
        		doorCellCol = cell.getColumn() + 1;
        		tempBoardCell = getCell(doorCellRow, doorCellCol);
        		if(tempBoardCell.getCellInitial() == 'W') {
        			adjList.add(tempBoardCell);
        		}
        		
        		// up
        		doorCellRow = cell.getRow() - 1;
        		doorCellCol = cell.getColumn();
        		tempBoardCell = getCell(doorCellRow, doorCellCol);
        		if(tempBoardCell.getCellInitial() == 'W') {
        			adjList.add(tempBoardCell);
        		}
        		
        	}else if(cell.getDoorDirection()== DoorDirection.RIGHT){
        		// Go inside the door to the room
        		int doorCellRow = cell.getRow();
        		int doorCellCol = cell.getColumn() + 1;
        		BoardCell tempBoardCell = getCell(doorCellRow, doorCellCol);
        		char targetCellInitial = tempBoardCell.getCellInitial();
        		
        		
        		// Loop until cell initial && isRoomCenter
        		adjList.add(getRoom(targetCellInitial).getCenterCell());
        		
        		// Add other walkway cells: check up, left, down
        		// Down
        		doorCellRow = cell.getRow() - 1;
        		doorCellCol = cell.getColumn();
        		tempBoardCell = getCell(doorCellRow, doorCellCol);
        		if(tempBoardCell.getCellInitial() == 'W') {
        			adjList.add(tempBoardCell);
        		}
        		
        		// left
        		doorCellRow = cell.getRow();
        		doorCellCol = cell.getColumn() - 1;
        		tempBoardCell = getCell(doorCellRow, doorCellCol);
        		if(tempBoardCell.getCellInitial() == 'W') {
        			adjList.add(tempBoardCell);
        		}
        		
        		// up
        		doorCellRow = cell.getRow() + 1;
        		doorCellCol = cell.getColumn();
        		tempBoardCell = getCell(doorCellRow, doorCellCol);
        		if(tempBoardCell.getCellInitial() == 'W') {
        			adjList.add(tempBoardCell);
        		}
        		
        	}
        	
        }   
        // Is a walkway, but it is not a door
        else if (cell.getCellInitial() == 'W' && !cell.isDoorway()) {
    		int doorCellRow = cell.getRow();
    		int doorCellCol = cell.getColumn();
    		BoardCell tempBoardCell = getCell(doorCellRow, doorCellCol);
    		
    		// Add other walkway cells: check up, left, down
    		// Down
    		doorCellRow = cell.getRow() + 1;
    		doorCellCol = cell.getColumn();
    		
    		
    		if(doorCellRow < numRows && getCell(doorCellRow, doorCellCol).getCellInitial() == 'W') {
    			tempBoardCell = getCell(doorCellRow, doorCellCol);
    			adjList.add(tempBoardCell);
    		}
    		
    		// left
    		doorCellRow = cell.getRow();
    		doorCellCol = cell.getColumn() - 1;
    		
    		if(doorCellCol >= 0 && getCell(doorCellRow, doorCellCol).getCellInitial() == 'W') {
    			
    			tempBoardCell = getCell(doorCellRow, doorCellCol);
    			adjList.add(tempBoardCell);
    		}
    		
    		// up
    		doorCellRow = cell.getRow() - 1;
    		doorCellCol = cell.getColumn();
    
    		if(doorCellCol >= 0 && getCell(doorCellRow, doorCellCol).getCellInitial() == 'W') {
    			tempBoardCell = getCell(doorCellRow, doorCellCol);
    			adjList.add(tempBoardCell);
    		}
    		
    		// Right
    		doorCellRow = cell.getRow();
    		doorCellCol = cell.getColumn() + 1;
    		
    		if(doorCellCol < numColumns && getCell(doorCellRow, doorCellCol).getCellInitial() == 'W') {
    			tempBoardCell = getCell(doorCellRow, doorCellCol);
    			adjList.add(tempBoardCell);
    		}
        }
        return adjList;
    }

    /**
     * Helper method: Checks if (r,c) is within board boundaries.
     */
    private boolean isValid(int r, int c) {
        return r >= 0 && r < numRows && c >= 0 && c < numColumns;
    }

    /**
     * Helper method: Scans the grid for a room center cell with the given room initial.
     */
    private BoardCell findRoomCenter(char roomInitial) {
        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numColumns; c++) {
                BoardCell candidate = getCell(r, c);
                if (candidate.isRoomCenter() && candidate.getCellInitial() == roomInitial) {
                    return candidate;
                }
            }
        }
        return null;
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
