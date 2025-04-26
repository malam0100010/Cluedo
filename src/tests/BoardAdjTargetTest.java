package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.BoardCell;

public class BoardAdjTargetTest {
	// We make the Board static because we can load it one time and 
	// then do all the tests. 
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

	// Ensure that player does not move around within room
	// These cells are LIGHT ORANGE on the planning spreadsheet
	@Test
	public void testAdjacenciesRooms()
	{
		// Concession stand test
		Set<BoardCell> testList = board.getAdjList(7, 2);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCell(10, 6)));
		assertTrue(testList.contains(board.getCell(18, 21)));
		assertTrue(testList.contains(board.getCell(3, 6)));
		
		// Locker room
		testList = board.getAdjList(18, 21);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCell(23, 19)));
		assertTrue(testList.contains(board.getCell(14, 20)));
		assertTrue(testList.contains(board.getCell(7, 2)));
		
	}

	
	// Ensure door locations include their rooms and also additional walkways
	@Test
	public void testAdjacencyDoor()
	{
		// Locker room door opening RIGHT with 3 adj
		Set<BoardCell> testList = board.getAdjList(14, 20);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCell(14,19)));
		assertTrue(testList.contains(board.getCell(13, 20)));
		assertTrue(testList.contains(board.getCell(18, 21)));

		// Press box door opening DOWN with 4 adj
		testList = board.getAdjList(20, 17);
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCell(20, 16)));
		assertTrue(testList.contains(board.getCell(20, 18)));
		assertTrue(testList.contains(board.getCell(23, 11)));
		assertTrue(testList.contains(board.getCell(19, 17)));
		

	}
	
	// Test a variety of walkway scenarios
	@Test
	public void testAdjacencyWalkways()
	{
		// Test on bottom edge of board, just one walkway piece
		Set<BoardCell> testList = board.getAdjList(24, 23);
		assertEquals(1, testList.size());
		assertTrue(testList.contains(board.getCell(24, 22)));
		
		// Test near a door but not adjacent
		testList = board.getAdjList(1, 6);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCell(1, 7)));
		assertTrue(testList.contains(board.getCell(1, 5)));
		assertTrue(testList.contains(board.getCell(2, 6)));

		// Test adjacent to walkways
		testList = board.getAdjList(19, 6);
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCell(19, 5)));
		assertTrue(testList.contains(board.getCell(19, 7)));
		assertTrue(testList.contains(board.getCell(18, 6)));
		assertTrue(testList.contains(board.getCell(20, 6)));

	
	}
	
	@Test
	public void testTargetsInBathroom() {
		// test a roll of 1 at B
		board.calcTargets(board.getCell(24, 2), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(2, targets.size());
		assertTrue(targets.contains(board.getCell(26, 5)));
		assertTrue(targets.contains(board.getCell(2, 19)));	
		
		// test a roll of 3
		board.calcTargets(board.getCell(24, 2), 3);
		targets= board.getTargets();
		assertEquals(5, targets.size());
		assertTrue(targets.contains(board.getCell(25, 6)));
		assertTrue(targets.contains(board.getCell(26, 7)));	
		assertTrue(targets.contains(board.getCell(24, 5)));
		
		// test a roll of 4
		board.calcTargets(board.getCell(24, 2), 4);
		targets= board.getTargets();
		assertEquals(9, targets.size());
		assertTrue(targets.contains(board.getCell(23, 5)));
		assertTrue(targets.contains(board.getCell(25, 7)));	
		assertTrue(targets.contains(board.getCell(27, 5)));
	}

	//Test targets at door with rolls of: 1, 3 and 4
	@Test
	public void testTargetsAtDoor() {
		// test a roll of 1, at door
		board.calcTargets(board.getCell(3, 6), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCell(3, 7)));
		assertTrue(targets.contains(board.getCell(2, 6)));	
		assertTrue(targets.contains(board.getCell(4, 6)));	
		
		// test a roll of 3
		board.calcTargets(board.getCell(3, 6), 3);
		targets= board.getTargets();
		assertEquals(10, targets.size());
		assertTrue(targets.contains(board.getCell(4, 6)));
		assertTrue(targets.contains(board.getCell(2, 8)));
		assertTrue(targets.contains(board.getCell(3, 7)));	
		assertTrue(targets.contains(board.getCell(2, 6)));
		assertTrue(targets.contains(board.getCell(1, 5)));	
		
		// test a roll of 4
		board.calcTargets(board.getCell(3, 6), 4);
		targets= board.getTargets();
		assertEquals(12, targets.size());
		assertTrue(targets.contains(board.getCell(1, 6)));
		assertTrue(targets.contains(board.getCell(2, 7)));
		assertTrue(targets.contains(board.getCell(4, 7)));	
		assertTrue(targets.contains(board.getCell(2, 3)));
	}



	@Test
	// test to make sure occupied locations do not cause problems
	public void testTargetsOccupied() {
		// roll 4 test
		board.getCell(1, 4).setOccupied(true);
		board.calcTargets(board.getCell(8, 7), 4);
		board.getCell(1, 4).setOccupied(false);
		Set<BoardCell> targets = board.getTargets();
		assertEquals(16, targets.size());
		assertTrue(targets.contains(board.getCell(8, 11)));
		assertTrue(targets.contains(board.getCell(4, 7)));
		assertTrue(targets.contains(board.getCell(9, 6)));	

	
		// we want to make sure we can get into a room, even if flagged as occupied
		board.getCell(7, 2).setOccupied(true);
		board.getCell(3, 6).setOccupied(true);
		board.calcTargets(board.getCell(2, 6), 1);
		board.getCell(7, 2).setOccupied(false);
		board.getCell(2, 2).setOccupied(false);
		targets= board.getTargets();
		assertEquals(3, targets.size());
		assertTrue(targets.contains(board.getCell(2, 7)));	
		assertTrue(targets.contains(board.getCell(1, 6)));	
		assertTrue(targets.contains(board.getCell(2, 5)));	
		
		// check leaving a room with a blocked doorway
		board.getCell(14, 20).setOccupied(true);
		board.calcTargets(board.getCell(24, 21), 3);
		board.getCell(14, 20).setOccupied(false);
		targets= board.getTargets();
		assertEquals(7, targets.size());
		assertTrue(targets.contains(board.getCell(24, 22)));
		assertTrue(targets.contains(board.getCell(24, 18)));	
		assertTrue(targets.contains(board.getCell(26, 20)));

	}
}
