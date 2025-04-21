package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.BoardCell;
import clueGame.Card;
import clueGame.CardType;
import clueGame.ComputerPlayer;
import clueGame.HumanPlayer;
import clueGame.Player;
import clueGame.Room;
import clueGame.Solution;

public class GameSolutionTest {
	
	//constants
	private static Board board;

	@BeforeAll
	public static void setUp() {
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("./data/Clue_Layout.csv", "./data/ClueSetup.txt");		
		// Initialize will load config files 
		board.initialize();
		
		board.dealCards();
		
	}
	
	@Test
	void testAccusation() {
		//	    solution that is correct		
		Card rightSolPerson = new Card("Fred the Red", CardType.PLAYER);
		Card rightSolWeapon = new Card("Wistle", CardType.WEAPON);
		Card rightSolRoom = new Card("Kit Room", CardType.ROOM);
		
		board.setSolution(rightSolRoom, rightSolPerson, rightSolWeapon);
		Solution rightAccusation = new Solution(rightSolRoom, rightSolPerson, rightSolWeapon);
		assertTrue(board.checkAccusation(rightAccusation));
				
		//	    solution with wrong person
		Card wrongSolPerson = new Card("Alexander Isak", CardType.PLAYER);
		Solution wrongPersonAccusation = new Solution(rightSolRoom, wrongSolPerson, rightSolWeapon);
		assertFalse(board.checkAccusation(wrongPersonAccusation));
		
		//	    solution with wrong weapon
		Card wrongSolWeapon = new Card("Broken Pint", CardType.WEAPON);
		Solution wrongWeaponAccusation = new Solution(rightSolRoom, rightSolPerson, wrongSolWeapon);
		assertFalse(board.checkAccusation(wrongWeaponAccusation));
		
		//	    solution with wrong room
		Card wrongSolRoom = new Card("Press Box", CardType.ROOM);
		Solution wrongRoomAccusation = new Solution(wrongSolRoom, rightSolPerson, rightSolWeapon);
		assertFalse(board.checkAccusation(wrongRoomAccusation));
		
	}
	
	@Test
	void testPlayerDissproval() {
	    HumanPlayer playerCharacter = new HumanPlayer("Bruno ", "Blue", 0, 0);
	    Card correctPerson    = new Card("Alexander Isak", CardType.PLAYER);
	    Card correctWeapon    = new Card("Broken Pint", CardType.WEAPON);
	    
	    Card wrongRoom   = new Card("Kit Room", CardType.ROOM);
	    Card wrongPerson = new Card("Fred the Red", CardType.PLAYER);
	    Card wrongWeapon = new Card("Whistle", CardType.WEAPON);

	 
	    playerCharacter.updateHand(correctPerson);
	    playerCharacter.updateHand(correctWeapon);

	    Solution oneMatch = new Solution(wrongRoom, correctPerson, wrongWeapon);
	    Card singleCardCorrect = playerCharacter.disproveSuggestion(oneMatch);
	    assertEquals(correctPerson, singleCardCorrect);

	    
	    Solution noMatchesFound = new Solution(wrongRoom, wrongPerson, wrongWeapon);
	    Card noCardCorrect = playerCharacter.disproveSuggestion(noMatchesFound);
	    assertNull(noCardCorrect);

	    
	    Solution twoCorrectMatches = new Solution(wrongRoom, correctPerson, correctWeapon);
	    Card twoCardsCorrect = playerCharacter.disproveSuggestion(twoCorrectMatches);
	    
	    assertTrue(twoCardsCorrect.equals(correctPerson) || twoCardsCorrect.equals(correctWeapon));
	}
	
	@Test
	void testHandleSuggestion() {
	    Card suggestedRoom  = new Card("Kit Room",   CardType.ROOM);
	    Card suggestedPerson = new Card("Bruno Fernandez", CardType.PLAYER);
	    Card suggestedWeapon = new Card("Whistle", CardType.WEAPON);

	    // Suggestion no one can disprove returns null
	    board.getPlayers().clear();
	    Player playerMakingAccusation0 = new HumanPlayer("Fred the Red","Red",0,0);
	    Player humanPlayer0   = new HumanPlayer("Bruno Fernandez","Blue",1,1);
	    Player computerPlayer0 = new ComputerPlayer("Alejandro Garnacho","Yellow",2,2);
	    board.getPlayers().add(playerMakingAccusation0);
	    board.getPlayers().add(humanPlayer0);
	    board.getPlayers().add(computerPlayer0);

	    Solution firstReqSol = new Solution(suggestedRoom, suggestedPerson, suggestedWeapon);
	    assertNull(board.handleSuggestion(playerMakingAccusation0, firstReqSol));

	    // Suggestion only suggesting player can disprove returns null
	    board.getPlayers().clear();
	    Player playerMakingAccusation1 = new HumanPlayer("Fred the Red","Red",0,0);
	    Player humanPlayer1   = new HumanPlayer("Bruno Fernandez","Blue",1,1);
	    Player computerPlayer1 = new ComputerPlayer("Alejandro Garnacho","Yellow",2,2);
	    board.getPlayers().add(playerMakingAccusation1);
	    board.getPlayers().add(humanPlayer1);
	    board.getPlayers().add(computerPlayer1);

	
	    playerMakingAccusation1.updateHand(suggestedPerson);
	    Solution secondReqSol = new Solution(suggestedRoom, suggestedPerson, suggestedWeapon);
	    assertNull(board.handleSuggestion(playerMakingAccusation1, secondReqSol));

	    // Suggestion only human can disprove returns answer (i.e., card that disproves suggestion)
	    board.getPlayers().clear();
	    Player playerMakingAccusation2 = new HumanPlayer("Fred the Red","Red",0,0);
	    Player humanPlayer2 = new HumanPlayer("Bruno Fernandez","Blue",1,1);
	    Player computerPlayer2 = new ComputerPlayer("Alejandro Garnacho","Yellow",2,2);
	    board.getPlayers().add(playerMakingAccusation2);
	    board.getPlayers().add(humanPlayer2);
	    board.getPlayers().add(computerPlayer2);

	    humanPlayer2.updateHand(suggestedWeapon);
	    Solution thirdReqSol = new Solution(suggestedRoom, suggestedPerson, suggestedWeapon);
	    Card disprovedByHuman = board.handleSuggestion(playerMakingAccusation2, thirdReqSol);
	    assertEquals(suggestedWeapon, disprovedByHuman);

	    // Suggestion that two players can disprove, correct player (based on starting with next player in list) returns answer
	    board.getPlayers().clear();
	    Player playerMakingAccusation3 = new HumanPlayer("Fred the Red","Red",0,0);
	    Player humanPlayer3 = new HumanPlayer("Bruno Fernandez","Blue",1,1);
	    Player computerPlayer3 = new ComputerPlayer("Alejandro Garnacho","Yellow",2,2);
	    board.getPlayers().add(playerMakingAccusation3);
	    board.getPlayers().add(humanPlayer3);
	    board.getPlayers().add(computerPlayer3);

	    humanPlayer3.updateHand(suggestedWeapon);
	    computerPlayer3.updateHand(suggestedPerson);
	    Solution fourthReqSol = new Solution(suggestedRoom, suggestedPerson, suggestedWeapon);
	    Card firstDisprove = board.handleSuggestion(playerMakingAccusation3, fourthReqSol);
	    assertEquals(suggestedWeapon, firstDisprove);
		
	}
	
	@Test
	void testComputerSuggestion() {
	    Set<Card> completeDeck = board.getCards();

	    List<Card> roomsList   = new ArrayList<>();
	    List<Card> playersList = new ArrayList<>();
	    List<Card> weaponsList = new ArrayList<>();
	    
	    for (Card card : completeDeck) {
	        switch (card.getType()) {
	            case ROOM:   roomsList.add(card);   break;
	            case PLAYER: playersList.add(card); break;
	            case WEAPON: weaponsList.add(card); break;
	        }
	    }

	    Card testRoom = roomsList.get(0);
	    Card miscPerson = new Card("Fred the Red", CardType.PLAYER);
	    Card miscWeapon = new Card("Whistle", CardType.WEAPON);
	    
	    Solution computerPlayerSuggestion = new Solution(testRoom, miscPerson, miscWeapon);

	    // Room matches current location
	    ComputerPlayer computerPlayer1 = new ComputerPlayer("Alexander Isak","White", 0, 0);
	    Solution roomCorrect = computerPlayer1.createSuggestion(computerPlayerSuggestion);
	    assertEquals(testRoom, roomCorrect.getRoom());

	    // If only one weapon not seen, it's selected
	    ComputerPlayer computerPlayer2 = new ComputerPlayer("Alexander Isak","White", 0, 0);
	    Card weaponSuggestion = weaponsList.get(0);
	    
	    
	    for (int i = 1; i < weaponsList.size(); ++i) {
	        computerPlayer2.updateSeen(weaponsList.get(i));
	    }
	    
	    Solution weaponCorrect = computerPlayer2.createSuggestion(computerPlayerSuggestion);
	    assertEquals(weaponSuggestion, weaponCorrect.getWeapon());


	    // If multiple persons not seen, one of them is randomly selected
	    ComputerPlayer computerPlayer3 = new ComputerPlayer("Alexander Isak","White", 0, 0);
	    Solution randSelect = computerPlayer3.createSuggestion(computerPlayerSuggestion);
	    assertTrue(weaponsList.contains(randSelect.getWeapon()));
	    assertTrue(playersList.contains(randSelect.getPerson()));
	}

	
	@Test
	void testComputerSelectTarget() {
	    ComputerPlayer computerPlayer = new ComputerPlayer("Alejandro Garnacho", "Green", 0, 0);

	    // if no rooms in list, select randomly
	    Set<BoardCell> miscTargets = new HashSet<>();
	    int minTargetsRequired = 2;
	    for (int row = 0; row < board.getNumRows() && miscTargets.size() < minTargetsRequired; row++) {
	        for (int col = 0; col < board.getNumColumns() && miscTargets.size() < minTargetsRequired; col++) {
	            BoardCell cell = board.getCell(row, col);
	            if (cell.getCellInitial() == 'W' && !cell.isDoorway()) {
	                miscTargets.add(cell);
	            }
	        }
	    }
	    assertEquals(2, miscTargets.size());

	    BoardCell targetChoice1 = computerPlayer.selectTarget(miscTargets);
	    assertTrue(miscTargets.contains(targetChoice1));

	    // if room in list that has not been seen, select it
	    BoardCell doorCell = null;
	    
	    for (int row = 0; row < board.getNumRows(); ++row) {
	        for (int col = 0; col < board.getNumColumns(); ++col) {
	            BoardCell cell = board.getCell(row, col);
	            if (cell.isDoorway()) {
	                doorCell = cell;
	                break;
	            }
	        }
	        if (doorCell != null) {
	        	break;
	        }
	    }
	    assertNotNull(doorCell);

	    BoardCell walkwayCell = miscTargets.iterator().next();
	    Set<BoardCell> targetsWithDoor = Set.of(walkwayCell, doorCell);

	    BoardCell targetChoice2 = computerPlayer.selectTarget(targetsWithDoor);
	    assertEquals(doorCell, targetChoice2);

	    // if room in list that has been seen, each target (including room) selected randomly
	    int row = doorCell.getRow();
	    int col = doorCell.getColumn();
	    switch (doorCell.getDoorDirection()) {
	        case UP:    --row; break;
	        case DOWN:  ++row; break;
	        case LEFT:  --col; break;
	        case RIGHT: ++col; break;
	    }
	    
	    BoardCell someRoom = board.getCell(row, col);
	    Room miscRoom = board.getRoom(someRoom.getCellInitial());
	    computerPlayer.updateSeen(new Card(miscRoom.getName(), CardType.ROOM));

	    BoardCell targetChoice3 = computerPlayer.selectTarget(targetsWithDoor);
	    assertTrue(targetsWithDoor.contains(targetChoice3));
	}

}
