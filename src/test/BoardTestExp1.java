package test;

import experiment.TestBoard;
import experiment.TestBoardCell;
//import junit.framework.Assert;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Set;

class BoardTestExp1 {

    private TestBoard grid;

    @BeforeEach
    void setUp() {
        grid = new TestBoard();
    }

    @Test
    public void testTargetNormal() {
        TestBoardCell cell = grid.getCell(0, 0);
        grid.calcTargets(cell, 3);
        Set<TestBoardCell> targets = grid.getTargets();  // Fixed type
        
        Assertions.assertEquals(6, targets.size());  // Fixed method
        Assertions.assertTrue(targets.contains(grid.getCell(3, 0)));
        Assertions.assertTrue(targets.contains(grid.getCell(2, 1)));
        Assertions.assertTrue(targets.contains(grid.getCell(0, 1)));
        Assertions.assertTrue(targets.contains(grid.getCell(0, 3)));
        Assertions.assertTrue(targets.contains(grid.getCell(1, 0)));
    }

    @Test
    public void testTargetsMixed() {
        // Set up occupied and room cells
        grid.getCell(0, 2).setOccupied(true);
        grid.getCell(1, 2).setRoom(true);  // Fixed method

        TestBoardCell cell = grid.getCell(0, 3);
        grid.calcTargets(cell, 3);
        Set<TestBoardCell> targets = grid.getTargets();
        
        Assertions.assertEquals(3, targets.size());  // Fixed method
        Assertions.assertTrue(targets.contains(grid.getCell(1, 2)));
        Assertions.assertTrue(targets.contains(grid.getCell(2, 2)));
        Assertions.assertTrue(targets.contains(grid.getCell(3, 3)));
    }
    
    @Test
    public void testAdjacency() 
    {
    	TestBoardCell cell = grid.getCell(0,0);
    	Set<TestBoardCell> targets = cell.getAdjList();
    	Assertions.assertEquals(2, targets.size());
    	Assertions.assertTrue(targets.contains(grid.getCell(1, 0)));
    	Assertions.assertTrue(targets.contains(grid.getCell(0, 1)));
    }
    
    @Test
    public void testTargetIsRoom() 
    {
    	grid.getCell(1,  1).setRoom(true);
    	grid.calcTargets(grid.getCell(0, 0), 2);
    	Set<TestBoardCell> targets = grid.getTargets();
    	Assertions.assertEquals(1, targets.size());
    	Assertions.assertTrue(targets.contains(grid.getCell(1, 1)));
    }
    
    @Test
    public void testTargetIsOccupied() 
    {
    	grid.getCell(1, 1).setOccupied(true);
        grid.calcTargets(grid.getCell(0, 0), 2);
        Set<TestBoardCell> targets = grid.getTargets();
        Assertions.assertFalse(targets.contains(grid.getCell(1, 1)));
        Assertions.assertTrue(targets.size() < 0);
    }
}