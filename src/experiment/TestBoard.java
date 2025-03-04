package experiment;

import java.util.Set;

public class TestBoard 
{
	private static final int SIZE = 4;
    private TestBoardCell[][] board = new TestBoardCell[SIZE][SIZE];
    Set<TestBoardCell> targets;


    public TestBoard()
    {
    	for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                board[row][col] = new TestBoardCell(row, col);
            }
        }
    }

    public void calcTargets( TestBoardCell startCell, int pathlength)
    {
        targets.clear();
    }

    public TestBoardCell getCell(int row, int col)
    {
        return board[row][col];
    }

    public Set<TestBoardCell> getTargets()
    {
        return targets;
    }


}
