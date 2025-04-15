package tests;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.*;

class GameSetupTest {
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
	}
	
	@Test
	void testLoadCharacters() {
		Set<Player> players = board.getPlayers();
		//confirm 6 players
        assertEquals(6, players.size());
        //confirm 1 human and 5 computer players
        //assertEquals(1, humanCount);
        //assertEquals(5, computerCount);
        //confirm certain players are players
        assertTrue(players.contains(""));
		assertTrue(players.contains(""));
	}
	
	@Test
	void testPlayerAttributes() {
		
		
	}
	
	@Test
	void testCardsInDeck() {
		Set<Card> cards = board.getCards();
		assertEquals()
		
	}
	
	@Test
	void testPlayersCards() {
		
	}
	
	@Test
	void testSolution() {
		
	}

}
