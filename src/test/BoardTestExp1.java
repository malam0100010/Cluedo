package test;

import experiment.TestBoard;
import experiment.TestBoardCell;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Set;

class BoardTestExp1 {

    private TestBoard board;

    @BeforeEach
    void setUp() {
        board = new TestBoard();
    }

    @Test
    public void testTargetNormal() {
        TestBoardCell cell = board.getCell(0, 0);
        board.calcTargets(cell, 3);
        Set<TestBoardCell> targets = board.getTargets();  // Fixed type
        
        Assertions.assertEquals(6, targets.size());  // Fixed method
        Assertions.assertTrue(targets.contains(board.getCell(3, 0)));
        Assertions.assertTrue(targets.contains(board.getCell(2, 1)));
        Assertions.assertTrue(targets.contains(board.getCell(0, 1)));
        Assertions.assertTrue(targets.contains(board.getCell(0, 3)));
        Assertions.assertTrue(targets.contains(board.getCell(1, 0)));
    }

    @Test
    public void testTargetsMixed() {
        // Set up occupied and room cells
        board.getCell(0, 2).setOccupied(true);
        board.getCell(1, 2).setRoom(true);  // Fixed method

        TestBoardCell cell = board.getCell(0, 3);
        board.calcTargets(cell, 3);
        Set<TestBoardCell> targets = board.getTargets();
        
        Assertions.assertEquals(3, targets.size());  // Fixed method
        Assertions.assertTrue(targets.contains(board.getCell(1, 2)));
        Assertions.assertTrue(targets.contains(board.getCell(2, 2)));
        Assertions.assertTrue(targets.contains(board.getCell(3, 3)));
    }
}