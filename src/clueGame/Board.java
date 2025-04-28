/*
 * Class: This is the board class which is responsible drawing the board and board functionality of players
 * Authors: Musad Alam and Valor Buck
 * Date: 2/25/2025
 * Collaborators: Received help from Jack Brennan, Xandier Fermier, and Ivan Lopez-Rubio
 * Sources: StackOverflow, W3 Schools, and ChatGPT
 */
package clueGame;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import javax.swing.JOptionPane;

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
    private Set<Player> players;
    private Set<String> weapons;
    private Set<Card> cards;
    private Set<Card> cardHand;
    private Solution solution;
    private int  currIdx = -1;              
    private boolean humanTurn = false;     
    private Set<BoardCell> currTargets = new HashSet<>();
    private BoardPanel boardPanel;
    private final List<Player> playersInOrder = new ArrayList<>();



    private static Board theInstance = new Board();
    // constructor is private to ensure only one can be created
    private Board() {
        super();
    }

    public static Board getInstance() {
        return theInstance;
    }

    public void initialize() {
        players   = new HashSet<>();
        weapons   = new HashSet<>();
        cards     = new HashSet<>();
        cardHand  = new HashSet<>();
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
    			
        		String[] configInfo = readLine.split(",");
        		String cardName = "";
        		String cardType = "";
        		String humanOrComputer = "";
        		char symbolOnBoard = ' ';
        		int playerInitRow = 0;
        		int playerInitCol = 0;
        		String playerColor = "";
        		
        		if(configInfo.length == 2) {
        			// Make weapon card
        			cardName = configInfo[1].trim();
        			cardType = configInfo[0].trim();
        			
        		} else if(configInfo.length == 3) {
        			// Make Room Card
        			cardName = configInfo[1].trim();
        			cardType = configInfo[0].trim();
        			symbolOnBoard = configInfo[2].trim().charAt(0);
        			
        		}else if(configInfo.length == 6) {
        			// Make player Card
        			cardName = configInfo[1].trim();
        			cardType = configInfo[0].trim();
        			playerInitRow = Integer.parseInt(configInfo[2].trim());
        			playerInitCol = Integer.parseInt(configInfo[3].trim());
        			playerColor = configInfo[4].trim();
        			humanOrComputer = configInfo[5].trim();
        		}
        		
        		// Room
        		if(cardType.equals("Room")) {
        			roomMap.put(symbolOnBoard, new Room(cardName, symbolOnBoard, null, null));
        			cards.add(new Card(cardName, CardType.ROOM));

        		}
        		// Space
        		else if(cardType.equals("Space")) {
        			roomMap.put(symbolOnBoard, new Room(cardName, symbolOnBoard, null, null));
        		}
        		// Player
        		else if(cardType.equals("Player")) {
        			if("Human".equals(humanOrComputer)) {
        				players.add(new HumanPlayer(cardName, playerColor, playerInitRow, playerInitCol));
        			}else {
        				players.add(new ComputerPlayer(cardName, playerColor, playerInitRow, playerInitCol));
        			}
        			cards.add(new Card(cardName, CardType.PLAYER));
        		}
        		// Weapon
        		else if(cardType.equals("Weapon")) {
        			cards.add(new Card(cardName, CardType.WEAPON));
        			weapons.add(cardName);
        		}else {
        			myReader.close();
        			throw new BadConfigFormatException(symbolOnBoard);
        		}
    			
    		}
    		
    		myReader.close();

    	} catch(FileNotFoundException e) {
    		System.out.println(this.setupConfigFile + "could not be located");

    	}
        
    }
    
    /**
     * Parses the layout config file and initializes the board grid
     * Sets cell properties like rooms, doors, labels, and passages.
     * 
     * @throws FileNotFoundException if the file can't be found
     * @throws BadConfigFormatException if the layout is invalid
     */

    public void loadLayoutConfig() throws FileNotFoundException, BadConfigFormatException {
        int fileRows = 0;
        
        //validate structure of the layout file and determine dimensions
        try (Scanner myReader = new Scanner(new FileReader(this.layoutConfigFiles))) {
            while (myReader.hasNextLine()) {
                String readLine = myReader.nextLine().trim();
                
                //skip empty lines
                if (readLine.isEmpty()) continue; 
                
                String[] locationInfo = readLine.split(",");
                
                if (fileRows == 0) {
                //first row determines the number of columns
                    numColumns = locationInfo.length;
                } else if (locationInfo.length != numColumns) {  
                //all rows must have the same num of columns
                    throw new BadConfigFormatException("Inconsistent column count at: " + readLine);
                }
                ++fileRows;  
            }
        } catch (FileNotFoundException e) {
            System.out.println(this.layoutConfigFiles + " could not be located.");
        }

        //throw exception if file is empty or corrupted
        if (fileRows == 0 || numColumns == 0) {
            throw new BadConfigFormatException("Layout file is empty or improperly formatted.");
        }
        
        numRows = fileRows;
        //initialize grid
        grid = new BoardCell[numRows][numColumns]; 

        int rows = 0;
        
        //populate the grid with BoardCell objects based on layout tokens
        try (Scanner myReaderNew = new Scanner(new FileReader(this.layoutConfigFiles))) {
            while (myReaderNew.hasNextLine()) {
                String readLine = myReaderNew.nextLine().trim();
                //skip empty lines
                if (readLine.isEmpty()) continue;  
                
                String[] locationInfo = readLine.split(",");
                int cols = 0;  

                for (String token : locationInfo) {
                	
                //check if the token references a valid room symbol
                    if (roomMap.containsKey(token.charAt(0))) {
                        grid[rows][cols] = new BoardCell(rows, cols);
                        grid[rows][cols].setCellInitial(token.charAt(0));
                        
                //mark as a room if not walkway or room
                        if(grid[rows][cols].getCellInitial() != ('W') && grid[rows][cols].getCellInitial() != 'X'){
                        	grid[rows][cols].setIsRoom(true);
                        }
                        
               //handle second character features if exist
                        if (token.length() == 2) {
                            if (token.charAt(1) == '>') {
                                grid[rows][cols].setDoorDirection(DoorDirection.RIGHT);
                                grid[rows][cols].setDoorway(true);
                               
                            } else if (token.charAt(1) == '<') {
                                grid[rows][cols].setDoorDirection(DoorDirection.LEFT);
                                grid[rows][cols].setDoorway(true);
                               
                            } else if (token.charAt(1) == '^') {
                                grid[rows][cols].setDoorDirection(DoorDirection.UP);
                                grid[rows][cols].setDoorway(true);
                                
                                
                            } else if (token.charAt(1) == 'v') {
                                grid[rows][cols].setDoorDirection(DoorDirection.DOWN);
                                grid[rows][cols].setDoorway(true);
                                
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
                    	//not in the setup config
                        throw new BadConfigFormatException("Invalid token: " + token);
                    }
                    ++cols;
                }
                ++rows;
            }

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
    
    /**
     * Calculates all reachable target cells from a starting cell given a path length.
     * Initializes the visited and targets sets and calls the recursive helper.
     * 
     * @param someCell - the starting cell
     * @param lengthToPath - the number of steps allowed
     */
    public void calcTargets(BoardCell someCell, int lengthToPath)
    {
    	visited = new HashSet<>();
    	targets = new HashSet<>();
    	
    	//start from the current cell
    	visited.add(someCell);
    	//recursive call
    	findTargets(someCell, lengthToPath);
    }
    	
    /**
     * 	Recursively finds all target cells reachable from the current cell.
     * 
     * @param someCell - the current cell in the path
     * @param lengthToPath - remaining steps allowed
     */
    public void findTargets(BoardCell someCell, int lengthToPath) {
    	//loop through the adjacent cells
    	 for (BoardCell adjCell : getAdjList(someCell.getRow(), someCell.getColumn())) {
    		 //only consider cells that haven’t been visited and are either not occupied or are rooms
  			if(!visited.contains(adjCell) && (!adjCell.getIsOccupied() || adjCell.getIsRoom()) ) {
 				//mark cell as visited
  				visited.add(adjCell);
  				//add to targets if last step or room
 				if(lengthToPath == 1 || adjCell.getIsRoom()) {
 					targets.add(adjCell);
 					//recursively search subtracting one step
 				} else {
 					findTargets(adjCell, lengthToPath -1);
 				}
 				//unmark cell to allow for other paths
 				visited.remove(adjCell);
 			}
          }
	
	 }

    
    /**
     * Gets the set of adjacent cells (adjacency list) for a given cell on the board.
     * This accounts for walkways, doorways, and room centers with potential secret passages.
     * 
     * @param row - the row of the cell
     * @param col - the column of the cell
     * @return - a set of adjacent BoardCells
     */
    public Set<BoardCell> getAdjList(int row, int col) {
        Set<BoardCell> adjList = new HashSet<>();
        BoardCell cell = getCell(row, col);

        // In room center
        if (cell.isRoomCenter()) {
            // Secret Passage check
            char secret = cell.getSecretPassage();
            //if no direct secret passage, check other cells in same room
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
            //Add destination room center from secret passage, if found
            if (secret != '\0') {
                BoardCell destCenter = findRoomCenter(secret);
                if (destCenter != null) {
                    adjList.add(destCenter);
                }
            }
            //Add all doorways that lead into this room
            for (int r = 0; r < numRows; r++) {
                for (int c = 0; c < numColumns; c++) {
                    BoardCell potentialDoor = getCell(r, c);
                    if (potentialDoor.isDoorway()) {
                        int doorRow = potentialDoor.getRow();
                        int doorCol = potentialDoor.getColumn();
                        int insideRow = doorRow;
                        int insideCol = doorCol;
                        
                        //check which cell the door opens into
                        switch (potentialDoor.getDoorDirection()) {
                            case UP:    insideRow = doorRow - 1; break;
                            case DOWN:  insideRow = doorRow + 1; break;
                            case LEFT:  insideCol = doorCol - 1; break;
                            case RIGHT: insideCol = doorCol + 1; break;
                            default:    break;
                        }
                        
                        //If that adjacent cell is part of the same room, add the doorway
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
        		for (int r = 0; r < numRows; r++) {
        		    for (int c = 0; c < numColumns; c++) {
        		        BoardCell tempCell = getCell(r, c);
        		        if (tempCell.getCellInitial() == targetCellInitial && tempCell.isRoomCenter()) {
        		            adjList.add(tempCell);
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
        		doorCellRow = cell.getRow() -1;
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
     * @param r - int that represents row number
     * @param c - int that represents the column number
     * @return - true of false
     */
    private boolean isValid(int r, int c) {
        return r >= 0 && r < numRows && c >= 0 && c < numColumns;
    }

    /**
     * Helper method: Scans the grid for a room center cell with the given room initial.
     * @param - char that represents the initial of the room
     */
    private BoardCell findRoomCenter(char roomInitial) {
        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numColumns; c++) {
                BoardCell tempBoardCell = getCell(r, c);
                if (tempBoardCell.isRoomCenter() && tempBoardCell.getCellInitial() == roomInitial) {
                    return tempBoardCell;
                }
            }
        }
        return null;
    }

    /**
     * Chooses one room, one player, and one weapon card for the solution,
     * then deals the remaining cards round‑robin to every player.
     * 
     * (Everything after the second Collections.shuffle(deck) call is unchanged.)
     */
    public void dealCards() {

        List<Card> allPlayers = new ArrayList<>();
        List<Card> allWeapons = new ArrayList<>();
        List<Card> allRooms   = new ArrayList<>();

        for (Card card : cards) {
            if (card.getType() == CardType.ROOM) {
                allRooms.add(card);
            } else if (card.getType() == CardType.PLAYER) {
                allPlayers.add(card);
            } else {
                allWeapons.add(card);
            }
        }

        Collections.shuffle(allPlayers);
        Collections.shuffle(allWeapons);
        Collections.shuffle(allRooms);


        this.solution = new Solution(
                allRooms.get(allRooms.size()   - 1),
                allPlayers.get(allPlayers.size() - 1),
                allWeapons.get(allWeapons.size() - 1)
        );

        allRooms.remove(allRooms.size()     - 1);
        allPlayers.remove(allPlayers.size() - 1);
        allWeapons.remove(allWeapons.size() - 1);

        List<Card> deck = new ArrayList<>();
        deck.addAll(allRooms);
        deck.addAll(allPlayers);
        deck.addAll(allWeapons);
        Collections.shuffle(deck);

        for (Player player : players) {
            player.getCardsInHand().clear();
        }

        Player[] playerArray = players.toArray(new Player[0]);
        for (int i = 0; i < deck.size(); i++) {
            playerArray[i % playerArray.length].updateHand(deck.get(i));
        }
    }
    
    public boolean checkAccusation(Solution providedAccusation) {
    	
    	return getSolution().equals(providedAccusation);
    }
    
    
    public Card handleSuggestion(Player playerAccusing, Solution playerSuggestion) {
        List<Player> playersInOrder = new ArrayList<>(players);

        int startPlayerIdx = playersInOrder.indexOf(playerAccusing);
        int numPlayers = playersInOrder.size();
        
        
        for (int i = 1; i < numPlayers; i++) {
        	
            Player thePlayer = playersInOrder.get((startPlayerIdx + i) % numPlayers);
            Card cardDisproved = thePlayer.disproveSuggestion(playerSuggestion);
            if (cardDisproved != null) {
                return cardDisproved;
            }
        }
        return null;
    }
    
    private void movePlayerAccused(Solution providedSolution) {
    	Player accusedPlayer = null;
    	
    	for(Player player : players) {
    		if(player.getName().equalsIgnoreCase(providedSolution.getPerson().getName())) {
    			accusedPlayer = player;
    			break;
    		}
    	}
    	
    	Card roomCard = providedSolution.getRoom();
    	char roomChar = roomCard.getName().charAt(0);
    	Room roomOfJustice = getRoom(roomChar);
    	BoardCell centerSolutionRoom = roomOfJustice.getCenterCell();
    	
    	movePlayer(accusedPlayer, centerSolutionRoom);
    	accusedPlayer.setPulledMoved(true);
    	
    }
    
    Card processSuggestion(Player playerMakingSuggestion, Solution playerSuggestion, GameControlPanel panel, ClueCardsPanel cardsPanel) {

    	movePlayerAccused(playerSuggestion);
    	
    	Card shownCard = handleSuggestion(playerMakingSuggestion, playerSuggestion);
    	
    	System.out.println(">> processSuggestion: suggester="
    		    + playerMakingSuggestion.getName()
    		    + ", shownCard=" + shownCard);
    	
        if (playerMakingSuggestion instanceof HumanPlayer && shownCard != null) {
            ((HumanPlayer)playerMakingSuggestion).updateSeen(shownCard);
        }
    	
    	cardsPanel.refresh();
    	
    	panel.updateGuess(playerSuggestion.toString());
    	
    	if(shownCard == null) {
    		panel.updateGuessFeedback("No clue cards");
    	}else if(playerMakingSuggestion instanceof HumanPlayer) {
    		panel.updateGuessFeedback(shownCard.getName() + " provided by player: " + shownCard.getPlayerWhoShowedCard().getName());
    	}else {
    		panel.updateGuessFeedback("Suggestion disproved by " + shownCard.getPlayerWhoShowedCard().getName());
    	}
    	
    	return shownCard;
    }
    
    public void terminatePlayer(Player terminatedPlayer) {
    	playersInOrder.remove(terminatedPlayer);
    	
    	if (terminatedPlayer instanceof HumanPlayer) {
    		((HumanPlayer) terminatedPlayer).setEliminationStatus(true);
    	}
    }
    
    public void nextTurn(GameControlPanel panel, ClueCardsPanel cards) {
        if (humanTurn) {
            JOptionPane.showMessageDialog(panel,
                "Please select a move target first.",
                "Move required",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (targets != null) {
            for (BoardCell prevCell : targets) {
                prevCell.highlightCell(false);
            }
        }
        if (playersInOrder.isEmpty()) {
            playersInOrder.addAll(players);
            for (int i = 0; i < playersInOrder.size(); i++) {
                if (playersInOrder.get(i) instanceof HumanPlayer) {
                    Player human = playersInOrder.remove(i);
                    playersInOrder.add(0, human);
                    break;
                }
            }
        }
        Player currPlayer;
        do {
            currIdx = (currIdx + 1) % playersInOrder.size();
            currPlayer = playersInOrder.get(currIdx);
        } while (currPlayer.getEliminationStatus());
        int diceRoll = 1 + new Random().nextInt(6);
        panel.updateTurnInfo(currPlayer, diceRoll);
        BoardCell startCell = getCell(currPlayer.getRow(), currPlayer.getColumn());
        calcTargets(startCell, diceRoll);
        targets = getTargets();
        if (currPlayer instanceof HumanPlayer) {
            humanTurn = true;
            for (BoardCell targetCells : targets) {
                targetCells.highlightCell(true);
            }
            boardPanel.repaint();
            return;
        }
        ComputerPlayer computerPlayer = (ComputerPlayer) currPlayer;
        if (computerPlayer.canMakeAccusation()) {
            Solution accusation = computerPlayer.getCompPlayerAccusation();
            boolean correct = checkAccusation(accusation);
            String msg;
            if (correct) {
                msg = computerPlayer.getName() + " makes an accusation:\n"
                    + accusation.toString() + "\n\n"
                    + "That accusation is CORRECT! Game over.";
            } else {
                msg = computerPlayer.getName() + " makes an accusation:\n"
                    + accusation.toString() + "\n\n"
                    + "That accusation is INCORRECT! Game over.";
            }
            JOptionPane.showMessageDialog(panel, msg);
            System.exit(0);
        }
        BoardCell endCell = currPlayer.selectTarget(startCell, targets);
        movePlayer(currPlayer, endCell);
        if (endCell.isDoorway()) {
            computerPlayer.markVisitedRoom(endCell.getCellInitial());
        }
        for (BoardCell targetCells : targets) {
            targetCells.highlightCell(false);
        }
        boardPanel.repaint();
        if (endCell.getIsRoom()) {
            Room suggestionRoom = getRoom(endCell.getCellInitial());
            Solution computerSuggestion = computerPlayer.computerCreatedSuggestion(suggestionRoom);
            Card shownCard = processSuggestion(computerPlayer, computerSuggestion, panel, cards);
            boolean noDisaprovesProvided = (shownCard == null);
            boolean computerHasRoomCard = false;
            for (Card card : computerPlayer.getCardsInHand()) {
                if (card.equals(computerSuggestion.getRoom())) {
                    computerHasRoomCard = true;
                    break;
                }
            }
            if (noDisaprovesProvided && !computerHasRoomCard) {
                computerPlayer.setCompPlayerAccusationReady(true);
            } else {
                computerPlayer.setCompPlayerAccusationReady(false);
            }
            computerPlayer.setCompPlayerAccusation(computerSuggestion);
        }
    }

    
    public boolean getHumanTurn() {
    	return humanTurn;
    }
    
    public void movePlayer(Player player, BoardCell finalCell) {

        BoardCell start = getCell(player.getRow(), player.getColumn());
        start.setOccupied(false);
        

        BoardCell target = finalCell;

        if (finalCell.isDoorway() && !start.getIsRoom()) {
            Room room = getRoom(finalCell.getCellInitial());
            
            BoardCell centerOfRoom = room.getCenterCell();

        }

        target.setOccupied(true);
        player.setLocation(target.getRow(), target.getColumn());
        
        player.setPulledMoved(false);
    }
    
  
    public void boardClick(BoardCell clickedCell) {

        if (!humanTurn) {
        	return;
        }

        BoardCell finalCell = clickedCell;
        
        if (clickedCell.isDoorway()) { 
            Room room = getRoom(clickedCell.getCellInitial());
            if (room.getCenterCell() != null) {
            	finalCell = room.getCenterCell();
            }
        }

        if (targets == null || !targets.contains(finalCell)) {
            JOptionPane.showMessageDialog(boardPanel, "Please click one of the highlighted cells", "Invalid Cell Click", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Player humanPlayer = null;
        for (Player player : players) {
            if (player instanceof HumanPlayer) { 
            	humanPlayer = player; break; 
            }
        }

        movePlayer(humanPlayer, clickedCell);

        for (BoardCell targetCells : targets) {
        	targetCells.highlightCell(false);
        }
        boardPanel.repaint();
        humanTurn = false;
    }

    public void setBoardPanel(BoardPanel boardPanel) {
    	this.boardPanel = boardPanel;
    }
    
    public Solution getSolution() {
    	return solution;
    }
    
	public Set<Card> getCards() {
		return cards;
	}
    
	public Set<Card> getPlayerCards() {
		return cardHand;
	}
	
	public Set<String> getWeapons() {
		return weapons;
	}

    public Set<BoardCell> getTargets() {
    	return targets;
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
    
    public Set<Player> getPlayers() {
    	return players;
    }
    
    public void setSolution(Card forceRoom, Card forcePerson, Card forceWeapon) {
    	solution = new Solution(forceRoom, forcePerson, forceWeapon);
    }
    
    


}
