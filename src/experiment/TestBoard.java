package experiment;

import java.util.Set;

public class TestBoard 
{
    private TestBoardCell[][] board;
    private int numRows;
    private int numCols;
    TestBoardCell testBoardCell;
    Set<TestBoardCell> targets;

    public TestBoard()
    {

    }

    public void calcTargets( TestBoardCell startCell, int pathlength)
    {
        return;
    }

    public TestBoardCell getCell(int row, int col)
    {
        return testBoardCell;
    }

    public Set<TestBoardCell> getTargets()
    {
        return targets;
    }


}
