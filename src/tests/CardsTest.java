package tests;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.*;

class CardsTest {
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
        assertEquals(6, players.size());
	}
	
	@Test
	void testPlayerAttributes() {
		
	}
	
	@Test
	void testCardsLoaded() {
		
	}
	
	@Test
	void testPlayersCards() {
		
	}
	
	@Test
	void testSolution() {
		
	}

}
