package experiment;

import java.util.Set;

public class TestBoard 
{
    private TestBoardCell[][] grid;
    final static int COLS = 4;
    final static int ROWS = 4;
    private Set<TestBoardCell> visited; 
    private Set<TestBoardCell> targets;

    TestBoard()
    {

    }

    void calcTargets( TestBoardCell startCell, int pathlength)
    {
        return;
    }

    TestBoardCell getCell(int row, int col)
    {
        return grid[row][col];
    }

    Set<TestBoardCell> getTargets()
    {
        return targets;
    }


}
