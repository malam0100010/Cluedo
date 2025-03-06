package tests;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.*;

public class FileInitTest {
    // Consts used to test file loading
    public static final int LEGEND_SIZE = 10;
	public static final int NUM_ROWS = 25;
	public static final int NUM_COLUMNS = 20;

    private static Board board;

    
	@BeforeAll
	public static void setUp() 
    {
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("Clue_Layout_Original.csv", "ClueSetup306Original.txt");
		// Initialize will load BOTH config files
		board.initialize();
	}

    @Test
	public void testRoomLabels() {
		// To ensure data is correctly loaded, test retrieving a few rooms
		// from the hash, including the first and last in the file and a few others
		assertEquals("Kit Room", board.getRoom('K').getName() );
		assertEquals("LockerRoom", board.getRoom('L').getName() );
		assertEquals("Manchester Suite", board.getRoom('M').getName() );
		//assertEquals("Benches", board.getRoom('Bn').getName() );
		assertEquals("Bathroom", board.getRoom('B').getName() );
        assertEquals("Concssion Stand", board.getRoom('C').getName() );
        assertEquals("Ticket Booth", board.getRoom('T').getName() );
        assertEquals("Indoor Arcade", board.getRoom('I').getName() );
        assertEquals("Concssion Stand", board.getRoom('C').getName() );
	}

    @Test
	public void testBoardDimensions() {
		// Ensure we have the proper number of rows and columns
		assertEquals(NUM_ROWS, board.getNumRows());
		assertEquals(NUM_COLUMNS, board.getNumColumns());
	}

    @Test
	public void testDoorDirections() 
    {
		BoardCell cell = board.getCell(3, 6);
		assertTrue(cell.isDoorway());
		assertEquals(DoorDirection.LEFT, cell.getDoorDirection());
		cell = board.getCell(8, 2);
		assertTrue(cell.isDoorway());
		assertEquals(DoorDirection.DOWN, cell.getDoorDirection());
		cell = board.getCell(8, 2);
		assertTrue(cell.isDoorway());
		assertEquals(DoorDirection.RIGHT, cell.getDoorDirection());
		cell = board.getCell(20, 14);
		assertTrue(cell.isDoorway());
		assertEquals(DoorDirection.UP, cell.getDoorDirection());
		// Test that walkways are not doors
		cell = board.getCell(19, 23);
		assertFalse(cell.isDoorway());
	}

    



}
