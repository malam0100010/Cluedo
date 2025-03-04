package experiment;

import java.util.HashSet;
import java.util.Set;

public class TestBoard 
{
	private static final int SIZE = 4;
    private TestBoardCell[][] grid = new TestBoardCell[SIZE][SIZE];
    Set<TestBoardCell> targets = new HashSet<>();
    private Set<TestBoardCell> visited;
    final static int COLS = 4;
    final static int ROWS = 4;


    public TestBoard()
    {
    	for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                grid[row][col] = new TestBoardCell(row, col);
            }
        }
    }

    public void calcTargets( TestBoardCell startCell, int pathLength)
    {
        targets.clear();
    }

    public TestBoardCell getCell(int row, int col)
    {
        return grid[row][col];
    }

    public Set<TestBoardCell> getTargets()
    {
        return targets;
    }

}
