package test;
import experiment.*;


import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import experiment.TestBoard;


public class BoardTestsExp {
TestBoard board;

@Before
public void setUp() {
board = new TestBoard();
}

@Test
public void testAdjacency() {
	TestBoardCell cell = board.getCell(1,1);
	Set<TestBoardCell> testList = cell.getAdjList();
	Assertions.assertTrue(testList.contains(board.getCell(0,1)));
	Assertions.assertTrue(testList.contains(board.getCell(1,0)));
	Assertions.assertTrue(testList.contains(board.getCell(2,1)));
	Assertions.assertTrue(testList.contains(board.getCell(1,2)));
	Assertions.assertEquals(4, testList.size());
}

@Test
public void testTargetsNormal() {
	TestBoardCell cell = board.getCell(0,0);
	board.calcTargets(cell, 3);
	Set<TestBoardCell> targets = board.getTargets();
	Assertions.assertEquals(6, targets.size());
	Assertions.assertTrue(targets.contains(board.getCell(3,0)));
	Assertions.assertTrue(targets.contains(board.getCell(2,1)));
	Assertions.assertTrue(targets.contains(board.getCell(0,1)));
	Assertions.assertTrue(targets.contains(board.getCell(1,2)));
	Assertions.assertTrue(targets.contains(board.getCell(0,3)));
	Assertions.assertTrue(targets.contains(board.getCell(1,0)));
}

// @Test
public void testTargetsRoom() {
	TestBoardCell cell = board.getCell(0,0);
	board.getCell(0,1).setRoom(true);
	Set<TestBoardCell> targets = board.getTargets();
	Assertions.assertEquals(2, targets.size());
	Assertions.assertTrue(targets.contains(board.getCell(0, 1)));

}

// @Test
public void testTargetsMixed() {
	board.getCell(0,2).setOccupied(true);
	board.getCell(1,2).setRoom(true);
	TestBoardCell cell = board.getCell(0,3);
	board.calcTargets(cell, 3);
	Set<TestBoardCell> targets = board.getTargets();
	Assertions.assertEquals(3, targets.size());
	Assertions.assertTrue(targets.contains(board.getCell(1,2)));
	Assertions.assertTrue(targets.contains(board.getCell(2,2)));
	Assertions.assertTrue(targets.contains(board.getCell(3,3)));

}

// @Test
public void testTargetsOccupied() {
	board.getCell(0,2).setOccupied(true);
	board.getCell(1,2).setRoom(true);
	TestBoardCell cell = board.getCell(0,3);
	board.calcTargets(cell, 3);
	Set<TestBoardCell> targets = board.getTargets();
	Assertions.assertEquals(3, targets.size());
	Assertions.assertTrue(targets.contains(board.getCell(1,2)));
	Assertions.assertTrue(targets.contains(board.getCell(2,2)));
	Assertions.assertTrue(targets.contains(board.getCell(3,3)));

}

}