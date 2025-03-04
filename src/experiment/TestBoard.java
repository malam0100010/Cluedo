package experiment;

import java.util.HashSet;
import java.util.Set;

public class TestBoard {
    private TestBoardCell[][] grid;
    private Set<TestBoardCell> targets;
    private Set<TestBoardCell> visited;
    final static int COLS = 4;
    final static int ROWS = 4;


    public TestBoard() {
    	grid = new TestBoardCell[ROWS][COLS];
    	targets = new HashSet<>();
    	visited = new HashSet<>();
    	
    	//build grid
    	for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                grid[row][col] = new TestBoardCell(row, col, false, false);
            }
        }
    	
    	//create adjacency list
    	for (int row = 0; row < ROWS; row++) {
    		for (int col = 0; col < COLS; col++) {
    			TestBoardCell cell = grid[row][col];
    			
    			if (row > 0) {
    				cell.addAdjacency(grid[row - 1][col]);
    			}
    			if (row < ROWS - 1) {
    				cell.addAdjacency(grid[row + 1][col]);
    			}
    			if (col > 0) {
    				cell.addAdjacency(grid[row][col - 1]);
    			}
    			if (col < COLS - 1) {
    				cell.addAdjacency(grid[row][col + 1]);
    			}
    		}
    	}
    }

    public void calcTargets( TestBoardCell startCell, int pathLength) {
        targets.clear();
        visited.clear();
        
        //recursive
        findTargets(startCell, pathLength);
        
    }

    public TestBoardCell getCell(int row, int col) {
        return grid[row][col];
    }

    public Set<TestBoardCell> getTargets() {
        return targets;
    }
    
    //recursive function
    private void findTargets(TestBoardCell currentCell, int remainingSteps) {
    	visited.add(currentCell);
    	for (TestBoardCell adjCell : currentCell.getAdjList()) {
    		if (visited.contains(adjCell)) {
    			continue;
    		}
    		
    		if (adjCell.getOccupied()) {
    			continue;
    		}
    		
    		if (adjCell.isRoom() && remainingSteps > 1) {
    			continue;
    		}
    		visited.add(adjCell);
    		
    		if (remainingSteps == 1 || adjCell.isRoom()) {
    			targets.add(adjCell);
    		}
    		
    		else {
    			findTargets(adjCell, remainingSteps - 1);
    		}
    		
    		visited.remove(adjCell);
    	}
    }

}
