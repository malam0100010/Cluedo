package test;

import experiment.TestBoardCell;

public class BoardTestExp 
{
    private BoardTestExp board;
    
    @BeforeEach
    void setUp()
    {
        board = new BoardTestExp(4,4);
    }

    @Test
    public void testTargetNormal()
    {
        TestBoardCell cell = board.getCell(0,0);
        board.calcTargets(cell, 3);
        Set<calcTargets> targets = board.getTargets();
        Assert.assertEquals(6, targets.size());
        Assert.assertTrue(targets.contains(board.getCell(3,0)));
        Assert.assertTrue(targets.contains(board.getCell(2,1)));
        Assert.assertTrue(targets.contains(board.getCell(0,1)));
        Assert.assertTrue(targets.contains(board.getCell(0,3)));
        Assert.assertTrue(targets.contains(board.getCell(1,0)));

    }

    @Test
    public void testTargetsMixed()
    {
        // set up occupied cells
        board.getCell(0,2).setOccupied(true);
        board.getCell(1,2).setIsRoom(true);
        TestBoardCell cell = board.getCell(0,3);
        board.calcTargets(cell, 3);
        Set<TestBoardCell> targets = board.getTargets();
        Assert.assertEqual(3, targets.size());
        Assert.assertTrue(targets.contains(board.getCell(1,2)));
        Assert.assertTrue(targets.contains(board.getCell(2,2)));
        Assert.assertTrue(targets.contains(board.getCell(3,3)));

    }


}
